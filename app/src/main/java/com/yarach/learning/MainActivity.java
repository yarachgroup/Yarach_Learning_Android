package com.yarach.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yarach.learning.data.model.LoggedInUser;
import com.yarach.learning.ui.login.LoginActivity;
import com.yarach.learning.ui.login.LoginViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_LOGIN = 1;
    public static final int REQUEST_CODE_HOME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = getFilesDir().getPath().toString() + "/userInfo.txt";

        File file = new File(path);

        if (file.exists()) {
            Intent intent = new Intent(MainActivity.this, YarachHomeActivity.class);
            startActivityForResult(intent, REQUEST_CODE_HOME);
            finish();
        }

        TextView login_btn = (TextView) findViewById(R.id.start_mainBtn);

        TextView reg_btn = (TextView) findViewById(R.id.start_mainBtn2);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, YarachLoginActivity.class);
                startActivity(intent);
                finish();
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
}