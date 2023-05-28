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

public class YarachMyCoursesActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ACT = 1;

    public static final int REQUEST_CODE_LOGIN = 1;
    private MyCoursesData responseFromServer;
    String userEmail;

    //url for check data of Yarach Learning API
    public static final String yarachLearningApiUrl = "https://learning.yarach.ru";
    ProgressBar pogress_bar;

    private ArrayAdapter<String> mAdapter;
    ArrayList<String> arrayCourses = new ArrayList<String>();
    ArrayList<String> arrayCoursesId = new ArrayList<String>();

    ImageView my_c_no_1;
    TextView my_c_no_2;
    TextView my_c_no_3;
    TextView my_c_no_4;
    TextView text_mycourses_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yarach_my_courses);

        text_mycourses_error = (TextView) findViewById(R.id.text_mycourses_error);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView icon_mycourses_back = (ImageView) findViewById(R.id.icon_mycourses_back);
        pogress_bar = (ProgressBar) findViewById(R.id.my_c_progress_bar);
        pogress_bar.setVisibility(ProgressBar.VISIBLE);

        my_c_no_1 = (ImageView) findViewById(R.id.my_c_no_1);
        my_c_no_2 = (TextView) findViewById(R.id.my_c_no_2);
        my_c_no_3 = (TextView) findViewById(R.id.my_c_no_3);
        my_c_no_4 = (TextView) findViewById(R.id.my_c_no_4);

        my_c_no_1.setVisibility(ImageView.GONE);
        my_c_no_2.setVisibility(TextView.GONE);
        my_c_no_3.setVisibility(TextView.GONE);
        my_c_no_4.setVisibility(TextView.GONE);

        text_mycourses_error.setText("");

        String path = getFilesDir().getPath().toString() + "/userInfo.txt";

        File file = new File(path);

        if (!file.exists()) {
            Intent intent = new Intent(YarachMyCoursesActivity.this, LoginActivity.class);
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

        new checkUserData().execute(sb.toString());

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ListView lvMain = (ListView) findViewById(R.id.lv_mycourses);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayCourses);

        lvMain.setAdapter(mAdapter);

        lvMain.setClickable(true);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = lvMain.getItemAtPosition(position);
                Intent intent = new Intent(YarachMyCoursesActivity.this, AboutCourseActivity.class);
                intent.putExtra("courseId", getCourseId(position));
                startActivityForResult(intent, REQUEST_CODE_ACT);
            }

            private String getCourseId(int position) {
                return arrayCoursesId.get(position);
            }
        });

        icon_mycourses_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YarachMyCoursesActivity.this, YarachHomeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ACT);
                finish();
            }
        });

        my_c_no_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://learning.yarach.ru/courses.php"));
                startActivity(browserIntent);
            }
        });
    }

    private class checkUserData extends AsyncTask<String, Integer, MyCoursesData> {

        // метод выполняемый перед запуском кода второго потока
        // имеет доступ к активности
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pogress_bar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected MyCoursesData doInBackground(String... args) {
            userEmail = args[0];

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(yarachLearningApiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // установка методов для доступа к серверу
            UserService service = retrofit.create(UserService.class);
            // настройка запроса
            Call<MyCoursesData> response = service.getMyCourses(userEmail, "getUserCourses");
            try {
                // отправка запроса
                Response<MyCoursesData> userResponse = response.execute();
                // получение тела запроса в вормате объекта класса
                responseFromServer = userResponse.body();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseFromServer;
        }

        @Override
        protected void onPostExecute(MyCoursesData currentData) {
            super.onPostExecute(currentData);
            if (currentData.error_code.contains("200")) {
                String[] coursesNameArray = currentData.courses_name.split(" &&%%&& ");
                for(int i = 0; i < coursesNameArray.length; i++) {
                    arrayCourses.add(coursesNameArray[i]);
                }
                String[] coursesIdArray = currentData.courses_id.split(" &&%%&& ");
                for(int i = 0; i < coursesIdArray.length; i++) {
                    arrayCoursesId.add(coursesIdArray[i]);
                }
            }

            pogress_bar.setVisibility(ProgressBar.GONE);

            if(arrayCourses.isEmpty()) {
                my_c_no_1.setVisibility(ImageView.VISIBLE);
                my_c_no_2.setVisibility(TextView.VISIBLE);
                my_c_no_3.setVisibility(TextView.VISIBLE);
                my_c_no_4.setVisibility(TextView.VISIBLE);
            } else {
                my_c_no_1.setVisibility(ImageView.GONE);
                my_c_no_2.setVisibility(TextView.GONE);
                my_c_no_3.setVisibility(TextView.GONE);
                my_c_no_4.setVisibility(TextView.GONE);
            }
        }
    }
}