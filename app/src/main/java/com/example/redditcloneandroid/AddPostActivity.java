package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.retrofit.PostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {

    EditText inputTitle;
    EditText inputDescription;
    EditText inputFlair;
    AppCompatButton btnSubmit;

    int communityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            communityId = extras.getInt("communityId");

        }

        System.out.println("Community id " + communityId);

        RetrofitService retrofitService = new RetrofitService();

        inputTitle = findViewById(R.id.titleTxt);
        inputDescription = findViewById(R.id.descriptionTxt);
        inputFlair = findViewById(R.id.flairTxt);
        btnSubmit = findViewById(R.id.btnAddPost);

        ImageView btn = findViewById(R.id.backImgCommunity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPostActivity.super.onBackPressed();
            }
        });

        PostService postService = retrofitService.getRetrofit().create(PostService.class);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = inputTitle.getText().toString();
                String picture = "";
                String description = inputDescription.getText().toString();

                User user = JWTUtils.getCurrentUser();

                if (isValid()){
                    Post post = new Post();
                    post.setText(description);
                    post.setTitle(title);
                    post.setImagePath(picture);
                    post.setCommunity(communityId);
                    post.setFlair(1);
                    post.setUser(user.getUserId());

                    postService.createPost(post).enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            Toast.makeText(AddPostActivity.this, "Successful add post!", Toast.LENGTH_SHORT).show();
                            AddPostActivity.super.onBackPressed();
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {
                            Toast.makeText(AddPostActivity.this, "Failed add post!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(AddPostActivity.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });

                }




            }
        });

    }


    public boolean isValid(){

        if (inputTitle.length() == 0) {
            inputTitle.setError("This field is required");
            return false;
        }

        if (inputDescription.length() == 0) {
            inputDescription.setError("This field is required");
            return false;
        }


        return true;
    }






}