package com.qdocs.ssre241123.teachers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.qdocs.ssre241123.Login;
import com.qdocs.ssre241123.R;

public class TeacherLogin extends AppCompatActivity {

    TextView goback;

    LinearLayout teacher_btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_login);

        teacher_btn_login = (LinearLayout) findViewById(R.id.teacher_btn_login);
        goback = (TextView) findViewById(R.id.go_back_to_login_text);


        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

            }
        });

        teacher_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeacherDashboard.class);
                startActivity(intent);
                finish();
            }
        });





    }
}