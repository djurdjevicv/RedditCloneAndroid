package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.PostList;
import com.example.redditcloneandroid.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostService {


    @GET("api/posts")
    Call<List<Post>> getAllPosts();

    @GET("api/posts/{postId}")
    Call<Post> getPostByID(@Path("postId") int postId);

    @GET("api/posts/community/{communityId}")
    Call<List<Post>> getPostsById(@Path("communityId") int communityId);

    @POST("api/posts/create")
    Call<Post> createPost(@Body Post post);

    @PUT("api/posts/{postId}")
    Call<Post> changePost(@Body Post post, @Path("postId") int postId);

    @GET("api/posts/user/{username}")
    Call<List<Post>> getOneUserPosts(@Path("username") String username);

    @DELETE("/api/posts/{postId}")
    Call<ResponseBody> deleteItem(@Path("postId") int postId);

}
