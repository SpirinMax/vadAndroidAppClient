package com.example.serverregister;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import entites.User;

public class ProfileActivity extends AppCompatActivity {
    Context thisContext;
    FragmentManager fragmentManager;
    SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private User userData = new User();
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
        imageViewPhotoUser = findViewById(R.id.imageViewPhotoUser);
        surnamenameTextview = findViewById(R.id.surnamenameTextview);
        patronymicTextview = findViewById(R.id.patronymicTextview);
        labelCountHelp = findViewById(R.id.labelCountHelp);

        FileOutputStream fileInputStream = null;

        Bundle userDataBundle = getIntent().getExtras();
        userData = (User) userDataBundle.getSerializable(User.class.getSimpleName());

        byte[] photo = userData.getPhoto();

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

        displayActivePage();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap selectedImageInGallery = null;
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri UriRequiredImage = imageReturnedIntent.getData();
                try {
                    selectedImageInGallery = MediaStore.Images.Media.getBitmap(getContentResolver(), UriRequiredImage);
                    Bitmap reducedImage = reduceImage(selectedImageInGallery);
                    Bitmap finalImage = recieveRotatedImage(UriRequiredImage, reducedImage);
                    imageViewPhotoUser.setImageBitmap(finalImage);
                    userData.setPhoto(receiveByteArrayUserPhoto(finalImage));
                } catch (IOException e) {
                    Toast.makeText(thisContext, "Не удалось загрузить картинку из галереи", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private Bitmap reduceImage(Bitmap basicImage) {
        int WIDTH_BITMAP = basicImage.getWidth() / 10;
        int HEIGHT_BITMAP = basicImage.getHeight() / 10;
        Bitmap reducedImage = Bitmap.createScaledBitmap(basicImage, WIDTH_BITMAP, HEIGHT_BITMAP, false);
        return reducedImage;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Bitmap recieveRotatedImage(Uri selectedImageInGallery, Bitmap noRotatedBitmap) {
        try {
            ExifInterface exifInterface = new ExifInterface(thisContext.getContentResolver().openInputStream(selectedImageInGallery));
            int rotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0f) {
                matrix.setRotate(rotationInDegrees);
                Bitmap finalBitmap = Bitmap.createBitmap(noRotatedBitmap, 0, 0, noRotatedBitmap.getWidth(), noRotatedBitmap.getHeight(), matrix, true);
                return finalBitmap;
            }
        } catch (IOException ex) {
            Log.e("LOG EXIF", "Failed to get Exif data", ex);
            Toast.makeText(thisContext, "Не удалось перевернуть картинку", Toast.LENGTH_SHORT).show();
        }
        return noRotatedBitmap;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private byte[] receiveByteArrayUserPhoto(Bitmap userPhoto) {
        ByteArrayOutputStream imageStream = null;
        byte[] byteUserPhoto = {0};
        try {
            imageStream = new ByteArrayOutputStream();
            userPhoto.compress(Bitmap.CompressFormat.PNG, 100, imageStream);
            byteUserPhoto = imageStream.toByteArray();
            return byteUserPhoto;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteUserPhoto;
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