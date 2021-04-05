package com.example.serverregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText useremail,userpassword;
    Button buttonRegister;
    TextView textviewHello;
    User userRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        useremail = findViewById(R.id.useremail);
        userpassword = findViewById(R.id.userpassword);
        textviewHello = findViewById(R.id.textviewHello);

        Bundle userFullname = getIntent().getExtras();
        userRequest = (User) userFullname.getSerializable(User.class.getSimpleName());

        textviewHello.setText(userRequest.getFirstname()+" "+userRequest.getPatronymic()+",\n Укажите электронную почту и пароль");

    }

    public void registrationUser(View view){
        createСredentials(userRequest);
        saveUser(userRequest);
        goAuthActivity(view);
        //TODO Добавить функцию отключения переключения "назад", чтобы нельзя было вернуться к кнопке регистрации
    }

    public void createСredentials(User userRequest){
        userRequest.setEmail(useremail.getText().toString());
        userRequest.setPassword(userpassword.getText().toString());
    }

    public void saveUser(User userRequest){
        Call<User> userResponseCall = ApiClient.getUserService().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Запрос успешно отправлен!",Toast.LENGTH_SHORT).show();
                    String idUser = String.valueOf(response.body().getId());
                    textviewHello.setText(idUser);
                } else{
                    Toast.makeText(RegisterActivity.this,"Некорректный запрос!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Некорректный запрос!" + t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goAuthActivity(View view){
        Intent authIntent = new Intent(this, AuthenticationActivity.class);
        startActivity(authIntent);
    }
}