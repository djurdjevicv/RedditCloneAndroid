package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommunityService {

    @GET("api/communities/{communityId}")
    Call<Community> getCommunityById(@Path("communityId") int id);

    @GET("api/communities/byCommunityName/{communityName}")
    Call<Community> getCommunityByName(@Path("communityName") String communityName);

    @GET("api/communities")
    Call<List<Community>> getAllCommunity();

    @POST("api/communities/create")
    Call<Community> createCommunity(@Body Community community);

    @DELETE("api/communities/{communityId}")
    Call<ResponseBody> deleteCommunity(@Path("communityId") int communityId);
}
