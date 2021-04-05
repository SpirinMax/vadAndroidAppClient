package com.example.serverregister;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("RegistrationUser") //http://localhost:8081/serverapp/RegistrationUser
    Call<User> saveUser(@Body User userRequest);

    @POST("AuthenticationServlet")
    Call<User> sendCredentialsData(@Body User userRequest);
}
