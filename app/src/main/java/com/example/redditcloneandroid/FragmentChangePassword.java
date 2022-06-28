package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.redditcloneandroid.model.ResetPassword;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.UserService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChangePassword extends Fragment {

    EditText inputCurrentPassword;
    EditText inputNewPassword;
    AppCompatButton btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        inputCurrentPassword = view.findViewById(R.id.currentPasswordTxt);
        inputNewPassword = view.findViewById(R.id.newPasswordTxt);
        btnSubmit = view.findViewById(R.id.btnSubmitChangePassword);

        User user = JWTUtils.getCurrentUser();
        RetrofitService retrofitService = new RetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentPassword = inputCurrentPassword.getText().toString();
                String newPassword = inputNewPassword.getText().toString();

                if (isValid()){

                    ResetPassword resetPassword = new ResetPassword();
                    resetPassword.setOldPassword(currentPassword);
                    resetPassword.setNewPassword(newPassword);

                    Call<ResponseBody> call = userService.changePassword(resetPassword, user.getUsername());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.code() == 200){
                                Toast.makeText(getContext(), "Successful change password!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), UserActivity.class);
                                startActivity(intent);
                            }else {
                                inputCurrentPassword.setText("");
                                inputNewPassword.setText("");
                                Toast.makeText(getContext(), "Change password faled!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), "Change password faled!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });

                }



            }
        });



        return view;
    }

    private boolean isValid(){

        if (inputCurrentPassword.length() == 0){
            inputCurrentPassword.setError("This field is required!");
        }

        Pattern VALID_PASSWORD_ADDRESS_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = VALID_PASSWORD_ADDRESS_REGEX.matcher(inputNewPassword.getText().toString());

        if (inputNewPassword.length() == 0) {
            inputNewPassword.setError("This field is required");
            return false;
        }else if(!matcher1.find()){
            inputNewPassword.setError("Password must have minimum eight characters, at least one uppercase letter, one lowercase letter and one number!");
            return false;
        }

        return true;
    }

}