package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Rule;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RuleService {

    @GET("api/rules/community/{communityId}")
    Call<List<Rule>> getRulesByCommunity(@Path("communityId") int communityId);

    @POST("api/rules/create")
    Call<Rule> createRule(@Body Rule rule);

    @PUT("api/rules/{ruleId}")
    Call<Rule> updateRule(@Path("ruleId") int ruleId, @Body Rule rule);

    @DELETE("api/rules/{ruleId}")
    Call<ResponseBody> deleteRule(@Path("ruleId") int ruleId);

}
