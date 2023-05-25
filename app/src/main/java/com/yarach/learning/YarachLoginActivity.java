package com.yarach.learning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.yarach.learning.ui.login.LoginActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YarachLoginActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_LOGIN = 1;
    private CurrentData responseFromServer;

    //url for check data of Yarach ID API
    public static final String yarachIdApiUrl = "https://id.yarach.ru";
    ProgressBar pogress_bar;
    TextView errorMessageBlock;
    public String loginErrorMessage = "Ошибка подключения. Попробуйте позже";
    private String userEmailForCheck;
    private String userPasswordForCheck;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yarach_login);

        Button login_btn = (Button) findViewById(R.id.start_btn);
        TextView reg_btn = (TextView) findViewById(R.id.reg);
        pogress_bar = (ProgressBar) findViewById(R.id.login_progress_bar);
        errorMessageBlock = (TextView) findViewById(R.id.login_error_message);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView back_btn = (ImageView) findViewById(R.id.login_back_btn);
        EditText edit_email = (EditText) findViewById(R.id.log_email);
        EditText edit_password = (EditText) findViewById(R.id.log_password);
        pogress_bar.setVisibility(ProgressBar.INVISIBLE);
        errorMessageBlock.setVisibility(ProgressBar.INVISIBLE);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new checkUserData().execute(edit_email.getText().toString(), edit_password.getText().toString());
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YarachLoginActivity.this, MainActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LOGIN);
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://yarach.ru/useractions/signup.php"));
                startActivity(browserIntent);
            }
        });
    }

    private class checkUserData extends AsyncTask<String, Integer, CurrentData> {

        // метод выполняемый перед запуском кода второго потока
        // имеет доступ к активности
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pogress_bar.setVisibility(ProgressBar.VISIBLE);
        }

        // код второго потока
        // получает массив типа String
        // возвращает переменную типа String
        // НЕ имеет доступа к активности
        @Override
        protected CurrentData doInBackground(String... args) {
            userEmailForCheck = args[0];
            userPasswordForCheck = args[1];

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(yarachIdApiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // установка методов для доступа к серверу
            UserService service = retrofit.create(UserService.class);
            // настройка запроса
            Call<CurrentData> response = service.getCurrentDataCall(userEmailForCheck, userPasswordForCheck);
            try {
                // отправка запроса
                Response<CurrentData> userResponse = response.execute();
                // получение тела запроса в вормате объекта класса
                responseFromServer = userResponse.body();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseFromServer;
        }

        @Override
        protected void onPostExecute(CurrentData currentData) {
            super.onPostExecute(currentData);
            if (currentData.error_code.contains("200")) {
                String path = getFilesDir().getPath().toString() + "/userInfo.txt";

                try {
                    /*ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
                    oos.write(userEmailForCheck.getBytes(StandardCharsets.UTF_8));
                    oos.close();*/
                    FileWriter wr = new FileWriter(path);
                    wr.write(userEmailForCheck);
                    wr.close();

                    loginErrorMessage = "Вы вошли в аккаунт Yarach ID";
                    Toast.makeText(getApplicationContext(), loginErrorMessage, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(YarachLoginActivity.this, YarachHomeActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                    finish();
                } catch (IOException e) {
                    errorMessageBlock.setText("Что-то пошло не так. Попробуйте позже");
                }
            }
            else {
                // print error and stop checking
                pogress_bar.setVisibility(ProgressBar.INVISIBLE);
                errorMessageBlock.setVisibility(TextView.VISIBLE);
                if(currentData.error_code.contains("001")) {
                    errorMessageBlock.setText("Не удается подключиться к серверам. Попробуйте позже");
                } else if(currentData.error_code.contains("002")) {
                    errorMessageBlock.setText("Неправильная почта или пароль");
                } else if(currentData.error_code.contains("003")) {
                    errorMessageBlock.setText("Введите вашу почту и пароль");
                } else if(currentData.error_code.contains("004")) {
                    errorMessageBlock.setText("Слишком много попыток входа");
                } else {
                    errorMessageBlock.setText("Неправильная почта или пароль");
                }
            }
        }
    }
}