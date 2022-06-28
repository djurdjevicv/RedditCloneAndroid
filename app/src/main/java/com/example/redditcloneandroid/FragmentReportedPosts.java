package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Adapters.BlockModeratorAdapter;
import Adapters.PostAdapter;
import Adapters.ReportCommentsAdapter;
import Adapters.ReportPostAdapter;

public class FragmentReportedPosts extends Fragment {

    RecyclerView recyclerView;
    String usernamePost[], communityPost[], titlePost[], textPost[], sortPosts[];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reported_posts, container, false);

        recyclerView = view.findViewById(R.id.reportedPostRecycle);

        usernamePost = getResources().getStringArray(R.array.username_post);
        communityPost = getResources().getStringArray(R.array.community_post);
        titlePost = getResources().getStringArray(R.array.title_post);
        textPost = getResources().getStringArray(R.array.text_post);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ReportPostAdapter postAdapter = new ReportPostAdapter(requireActivity(), usernamePost, communityPost, titlePost, textPost);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

    }

}