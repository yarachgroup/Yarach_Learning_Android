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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

public class AboutCourseActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ACT = 1;

    //url for check data of Yarach Learning API
    public static final String yarachLearningApiUrl = "https://learning.yarach.ru";
    ProgressBar pogress_bar;
    private AboutCourseData responseFromServer;

    String courseId;
    String userEmail;

    TextView course_title;
    TextView course_about;

    TextView text_aboutcourse_b1_m;
    TextView text_aboutcourse_b1_1;
    TextView text_aboutcourse_b1_1_2;
    TextView text_aboutcourse_b1_2;
    TextView text_aboutcourse_b1_2_2;
    TextView text_aboutcourse_b1_3;
    TextView text_aboutcourse_b1_3_2;
    TextView text_aboutcourse_b1_4;
    TextView text_aboutcourse_b1_4_2;
    TextView text_aboutcourse_b1_5;
    TextView text_aboutcourse_b1_5_2;
    TextView text_aboutcourse_b1_6;
    TextView text_aboutcourse_b1_6_2;

    TextView text_aboutcourse_b2_m;
    TextView text_aboutcourse_b2_1;
    TextView btn_aboutcourse_1;

    TextView text_aboutcourse_b3_m;
    TextView text_aboutcourse_b3_1;

    private ArrayAdapter<String> mAdapter;
    ArrayList<String> arrayLessonsTitle = new ArrayList<String>();
    ArrayList<String> arrayLessonsId = new ArrayList<String>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_course);

        courseId = getIntent().getStringExtra("courseId");

        pogress_bar = (ProgressBar) findViewById(R.id.course_progress_bar);
        ImageView btn_back = (ImageView) findViewById(R.id.icon_course_back);
        TextView btn_course_main = (TextView) findViewById(R.id.btn_course_main);
        TextView btn_aboutcourse_2 = (TextView) findViewById(R.id.btn_aboutcourse_2);

        btn_back.setClickable(true);

        course_title = (TextView) findViewById(R.id.course_title);
        course_about = (TextView) findViewById(R.id.course_about);

        text_aboutcourse_b1_m = (TextView) findViewById(R.id.text_aboutcourse_b1_m);
        text_aboutcourse_b1_1 = (TextView) findViewById(R.id.text_aboutcourse_b1_1);
        text_aboutcourse_b1_1_2 = (TextView) findViewById(R.id.text_aboutcourse_b1_1_2);
        text_aboutcourse_b1_2 = (TextView) findViewById(R.id.text_aboutcourse_b1_2);
        text_aboutcourse_b1_2_2 = (TextView) findViewById(R.id.text_aboutcourse_b1_2_2);
        text_aboutcourse_b1_3 = (TextView) findViewById(R.id.text_aboutcourse_b1_3);
        text_aboutcourse_b1_3_2 = (TextView) findViewById(R.id.text_aboutcourse_b1_3_2);
        text_aboutcourse_b1_4 = (TextView) findViewById(R.id.text_aboutcourse_b1_4);
        text_aboutcourse_b1_4_2 = (TextView) findViewById(R.id.text_aboutcourse_b1_4_2);
        text_aboutcourse_b1_5 = (TextView) findViewById(R.id.text_aboutcourse_b1_5);
        text_aboutcourse_b1_5_2 = (TextView) findViewById(R.id.text_aboutcourse_b1_5_2);
        text_aboutcourse_b1_6 = (TextView) findViewById(R.id.text_aboutcourse_b1_6);
        text_aboutcourse_b1_6_2 = (TextView) findViewById(R.id.text_aboutcourse_b1_6_2);

        text_aboutcourse_b2_m = (TextView) findViewById(R.id.text_aboutcourse_b2_m);
        text_aboutcourse_b2_1 = (TextView) findViewById(R.id.text_aboutcourse_b2_1);
        btn_aboutcourse_1 = (TextView) findViewById(R.id.btn_aboutcourse_1);

        text_aboutcourse_b3_m = (TextView) findViewById(R.id.text_aboutcourse_b3_m);
        text_aboutcourse_b3_1 = (TextView) findViewById(R.id.text_aboutcourse_b3_1);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutCourseActivity.this, YarachMyCoursesActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ACT);
                finish();
            }
        });

        btn_course_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://learning.yarach.ru/aboutcourse.php?csid=" + courseId));
                startActivity(browserIntent);
            }
        });
        btn_aboutcourse_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutCourseActivity.this, LessonsActivity.class);
                intent.putExtra("courseId", courseId);
                startActivityForResult(intent, REQUEST_CODE_ACT);
            }
        });

        //check user email
        String path = getFilesDir().getPath().toString() + "/userInfo.txt";

        File file = new File(path);

        if (!file.exists()) {
            Intent intent = new Intent(AboutCourseActivity.this, LoginActivity.class);
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
        new AboutCourseActivity.getCourseInfo().execute(courseId, sb.toString());
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
            userEmail = args[0];

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

                course_title.setText(currentData.course_title);
                course_about.setText(currentData.course_about);

                text_aboutcourse_b1_1_2.setText(currentData.course_teacher_name);
                switch(currentData.course_about_continue) {
                    case "7-days":
                        text_aboutcourse_b1_2_2.setText("Длится 7 дней");
                        break;
                    case "21-days":
                        text_aboutcourse_b1_2_2.setText("Длится 21 день");
                        break;
                    case "1-month":
                        text_aboutcourse_b1_2_2.setText("Длится 1 месяц");
                        break;
                    case "3-months":
                        text_aboutcourse_b1_2_2.setText("Длится 3 месяца");
                        break;
                    case "9-months":
                        text_aboutcourse_b1_2_2.setText("Длится 9 месяцев");
                        break;
                    default:
                        text_aboutcourse_b1_2_2.setText("Длится 12 месяцев");
                        break;
                }
                switch(currentData.course_about_category) {
                    case "english":
                        text_aboutcourse_b1_3_2.setText("Английский");
                        break;
                    case "programming":
                        text_aboutcourse_b1_3_2.setText("Программирование");
                        break;
                    case "marketing":
                        text_aboutcourse_b1_3_2.setText("Маркетинг");
                        break;
                    case "management":
                        text_aboutcourse_b1_3_2.setText("Управление");
                        break;
                    case "design":
                        text_aboutcourse_b1_3_2.setText("Дизайн");
                        break;
                    case "psychology":
                        text_aboutcourse_b1_3_2.setText("Психология");
                        break;
                    case "science":
                        text_aboutcourse_b1_3_2.setText("Наука");
                        break;
                    case "engineering":
                        text_aboutcourse_b1_3_2.setText("Инженерия");
                        break;
                    case "multimedia":
                        text_aboutcourse_b1_3_2.setText("Мультимедиа");
                        break;
                    case "language":
                        text_aboutcourse_b1_3_2.setText("Изучается язык");
                        break;
                    default:
                        text_aboutcourse_b1_3_2.setText("Другое");
                        break;
                }
                switch(currentData.course_about_level) {
                    case "beginner":
                        text_aboutcourse_b1_4_2.setText("Начальный уровень");
                        break;
                    case "advanced":
                        text_aboutcourse_b1_4_2.setText("Продвинутый уровень");
                        break;
                    default:
                        text_aboutcourse_b1_4_2.setText("На данном курсе это не важно");
                        break;
                }
                switch(currentData.course_about_pay_period) {
                    case "lesson":
                        text_aboutcourse_b1_5_2.setText("Оплата совершается за каждое занятие");
                        break;
                    case "week":
                        text_aboutcourse_b1_5_2.setText("Оплата раз в неделю");
                        break;
                    case "month":
                        text_aboutcourse_b1_5_2.setText("Оплата раз в месяц");
                        break;
                    default:
                        text_aboutcourse_b1_5_2.setText("Оплата вносится сразу за весь курс");
                        break;
                }
                text_aboutcourse_b1_6_2.setText(currentData.course_about_lang);

                text_aboutcourse_b2_1.setText(currentData.course_teacher_about);
                btn_aboutcourse_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new
                                Intent(Intent.ACTION_VIEW, Uri.parse("https://speech.yarach.ru/messenger.php?u=" + currentData.course_teacher_id));
                        startActivity(browserIntent);
                    }
                });

                switch(currentData.course_certificate_about) {
                    case "true":
                        text_aboutcourse_b3_1.setText("Будет выдан сертификат или диплом об оканчании данного курса. По вопросам получения или описания сертификата обращайтесь к преподователю курса");
                        break;
                    default:
                        text_aboutcourse_b3_1.setText("Сертификат или диплом об оканчании данного курса не выдаётся");
                        break;
                }
            } else {
                Toast.makeText(getApplicationContext(), "Что-то пошло не так. Возможно, сервис недоступен. Попробуйте позже", Toast.LENGTH_SHORT).show();
            }
        }
    }
}