package service;

import android.content.Context;
import android.widget.Toast;

import com.example.serverregister.BehaviorActivity;
import com.example.serverregister.SharedPreferencesUserInfo;

import entites.User;
import retrofit.ServerError;
import retrofit2.Response;

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
                    response.body().getFirstname(),
                    response.body().getPatronymic(),
                    response.body().getEmail(),
                    response.body().getPassword()
            );
        } else {
            serverError.handleError(serverStatusCode, behaviorActivity);
        }
    }


//    public void saveUser(Callback<User> responseBody,User userRequest, Context context,FragmentManager fragmentManager) {
//
//        Call<User> userResponseCall = ApiClient.getUserService().saveUser(userRequest);
////        userResponseCall.enqueue(new Callback<User>() {
////            @Override
////            public void onResponse(Call<User> call, Response<User> response) {
////                if (response.isSuccessful()) {
//                    sharedPreferencesUserInfo.setSettings(
//                            context,
//                            response.body().getLastname(),
//                            response.body().getFirstname(),
//                            response.body().getPatronymic(),
//                            response.body().getEmail(),
//                            response.body().getPassword()
//                    );
//                    Toast.makeText(context, "Запрос успешно отправлен!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Некорректный запрос!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                serverError.DisplayDialogLossConnection(context,fragmentManager);
//            }
//        });
//    }

    public void loginApp(Response<User> response, BehaviorActivity behaviorActivity) {
        int serverStatusCode = response.code();
        Context context = behaviorActivity.receiveContext();
        if (response.isSuccessful()) {
            String name = String.valueOf(response.body().getFirstname()) + " " + String.valueOf(response.body().getLastname());
            Toast.makeText(context, name + "," + " поздравляем с успешной авторизацией", Toast.LENGTH_SHORT).show();
            sharedPreferencesUserInfo.setSettings(
                    context,
                    response.body().getLastname(),
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



    public void sendEntityUser(Response<User> response, User user) {
        user = response.body();
    }

//    public void loginApp(User userRequest, Context context, FragmentManager fragmentManager) {
//        Call<User> userResponseCall = ApiClient.getUserService().loginApp(userRequest);
//        userResponseCall.enqueue(new Callback<User>()  {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if(response.isSuccessful()){
//                    String name = String.valueOf(response.body().getFirstname())+ " "+String.valueOf(response.body().getLastname());
//                    Toast.makeText(context, name+","+" поздравляем с успешной авторизацией",Toast.LENGTH_SHORT).show();
//                    sharedPreferencesUserInfo.setSettings(
//                            context,
//                            response.body().getLastname(),
//                            response.body().getFirstname(),
//                            response.body().getPatronymic(),
//                            response.body().getEmail(),
//                            response.body().getPassword()
//                    );
//                } else {
//                    String name = String.valueOf(response.code());
//                    Toast.makeText(context,"Неверный логин и пароль. Код ошибки: "+name,Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                serverError.DisplayDialogLossConnection(context,fragmentManager);
//            }
//        });
//    }

    public void createCredentials(User userRequest, String email, String password) {
        userRequest.setEmail(email);
        userRequest.setPassword(password);
    }
}
