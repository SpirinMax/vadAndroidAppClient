package com.example.serverregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationActivity extends AppCompatActivity {
    EditText useremail,userpassword;
    Button buttonAuth;
    TextView textviewHello;
    User userRequest = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        useremail = findViewById(R.id.useremail);
        userpassword = findViewById(R.id.userpassword);
        textviewHello = findViewById(R.id.textviewHello);
    }

    public void authenticationUser(View view){
        createCredentials(userRequest);
        sendCredentialsData(userRequest);
    }

    public void createCredentials(User userRequest){
        userRequest.setEmail(useremail.getText().toString());
        userRequest.setPassword(userpassword.getText().toString());
    }

    public void sendCredentialsData(User userRequest){
        Call<User> userResponseCall = ApiClient.getUserService().sendCredentialsData(userRequest);
        userResponseCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(AuthenticationActivity.this, "Успешная авторизация",Toast.LENGTH_SHORT).show();
                    String name = String.valueOf(response.body().getFirstname())+ " "+String.valueOf(response.body().getLastname());
                    textviewHello.setText(name);
                } else{
                    Toast.makeText(AuthenticationActivity.this,"Неверный логин и пароль",Toast.LENGTH_SHORT).show();
                    String name = String.valueOf(response.code());
                    textviewHello.setText(name);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AuthenticationActivity.this,"Некорректный запрос!" + t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void goRegisterActivity (View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}

