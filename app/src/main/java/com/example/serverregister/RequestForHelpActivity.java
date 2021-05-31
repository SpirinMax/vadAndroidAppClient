package com.example.serverregister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import entites.RequestForHelp;
import entites.User;
import retrofit.ApiClient;
import retrofit.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.RequestForHelpService;

public class RequestForHelpActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Context thisContext;
    private View thisView;
    private BehaviorActivity behaviorActivity;
    private final ServerError serverError = new ServerError();
    private final SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();

    private ListView listParticipants;
    private RequestForHelp requestForHelp = new RequestForHelp();
    private User owner;
    RequestForHelpService baseRequestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_help);
        getSupportActionBar().hide();

        thisContext = this;
        fragmentManager = getSupportFragmentManager();
        behaviorActivity = new BehaviorActivity(thisContext, fragmentManager);
        listParticipants = findViewById(R.id.listViewParticipantsRequest);

        Bundle selectedRequestForHelp = getIntent().getExtras();
        requestForHelp = (RequestForHelp) selectedRequestForHelp.getSerializable(RequestForHelp.class.getSimpleName());

        ViewGroup parent = (ViewGroup) findViewById(R.id.groupElementsRequest);
        thisView = LayoutInflater.from(this).inflate(R.layout.activity_request_for_help, parent, true);
        baseRequestService = new RequestForHelpService(requestForHelp);
        baseRequestService.fillTextFields(thisView, listParticipants);

        owner = sharedPreferencesUserInfo.getSavedSettings(thisContext);
        checkOwnerForParticipation();
    }

    public void participateInRequest(View view) {
        int idOwner = owner.getId();
        int idRequest = requestForHelp.getId();
        if (baseRequestService.checkOwnerInAuthorRequest(owner) && baseRequestService.checkOwnerInListParticipants(owner)) {
            Call<RequestForHelp> newParticipantCall = ApiClient.getUserService().becomeParticipant(idOwner, idRequest);
            newParticipantCall.enqueue(new Callback<RequestForHelp>() {
                @Override
                public void onResponse(Call<RequestForHelp> call, Response<RequestForHelp> response) {
                    int serverStatusCode = response.code();
                    if (response.isSuccessful()) {
                        RequestForHelp editedRequest = response.body();
                        requestForHelp = editedRequest;
                        baseRequestService = new RequestForHelpService(editedRequest);
                        baseRequestService.fillTextFields(thisView, listParticipants);
                        checkOwnerForParticipation();

                        behaviorActivity.goInActivity(StartActivity.class);
                        finish();
                    } else {
                        serverError.handleError(serverStatusCode, behaviorActivity);
                    }
                }

                @Override
                public void onFailure(Call<RequestForHelp> call, Throwable t) {
                    ServerError.DisplayDialogLossConnection(thisContext, fragmentManager);
                }
            });
        } else if (!baseRequestService.checkOwnerInListParticipants(owner)) {
            Call<RequestForHelp> deleteParticipantCall = ApiClient.getUserService().cancelParticipationInRequest(idOwner, idRequest);
            deleteParticipantCall.enqueue(new Callback<RequestForHelp>() {
                @Override
                public void onResponse(Call<RequestForHelp> call, Response<RequestForHelp> response) {
                    int serverStatusCode = response.code();
                    if (response.isSuccessful()) {
                        RequestForHelp editedRequest = response.body();
                        requestForHelp = editedRequest;
                        baseRequestService = new RequestForHelpService(editedRequest);
                        baseRequestService.fillTextFields(thisView, listParticipants);
                        checkOwnerForParticipation();
                    } else {
                        serverError.handleError(serverStatusCode, behaviorActivity);
                    }
                }

                @Override
                public void onFailure(Call<RequestForHelp> call, Throwable t) {
                    ServerError.DisplayDialogLossConnection(thisContext, fragmentManager);
                }
            });
        }
    }

    public void choosePhotoForPhotoReport(View view) {
        Intent photoReportIntent = new Intent(thisContext, PhotoReportCreatingActivity.class);
        behaviorActivity.sendDataInActivity(photoReportIntent, RequestForHelp.class.getSimpleName(), requestForHelp);
    }

    private void checkOwnerForParticipation() {
        Button buttonParticipant = findViewById(R.id.buttonParticipant);
        TextView labelAfterButton = findViewById(R.id.labelAfterButton);

        if (!baseRequestService.checkOwnerInAuthorRequest(owner)) {
            buttonParticipant.setBackgroundColor(getResources().getColor(R.color.white_red));
            labelAfterButton.setText("Вы автор заявки!");
        } else if (!baseRequestService.checkOwnerInListParticipants(owner)) {
            buttonParticipant.setBackgroundColor(getResources().getColor(R.color.white_red));
            buttonParticipant.setText("Отменить участие");
            labelAfterButton.setText("Вы уже участник заявки!");
        } else if (baseRequestService.checkOwnerInAuthorRequest(owner) && baseRequestService.checkOwnerInListParticipants(owner)) {
            buttonParticipant.setBackgroundColor(getResources().getColor(R.color.white_green));
            buttonParticipant.setText("Принять участие");
            labelAfterButton.setText("");
        }

    }

}