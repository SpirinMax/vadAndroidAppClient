package com.example.serverregister;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import entites.User;

public class ProfileActivity extends AppCompatActivity {

    TextView surnamenameTextview,patronymicTextview,labelCountHelp;
    SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private User userData = new User();
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        surnamenameTextview = findViewById(R.id.surnamenameTextview);
        patronymicTextview = findViewById(R.id.patronymicTextview);
        labelCountHelp= findViewById(R.id.labelCountHelp);

        Bundle userDataBundle = getIntent().getExtras();
        userData = (User) userDataBundle.getSerializable(User.class.getSimpleName());

        surnamenameTextview.setText(userData.getFirstname()+" "+userData.getLastname());
        patronymicTextview.setText(userData.getPatronymic());
        labelCountHelp.setText(String.valueOf(userData.getHelpcounter()));

        displayActivePage();

    }




    private void displayActivePage (){
        RelativeLayout containerIcon = findViewById(R.id.profileButton);
        ImageButton ImageButton = findViewById(R.id.profile);
        TextView IconTextView = findViewById(R.id.profileTextview);
        ImageButton.setEnabled(false);
        IconTextView.setEnabled(false);
        Resources resources = getResources();
        ImageButton.setBackgroundColor(resources.getColor(R.color.activeIcon));
        containerIcon.setBackgroundColor(resources.getColor(R.color.activeIcon));
        IconTextView.setTextColor(resources.getColor(R.color.red));
    }
}