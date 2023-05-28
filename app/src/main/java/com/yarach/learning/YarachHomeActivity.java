package com.yarach.learning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yarach.learning.adapters.MyListAdapter;
import com.yarach.learning.ui.login.LoginActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YarachHomeActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ACT = 1;
    public static final int REQUEST_CODE_HOME = 1;

    private User responseFromServer;
    String userEmail;

    //url for check data of Yarach Id API
    public static final String yarachIdApiUrl = "https://id.yarach.ru";

    TextView user_name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yarach_home);

        String path = getFilesDir().getPath().toString() + "/userInfo.txt";

        File file = new File(path);

        if (!file.exists()) {
            Intent intent = new Intent(YarachHomeActivity.this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_CODE_HOME);
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

        new YarachHomeActivity.getUserData().execute(sb.toString());

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView userName = (TextView) findViewById(R.id.user_name);
        TextView btn_settings_main =  (TextView) findViewById(R.id.btn_settings_main);

        // hello user message "Hello, User"

        userName.setText("Секундочку...");

        TextView btn_mycourses = (TextView) findViewById(R.id.btn_mycourses);
        TextView btn_createdcourses = (TextView) findViewById(R.id.btn_createdcourses);
        TextView btn_settings = (TextView) findViewById(R.id.btn_settings);

        user_name =  (TextView) findViewById(R.id.user_name);

        btn_mycourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YarachHomeActivity.this, YarachMyCoursesActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ACT);
            }
        });
        btn_createdcourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YarachHomeActivity.this, YarachCreatedCoursesActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ACT);
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YarachHomeActivity.this, SettingsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ACT);
            }
        });
        btn_settings_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YarachHomeActivity.this, SettingsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ACT);
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
                user_name.setText(currentData.name + ",");
            }
        }
    }
}