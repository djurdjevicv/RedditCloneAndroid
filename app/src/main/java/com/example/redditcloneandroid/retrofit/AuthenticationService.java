package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.AuthenticationRequest;
import com.example.redditcloneandroid.model.Token;
import com.example.redditcloneandroid.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationService {

    @POST("api/users/login")
    Call<Token> login(@Body AuthenticationRequest request);

}
