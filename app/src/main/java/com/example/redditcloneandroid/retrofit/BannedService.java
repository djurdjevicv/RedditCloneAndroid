package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Banned;
import com.example.redditcloneandroid.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BannedService {

    @POST("api/banned/create")
    Call<Banned> createBan(@Body Banned banned);
}
