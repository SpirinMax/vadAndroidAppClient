package com.example.serverregister;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import entites.RequestForHelp;
import entites.TypeRequests;
import entites.User;
import retrofit.ApiClient;
import retrofit.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;
import ui.TransitIconToolbar;
import ui.errorsServer.RefreshInActivity;
import ui.listview_adapters.TypeRequestsAdapter;
import ui.registration.UiRegistration;

public class RequestCreationStageActivity extends AppCompatActivity implements TransitIconToolbar, RefreshInActivity {
    Context thisContext;
    FragmentManager fragmentManager;
    private BehaviorActivity behaviorActivity;
    private ListView listViewTypesRequest;
    private final ServerError serverError = new ServerError();
    SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private List<TypeRequests> listTypesRequest = new ArrayList<TypeRequests>();
    private TypeRequests checkedType;
    private TypeRequestsAdapter editedAdapter;
    private List<TypeRequests> chechedItem = new ArrayList<TypeRequests>();
    private Calendar dateAndTime = Calendar.getInstance();
    private EditText editTextDate, editTextTime;

    private User authRequestUser = new User();
    private User userData = new User();
    private UserService userService = new UserService();
    private RequestForHelp requestForHelp = new RequestForHelp();
    private String typeRequest = "";

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
                        setContentView(R.layout.activity_request_creation_stage);
                        displayActivePage();
                        View footerAfterListView = getLayoutInflater().inflate(R.layout.footer_litsview_request_creation_activity, null);
                        View headerListView = getLayoutInflater().inflate(R.layout.header_litsview_request_creation_activity, null);
                        chechedItem = new ArrayList<TypeRequests>();
                        listViewTypesRequest = findViewById(R.id.listViewTypesRequest);
                        editTextDate = footerAfterListView.findViewById((R.id.startDateRequest));
                        editTextTime = footerAfterListView.findViewById((R.id.startTimeRequest));
                        listViewTypesRequest.addFooterView(footerAfterListView, "ff", true);
                        listViewTypesRequest.addHeaderView(headerListView, "ff", true);
                        setListTypeRequest();
                        TypeRequestsAdapter typeRequestsAdapter =
                                new TypeRequestsAdapter(thisContext, R.layout.list_type_request_layout, listTypesRequest);
                        listViewTypesRequest.setAdapter(typeRequestsAdapter);

                        userData = userService.receiveUserData(response);

                        listViewTypesRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //TODO в блоке try исключение (при нажатии на один элемент, когда listview сжался)
                                //java.lang.ClassCastException: java.lang.String cannot be cast to entites.TypeRequests
                                chechedItem.clear();
                                try {
                                    checkedType = (TypeRequests) parent.getAdapter().getItem(position);
                                    typeRequest = checkedType.getName();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                checkedType.setIconCheck(R.drawable.ic_ok);
                                chechedItem.add(checkedType);
                                editedAdapter = new TypeRequestsAdapter(thisContext, R.layout.list_type_request_layout, chechedItem);
                                listViewTypesRequest.setAdapter(editedAdapter);
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

    public void selectDateRequest(View view) {
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(thisContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //month+1 нужен только в datePickerDialog!!
                        String m = String.valueOf(month + 1);
                        String day = String.valueOf(dayOfMonth);
                        String y = String.valueOf(year);
                        if (month < 10) {
                            m = "0" + String.valueOf(month + 1);
                        }
                        if (dayOfMonth < 10) {
                            day = "0" + String.valueOf(dayOfMonth);
                        }
                        editTextDate.setText(day + "." + m + "." + y);
                    }
                },
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();


    }

