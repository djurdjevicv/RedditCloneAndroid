package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.model.enums.Roles;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAddCommunity extends Fragment {

    EditText inputCommunityName;
    EditText inputDescription;
    AppCompatButton btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_community, container, false);

        RetrofitService retrofitService = new RetrofitService();

        inputCommunityName = view.findViewById(R.id.communityNameTxt);
        inputDescription = view.findViewById(R.id.descriptionTxt);
        btnSubmit = view.findViewById(R.id.btnAddCommunity);

        CommunityService communityService = retrofitService.getRetrofit().create(CommunityService.class);
        UserService userService = retrofitService.getRetrofit().create(UserService.class);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String communityName = inputCommunityName.getText().toString();
                String description = inputDescription.getText().toString();

                User user = JWTUtils.getCurrentUser();

                if (isValid()){
                    Community community = new Community();
                    community.setName(communityName);
                    community.setDescription(description);
                    community.setModerator(user.getUserId());

                    if (user.getRoles() == Roles.USER){
                        userService.changeUserToModerator(user.getUserId()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(getContext(), "You are now moderator!", Toast.LENGTH_SHORT).show();
                                JWTUtils.logout();
                                Intent intent = new Intent(getContext(), HomeActivity.class);

                                startActivity(intent);
                                Toast.makeText(getContext(), "Please login!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), "Change user roles failed!", Toast.LENGTH_SHORT).show();
                                Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });
                    }


                    communityService.createCommunity(community).enqueue(new Callback<Community>() {
                        @Override
                        public void onResponse(Call<Community> call, Response<Community> response) {
                            Toast.makeText(getContext(), "Successful add new community", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), UserActivity.class);

                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Community> call, Throwable t) {
                            Toast.makeText(getContext(), "Add community faled!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });

                }

            }
        });

        return view;


    }

    public boolean isValid(){

        if (inputCommunityName.length() == 0) {
            inputCommunityName.setError("This field is required");
            return false;
        }

        if (inputDescription.length() == 0) {
            inputDescription.setError("This field is required");
            return false;
        }


        return true;
    }


}