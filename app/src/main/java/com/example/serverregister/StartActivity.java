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

import entites.RequestForHelp;
import retrofit.ApiClient;
import retrofit.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ui.TransitIconToolbar;
import ui.errorsServer.RefreshInActivity;
import ui.listview_adapters.RequestsForHelpAdapter;
import ui.registration.TransitToRegistration;

public class StartActivity extends AppCompatActivity implements RefreshInActivity, TransitToRegistration, TransitIconToolbar {
    private FragmentManager fragmentManager;
    private Context thisContext;
    private BehaviorActivity behaviorActivity;
    private ListView listViewRequests;
    private final ServerError serverError = new ServerError();
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private List<RequestForHelp> listRequests = new ArrayList<RequestForHelp>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        thisContext = this;
        fragmentManager = getSupportFragmentManager();
        behaviorActivity = new BehaviorActivity(thisContext, fragmentManager);

        if (sharedPreferencesUserInfo.checkPresenceSettings(StartActivity.this)) {
            listViewRequests = findViewById(R.id.listViewRequests);
            Call<List<RequestForHelp>> allRequestCall = ApiClient.getUserService().receiveListAllRequests();
            allRequestCall.enqueue(new Callback<List<RequestForHelp>>() {
                @Override
                public void onResponse(Call<List<RequestForHelp>> call, Response<List<RequestForHelp>> response) {

                    int serverStatusCode = response.code();
                    View footerAfterListView = getLayoutInflater().inflate(R.layout.footer_listvew_stub, null);
                    displayActivePage();
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
}