package com.example.serverregister;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
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
import ui.registration.UiRegistration;


public class RegisterActivity extends AppCompatActivity implements RefreshInActivity {
    private Context thisContext;
    private FragmentManager fragmentManager;
    private BehaviorActivity behaviorActivity;
    private EditText useremail,userpassword;
    private TextView textviewHello;
    private User userRequest;
    private LinearLayout linearLayoutEditContent;
    private UserService userService = new UserService();
    private ServerError serverError = new ServerError();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        thisContext=this;
        fragmentManager = getSupportFragmentManager();
        behaviorActivity=new BehaviorActivity(thisContext,fragmentManager);

        useremail = findViewById(R.id.useremail);
        userpassword = findViewById(R.id.userpassword);
        textviewHello = findViewById(R.id.textviewHello);
        linearLayoutEditContent = findViewById(R.id.linearLayoutEditContent);

        Bundle userFullname = getIntent().getExtras();
        userRequest = (User) userFullname.getSerializable(User.class.getSimpleName());

        textviewHello.setText(userRequest.getFirstname()+" "+userRequest.getPatronymic()+",\n Укажите электронную почту и пароль");

    }

    public void registrationUser(View view){
        if (UiRegistration.checkOfNull(linearLayoutEditContent,thisContext)){
            userService.createCredentials(userRequest,useremail.getText().toString(),userpassword.getText().toString());
            Call<User> userResponseCall = ApiClient.getUserService().saveUser(userRequest);
            userResponseCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    userService.saveUser(response,behaviorActivity);
//                    behaviorActivity.goInActivity(AuthenticationActivity.class);
//                    finish();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    serverError.DisplayDialogLossConnection(thisContext,fragmentManager);
                }
            });

        }
    }

    public void refreshActivity(){
        this.finish();
        startActivity(getIntent());
    }


    public void goAuthActivity(View view) {
        behaviorActivity.goInActivity(AuthenticationActivity.class);
    }
}