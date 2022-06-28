package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.PostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.PostAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityPageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPosts;
    private String communityName;
    private SwipeRefreshLayout refreshLayout;

    public Community community = new Community();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            communityName = extras.getString("communityName");

        }

        User logUser = JWTUtils.getCurrentUser();

        getSupportActionBar().hide();

        ImageView btn = findViewById(R.id.backImgCommunity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommunityPageActivity.super.onBackPressed();
            }
        });

        Button rules = findViewById(R.id.btnRules);
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityPageActivity.this, CommunityRulesActivity.class);
                intent.putExtra("communityName", communityName);
                startActivity(intent);
            }
        });

        AppCompatButton btnFlairs = findViewById(R.id.btnFlairs);
        btnFlairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityPageActivity.this, CommunityFlairsActivity.class);
                intent.putExtra("communityName", communityName);
                startActivity(intent);
            }
        });


        AppCompatButton addPost = findViewById(R.id.btnAddPost);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (logUser == null){
                    Toast.makeText(view.getContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(CommunityPageActivity.this, AddPostActivity.class);
                    intent.putExtra("communityId",community.getCommunityId());
                    startActivity(intent);
                }


            }
        });

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getAllPosts();

                refreshLayout.setRefreshing(false);
            }
        });

        recyclerViewPosts = findViewById(R.id.recyclerViewPostsCommunity);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));

    }


    public void onResume(){
        super.onResume();
        getCommunity();
        getAllPosts();
    }

    public void getCommunity(){

        RetrofitService retrofitService = new RetrofitService();
        CommunityService communityService = retrofitService.getRetrofit().create(CommunityService.class);

        Call<Community> call = communityService.getCommunityByName(communityName);
        call.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(Call<Community> call, Response<Community> response) {

                community = response.body();
            }

            @Override
            public void onFailure(Call<Community> call, Throwable t) {
                Logger.getLogger(CommunityPageActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });


    }

    public void getAllPosts(){

        RetrofitService retrofitService = new RetrofitService();
        PostService postService = retrofitService.getRetrofit().create(PostService.class);

        Call<List<Post>> call = postService.getPostsById(community.getCommunityId());

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                recyclerViewPosts.setAdapter(new PostAdapter(response.body(), getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(CommunityPageActivity.this, "Load community posts faled!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(CommunityPageActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

    }




}