package com.example.serverregister;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import entites.ParametersRequestForQuest;
import entites.RequestForHelp;
import entites.User;
import retrofit.ApiClient;
import retrofit.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ui.TransitIconToolbar;
import ui.errorsServer.RefreshInActivity;
import ui.adapters.RequestsForHelpAdapter;
import ui.registration.TransitToRegistration;

public class StartActivity extends AppCompatActivity implements RefreshInActivity, TransitToRegistration, TransitIconToolbar {
    private FragmentManager fragmentManager;
    private Context thisContext;
    private BehaviorActivity behaviorActivity;
    private ListView listViewRequests;
    private final ServerError serverError = new ServerError();
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private List<RequestForHelp> listRequests = new ArrayList<RequestForHelp>();
    private ParametersRequestForQuest parametersQuest = new ParametersRequestForQuest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        thisContext = this;
        fragmentManager = getSupportFragmentManager();
        behaviorActivity = new BehaviorActivity(thisContext, fragmentManager);
        listViewRequests = findViewById(R.id.listViewRequests);
        displayActivePage();

        if (sharedPreferencesUserInfo.checkPresenceSettings(StartActivity.this)) {
            Bundle parametersQuestBundle = getIntent().getExtras();
            if (parametersQuestBundle != null) {
                parametersQuest = (ParametersRequestForQuest) parametersQuestBundle.getSerializable(ParametersRequestForQuest.class.getSimpleName());
                Call<List<RequestForHelp>> QuestRequestsCall = ApiClient.getUserService().findRequestByParameters(parametersQuest);
                QuestRequestsCall.enqueue(new Callback<List<RequestForHelp>>() {
                    @Override
                    public void onResponse(Call<List<RequestForHelp>> call, Response<List<RequestForHelp>> response) {
                        int serverStatusCode = response.code();
                        View footerAfterListView = getLayoutInflater().inflate(R.layout.footer_listvew_stub, null);
                        listViewRequests.addFooterView(footerAfterListView, "ff", true);
                        if (response.isSuccessful()) {
                            listRequests = response.body();
                            RequestsForHelpAdapter requestsAdapter =
                                    new RequestsForHelpAdapter(thisContext, R.layout.list_requests_layout, listRequests);
                            listViewRequests.setAdapter(requestsAdapter);

                            clickRequestInListView();
                        } else {
                            serverError.handleError(serverStatusCode, behaviorActivity);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RequestForHelp>> call, Throwable t) {
                        ServerError.DisplayDialogLossConnection(thisContext, fragmentManager);
                    }
                });
            } else {
                displayAllRequests();
            }
        } else {
            Toast.makeText(this, R.string.needAuth, Toast.LENGTH_SHORT).show();
        }
    }

    public void goProfileActivity(View view) {
        behaviorActivity.goInActivity(ProfileActivity.class);
    }

    @Override
    public void goListRequests(View view) {
        //это данная активность
    }

    @Override
    public void goHistory(View view) {

    }

    public void goRequestCreationActivity(View view) {
        behaviorActivity.goInActivity(RequestCreationStageActivity.class);
    }

    public void refreshActivity() {
        this.finish();
        startActivity(getIntent());
    }

    @Override
    public void moveToRegistration() {
        behaviorActivity.goInActivity(AuthenticationActivity.class);
    }

    public void goQuestActivity(View view) {
        behaviorActivity.goInActivity(QuestByParametersActivity.class);
    }

    public void displayAllRequestsClick(View view) {
        displayAllRequests();
    }

    public void displayRequestWhereIParticipant(View view) {
        User owner = sharedPreferencesUserInfo.getSavedSettings(thisContext);
        int idOwnerUser = owner.getId();
        Call<List<RequestForHelp>> RequestsForPartCall = ApiClient.getUserService().receiveRequestsForParticipant(idOwnerUser);
        RequestsForPartCall.enqueue(new Callback<List<RequestForHelp>>() {
            @Override
            public void onResponse(Call<List<RequestForHelp>> call, Response<List<RequestForHelp>> response) {
                int serverStatusCode = response.code();
                View footerAfterListView = getLayoutInflater().inflate(R.layout.footer_listvew_stub, null);
                displayActivePage();
                listViewRequests.addFooterView(footerAfterListView, "ff", true);
                if (response.isSuccessful()) {
                    paintColorTabItem(findViewById(R.id.iPartRequest));
                    listRequests = response.body();
                    RequestsForHelpAdapter requestsAdapter =
                            new RequestsForHelpAdapter(thisContext, R.layout.list_requests_layout, listRequests);
                    listViewRequests.setAdapter(requestsAdapter);

                    clickRequestInListView();

                } else {
                    serverError.handleError(serverStatusCode, behaviorActivity);
                }
            }

            @Override
            public void onFailure(Call<List<RequestForHelp>> call, Throwable t) {
                ServerError.DisplayDialogLossConnection(thisContext, fragmentManager);
            }
        });
    }

