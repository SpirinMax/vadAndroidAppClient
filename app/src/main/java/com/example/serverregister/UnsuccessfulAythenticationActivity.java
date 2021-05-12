package com.example.serverregister;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class UnsuccessfulAythenticationActivity extends AppCompatActivity {
    Button buttonSignUp, buttonSingIn;
    BehaviorActivity behaviorActivity;
    Context thisContext;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsuccessful_aythentication);
        getSupportActionBar().hide();

        thisContext = this;
        fragmentManager = getSupportFragmentManager();
        behaviorActivity = new BehaviorActivity(thisContext, fragmentManager);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSingIn = findViewById(R.id.buttonSingIn);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behaviorActivity.goInActivity(FillingNameActivity.class);
            }
        });

        buttonSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behaviorActivity.goInActivity(AuthenticationActivity.class);
            }
        });

    }
}