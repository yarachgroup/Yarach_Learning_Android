package com.yarach.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LessonActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ACT = 1;

    //url for check data of Yarach Learning API
    public static final String yarachLearningApiUrl = "https://learning.yarach.ru";
    ProgressBar pogress_bar;
    private LessonData responseFromServer;

    String courseId;
    String lessonId;
    String userEmail;

    TextView text_lesson_b1_m;
    TextView text_lesson_b1_1;
    TextView text_lesson_b1_1_2;
    TextView text_lesson_b1_2;
    TextView text_lesson_b1_2_2;
    TextView text_lesson_b1_3;
    TextView text_lesson_b1_3_2;
    TextView btn_lesson_1;

    TextView text_lesson_b2_m;
    TextView text_lesson_b2_1;
    TextView text_lesson_b2_1_2;
    TextView text_lesson_b2_2;
    TextView text_lesson_b2_2_2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        courseId = getIntent().getStringExtra("courseId");
        lessonId = getIntent().getStringExtra("lessonId");

        pogress_bar = (ProgressBar) findViewById(R.id.lesson_progress_bar);
        ImageView btn_back = (ImageView) findViewById(R.id.icon_lesson_back);
        TextView btn_lesson_main = (TextView) findViewById(R.id.btn_lesson_main);

        btn_back.setClickable(true);

        text_lesson_b1_m = (TextView) findViewById(R.id.text_lesson_b1_m);
        text_lesson_b1_1 = (TextView) findViewById(R.id.text_lesson_b1_1);
        text_lesson_b1_1_2 = (TextView) findViewById(R.id.text_lesson_b1_1_2);
        text_lesson_b1_2 = (TextView) findViewById(R.id.text_lesson_b1_2);
        text_lesson_b1_2_2 = (TextView) findViewById(R.id.text_lesson_b1_2_2);
        text_lesson_b1_3 = (TextView) findViewById(R.id.text_lesson_b1_3);
        text_lesson_b1_3_2 = (TextView) findViewById(R.id.text_lesson_b1_3_2);

        text_lesson_b2_m = (TextView) findViewById(R.id.text_lesson_b2_m);
        text_lesson_b2_1 = (TextView) findViewById(R.id.text_lesson_b2_1);
        text_lesson_b2_1_2 = (TextView) findViewById(R.id.text_lesson_b2_1_2);
        text_lesson_b2_2 = (TextView) findViewById(R.id.text_lesson_b2_2);
        text_lesson_b2_2_2 = (TextView) findViewById(R.id.text_lesson_b2_2_2);
        btn_lesson_1 = (TextView) findViewById(R.id.btn_lesson_1);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessonActivity.this, LessonsActivity.class);
                intent.putExtra("courseId", courseId);
                startActivityForResult(intent, REQUEST_CODE_ACT);
            }
        });

        btn_lesson_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://learning.yarach.ru/aboutcourse.php?csid=" + courseId + "#lesson" + lessonId));
                startActivity(browserIntent);
            }
        });

        String path = getFilesDir().getPath().toString() + "/userInfo.txt";

        File file = new File(path);

        if (!file.exists()) {
            Intent intent = new Intent(LessonActivity.this, LoginActivity.class);
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

        new getLesson().execute(sb.toString(), courseId, lessonId);
    }

    private class getLesson extends AsyncTask<String, Integer, LessonData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pogress_bar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected LessonData doInBackground(String... args) {
            userEmail = args[0];
            courseId = args[1];
            lessonId = args[2];

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(yarachLearningApiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // установка методов для доступа к серверу
            UserService service = retrofit.create(UserService.class);
            // настройка запроса
            Call<LessonData> response = service.getLessonInfo(userEmail, courseId, lessonId, "getLessonInfoForPupil");
            try {
                // отправка запроса
                Response<LessonData> userResponse = response.execute();
                // получение тела запроса в вормате объекта класса
                responseFromServer = userResponse.body();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseFromServer;
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(LessonData currentData) {
            super.onPostExecute(currentData);
            if (currentData.error_code.contains("200")) {
                pogress_bar.setVisibility(ProgressBar.GONE);

                text_lesson_b1_1_2.setText(currentData.lesson_vebinar);
                text_lesson_b1_2_2.setText(currentData.lesson_date);
                text_lesson_b1_3_2.setText(currentData.lesson_about
                        .replace("##s", "")
                        .replace("##e", "")
                        .replace("[[s", "")
                        .replace("[[e", "")
                        .replace("==s", "")
                        .replace("==e", "")
                        .replace("~~s", "")
                        .replace("~~e", "")
                        .replace("!!s", "")
                        .replace("!!e", "")
                        .replace("__s", "")
                        .replace("__e", "")
                        .replace("--s", "")
                        .replace("--e", "")
                        .replace("**s", "")
                        .replace("**e", "")
                        .replace("++s", "")
                        .replace("++e", ""));

                if(currentData.lesson_vebinar.isEmpty()) {
                    text_lesson_b1_1.setVisibility(TextView.GONE);
                    text_lesson_b1_1_2.setVisibility(TextView.GONE);
                    btn_lesson_1.setVisibility(TextView.GONE);
                } else {
                    btn_lesson_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new
                                    Intent(Intent.ACTION_VIEW, Uri.parse(currentData.lesson_vebinar));
                            startActivity(browserIntent);
                        }
                    });
                    text_lesson_b1_1_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new
                                    Intent(Intent.ACTION_VIEW, Uri.parse(currentData.lesson_vebinar));
                            startActivity(browserIntent);
                        }
                    });
                }

                if(currentData.hometask_about.isEmpty()) {
                    text_lesson_b2_m.setVisibility(TextView.GONE);
                    text_lesson_b2_1.setVisibility(TextView.GONE);
                    text_lesson_b2_1_2.setVisibility(TextView.GONE);
                    text_lesson_b2_2.setVisibility(TextView.GONE);
                    text_lesson_b2_2_2.setVisibility(TextView.GONE);
                } else {
                    text_lesson_b2_1_2.setText(currentData.hometask_date);
                    text_lesson_b2_2_2.setText(currentData.hometask_about
                            .replace("##s", "")
                            .replace("##e", "")
                            .replace("[[s", "")
                            .replace("[[e", "")
                            .replace("==s", "")
                            .replace("==e", "")
                            .replace("~~s", "")
                            .replace("~~e", "")
                            .replace("!!s", "")
                            .replace("!!e", "")
                            .replace("__s", "")
                            .replace("__e", "")
                            .replace("--s", "")
                            .replace("--e", "")
                            .replace("**s", "")
                            .replace("**e", "")
                            .replace("++s", "")
                            .replace("++e", ""));
                }
            } else {
                Toast.makeText(getApplicationContext(), "Что-то пошло не так. Возможно, сервис недоступен. Попробуйте позже", Toast.LENGTH_SHORT).show();
            }
        }
    }
}