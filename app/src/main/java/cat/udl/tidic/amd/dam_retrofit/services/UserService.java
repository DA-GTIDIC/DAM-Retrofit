package cat.udl.tidic.amd.dam_retrofit.services;



import java.util.Map;

import cat.udl.tidic.amd.dam_retrofit.models.UserModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;

public interface UserService {

    //@Headers("Authorization:656e50e154865a5dc469b80437ed2f963b8f58c8857b66c9bf")
    @GET("/account/profile")
    Call<UserModel> getUserProfile(@HeaderMap Map<String, String> headers);
}
