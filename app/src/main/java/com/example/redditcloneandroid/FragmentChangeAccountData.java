package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.retrofit.ReactionCommentService;
import com.example.redditcloneandroid.retrofit.ReactionPostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.UserService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChangeAccountData extends Fragment {

    EditText userEmail, userDisplayName, userDescription;
    AppCompatButton submitBtn;
    User logUser;
    TextView postKarma, commentKarma;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_account_data, container, false);

        logUser = JWTUtils.getCurrentUser();

        userEmail = view.findViewById(R.id.userEmail);
        userDisplayName = view.findViewById(R.id.userDisplayName);
        userDescription = view.findViewById(R.id.userDescription);
        submitBtn = view.findViewById(R.id.btnChangeData);
        postKarma = view.findViewById(R.id.postKarma);
        commentKarma = view.findViewById(R.id.commentKarma);

        userEmail.setText(logUser.getEmail());
        userEmail.setEnabled(false);
        userDisplayName.setText(logUser.getDisplayName());
        userDisplayName.setEnabled(false);
        userDescription.setText(logUser.getDescription());
        userDescription.setEnabled(false);
        submitBtn.setText("CHANGE");

        getUserKarma();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (submitBtn.getText().toString().equals("CHANGE")){

                    userEmail.setEnabled(true);
                    userDisplayName.setEnabled(true);
                    userDescription.setEnabled(true);
                    submitBtn.setText("SUBMIT");
                }else {

                    String newEmail = userEmail.getText().toString();
                    String newDisplayName = userDisplayName.getText().toString();
                    String newDescription = userDescription.getText().toString();

                    if (isValid()){

                        logUser.setEmail(newEmail);
                        logUser.setDisplayName(newDisplayName);
                        logUser.setDescription(newDescription);

                        RetrofitService retrofitService = new RetrofitService();
                        UserService userService = retrofitService.getRetrofit().create(UserService.class);

                        Call<User> call = userService.updateUser(logUser, logUser.getUsername());

                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {

                                Toast.makeText(getContext(), "Successful change data!", Toast.LENGTH_SHORT).show();
                                userEmail.setEnabled(false);
                                userDisplayName.setEnabled(false);
                                userDescription.setEnabled(false);
                                submitBtn.setText("CHANGE");

                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(getContext(), "Change data failed!", Toast.LENGTH_SHORT).show();
                                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });

                    }

                }

            }
        });



        return view;
    }


    private boolean isValid(){

        //EMAIL
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(userEmail.getText().toString());

        if (userEmail.length() == 0) {
            userEmail.setError("This field is required");
            return false;
        }else if(!matcher.find()){
            userEmail.setError("Email must be like example@gmail.com");
            return false;
        }

        //DISPLAY NAME
        if (userDisplayName.length() == 0) {
            userDisplayName.setError("This field is required");
            return false;
        }

        //DESCRIPTION
        if (userDescription.length() == 0) {
            userDescription.setError("This field is required");
            return false;
        }


        return true;
    }

    private void getUserKarma(){

        RetrofitService retrofitService = new RetrofitService();
        ReactionCommentService reactionCommentService = retrofitService.getRetrofit().create(ReactionCommentService.class);
        ReactionPostService reactionPostService = retrofitService.getRetrofit().create(ReactionPostService.class);

        Call<Karma> getPostKarma = reactionPostService.getPostKarmaByUser(logUser.getUsername());
        Call<Karma> getCommentKarma = reactionCommentService.getCommentKarmaByUser(logUser.getUsername());

        getPostKarma.enqueue(new Callback<Karma>() {
            @Override
            public void onResponse(Call<Karma> call, Response<Karma> response) {

                postKarma.setText(String.valueOf(response.body().getKarma()));

            }

            @Override
            public void onFailure(Call<Karma> call, Throwable t) {
                Toast.makeText(getContext(), "Failed load post karma!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

        getCommentKarma.enqueue(new Callback<Karma>() {
            @Override
            public void onResponse(Call<Karma> call, Response<Karma> response) {
                commentKarma.setText(String.valueOf(response.body().getKarma()));
            }

            @Override
            public void onFailure(Call<Karma> call, Throwable t) {
                Toast.makeText(getContext(), "Failed load comment karma!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });




    }


}