package com.example.serverregister;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;

import entites.User;
import service.UserService;

public class ProfileActivity extends AppCompatActivity {
    Context thisContext;
    FragmentManager fragmentManager;
    SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    BehaviorActivity behaviorActivity;
    private User userData = new User();
    private User dataRequestUpdateProfile = new User();
    private UserService userService = new UserService();
    private Intent updateUserDataIntent;
    TextView surnamenameTextview, patronymicTextview, labelCountHelp;
    ImageView imageViewPhotoUser;
    static final int GALLERY_REQUEST = 1;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        thisContext = this;
        fragmentManager = getSupportFragmentManager();
        displayActivePage();
        imageViewPhotoUser = findViewById(R.id.imageViewPhotoUser);
        surnamenameTextview = findViewById(R.id.surnamenameTextview);
        patronymicTextview = findViewById(R.id.patronymicTextview);
        labelCountHelp = findViewById(R.id.labelCountHelp);
        behaviorActivity = new BehaviorActivity(thisContext, fragmentManager);

        Bundle userDataBundle = getIntent().getExtras();
        userData = (User) userDataBundle.getSerializable(User.class.getSimpleName());
        dataRequestUpdateProfile = userData;

        byte[] photoUserByte = userData.getPhoto();
        if (photoUserByte != null) {
            Bitmap photoUserBitmap = userService.receiveBitmapFromByteArray(photoUserByte);
            imageViewPhotoUser.setImageBitmap(photoUserBitmap);
        }

        imageViewPhotoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        surnamenameTextview.setText(userData.getFirstname() + " " + userData.getLastname());
        patronymicTextview.setText(userData.getPatronymic());
        labelCountHelp.setText(String.valueOf(userData.getHelpcounter()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap selectedImageInGallery = null;
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uriRequiredImage = imageReturnedIntent.getData();
                try {
                    selectedImageInGallery = MediaStore.Images.Media.getBitmap(getContentResolver(), uriRequiredImage);
                    Bitmap editedPhotoUser = userService.receiveEditedPhotoUser(selectedImageInGallery, uriRequiredImage, behaviorActivity);
                    imageViewPhotoUser.setImageBitmap(editedPhotoUser);
                    byte[] photoUserInByteArray = userService.receiveByteArrayUserPhoto(editedPhotoUser);
                    dataRequestUpdateProfile.setPhoto(photoUserInByteArray);
                } catch (IOException e) {
                    Toast.makeText(thisContext, "Не удалось загрузить картинку из галереи", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void goUserDataEditingActivity(View view) {
        updateUserDataIntent = new Intent(thisContext, UpdatingUserDataActivity.class);
        behaviorActivity.receiveDataInActivity(updateUserDataIntent, User.class.getSimpleName(), dataRequestUpdateProfile);
    }

    private void displayActivePage() {
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