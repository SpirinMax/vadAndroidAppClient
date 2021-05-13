package retrofit;

import java.util.List;

import entites.RequestForHelp;
import entites.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST("RegistrationUser")
        //http://localhost:8081/serverapp/RegistrationUser
    Call<User> saveUser(@Body User userRequest);

    @POST("AuthenticationServlet")
    Call<User> loginApp(@Body User userRequest);

    @POST("DataUserEditedServlet")
    Call<User> updateUserData(@Body User dataRequestUpdateProfile);

    @POST("CreatingNewRequestServlet")
    Call<RequestForHelp> createRequestForHelp(@Body RequestForHelp requestForHelp);

    @GET("ReceivingAllRequests")
    Call<List<RequestForHelp>> receiveListAllRequests();

    @GET("ProcessingAddedParticipantServlet")
    Call<RequestForHelp> becomeParticipant(@Query("idPart") int idPart, @Query("idRequest") int idRequest);

    @GET("DeletingParticipantServlet")
    Call<RequestForHelp> cancelParticipationInrequest(@Query("idPart") int idPart, @Query("idRequest") int idRequest);

}
