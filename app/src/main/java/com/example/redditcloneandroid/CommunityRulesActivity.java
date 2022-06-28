package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Rule;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.RuleService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.PostAdapter;
import Adapters.RulesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityRulesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button addRules;
    String communityName;
    private SwipeRefreshLayout refreshLayout;
    public Community community;

    public int communityId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_rules);

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

        recyclerView = findViewById(R.id.recyclerViewRules);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ImageView btn = findViewById(R.id.backButtonRules);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommunityRulesActivity.super.onBackPressed();
            }
        });

        addRules = findViewById(R.id.addRulesBtn);
        addRules.setOnClickListener(new View.OnClickListener() {
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
                inputNewRuleDescription.setHint("Rule description");

                AppCompatButton addRule = popupView.findViewById(R.id.btnSubmitChangeTitle);
                addRule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String ruleDescription = inputNewRuleDescription.getText().toString();

                        Rule rule = new Rule();
                        rule.setCommunity(communityId);
                        rule.setDescription(ruleDescription);

                        RetrofitService retrofitService = new RetrofitService();
                        RuleService ruleService = retrofitService.getRetrofit().create(RuleService.class);
                        Call<Rule> createRule = ruleService.createRule(rule);
                        createRule.enqueue(new Callback<Rule>() {
                            @Override
                            public void onResponse(Call<Rule> call, Response<Rule> response) {
                                Toast.makeText(CommunityRulesActivity.this, "Successful create rule!", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();
                            }

                            @Override
                            public void onFailure(Call<Rule> call, Throwable t) {
                                Toast.makeText(CommunityRulesActivity.this, "Failed to create rule!", Toast.LENGTH_SHORT).show();
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

    private void getRules(int communityId, int moderator) {

        RetrofitService retrofitService = new RetrofitService();
        RuleService ruleService = retrofitService.getRetrofit().create(RuleService.class);

        Call<List<Rule>> getAllRules = ruleService.getRulesByCommunity(communityId);
        getAllRules.enqueue(new Callback<List<Rule>>() {
            @Override
            public void onResponse(Call<List<Rule>> call, Response<List<Rule>> response) {
                recyclerView.setAdapter(new RulesAdapter(response.body(), getApplicationContext(), moderator));
            }

            @Override
            public void onFailure(Call<List<Rule>> call, Throwable t) {

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
                getRules(response.body().getCommunityId(), response.body().getModerator());

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
            addRules.setVisibility(View.GONE);
        }

    }




}