package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Flair;
import com.example.redditcloneandroid.model.Rule;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FlairService {

    @GET("api/communityFlair/{communityId}")
    Call<List<Flair>> getFlairsByCommunity(@Path("communityId") int communityId);

    @POST("api/communityFlair/create")
    Call<Flair> createFlair(@Body Flair flair);

    @PUT("api/communityFlair/{flairId}")
    Call<Flair> updateFlair(@Path("flairId") int flairId, @Body Flair flair);

    @DELETE("api/communityFlair/{flairId}")
    Call<ResponseBody> deleteFlair(@Path("flairId") int flairId);

}