    public void selectTimeRequest(View view) {
        final Calendar cal = Calendar.getInstance();
        int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String editTextTimeParam = hourOfDay + ":" + minute;
                        editTextTime.setText(editTextTimeParam);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void sendDataToCreateRequest(View view) throws ParseException {
        LinearLayout containerNameRequest = findViewById(R.id.containerNameRequest);
        LinearLayout containerAdresRequest = findViewById(R.id.containerAdresRequest);
        LinearLayout containerDateRequest = findViewById(R.id.containerDateRequest);
        LinearLayout containerTimeRequest = findViewById(R.id.containerTimeRequest);
        if (typeRequest != "") {
            if (UiRegistration.checkOfNull(containerNameRequest, thisContext)) {
                if (UiRegistration.checkOfNull(containerAdresRequest, thisContext)) {
                    if (UiRegistration.checkOfNull(containerDateRequest, thisContext)) {
                        if (UiRegistration.checkOfNull(containerTimeRequest, thisContext)) {
                            fillRequestForHelp(requestForHelp);
                            Call<RequestForHelp> requestDataCall = ApiClient.getUserService().createRequestForHelp(requestForHelp);
                            requestDataCall.enqueue(new Callback<RequestForHelp>() {
                                @Override
                                public void onResponse(Call<RequestForHelp> call, Response<RequestForHelp> response) {
                                    int serverStatusCode = response.code();
                                    if (response.isSuccessful()) {
                                        Toast.makeText(thisContext, "Заявка успешно создана!", Toast.LENGTH_LONG).show();
                                        behaviorActivity.goInActivity(StartActivity.class);
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
                }
            }
        } else {
            Toast.makeText(thisContext, "Не выбран тип заявки!", Toast.LENGTH_LONG).show();
        }
    }

    public void updateListTypeRequests(View view) {
        listTypesRequest.clear();
        setListTypeRequest();
        typeRequest = "";
        TypeRequestsAdapter typeRequestsAdapter =
                new TypeRequestsAdapter(thisContext, R.layout.list_type_request_layout, listTypesRequest);
        listViewTypesRequest.setAdapter(typeRequestsAdapter);
    }

    @Override
    public void refreshActivity() {
        this.finish();
        startActivity(getIntent());
    }

    @Override
    public void goRequestCreationActivity(View view) {
        //это данная активность
    }

    public void goProfileActivity(View view) {
        behaviorActivity.goInActivity(ProfileActivity.class);
    }

    @Override
    public void goListRequests(View view) {
        behaviorActivity.goInActivity(StartActivity.class);
    }

    @Override
    public void goHistory(View view) {

    }

    private void fillRequestForHelp(RequestForHelp requestForHelp) throws ParseException {
        String name = ((EditText) findViewById(R.id.nameRequest)).getText().toString();
        String type = typeRequest;
        String region = ((EditText) findViewById(R.id.regionRequest)).getText().toString();
        String district = ((EditText) findViewById(R.id.districtRequest)).getText().toString();
        String city = ((EditText) findViewById(R.id.cityRequest)).getText().toString();
        String street = ((EditText) findViewById(R.id.streetRequest)).getText().toString();
        String houseNumber = ((EditText) findViewById(R.id.houseRequest)).getText().toString();
        Calendar startDateTime = receiveStartDateFromEditText(editTextDate, editTextTime);
        Calendar creationDateTime = Calendar.getInstance();
        String description = ((EditText) findViewById(R.id.descriptionRequest)).getText().toString();

        requestForHelp.setAuthorUser(userData);
        requestForHelp.setName(name);
        requestForHelp.setType(type);
        requestForHelp.setRegion(region);
        requestForHelp.setDistrict(district);
        requestForHelp.setCity(city);
        requestForHelp.setStreet(street);
        requestForHelp.setHouseNumber(houseNumber);
        requestForHelp.setStartDate(startDateTime);
        requestForHelp.setCreationDate(creationDateTime);
        requestForHelp.setDescription(description);

    }

    private Calendar receiveStartDateFromEditText(EditText editTextStartDate, EditText editTextTimeStart) throws ParseException {
        String dateString = editTextStartDate.getText().toString();
        String timeString = editTextTimeStart.getText().toString();
        String dateTimeString = dateString + " " + timeString;
        Calendar startDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.y HH:mm");
        startDate.setTime(sdf.parse(dateTimeString));
        int y = startDate.get(Calendar.YEAR);
        int m = startDate.get(Calendar.MONTH);
        int day = startDate.get(Calendar.DAY_OF_MONTH);
        int h = startDate.get(Calendar.HOUR_OF_DAY);
        int mun = startDate.get(Calendar.MINUTE);
        startDate.set(y, m, day, h, mun);
        return startDate;
    }

    private void setListTypeRequest() {
        int stubIcon = R.drawable.ic_stub;
        listTypesRequest.add(new TypeRequests(R.drawable.ic_profile, "Помощь пожилым людям", stubIcon));
        listTypesRequest.add(new TypeRequests(R.drawable.emblema_doo, "Помощь ветеранам", stubIcon));
        listTypesRequest.add(new TypeRequests(R.drawable.ic_profile, "Уборка", stubIcon));
        listTypesRequest.add(new TypeRequests(R.drawable.ic_profile, "Помощь людям в трудной ситуации", stubIcon));
    }

    private void displayActivePage() {
        RelativeLayout containerIcon = findViewById(R.id.creationRequestContainer);
        ImageButton ImageButton = findViewById(R.id.creationRequesticon);
        TextView IconTextView = findViewById(R.id.creationRequestTextView);
        ImageButton.setEnabled(false);
        IconTextView.setEnabled(false);
        Resources resources = getResources();
        ImageButton.setBackgroundColor(resources.getColor(R.color.activeIcon));
        containerIcon.setBackgroundColor(resources.getColor(R.color.activeIcon));
        IconTextView.setTextColor(resources.getColor(R.color.red));
    }
}
