package com.example.serverregister;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class UpdatingUserDataActivity extends AppCompatActivity implements RefreshInActivity {
    Context thisContext;
    FragmentManager fragmentManager;
    BehaviorActivity behaviorActivity;
    private User dataRequestUpdateProfile = new User();
    private User userDataResponseFromServer = new User();
    private UserService userService = new UserService();
    EditText emailEditText, phoneEditText, firstnameEditText, lastnameEditText, patronymicEditText,
            aboutMeEditText, regionEditText, districtEditText, cityEditText;
    String email, phone, firstname, lastname, patronymic, aboutMe, region, district, city;
    byte[] photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating_user_data);

        thisContext = this;
        fragmentManager = getSupportFragmentManager();
        behaviorActivity = new BehaviorActivity(thisContext, fragmentManager);

        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        firstnameEditText = findViewById(R.id.firstname);
        lastnameEditText = findViewById(R.id.lastname);
        patronymicEditText = findViewById(R.id.patronymic);
        aboutMeEditText = findViewById(R.id.aboutMe);
        regionEditText = findViewById(R.id.region);
        districtEditText = findViewById(R.id.district);
        cityEditText = findViewById(R.id.city);

        Bundle userDataBundle = getIntent().getExtras();
        dataRequestUpdateProfile = (User) userDataBundle.getSerializable(User.class.getSimpleName());

        email = dataRequestUpdateProfile.getEmail();
        phone = dataRequestUpdateProfile.getPhone();
        firstname = dataRequestUpdateProfile.getFirstname();
        lastname = dataRequestUpdateProfile.getLastname();
        patronymic = dataRequestUpdateProfile.getPatronymic();
        aboutMe = dataRequestUpdateProfile.getAboutuser();
        region = dataRequestUpdateProfile.getRegion();
        district = dataRequestUpdateProfile.getDistrict();
        city = dataRequestUpdateProfile.getCity();
        photo = dataRequestUpdateProfile.getPhoto();

        try {
            if (photo != null) {
                Toast.makeText(thisContext, "Чтобы изменить фото нажмите СОХРАНИТЬ ИЗМЕНЕНИЯ", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(thisContext, "БАН ФОТО>", Toast.LENGTH_LONG).show();
        }

        emailEditText.setText(email);
        phoneEditText.setText(phone);
        firstnameEditText.setText(firstname);
        lastnameEditText.setText(lastname);
        patronymicEditText.setText(patronymic);
        aboutMeEditText.setText(aboutMe);
        regionEditText.setText(region);
        districtEditText.setText(district);
        cityEditText.setText(city);
    }

    public void sendEditedUserData(View view) {
        dataRequestUpdateProfile.setEmail(emailEditText.getText().toString());
        dataRequestUpdateProfile.setPhone(phoneEditText.getText().toString());
        dataRequestUpdateProfile.setFirstname(firstnameEditText.getText().toString());
        dataRequestUpdateProfile.setLastname(lastnameEditText.getText().toString());
        dataRequestUpdateProfile.setPatronymic(patronymicEditText.getText().toString());
        dataRequestUpdateProfile.setAboutuser(aboutMeEditText.getText().toString());
        dataRequestUpdateProfile.setRegion(regionEditText.getText().toString());
        dataRequestUpdateProfile.setDistrict(districtEditText.getText().toString());
        dataRequestUpdateProfile.setCity(cityEditText.getText().toString());

        Call<User> userResponseCall = ApiClient.getUserService().updateUserData(dataRequestUpdateProfile);
        userResponseCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userService.updateUserData(response, behaviorActivity);
                if (response.isSuccessful()) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ServerError.DisplayDialogLossConnection(thisContext, getSupportFragmentManager());
            }
        });
    }

    public void refreshActivity() {
        Toast.makeText(thisContext, "Еще раз нажмите СОХРАНИТЬ ИЗМЕНЕНИЯ", Toast.LENGTH_SHORT).show();
    }

}