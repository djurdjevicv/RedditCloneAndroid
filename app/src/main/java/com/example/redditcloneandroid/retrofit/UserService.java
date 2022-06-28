package com.example.redditcloneandroid.retrofit;

import java.util.ArrayList;
import java.util.List;

import com.example.redditcloneandroid.model.ResetPassword;
import com.example.redditcloneandroid.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {


    @GET("api/users")
    Call<List<User>> getAllUsers();

    @GET("api/users/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @GET("api/users/byId/{userId}")
    Call<User> getUserById(@Path("userId") int userId);

    @POST("api/users/create")
    Call<User> createUser(@Body User user);

    @PUT("api/users/{username}")
    Call<ResponseBody> changePassword(@Body ResetPassword resetPassword, @Path("username") String username);

    @PUT("api/users/changeData/{username}")
    Call<User> updateUser(@Body User user, @Path("username") String username);

    @DELETE("api/users/changeUserToModerator/{id}")
    Call<ResponseBody> changeUserToModerator(@Path("id") int userId);


}
