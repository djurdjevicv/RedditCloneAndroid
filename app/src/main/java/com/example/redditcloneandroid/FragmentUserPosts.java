package com.example.redditcloneandroid;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.PostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.CommunityListAdapter;
import Adapters.PostAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUserPosts extends Fragment {

    private RecyclerView recyclerViewPosts;
    private RecyclerView recyclerViewCommunity;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_user_first_page, container, false);

        recyclerViewPosts = view.findViewById(R.id.postUserPage);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewCommunity = view.findViewById(R.id.recyclerViewCommunityList);
        recyclerViewCommunity.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getAllPosts();
                getAllCommunity();

                refreshLayout.setRefreshing(false);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getAllPosts();
        getAllCommunity();
    }



    public void getAllPosts(){

        RetrofitService retrofitService = new RetrofitService();
        PostService postService = retrofitService.getRetrofit().create(PostService.class);

        Call<List<Post>> call = postService.getAllPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                recyclerViewPosts.setAdapter(new PostAdapter(response.body(), getContext()));
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Logger.getLogger(FragmentUserPosts.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

    }

    public void getAllCommunity(){

        RetrofitService retrofitService = new RetrofitService();
        CommunityService communityService = retrofitService.getRetrofit().create(CommunityService.class);

        Call<List<Community>> call = communityService.getAllCommunity();

        call.enqueue(new Callback<List<Community>>() {
            @Override
            public void onResponse(Call<List<Community>> call, Response<List<Community>> response) {

                recyclerViewCommunity.setAdapter(new CommunityListAdapter(response.body(), getContext()));
            }

            @Override
            public void onFailure(Call<List<Community>> call, Throwable t) {
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });


    }


}
