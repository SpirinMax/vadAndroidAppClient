package com.example.serverregister;

import androidx.appcompat.app.AppCompatActivity;

import ui.transitions.TransitionActivity;

public class BehaviorActivity extends AppCompatActivity {
    TransitionActivity transitionActivity = new TransitionActivity();

    public void goInRegisterActivity(){
        transitionActivity.goInActivity(this,AuthenticationActivity.class);
    }

    public void exitTheApp() {
        System.exit(0);
    }

    public void refreshActivity(){
        this.finish();
        startActivity(getIntent());
    }

}