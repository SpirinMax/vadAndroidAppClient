package com.example.serverregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import entites.User;
import service.UserService;
import ui.registration.UiRegistration;

public class AuthenticationActivity extends AppCompatActivity {
    EditText useremail,userpassword;
    Button buttonAuth;
    TextView textviewHello;
    LinearLayout linearLayoutEditContent;
    User userRequest = new User();
    UserService userService=new UserService();
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        useremail = findViewById(R.id.useremail);
        userpassword = findViewById(R.id.userpassword);
        textviewHello = findViewById(R.id.textviewHello);
        linearLayoutEditContent = findViewById(R.id.linearLayoutEditContent);
    }

    public void authenticationUser(View view){
        if (UiRegistration.checkOfNull(linearLayoutEditContent,AuthenticationActivity.this)) {
            userService.createCredentials(userRequest,useremail.getText().toString(),userpassword.getText().toString());
            userService.loginApp(userRequest,AuthenticationActivity.this);
        }
    }

    public void goRegisterActivity (View view) {
        Intent registerIntent = new Intent(this, MainActivity.class);
        startActivity(registerIntent);
    }

}

