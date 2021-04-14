package com.example.serverregister;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import entites.User;
import retrofit.ApiClient;
import retrofit.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;
import ui.errorsServer.RefreshInActivity;
import ui.registration.TransitToRegistration;

public class StartActivity extends AppCompatActivity implements RefreshInActivity, TransitToRegistration {
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private User userRequest = new User();
    private UserService userService = new UserService();
    private BehaviorActivity behaviorActivity = new BehaviorActivity();
    RelativeLayout profileButton;
    Context thisContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        thisContext=this;
        behaviorActivity.identifyContext(thisContext);
        behaviorActivity.identifyFragmentManager(getSupportFragmentManager());

        profileButton = findViewById(R.id.profileButton);

        if (sharedPreferencesUserInfo.checkPresenceSettings(StartActivity.this)){
            userRequest = sharedPreferencesUserInfo.getSavedSettings(StartActivity.this);
            Call<User> userResponseCall = ApiClient.getUserService().loginApp(userRequest);
            userResponseCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    userService.loginApp(response,behaviorActivity);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    ServerError.DisplayDialogLossConnection(thisContext,getSupportFragmentManager());
                }
            });
        } else {
            Toast.makeText(this, R.string.needAuth, Toast.LENGTH_SHORT).show();
        }
    }

    public void goProfileActivity (View view){
        behaviorActivity.goActivityAfterCheck(ProfileActivity.class);
    }

    public void refreshActivity(){
        this.finish();
        startActivity(getIntent());
    }

    @Override
    public void moveToRegistration() {
        behaviorActivity.goInRegisterActivity();
    }
}