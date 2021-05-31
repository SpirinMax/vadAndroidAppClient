package com.example.serverregister;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entites.ImagePhotoReport;
import entites.PhotoReport;
import entites.RequestForHelp;
import entites.User;
import retrofit.ApiClient;
import retrofit.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.RequestForHelpService;
import ui.ProcessingImage;
import ui.adapters.PhotoReportImageAdapter;
import ui.registration.UiRegistration;

public class PhotoReportCreatingActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Context thisContext;
    private View thisView;
    private BehaviorActivity behaviorActivity;
    private final ServerError serverError = new ServerError();
    private final SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();

    private RequestForHelp requestForHelp = new RequestForHelp();
    private PhotoReport newPhotoReport = new PhotoReport();
    private List<Bitmap> images = new ArrayList<Bitmap>();
    private User owner, userData;

    private ViewPager viewPagerPhotoReport;
    TextView currentItemViewPagerTextView;

    static final int SELECT_PICTURES = 1;
    static final int COUNT_SELECTED_PHOTO_MAX = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_report_creating);
        getSupportActionBar().hide();

        thisContext = this;
        fragmentManager = getSupportFragmentManager();
        behaviorActivity = new BehaviorActivity(thisContext, fragmentManager);
        viewPagerPhotoReport = findViewById(R.id.viewPagerPhotoReport);
        currentItemViewPagerTextView = findViewById(R.id.currentItemViewPager);
        Button savingPhotoReportButton = findViewById(R.id.buttonSavingPhotoReport);

        ViewGroup parent = (ViewGroup) findViewById(R.id.descRequestInPhotoReport);
        thisView = LayoutInflater.from(this).inflate(R.layout.list_requests_layout, parent, true);

        Bundle requestBundle = getIntent().getExtras();
        if (requestBundle != null) {
            requestForHelp = (RequestForHelp) requestBundle.getSerializable(RequestForHelp.class.getSimpleName());
            //Вставка карточки заявки из главной страницы
            RequestForHelpService requestForHelpService = new RequestForHelpService(requestForHelp);
            requestForHelpService.fillShortTextFields(thisView);

            owner = sharedPreferencesUserInfo.getSavedSettings(thisContext);
            Call<User> userResponseCall = ApiClient.getUserService().loginApp(owner);
            userResponseCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    int serverStatusCode = response.code();
                    if (response.isSuccessful()) {
                        userData = response.body();
                        int idOwner = userData.getId();
                        if (requestForHelpService.checkOwnerInAuthorsPhotoReports(idOwner, requestForHelp.getPhotoReports())) {
                            savingPhotoReportButton.setBackgroundColor(getResources().getColor(R.color.white_red));
                        }
                    } else {
                        serverError.handleError(serverStatusCode, behaviorActivity);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        }
    }


    public void goPreviousPhoto(View view) {
        if (images.size() > 1) {
            int currentItem = viewPagerPhotoReport.getCurrentItem();
            if (currentItem == 0) {
                currentItem = COUNT_SELECTED_PHOTO_MAX;
            }
            viewPagerPhotoReport.setCurrentItem(currentItem - 1);
        }
    }

    public void goNextPhoto(View view) {
        if (images.size() > 1) {
            int currentItem = viewPagerPhotoReport.getCurrentItem();
            if (currentItem == COUNT_SELECTED_PHOTO_MAX - 1) {
                currentItem = -1;
            }
            viewPagerPhotoReport.setCurrentItem(currentItem + 1);
        }
    }

    public void addPhotos(View view) {
        if (images.size() > 0) {
            images.clear();
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
    }

    public void savePhotoReport(View view) {
        RequestForHelpService requestForHelpService = new RequestForHelpService(requestForHelp);
        LinearLayout containerDescriptionPhotoReport = findViewById(R.id.containerDescriptionPhotoReport);
        if (!requestForHelpService.checkOwnerInAuthorsPhotoReports(owner.getId(), requestForHelp.getPhotoReports())) {
            if (UiRegistration.checkOfNull(containerDescriptionPhotoReport, thisContext)) {
                if (newPhotoReport.getImages() != null) {
                    RequestForHelp requestWithPhotoReport = receiveRequestWithNewPhotoReport(newPhotoReport);
                    Call<RequestForHelp> newPhotoReportCall = ApiClient.getUserService().createPhotoReport(requestWithPhotoReport);
                    newPhotoReportCall.enqueue(new Callback<RequestForHelp>() {
                        @Override
                        public void onResponse(Call<RequestForHelp> call, Response<RequestForHelp> response) {
                            requestForHelp = response.body();
                            int serverStatusCode = response.code();
                            if (response.isSuccessful()) {
                                behaviorActivity.goInActivity(StartActivity.class);
                                Toast.makeText(thisContext, "УСПЕХ!", Toast.LENGTH_LONG).show();
                            } else {
                                serverError.handleError(serverStatusCode, behaviorActivity);
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestForHelp> call, Throwable t) {
                            ServerError.DisplayDialogLossConnection(thisContext, fragmentManager);
                        }
                    });
                } else {
                    Toast.makeText(thisContext, "Добавте фотографии к фотоотчету!", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(thisContext, "Вы уже сделали фотоотчет к этой заявке", Toast.LENGTH_LONG).show();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onActivityResult(int requestCode, int resultCode, Intent
            imagesReturnedIntent) {
        Set<ImagePhotoReport> hashSetPhotoInPhotoReport = new HashSet<ImagePhotoReport>();
        if (requestCode == SELECT_PICTURES) {
            if (resultCode == RESULT_OK) {
                try {
                    if (imagesReturnedIntent.getClipData() != null) {
                        int count = imagesReturnedIntent.getClipData().getItemCount();
                        int currentItem = 0;
                        int byteSize = 0;
                        if (count <= COUNT_SELECTED_PHOTO_MAX) {
                            while (currentItem < count) {
                                Uri imageUri = imagesReturnedIntent.getClipData().getItemAt(currentItem).getUri();
                                appendPhotoInPhotoReport(imageUri, images);
                                byte[] currentImageByte = ProcessingImage.receiveByteArrayUserPhoto(images.get(currentItem));
                                ImagePhotoReport imagePhotoReport = new ImagePhotoReport(currentImageByte);
                                hashSetPhotoInPhotoReport.add(imagePhotoReport);
                                currentItem = currentItem + 1;
                            }
                            Log.d("log", String.format("byte[]Size = %s", byteSize));
                            fillViewPager();
                            newPhotoReport.setImages(hashSetPhotoInPhotoReport);
                        } else {
                            Toast.makeText(thisContext, "Максимальное количество фото - "
                                    + String.valueOf(COUNT_SELECTED_PHOTO_MAX), Toast.LENGTH_LONG).show();
                            images.clear();
                        }

                    } else if (imagesReturnedIntent.getData() != null) {
                        Uri imageUri = imagesReturnedIntent.getData();
                        appendPhotoInPhotoReport(imageUri, images);

                        byte[] currentImageByte = ProcessingImage.receiveByteArrayUserPhoto(images.get(0));
                        ImagePhotoReport imagePhotoReport = new ImagePhotoReport(currentImageByte);
                        hashSetPhotoInPhotoReport.add(imagePhotoReport);

                        fillViewPager();
                        newPhotoReport.setImages(hashSetPhotoInPhotoReport);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, imagesReturnedIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void appendPhotoInPhotoReport(Uri imageUri, List<Bitmap> images) throws
            IOException {
        Bitmap selectedImageInGallery = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        selectedImageInGallery = ProcessingImage.decodeSampledBitmap(imageUri, 200, 200, thisContext);
        selectedImageInGallery = ProcessingImage.receiveRotatedImage(imageUri, selectedImageInGallery, thisContext);
        byte[] byteImage = ProcessingImage.receiveByteArrayUserPhoto(selectedImageInGallery);
        images.add(ProcessingImage.receiveBitmapFromByteArray(byteImage));
    }

    private void fillViewPager() {
        PhotoReportImageAdapter imageAdapter = new PhotoReportImageAdapter(thisContext, images);
        viewPagerPhotoReport.setAdapter(imageAdapter);
        int currentItem = viewPagerPhotoReport.getCurrentItem();
        currentItemViewPagerTextView.setText((currentItem + 1) + "/" + imageAdapter.getCount());
        viewPagerPhotoReport.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItemViewPagerTextView.setText((position + 1) + "/" + imageAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private RequestForHelp receiveRequestWithNewPhotoReport(PhotoReport photoReport) {
        EditText namePhotoReportEditText = findViewById(R.id.namePhotoReport);
        EditText descriptionPhotoReportEditText = findViewById(R.id.descriptionPhotoReport);
        String namePhotoReport = namePhotoReportEditText.getText().toString();
        String descriptionPhotoReport = descriptionPhotoReportEditText.getText().toString();

        photoReport.setAuthorReport(userData);
        photoReport.setName(namePhotoReport);
        photoReport.setDescription(descriptionPhotoReport);
        photoReport.setCreationDate(Calendar.getInstance());

        RequestForHelp requestWithPhotoReport = requestForHelp;
        Set<PhotoReport> newHashSetPhotoReports = requestForHelp.getPhotoReports();
        newHashSetPhotoReports.clear();
        newHashSetPhotoReports.add(photoReport);
        requestWithPhotoReport.setPhotoReports(newHashSetPhotoReports);
        return requestWithPhotoReport;
    }


}