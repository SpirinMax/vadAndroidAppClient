package com.example.serverregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import entites.User;
import ui.registration.UiRegistration;

public class MainActivity extends AppCompatActivity {

    EditText userfirstname,userlastname,userpatronymic;
    LinearLayout linearLayoutEditContent;
    User userRequest;
    Intent intent;
    BehaviorActivity behaviorActivity = new BehaviorActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //скрыть tollbar

        behaviorActivity.identifyContext(this);

        userRequest = new User();
        intent= new Intent(this, RegisterActivity.class);

        userfirstname = findViewById(R.id.userfirstname);
        userlastname = findViewById(R.id.userlastname);
        userpatronymic=findViewById(R.id.userpatronymic);
        linearLayoutEditContent= findViewById(R.id.linearLayoutEditContent);

    }
    public void sendNameUser (View view){
        if (UiRegistration.checkOfNull(linearLayoutEditContent,MainActivity.this)){
            userRequest.setFirstname(userfirstname.getText().toString());
            userRequest.setLastname(userlastname.getText().toString());
            userRequest.setPatronymic(userpatronymic.getText().toString());
            intent.putExtra(User.class.getSimpleName(), userRequest);
            startActivity(intent);
        }
    }


    public void goAuthActivity(View view){
        behaviorActivity.goInActivity(AuthenticationActivity.class);
    }

}