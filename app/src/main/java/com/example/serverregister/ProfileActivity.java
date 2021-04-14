package com.example.serverregister;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

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