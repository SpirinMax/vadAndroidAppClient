package com.example.serverregister;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import entites.ParametersRequestForQuest;
import entites.TypeRequests;
import retrofit.ServerError;
import ui.listview_adapters.TypeRequestsAdapter;

public class QuestByParametersActivity extends AppCompatActivity {

    Context thisContext;
    FragmentManager fragmentManager;
    private BehaviorActivity behaviorActivity;
    private ListView listViewTypesRequest;
    private final ServerError serverError = new ServerError();
    SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private List<TypeRequests> listTypesRequest = new ArrayList<TypeRequests>();
    private TypeRequests checkedType;
    private TypeRequestsAdapter editedAdapter;
    private List<TypeRequests> checkedItem = new ArrayList<TypeRequests>();
    private Calendar dateAndTime = Calendar.getInstance();
    private String typeRequest = "";
    private EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_by_parameters);
        getSupportActionBar().hide();

        thisContext = this;
        fragmentManager = getSupportFragmentManager();
        behaviorActivity = new BehaviorActivity(thisContext, fragmentManager);
        listViewTypesRequest = findViewById(R.id.listViewTypesRequest);
        View footerAfterListView = getLayoutInflater().inflate(R.layout.footer_quest_activity, null);
        View headerListView = getLayoutInflater().inflate(R.layout.header_litsview_request_creation_activity, null);
        editTextDate = footerAfterListView.findViewById((R.id.startDateQuest));
        listViewTypesRequest.addHeaderView(headerListView, "ff", true);
        listViewTypesRequest.addFooterView(footerAfterListView, "ff", true);

        setListTypeRequest();
        TypeRequestsAdapter typeRequestsAdapter =
                new TypeRequestsAdapter(thisContext, R.layout.list_type_request_layout, listTypesRequest);
        listViewTypesRequest.setAdapter(typeRequestsAdapter);
        listViewTypesRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO в блоке try исключение (при нажатии на один элемент, когда listview сжался)
                //java.lang.ClassCastException: java.lang.String cannot be cast to entites.TypeRequests
                checkedItem.clear();
                try {
                    checkedType = (TypeRequests) parent.getAdapter().getItem(position);
                    typeRequest = checkedType.getName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                checkedType.setIconCheck(R.drawable.ic_ok);
                checkedItem.add(checkedType);
                editedAdapter = new TypeRequestsAdapter(thisContext, R.layout.list_type_request_layout, checkedItem);
                listViewTypesRequest.setAdapter(editedAdapter);
            }
        });

    }

    public void sendParametersQuest(View view) {
        ParametersRequestForQuest parametersQuest = readDataFromEditText();
        Intent startActivityIntent = new Intent(thisContext, StartActivity.class);
        behaviorActivity.sendDataInActivity(startActivityIntent, ParametersRequestForQuest.class.getSimpleName(), parametersQuest);
        finish();
    }

    public void updateListTypeRequests(View view) {
        listTypesRequest.clear();
        setListTypeRequest();
        typeRequest = "";
        TypeRequestsAdapter typeRequestsAdapter =
                new TypeRequestsAdapter(thisContext, R.layout.list_type_request_layout, listTypesRequest);
        listViewTypesRequest.setAdapter(typeRequestsAdapter);
    }

    public void selectDateRequest(View view) {
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(thisContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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

    private void setListTypeRequest() {
        int stubIcon = R.drawable.ic_stub;
        listTypesRequest.add(new TypeRequests(R.drawable.ic_profile, "Помощь пожилым людям", stubIcon));
        listTypesRequest.add(new TypeRequests(R.drawable.emblema_doo, "Помощь ветеранам", stubIcon));
        listTypesRequest.add(new TypeRequests(R.drawable.ic_profile, "Уборка", stubIcon));
        listTypesRequest.add(new TypeRequests(R.drawable.ic_profile, "Помощь людям в трудной ситуации", stubIcon));
    }

    private ParametersRequestForQuest readDataFromEditText() {
        EditText regionQuestEditText, districtQuestEditText, cityQuestEditText, dateQuestEditText;
        regionQuestEditText = findViewById(R.id.regionQuest);
        districtQuestEditText = findViewById(R.id.districtQuest);
        cityQuestEditText = findViewById(R.id.cityQuest);
        dateQuestEditText = findViewById(R.id.startDateQuest);
        Calendar dateQuest = null;
        ParametersRequestForQuest parametersQuest = new ParametersRequestForQuest();
        int lengthStartDateEditText = dateQuestEditText.getText().toString().length();
        try {
            if (lengthStartDateEditText <= 10) {
                String typeRequestForQuest = typeRequest;
                String regionQuest = regionQuestEditText.getText().toString();
                String districtQuest = districtQuestEditText.getText().toString();
                String cityQuest = cityQuestEditText.getText().toString();
                if (lengthStartDateEditText != 0) {
                    dateQuest = receiveStartDateFromEditText(dateQuestEditText);
                }

                parametersQuest.setStartDate(dateQuest);
                parametersQuest.setTypeRequest(typeRequestForQuest);
                parametersQuest.setRegion(regionQuest);
                parametersQuest.setDistrict(districtQuest);
                parametersQuest.setCity(cityQuest);

            } else {
                Toast.makeText(thisContext, "Неверный формат даты", Toast.LENGTH_LONG).show();
            }
        } catch (ParseException e) {
            Toast.makeText(thisContext, "Неверный формат даты", Toast.LENGTH_LONG).show();
        }
        return parametersQuest;
    }

    private Calendar receiveStartDateFromEditText(EditText editTextStartDate) throws ParseException {
        String dateString = editTextStartDate.getText().toString();
        Calendar startDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.y");
        startDate.setTime(sdf.parse(dateString));
        int y = startDate.get(Calendar.YEAR);
        int m = startDate.get(Calendar.MONTH) + 1;
        int day = startDate.get(Calendar.DAY_OF_MONTH);
        startDate.set(y, m, day);
        return startDate;
    }

}