package com.yarach.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yarach.learning.ui.login.LoginActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LessonsActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ACT = 1;

    //url for check data of Yarach Learning API
    public static final String yarachLearningApiUrl = "https://learning.yarach.ru";
    ProgressBar pogress_bar;
    private AboutCourseData responseFromServer;

    String courseId;
    String userEmail;

    private ArrayAdapter<String> mAdapter;
    ArrayList<String> arrayLessonsTitle = new ArrayList<String>();
    ArrayList<String> arrayLessonsId = new ArrayList<String>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        courseId = getIntent().getStringExtra("courseId");

        pogress_bar = (ProgressBar) findViewById(R.id.lessons_progress_bar);
        ImageView btn_back = (ImageView) findViewById(R.id.icon_courselessons_back);

        btn_back.setClickable(true);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessonsActivity.this, AboutCourseActivity.class);
                intent.putExtra("courseId", courseId);
                startActivityForResult(intent, REQUEST_CODE_ACT);
                finish();
            }
        });

        //check user email
        String path = getFilesDir().getPath().toString() + "/userInfo.txt";

        File file = new File(path);

        if (!file.exists()) {
            Intent intent = new Intent(LessonsActivity.this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ACT);
            finish();
        }

        StringBuilder sb = new StringBuilder();
        InputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line = null;
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line);
        }

        //start getting course info
        new LessonsActivity.getCourseInfo().execute(courseId, sb.toString());

        ListView lvMain = (ListView) findViewById(R.id.lv_courselessons);
        mAdapter = new ArrayAdapter<String>(LessonsActivity.this,
                android.R.layout.simple_list_item_1, arrayLessonsTitle);

        lvMain.setAdapter(mAdapter);

        lvMain.setClickable(true);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = lvMain.getItemAtPosition(position);
                Intent intent = new Intent(LessonsActivity.this, LessonActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("lessonId", getLessonId(position));
                startActivityForResult(intent, REQUEST_CODE_ACT);
            }

            private String getLessonId(int position) {
                return arrayLessonsId.get(position);
            }
        });
    }

    private class getCourseInfo extends AsyncTask<String, Integer, AboutCourseData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pogress_bar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected AboutCourseData doInBackground(String... args) {
            courseId = args[0];
            userEmail = args[1];

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(yarachLearningApiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // установка методов для доступа к серверу
            UserService service = retrofit.create(UserService.class);
            // настройка запроса
            Call<AboutCourseData> response = service.getCourseInfo(courseId, userEmail, "getCourseInfoForPupil");
            try {
                // отправка запроса
                Response<AboutCourseData> userResponse = response.execute();
                // получение тела запроса в вормате объекта класса
                responseFromServer = userResponse.body();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseFromServer;
        }

        @Override
        protected void onPostExecute(AboutCourseData currentData) {
            super.onPostExecute(currentData);
            if (currentData.error_code.contains("200")) {
                pogress_bar.setVisibility(ProgressBar.GONE);
                String[] lessonsTitleArray = currentData.course_lessons_list_withtitles.split(" &&%%&& ");;
                for(int i = 0; i < lessonsTitleArray.length; i++) {
                    arrayLessonsTitle.add(lessonsTitleArray[i]);
                    mAdapter.notifyDataSetChanged();
                }
                String[] lessonsIdArray = currentData.course_lessons_list.split(" &&%%&& ");;
                for(int i = 0; i < lessonsIdArray.length; i++) {
                    arrayLessonsId.add(lessonsIdArray[i]);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Что-то пошло не так. Возможно, сервис недоступен. Попробуйте позже", Toast.LENGTH_SHORT).show();
            }
        }
    }
}