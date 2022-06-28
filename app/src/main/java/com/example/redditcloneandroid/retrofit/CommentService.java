package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Comment;
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

public interface CommentService {

    @GET("api/comments/{postId}")
    Call<List<Comment>> getCommentsByPost(@Path("postId") int postId);

    @POST("api/comments/create")
    Call<Comment> createComment(@Body Comment comment);



}
