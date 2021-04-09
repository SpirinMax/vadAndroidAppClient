package com.example.serverregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import entites.User;
import service.UserService;
import ui.registration.UiRegistration;

public class RegisterActivity extends AppCompatActivity {
    EditText useremail,userpassword;
    TextView textviewHello;
    User userRequest;
    LinearLayout linearLayoutEditContent;
    private UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        useremail = findViewById(R.id.useremail);
        userpassword = findViewById(R.id.userpassword);
        textviewHello = findViewById(R.id.textviewHello);
        linearLayoutEditContent = findViewById(R.id.linearLayoutEditContent);

        Bundle userFullname = getIntent().getExtras();
        userRequest = (User) userFullname.getSerializable(User.class.getSimpleName());

        textviewHello.setText(userRequest.getFirstname()+" "+userRequest.getPatronymic()+",\n Укажите электронную почту и пароль");

    }

    public void registrationUser(View view){
        if (UiRegistration.checkOfNull(linearLayoutEditContent,RegisterActivity.this)){
            createСredentials(userRequest);
            userService.saveUser(userRequest,RegisterActivity.this);
            goAuthActivity(view);
        }
    }

    public void createСredentials(User userRequest){
        userRequest.setEmail(useremail.getText().toString());
        userRequest.setPassword(userpassword.getText().toString());
    }

    public void goAuthActivity(View view){
        Intent authIntent = new Intent(this, AuthenticationActivity.class);
        startActivity(authIntent);
        this.finish();
    }
}