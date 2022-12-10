package com.example.myschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Controller.SessionManager;
import UI.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        queue= Volley.newRequestQueue(MainActivity.this);
        if(SessionManager.getInstance(this).isLoggedIn())
        {
            if(SessionManager.getInstance(this).getToken()!=null)
            {
                token=SessionManager.getInstance(this).getToken().getToken();
            }
        }else {
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return;
        }
    }
}