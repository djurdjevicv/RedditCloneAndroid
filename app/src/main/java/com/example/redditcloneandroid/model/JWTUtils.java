package com.example.redditcloneandroid.model;

import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.example.redditcloneandroid.HomeActivity;
import com.example.redditcloneandroid.retrofit.PostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.UserService;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JWTUtils {

    public static String currentUsername = "";
    public static String currentRoles = "";
    public static User currentUser;


    public static void decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));


            JSONObject object = new JSONObject(getJson(split[1]));

            currentUsername = object.getString("sub");
            currentRoles = object.getJSONObject("role").getString("authority");

        } catch (UnsupportedEncodingException e) {
            //Error
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static String getCurrentUserUsername(){
        return currentUsername;
    }

    public static String getCurrentUserRoles(){
        return currentRoles;
    }

    public static User getCurrentUser(){

        RetrofitService retrofitService = new RetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);

        if (currentUsername != ""){
            Call<User> call = userService.getUserByUsername(currentUsername);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    currentUser = response.body();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    //error
                }
            });

            return currentUser;

        }else{
            return null;
        }


    }

    public static void logout(){
        currentUsername = "";
        currentRoles = "";
        currentUser = null;
    }






}
