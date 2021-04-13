package com.example.serverregister;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import entites.User;
import service.UserService;
import ui.transitions.TransitionActivity;

public class StartActivity extends BehaviorActivity {
    private TransitionActivity transitionActivity = new TransitionActivity();
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private User user = new User();
    private UserService userService = new UserService();
    RelativeLayout profileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        profileButton = findViewById(R.id.profileButton);

        if (sharedPreferencesUserInfo.checkPresenceSettings(StartActivity.this)){
            user = sharedPreferencesUserInfo.getSavedSettings(StartActivity.this);
            userService.loginApp(user,StartActivity.this,getSupportFragmentManager());
        } else {
            Toast.makeText(this, R.string.needAuth, Toast.LENGTH_SHORT).show();
        }
    }

    public void goProfileActivity (View view){
        transitionActivity.goActivityAfterCheck(StartActivity.this,MainActivity.class,getSupportFragmentManager());
    }

}