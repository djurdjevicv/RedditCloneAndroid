package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.ReactionPost;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.model.enums.ReactionType;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReactionPostService {

    @GET("api/reactionPost/post/{postId}")
    Call<Karma> getPostKarma(@Path("postId") int postId);

    @POST("api/reactionPost/create")
    Call<ReactionPost> createReaction(@Body ReactionPost reactionPost);

    @GET("api/reactionPost/postByUser/{username}")
    Call<Karma> getPostKarmaByUser(@Path("username") String username);


}
