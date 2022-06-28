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

import Adapters.CommentAdapter;
import Adapters.PostAdapter;
import Adapters.ReportCommentsAdapter;

public class FragmentReportedComments extends Fragment {

    RecyclerView recyclerView;
    String usernamePost[], commentPost[];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reported_comments, container, false);

        recyclerView = view.findViewById(R.id.reportedCommentRecycle);

        usernamePost = getResources().getStringArray(R.array.username_post);
        commentPost = getResources().getStringArray(R.array.comments);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ReportCommentsAdapter reportCommentsAdapter = new ReportCommentsAdapter(requireActivity(), usernamePost, commentPost);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(reportCommentsAdapter);
    }
}