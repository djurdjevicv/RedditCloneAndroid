package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.BlockModeratorAdapter;
import Adapters.CommunityListAdapter;
import Adapters.SuspendCommunityAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSuspendCommunity extends Fragment {

    private RecyclerView recycleView;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_suspend_community, container, false);


        recycleView = view.findViewById(R.id.suspendCommunityCardRecycler);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getAllCommunity();

                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getAllCommunity();
    }

    public void getAllCommunity(){

        RetrofitService retrofitService = new RetrofitService();
        CommunityService communityService = retrofitService.getRetrofit().create(CommunityService.class);

        Call<List<Community>> call = communityService.getAllCommunity();

        call.enqueue(new Callback<List<Community>>() {
            @Override
            public void onResponse(Call<List<Community>> call, Response<List<Community>> response) {

                recycleView.setAdapter(new SuspendCommunityAdapter(response.body(), getContext()));
            }

            @Override
            public void onFailure(Call<List<Community>> call, Throwable t) {
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });


    }

}