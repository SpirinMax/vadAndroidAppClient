package com.example.serverregister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import entites.User;
import retrofit.ApiClient;
import retrofit.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;
import ui.errorsServer.RefreshInActivity;
import ui.registration.TransitToRegistration;
import ui.registration.UiRegistration;

public class AuthenticationActivity extends AppCompatActivity implements TransitToRegistration, RefreshInActivity {
    Context thisContext;
    FragmentManager fragmentManager;
    BehaviorActivity behaviorActivity;
    EditText useremail, userpassword;
    Button buttonAuth;
    TextView textviewHello;
    LinearLayout linearLayoutEditContent;
    User userRequest = new User();
    UserService userService = new UserService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        getSupportActionBar().hide();

        thisContext = this;
        fragmentManager = getSupportFragmentManager();
        behaviorActivity = new BehaviorActivity(thisContext, fragmentManager);

        useremail = findViewById(R.id.useremail);
        userpassword = findViewById(R.id.userpassword);
        textviewHello = findViewById(R.id.textviewHello);
        linearLayoutEditContent = findViewById(R.id.linearLayoutEditContent);
    }

    public void authenticationUser(View view) {
        if (UiRegistration.checkOfNull(linearLayoutEditContent, AuthenticationActivity.this)) {
            userService.createCredentials(userRequest, useremail.getText().toString(), userpassword.getText().toString());
            Call<User> userResponseCall = ApiClient.getUserService().loginApp(userRequest);
            userResponseCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    userService.loginApp(response, behaviorActivity);
                    Intent profileIntent = new Intent(thisContext, ProfileActivity.class);
                    User userData = new User();
                    userData = userService.receiveUserData(response);

                    behaviorActivity.receiveDataInActivity(profileIntent, User.class.getSimpleName(), userData);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    ServerError.DisplayDialogLossConnection(thisContext, fragmentManager);
                }
            });
        }
    }

    @Override
    public void moveToRegistration() {
        behaviorActivity.goInRegisterActivity();
    }

    public void refreshActivity() {
        this.finish();
        startActivity(getIntent());
    }

    private void goRegisterActivity(View view) {
        behaviorActivity.goInRegisterActivity();
    }

}

