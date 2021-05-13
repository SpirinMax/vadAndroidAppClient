package com.example.serverregister;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import entites.RequestForHelp;
import entites.User;
import retrofit.ApiClient;
import retrofit.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;
import ui.listview_adapters.ParticipantRequest;
import ui.listview_adapters.ParticipantRequestAdapter;

public class RequestForHelpActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Context thisContext;
    private BehaviorActivity behaviorActivity;
    private final ServerError serverError = new ServerError();
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();

    private ListView listParticipants;
    RequestForHelp requestForHelp = new RequestForHelp();
    private User owner;

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
        fillTextFields(requestForHelp);
        owner = sharedPreferencesUserInfo.getSavedSettings(thisContext);
        checkOwnerForParticipation();

    }

    public void participateInRequest(View view) {
        int idOwner = owner.getId();
        int idRequest = requestForHelp.getId();
        if (checkOwnerInAuthorRequest() && checkOwnerInListParticipants()) {
            Call<RequestForHelp> newParticipantCall = ApiClient.getUserService().becomeParticipant(idOwner, idRequest);
            newParticipantCall.enqueue(new Callback<RequestForHelp>() {
                @Override
                public void onResponse(Call<RequestForHelp> call, Response<RequestForHelp> response) {
                    int serverStatusCode = response.code();
                    if (response.isSuccessful()) {
                        RequestForHelp editedRequest = response.body();
                        requestForHelp = editedRequest;
                        fillTextFields(editedRequest);
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
        } else if (!checkOwnerInListParticipants()) {
            Call<RequestForHelp> deleteParticipantCall = ApiClient.getUserService().cancelParticipationInrequest(idOwner, idRequest);
            deleteParticipantCall.enqueue(new Callback<RequestForHelp>() {
                @Override
                public void onResponse(Call<RequestForHelp> call, Response<RequestForHelp> response) {
                    int serverStatusCode = response.code();
                    if (response.isSuccessful()) {
                        RequestForHelp editedRequest = response.body();
                        requestForHelp = editedRequest;
                        fillTextFields(editedRequest);
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

    private void checkOwnerForParticipation() {
        Button buttonParticipant = findViewById(R.id.buttonParticipant);
        TextView labelAfterButton = findViewById(R.id.labelAfterButton);

        if (!checkOwnerInAuthorRequest()) {
            buttonParticipant.setBackgroundColor(getResources().getColor(R.color.white_red));
            labelAfterButton.setText("Вы автор заявки!");
        } else if (!checkOwnerInListParticipants()) {
            buttonParticipant.setBackgroundColor(getResources().getColor(R.color.white_red));
            buttonParticipant.setText("Отменить участие");
            labelAfterButton.setText("Вы уже участник заявки!");
        } else if (checkOwnerInAuthorRequest() && checkOwnerInListParticipants()) {
            buttonParticipant.setBackgroundColor(getResources().getColor(R.color.white_green));
            buttonParticipant.setText("Принять участие");
            labelAfterButton.setText("");
        }

    }

    private Boolean checkOwnerInAuthorRequest() {
        int idOwner = owner.getId();
        int idAuthorRequest = requestForHelp.getAuthorUser().getId();
        if (idOwner == idAuthorRequest) {
            return false;
        } else {
            return true;
        }
    }

    private Boolean checkOwnerInListParticipants() {
        int idOwner = owner.getId();
        List<User> participants = new ArrayList<>(requestForHelp.getParticipants());
        byte count = 0;
        for (byte i = 0; i < participants.size(); i++) {
            int idPart = participants.get(i).getId();
            if (idOwner == idPart) {
                break;
            } else {
                count++;
            }
        }

        if (count == participants.size()) {
            return true;
        } else {
            return false;
        }
    }

    private void fillTextFields(RequestForHelp requestForHelp) {
        TextView nameRequest, cityInRequest, streetInRequest, houseInRequest, dateAndTimeRequest, descriptionRequest, countParticipants;
        nameRequest = findViewById(R.id.nameRequest);
        cityInRequest = findViewById(R.id.cityInRequest);
        streetInRequest = findViewById(R.id.streetInRequest);
        houseInRequest = findViewById(R.id.houseInRequest);
        dateAndTimeRequest = findViewById(R.id.dateAndTimeRequest);
        descriptionRequest = findViewById(R.id.descriptionRequest);
        countParticipants = findViewById(R.id.countParticipants);

        nameRequest.setText(requestForHelp.getName());
        cityInRequest.setText(requestForHelp.getCity() + ",");
        streetInRequest.setText(requestForHelp.getStreet() + ",");
        houseInRequest.setText(requestForHelp.getHouseNumber());
        dateAndTimeRequest.setText(UserService.receiveStringDateTime(requestForHelp.getStartDate()));
        descriptionRequest.setText(requestForHelp.getDescription());
        countParticipants.setText("(" + UserService.receiveCountParticipants(requestForHelp.getParticipants()) + " чел.)");
        fillParticipantsInRequest(requestForHelp.getParticipants());
    }

    private void fillParticipantsInRequest(Set<User> participants) {
        if (participants.size() != 0) {
            List<User> listPartFromRequest = new ArrayList<User>(participants);
            List<ParticipantRequest> listPart = new ArrayList<ParticipantRequest>();
            for (int i = 0; i < listPartFromRequest.size(); i++) {
                Bitmap imagePart = BitmapFactory.decodeResource(getResources(), R.drawable.empty_image);
                if (listPartFromRequest.get(i).getPhoto() != null) {
                    byte[] photo = listPartFromRequest.get(i).getPhoto();
                    imagePart = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                }
                String surname = listPartFromRequest.get(i).getFirstname();
                String name = listPartFromRequest.get(i).getLastname();
                ParticipantRequest participant = new ParticipantRequest(imagePart, name, surname);
                listPart.add(participant);
            }
            ParticipantRequestAdapter participantRequestAdapter =
                    new ParticipantRequestAdapter(thisContext, R.layout.participants_listview_layout, listPart);
            listParticipants.setAdapter(participantRequestAdapter);
        } else {
            List<ParticipantRequest> listPart = new ArrayList<ParticipantRequest>();
            ParticipantRequestAdapter participantRequestAdapter =
                    new ParticipantRequestAdapter(thisContext, R.layout.participants_listview_layout, listPart);
            listParticipants.setAdapter(participantRequestAdapter);
        }
    }

}