package retrofit;

import entites.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("RegistrationUser")
        //http://localhost:8081/serverapp/RegistrationUser
    Call<User> saveUser(@Body User userRequest);

    @POST("AuthenticationServlet")
    Call<User> loginApp(@Body User userRequest);

    @POST("DataUserEditedServlet")
    Call<User> updateUserData(@Body User dataRequestUpdateProfile);
}