    public void displayRequestWhereIAuthor(View view) {
        User owner = sharedPreferencesUserInfo.getSavedSettings(thisContext);
        int idOwnerUser = owner.getId();
        Call<List<RequestForHelp>> RequestsForPartCall = ApiClient.getUserService().receiveRequestsForAuthor(idOwnerUser);
        RequestsForPartCall.enqueue(new Callback<List<RequestForHelp>>() {
            @Override
            public void onResponse(Call<List<RequestForHelp>> call, Response<List<RequestForHelp>> response) {
                int serverStatusCode = response.code();
                View footerAfterListView = getLayoutInflater().inflate(R.layout.footer_listvew_stub, null);
                listViewRequests.addFooterView(footerAfterListView, "ff", true);
                if (response.isSuccessful()) {
                    paintColorTabItem(findViewById(R.id.iAuthorRequest));
                    listRequests = response.body();
                    RequestsForHelpAdapter requestsAdapter =
                            new RequestsForHelpAdapter(thisContext, R.layout.list_requests_layout, listRequests);
                    listViewRequests.setAdapter(requestsAdapter);

                    clickRequestInListView();

                } else {
                    serverError.handleError(serverStatusCode, behaviorActivity);
                }
            }

            @Override
            public void onFailure(Call<List<RequestForHelp>> call, Throwable t) {
                ServerError.DisplayDialogLossConnection(thisContext, fragmentManager);
            }
        });
    }

    private void displayAllRequests() {
        Call<List<RequestForHelp>> allRequestCall = ApiClient.getUserService().receiveListAllRequests();
        allRequestCall.enqueue(new Callback<List<RequestForHelp>>() {
            @Override
            public void onResponse(Call<List<RequestForHelp>> call, Response<List<RequestForHelp>> response) {
                int serverStatusCode = response.code();
                View footerAfterListView = getLayoutInflater().inflate(R.layout.footer_listvew_stub, null);
                listViewRequests.addFooterView(footerAfterListView, "ff", true);
                if (response.isSuccessful()) {
                    paintColorTabItem(findViewById(R.id.allRequest));
                    listRequests = response.body();
                    RequestsForHelpAdapter requestsAdapter =
                            new RequestsForHelpAdapter(thisContext, R.layout.list_requests_layout, listRequests);
                    listViewRequests.setAdapter(requestsAdapter);

                    clickRequestInListView();

                } else {
                    serverError.handleError(serverStatusCode, behaviorActivity);
                }
            }

            @Override
            public void onFailure(Call<List<RequestForHelp>> call, Throwable t) {
                ServerError.DisplayDialogLossConnection(thisContext, fragmentManager);
                t.printStackTrace();
            }
        });
    }

    private void clickRequestInListView() {
        listViewRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent requestIntent = new Intent(thisContext, RequestForHelpActivity.class);
                RequestForHelp selectedRequestForHelp = (RequestForHelp) parent.getAdapter().getItem(position);
                behaviorActivity.sendDataInActivity(requestIntent, RequestForHelp.class.getSimpleName(), selectedRequestForHelp);
            }
        });
    }

    private void displayActivePage() {
        RelativeLayout containerIcon = findViewById(R.id.containerIconRequest);
        ImageButton imageButton = findViewById(R.id.iconRequests);
        TextView IconTextView = findViewById(R.id.textViewRequest);
        imageButton.setEnabled(false);
        IconTextView.setEnabled(false);
        Resources resources = getResources();
        imageButton.setBackgroundColor(resources.getColor(R.color.activeIcon));
        containerIcon.setBackgroundColor(resources.getColor(R.color.activeIcon));
        IconTextView.setTextColor(resources.getColor(R.color.red));
    }

    private void paintColorTabItem(TextView textViewInTabMenu) {
        TextView textViewAllRequest, textViewPartRequest, textViewAuthorRequest;
        textViewAllRequest = findViewById(R.id.allRequest);
        textViewPartRequest = findViewById(R.id.iPartRequest);
        textViewAuthorRequest = findViewById(R.id.iAuthorRequest);

        if (textViewInTabMenu == textViewAllRequest) {
            textViewInTabMenu.setBackgroundResource(R.drawable.tab_menu_item_click);
            textViewPartRequest.setBackgroundColor(getResources().getColor(R.color.white));
            textViewAuthorRequest.setBackgroundColor(getResources().getColor(R.color.white));
        } else if (textViewInTabMenu == textViewPartRequest) {
            textViewInTabMenu.setBackgroundResource(R.drawable.tab_menu_item_click);
            textViewAllRequest.setBackgroundColor(getResources().getColor(R.color.white));
            textViewAuthorRequest.setBackgroundColor(getResources().getColor(R.color.white));
        } else if (textViewInTabMenu == textViewAuthorRequest) {
            textViewInTabMenu.setBackgroundResource(R.drawable.tab_menu_item_click);
            textViewAllRequest.setBackgroundColor(getResources().getColor(R.color.white));
            textViewPartRequest.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }
}