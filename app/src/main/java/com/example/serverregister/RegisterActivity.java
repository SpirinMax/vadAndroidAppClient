package com.example.serverregister;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import entites.User;
import service.UserService;
import ui.registration.UiRegistration;
import ui.transitions.TransitionActivity;

public class RegisterActivity extends BehaviorActivity {
    EditText useremail,userpassword;
    TextView textviewHello;
    User userRequest;
    LinearLayout linearLayoutEditContent;
    private UserService userService = new UserService();
    TransitionActivity transitionActivity = new TransitionActivity();
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
            userService.createCredentials(userRequest,useremail.getText().toString(),userpassword.getText().toString());
            try {
                userService.saveUser(userRequest,RegisterActivity.this,getSupportFragmentManager());
            } catch (Exception e) {
                e.printStackTrace();
            }
            transitionActivity.goInActivity(this,AuthenticationActivity.class);
            this.finish();
        }
    }

}