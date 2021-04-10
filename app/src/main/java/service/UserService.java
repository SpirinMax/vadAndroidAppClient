package service;

import android.content.Context;
import android.widget.Toast;

import com.example.serverregister.SharedPreferencesUserInfo;

import entites.User;
import retrofit.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();

    public void saveUser(User userRequest, Context context) {
        Call<User> userResponseCall = ApiClient.getUserService().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    sharedPreferencesUserInfo.setSettings(
                            context,
                            response.body().getLastname(),
                            response.body().getFirstname(),
                            response.body().getPatronymic(),
                            response.body().getEmail(),
                            response.body().getPassword()
                    );
                    Toast.makeText(context, "Запрос успешно отправлен!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Некорректный запрос!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Некорректный запрос!" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginApp(User userRequest, Context context){
        Call<User> userResponseCall = ApiClient.getUserService().loginApp(userRequest);
        userResponseCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    String name = String.valueOf(response.body().getFirstname())+ " "+String.valueOf(response.body().getLastname());
                    Toast.makeText(context, name+","+" поздравляем с успешной авторизацией",Toast.LENGTH_SHORT).show();
                    sharedPreferencesUserInfo.setSettings(
                            context,
                            response.body().getLastname(),
                            response.body().getFirstname(),
                            response.body().getPatronymic(),
                            response.body().getEmail(),
                            response.body().getPassword()
                    );
                } else{
                    String name = String.valueOf(response.code());
                    Toast.makeText(context,"Неверный логин и пароль. Код ошибки: "+name,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context,"Некорректный запрос!" + t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createCredentials(User userRequest, String email,String password){
        userRequest.setEmail(email);
        userRequest.setPassword(password);
    }
}
