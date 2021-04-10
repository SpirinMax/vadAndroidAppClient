package com.example.serverregister;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import entites.User;
import service.UserService;
import ui.transitions.BehaviorTransition;
import ui.transitions.TransitionActivity;

public class StartActivity extends AppCompatActivity implements BehaviorTransition {
    private TransitionActivity transitionActivity = new TransitionActivity();
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private User user = new User();
    private UserService userService = new UserService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        user = sharedPreferencesUserInfo.getSavedSettings(StartActivity.this);

        if (sharedPreferencesUserInfo.checkPresenceSettings(StartActivity.this)){
            userService.loginApp(user,StartActivity.this);
        }
    }

    public void goProfileActivity (View view){
        transitionActivity.goActivityAfterCheck(StartActivity.this,MainActivity.class,getSupportFragmentManager());
    }

    @Override
    public void goInActivity(){
        transitionActivity.goInActivity(StartActivity.this,AuthenticationActivity.class);
    }

}