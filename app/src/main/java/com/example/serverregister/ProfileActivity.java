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
import retrofit.ApiClient;
import retrofit.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;
import ui.TransitIconToolbar;
import ui.errorsServer.RefreshInActivity;
import ui.registration.TransitToRegistration;

public class ProfileActivity extends AppCompatActivity implements RefreshInActivity, TransitToRegistration, TransitIconToolbar {
    Context thisContext;
    FragmentManager fragmentManager;
    SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    BehaviorActivity behaviorActivity;
    private User authRequestUser = new User();
    private User userData = new User();
    private User dataRequestUpdateProfile = new User();
    private UserService userService = new UserService();
    private Intent updateUserDataIntent;
    private ServerError serverError = new ServerError();
    TextView surnamenameTextview, patronymicTextview, labelCountHelp;
    ImageView imageViewPhotoUser;

    static final int GALLERY_REQUEST = 1;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        thisContext = this;
        fragmentManager = getSupportFragmentManager();
        behaviorActivity = new BehaviorActivity(thisContext, fragmentManager);

        if (sharedPreferencesUserInfo.checkPresenceSettings(thisContext)) {
            authRequestUser = sharedPreferencesUserInfo.getSavedSettings(thisContext);
            Call<User> userResponseCall = ApiClient.getUserService().loginApp(authRequestUser);
            userResponseCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    int serverStatusCode = response.code();
                    if (response.isSuccessful()) {
                        setContentView(R.layout.activity_profile);
                        displayActivePage();
                        imageViewPhotoUser = findViewById(R.id.imageViewPhotoUser);
                        surnamenameTextview = findViewById(R.id.surnamenameTextview);
                        patronymicTextview = findViewById(R.id.patronymicTextview);
                        labelCountHelp = findViewById(R.id.labelCountHelp);

                        userData = userService.receiveUserData(response);
                        byte[] photoUserByte = userData.getPhoto();
                        if (photoUserByte != null) {
                            Bitmap photoUserBitmap = userService.receiveBitmapFromByteArray(photoUserByte);
                            imageViewPhotoUser.setImageBitmap(photoUserBitmap);
                        }

                        dataRequestUpdateProfile = userData;
                        surnamenameTextview.setText(userData.getFirstname() + " " + userData.getLastname());
                        patronymicTextview.setText(userData.getPatronymic());
                        labelCountHelp.setText(String.valueOf(userData.getHelpcounter()));

                        imageViewPhotoUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                            }
                        });

                    } else {
                        serverError.handleError(serverStatusCode, behaviorActivity);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    ServerError.DisplayDialogLossConnection(thisContext, getSupportFragmentManager());
                }
            });
        } else {
            behaviorActivity.goInActivity(UnsuccessfulAythenticationActivity.class);
            finish();
        }
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

    public void goRequestCreationActivity(View view) {
        behaviorActivity.goInActivity(RequestCreationStageActivity.class);
    }

    @Override
    public void goProfileActivity(View view) {
        //это текущая активность
    }

    @Override
    public void goListRequests(View view) {
        behaviorActivity.goInActivity(StartActivity.class);
    }

    @Override
    public void goHistory(View view) {
        //
    }

    public void goUserDataEditingActivity(View view) {
        updateUserDataIntent = new Intent(thisContext, UpdatingUserDataActivity.class);
        behaviorActivity.sendDataInActivity(updateUserDataIntent, User.class.getSimpleName(), dataRequestUpdateProfile);
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

    @Override
    public void refreshActivity() {
        this.finish();
        startActivity(getIntent());
    }

    @Override
    public void moveToRegistration() {
        behaviorActivity.goInActivity(AuthenticationActivity.class);
        finish();
    }
}