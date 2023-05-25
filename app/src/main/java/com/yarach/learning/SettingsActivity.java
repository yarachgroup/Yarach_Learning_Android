package com.yarach.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class SettingsActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ACT = 1;

    private User responseFromServer;
    String userEmail;

    //url for check data of Yarach Id API
    public static final String yarachIdApiUrl = "https://id.yarach.ru";

    TextView settings_top_title;
    TextView settings_top_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String path = getFilesDir().getPath().toString() + "/userInfo.txt";

        File file = new File(path);

        if (!file.exists()) {
            Intent intent = new Intent(SettingsActivity.this, YarachLoginActivity.class);
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

        new SettingsActivity.getUserData().execute(sb.toString());

        settings_top_title = (TextView) findViewById(R.id.settings_top_title);
        settings_top_about = (TextView) findViewById(R.id.settings_top_about);
        settings_top_about.setText(sb.toString());

        ImageView settings_back_btn = (ImageView) findViewById(R.id.settings_back_btn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout settings_1_btn = (LinearLayout) findViewById(R.id.settings_1_btn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout settings_2_btn = (LinearLayout) findViewById(R.id.settings_2_btn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout settings_3_btn = (LinearLayout) findViewById(R.id.settings_3_btn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout settings_4_btn = (LinearLayout) findViewById(R.id.settings_4_btn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout settings_5_btn = (LinearLayout) findViewById(R.id.settings_5_btn);

        settings_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, YarachHomeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ACT);
                finish();
            }
        });

        settings_1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://id.yarach.ru"));
                startActivity(browserIntent);
            }
        });
        settings_2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://speech.yarach.ru/messenger.php"));
                startActivity(browserIntent);
            }
        });
        settings_3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://disk.yarach.ru"));
                startActivity(browserIntent);
            }
        });
        settings_4_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.yarach.ru/notes"));
                startActivity(browserIntent);
            }
        });
        settings_5_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean deleteCheck = file.delete();
                if(deleteCheck) {
                    Toast.makeText(getApplicationContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Произошла ошибка. Попробуйте позже", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(SettingsActivity.this, YarachLoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ACT);
                finish();
            }
        });
    }

    private class getUserData extends AsyncTask<String, Integer, User> {

        // метод выполняемый перед запуском кода второго потока
        // имеет доступ к активности
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(String... args) {
            userEmail = args[0];

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(yarachIdApiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // установка методов для доступа к серверу
            UserService service = retrofit.create(UserService.class);
            // настройка запроса
            Call<User> response = service.getUserInfo(userEmail);
            try {
                // отправка запроса
                Response<User> userResponse = response.execute();
                // получение тела запроса в вормате объекта класса
                responseFromServer = userResponse.body();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseFromServer;
        }

        @Override
        protected void onPostExecute(User currentData) {
            super.onPostExecute(currentData);
            if (currentData.error_code.contains("200")) {
                settings_top_title.setText(currentData.name);
            }
        }
    }
}