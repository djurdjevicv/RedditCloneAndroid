package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.retrofit.PostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Adapters.PostAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    AppCompatButton btnSignUp;

    public List<User> allUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        getInputValue();

        TextView logIn = findViewById(R.id.logIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);

                startActivity(intent);
            }
        });

    }

    private void getInputValue(){
        inputUsername = findViewById(R.id.usernameTxt);
        inputEmail = findViewById(R.id.emailTxt);
        inputPassword = findViewById(R.id.passwordTxt);
        btnSignUp = findViewById(R.id.btnSignUp);

        RetrofitService retrofitService = new RetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);

        btnSignUp.setOnClickListener(view -> {
            String username = inputUsername.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            if (CheckAllFields()){
                User user = new User();
                user.setUsername(username);
                user.setEmail(email);
                user.setDisplayName("");
                user.setDescription("");
                user.setPassword(password);

            userService.createUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);

                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Save faled!", Toast.LENGTH_SHORT).show();
                    Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE,"Error", t);
                }
            });
            }





        });


    }


    private boolean CheckAllFields() {


        RetrofitService retrofitService = new RetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);

        Call<List<User>> call = userService.getAllUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                List<User> userFromBody = response.body();

                for (User user: userFromBody){
                    allUsers.add(user);
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Load users faled!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });




        //USERNAME
        if (inputUsername.length() == 0) {
            inputUsername.setError("This field is required");
            return false;
        }

        for (User user: allUsers){
            if (user.getUsername().equals(inputUsername.getText().toString())){
                inputUsername.setError("Username already exist");
                return false;
            }
        }

        //EMAIL
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(inputEmail.getText().toString());

        if (inputEmail.length() == 0) {
            inputEmail.setError("This field is required");
            return false;
        }else if(!matcher.find()){
            inputEmail.setError("Email must be like example@gmail.com");
            return false;
        }

        Pattern VALID_PASSWORD_ADDRESS_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = VALID_PASSWORD_ADDRESS_REGEX.matcher(inputPassword.getText().toString());

        //PASSWORD
        if (inputPassword.length() == 0) {
            inputPassword.setError("This field is required");
            return false;
        }else if(!matcher1.find()){
            inputPassword.setError("Password must have minimum eight characters, at least one uppercase letter, one lowercase letter and one number!");
            return false;
        }

        return true;
    }


}