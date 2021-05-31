package service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serverregister.BehaviorActivity;
import com.example.serverregister.R;
import com.example.serverregister.SharedPreferencesUserInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import entites.RequestForHelp;
import entites.User;
import retrofit.ServerError;
import retrofit2.Response;
import ui.adapters.ParticipantRequest;
import ui.adapters.ParticipantRequestAdapter;

public class UserService {
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private ServerError serverError = new ServerError();

    public void saveUser(Response<User> response, BehaviorActivity behaviorActivity) {
        int serverStatusCode = response.code();
        Context context = behaviorActivity.receiveContext();
        if (response.isSuccessful()) {
            sharedPreferencesUserInfo.setSettings(
                    context,
                    response.body().getLastname(),
                    response.body().getId(),
                    response.body().getFirstname(),
                    response.body().getPatronymic(),
                    response.body().getEmail(),
                    response.body().getPassword()
            );
        } else {
            serverError.handleError(serverStatusCode, behaviorActivity);
        }
    }

    public void loginApp(Response<User> response, BehaviorActivity behaviorActivity) {
        int serverStatusCode = response.code();
        Context context = behaviorActivity.receiveContext();
        if (response.isSuccessful()) {
            String name = String.valueOf(response.body().getFirstname()) + " " + String.valueOf(response.body().getLastname());
            Toast.makeText(context, name + "," + " поздравляем с успешной авторизацией", Toast.LENGTH_SHORT).show();
            sharedPreferencesUserInfo.setSettings(
                    context,
                    response.body().getLastname(),
                    response.body().getId(),
                    response.body().getFirstname(),
                    response.body().getPatronymic(),
                    response.body().getEmail(),
                    response.body().getPassword()
            );

        } else {
            serverError.handleError(serverStatusCode, behaviorActivity);
        }
    }

    public void updateUserData(Response<User> response, BehaviorActivity behaviorActivity) {
        int serverStatusCode = response.code();
        Context context = behaviorActivity.receiveContext();
        if (response.isSuccessful()) {
            Toast.makeText(context, "Данные успешно изменены!", Toast.LENGTH_SHORT).show();
            sharedPreferencesUserInfo.setSettings(
                    context,
                    response.body().getLastname(),
                    response.body().getId(),
                    response.body().getFirstname(),
                    response.body().getPatronymic(),
                    response.body().getEmail(),
                    response.body().getPassword()
            );
        } else {
            serverError.handleError(serverStatusCode, behaviorActivity);
        }
    }

    public User receiveUserData(Response<User> response) {
        User userData = new User();
        int id = response.body().getId();
        String firstname = response.body().getFirstname();
        String lastname = response.body().getLastname();
        String patronymic = response.body().getPatronymic();
        String email = response.body().getEmail();
        String password = response.body().getPassword();
        String aboutuser = response.body().getAboutuser();
        String phone = response.body().getPhone();
        byte[] photo = response.body().getPhoto();
        int helpcounter = response.body().getHelpcounter();
        String region = response.body().getRegion();
        String district = response.body().getDistrict();
        String city = response.body().getCity();

        userData.setId(id);
        userData.setFirstname(firstname);
        userData.setLastname(lastname);
        userData.setPatronymic(patronymic);
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setAboutuser(aboutuser);
        userData.setPhone(phone);
        userData.setPhoto(photo);
        userData.setHelpcounter(helpcounter);
        userData.setRegion(region);
        userData.setDistrict(district);
        userData.setCity(city);

        return userData;
    }

    public void createCredentials(User userRequest, String email, String password) {
        userRequest.setEmail(email);
        userRequest.setPassword(password);
    }

    public static String receiveCountParticipants(Set<User> participants) {
        int count = participants.size();
        return String.valueOf(count);
    }

    public static String receiveStringDateTime(Calendar dateTime) {
        String y = String.valueOf(dateTime.get(Calendar.YEAR));
        String m = convertIntMonthInString(dateTime.get(Calendar.MONTH));
        String day = String.valueOf(dateTime.get(Calendar.DAY_OF_MONTH));
        String h = String.valueOf(dateTime.get(Calendar.HOUR_OF_DAY));
        String mun = String.valueOf(dateTime.get(Calendar.MINUTE));
        String resultStringDateTime = day + " " + m + " " + y + " , " + h + ":" + mun;
        return resultStringDateTime;
    }

    private static String convertIntMonthInString(int monthInt) {
        String monthStr = null;
        if (monthInt == 1) {
            monthStr = "января";
        } else if (monthInt == 2) {
            monthStr = "февраля";
        } else if (monthInt == 3) {
            monthStr = "марта";
        } else if (monthInt == 4) {
            monthStr = "апреля";
        } else if (monthInt == 5) {
            monthStr = "мая";
        } else if (monthInt == 6) {
            monthStr = "июня";
        } else if (monthInt == 7) {
            monthStr = "июля";
        } else if (monthInt == 8) {
            monthStr = "августа";
        } else if (monthInt == 9) {
            monthStr = "сентября";
        } else if (monthInt == 10) {
            monthStr = "октября";
        } else if (monthInt == 11) {
            monthStr = "ноября";
        } else if (monthInt == 12) {
            monthStr = "декабря";
        }
        return monthStr;
    }

    public void fillTextFields(View view, RequestForHelp requestForHelp,ListView listParticipants){
        TextView nameRequest, cityInRequest, streetInRequest, houseInRequest, dateAndTimeRequest, descriptionRequest, countParticipants;
        nameRequest = view.findViewById(R.id.nameRequest);
        cityInRequest = view.findViewById(R.id.cityInRequest);
        streetInRequest = view.findViewById(R.id.streetInRequest);
        houseInRequest = view.findViewById(R.id.houseInRequest);
        dateAndTimeRequest = view.findViewById(R.id.dateAndTimeRequest);
        descriptionRequest = view.findViewById(R.id.descriptionRequest);
        countParticipants = view.findViewById(R.id.countParticipants);

        nameRequest.setText(requestForHelp.getName());
        cityInRequest.setText(requestForHelp.getCity() + ",");
        streetInRequest.setText(requestForHelp.getStreet() + ",");
        houseInRequest.setText(requestForHelp.getHouseNumber());
        dateAndTimeRequest.setText(UserService.receiveStringDateTime(requestForHelp.getStartDate()));
        descriptionRequest.setText(requestForHelp.getDescription());
        countParticipants.setText("(" + receiveCountParticipants(requestForHelp.getParticipants()) + " чел.)");
        fillParticipantsInRequest(view,requestForHelp.getParticipants(),listParticipants);
    }

    private void fillParticipantsInRequest(View view,Set<User> participants,ListView listParticipants) {
        Context context = view.getContext();
        if (participants.size() != 0) {
            List<User> listPartFromRequest = new ArrayList<User>(participants);
            List<ParticipantRequest> listPart = new ArrayList<ParticipantRequest>();
            for (int i = 0; i < listPartFromRequest.size(); i++) {
                Bitmap imagePart = BitmapFactory.decodeResource(view.getResources(), R.drawable.empty_image);
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
                    new ParticipantRequestAdapter(context, R.layout.participants_listview_layout, listPart);
            listParticipants.setAdapter(participantRequestAdapter);
        } else {
            List<ParticipantRequest> listPart = new ArrayList<ParticipantRequest>();
            ParticipantRequestAdapter participantRequestAdapter =
                    new ParticipantRequestAdapter(context, R.layout.participants_listview_layout, listPart);
            listParticipants.setAdapter(participantRequestAdapter);
        }
    }
}
