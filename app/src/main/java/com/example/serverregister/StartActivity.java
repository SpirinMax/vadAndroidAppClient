package com.example.serverregister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import entites.User;
import service.UserService;
import ui.TransitIconToolbar;
import ui.errorsServer.RefreshInActivity;
import ui.registration.TransitToRegistration;

public class StartActivity extends AppCompatActivity implements RefreshInActivity, TransitToRegistration, TransitIconToolbar {
    private FragmentManager thisFragmentManager;
    private Context thisContext;
    private User userRequest = new User();
    private UserService userService = new UserService();
    private BehaviorActivity behaviorActivity;
    Intent profileIntent;
    private User userData = new User();
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        thisContext = this;
        thisFragmentManager = getSupportFragmentManager();
        behaviorActivity = new BehaviorActivity(thisContext, thisFragmentManager);
        profileIntent = new Intent(thisContext, ProfileActivity.class);

        if (sharedPreferencesUserInfo.checkPresenceSettings(StartActivity.this) == false) {
            Toast.makeText(this, R.string.needAuth, Toast.LENGTH_SHORT).show();
        }
    }

    public void goProfileActivity(View view) {
        behaviorActivity.goInActivity(ProfileActivity.class);
    }

    @Override
    public void goListRequests(View view) {
        //это данная активность
    }

    @Override
    public void goHistory(View view) {

    }

    public void goRequestCreationActivity(View view) {
        behaviorActivity.goInActivity(RequestCreationStageActivity.class);
    }

    public void refreshActivity() {
        this.finish();
        startActivity(getIntent());
    }

    @Override
    public void moveToRegistration() {
        behaviorActivity.goInActivity(AuthenticationActivity.class);
    }
}