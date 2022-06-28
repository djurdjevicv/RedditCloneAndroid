package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.Flair;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Rule;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.FlairService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.RuleService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.FlairsAdapter;
import Adapters.RulesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityFlairsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button addFlair;
    String communityName;
    private SwipeRefreshLayout refreshLayout;
    public Community community;

    public int communityId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_flairs);

        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            communityName = extras.getString("communityName");
        }

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getCommunity();

                refreshLayout.setRefreshing(false);
            }
        });

        recyclerView = findViewById(R.id.recyclerViewFlairs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView btn = findViewById(R.id.backButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommunityFlairsActivity.super.onBackPressed();
            }
        });

        addFlair = findViewById(R.id.addFlairBtn);
        addFlair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.change_post_title_popup, null);

                int width = 600;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText inputNewRuleDescription = popupView.findViewById(R.id.newTitleForPost);
                inputNewRuleDescription.setHint("Flair name");

                AppCompatButton addFlair = popupView.findViewById(R.id.btnSubmitChangeTitle);
                addFlair.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String flairName = inputNewRuleDescription.getText().toString();

                        Flair flair = new Flair();
                        flair.setCommunityId(communityId);
                        flair.setName(flairName);

                        RetrofitService retrofitService = new RetrofitService();
                        FlairService flairService = retrofitService.getRetrofit().create(FlairService.class);
                        Call<Flair> createFlair = flairService.createFlair(flair);
                        createFlair.enqueue(new Callback<Flair>() {
                            @Override
                            public void onResponse(Call<Flair> call, Response<Flair> response) {
                                Toast.makeText(CommunityFlairsActivity.this, "Successful create flair!", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();
                            }

                            @Override
                            public void onFailure(Call<Flair> call, Throwable t) {
                                Toast.makeText(CommunityFlairsActivity.this, "Failed to create flair!", Toast.LENGTH_SHORT).show();
                                Logger.getLogger(CommunityPageActivity.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });


                        popupView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                popupWindow.dismiss();
                                return true;
                            }
                        });

                    }
                });

            }
        });


    }


    public void onResume(){
        super.onResume();
        getCommunity();

    }

    private void getFlairs(int communityId, int moderator) {

        RetrofitService retrofitService = new RetrofitService();
        FlairService flairService = retrofitService.getRetrofit().create(FlairService.class);

        Call<List<Flair>> getAllFlair = flairService.getFlairsByCommunity(communityId);
        getAllFlair.enqueue(new Callback<List<Flair>>() {
            @Override
            public void onResponse(Call<List<Flair>> call, Response<List<Flair>> response) {
                recyclerView.setAdapter(new FlairsAdapter(response.body(), getApplicationContext(), moderator));
            }

            @Override
            public void onFailure(Call<List<Flair>> call, Throwable t) {

            }
        });

    }

    private void getCommunity() {

        RetrofitService retrofitService = new RetrofitService();
        CommunityService communityService = retrofitService.getRetrofit().create(CommunityService.class);

        Call<Community> call = communityService.getCommunityByName(communityName);
        call.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(Call<Community> call, Response<Community> response) {

                communityId = response.body().getCommunityId();

                checkLoginUser(response.body().getModerator());
                getFlairs(response.body().getCommunityId(), response.body().getModerator());

            }

            @Override
            public void onFailure(Call<Community> call, Throwable t) {
                Logger.getLogger(CommunityPageActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

    }

    public void checkLoginUser(int moderator){

        User user = JWTUtils.getCurrentUser();

        if(!(user != null && user.getUserId() == moderator)){
            addFlair.setVisibility(View.GONE);
        }

    }

}