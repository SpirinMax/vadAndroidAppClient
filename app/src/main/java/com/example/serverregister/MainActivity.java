package com.example.serverregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ui.registration.UiRegistration;

public class MainActivity extends AppCompatActivity {

    EditText userfirstname,userlastname,userpatronymic;
    Button buttonFullName;
    LinearLayout linearLayoutEditContent;
    User userRequest;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //скрыть tollbar

        userRequest = new User();
        intent= new Intent(this, RegisterActivity.class);

        userfirstname = findViewById(R.id.userfirstname);
        userlastname = findViewById(R.id.userlastname);
        userpatronymic=findViewById(R.id.userpatronymic);
        buttonFullName = findViewById(R.id.buttonRegister);
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
        Intent authIntent = new Intent(this, AuthenticationActivity.class);
        startActivity(authIntent);
    }
//    public UserRequest createRequest(){
//        UserRequest userRequest = new UserRequest();
//        userRequest.setUserfirstname(userfirstname.getText().toString());
//        userRequest.setUserlastname(userlastname.getText().toString());
//        userRequest.setUseremail(useremail.getText().toString());
//        userRequest.setUserlogin(userlogin.getText().toString());
//        userRequest.setUserPhone(userphone.getText().toString());
//        userRequest.setUserpassword(userpassword.getText().toString());
//        return userRequest;
//    }
//
//    public void saveUser(UserRequest userRequest){
//        Call<UserResponse> userResponseCall = ApiClient.getUserService().saveUser(userRequest);
//        userResponseCall.enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                if(response.isSuccessful()){
//                    Toast.makeText(MainActivity.this,"Saved successfully",Toast.LENGTH_LONG);
//                    String idUser = String.valueOf(response.body().getId());
//                    id.setText(idUser);
//                } else{
//                    Toast.makeText(MainActivity.this,"Request failed!!",Toast.LENGTH_LONG);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//                Toast.makeText(MainActivity.this,"Request failed!! "+t.getLocalizedMessage(),Toast.LENGTH_LONG);
//            }
//        });
//    }
//
}