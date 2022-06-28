package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.AuthenticationRequest;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Token;
import com.example.redditcloneandroid.retrofit.AuthenticationService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.google.android.material.navigation.NavigationView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    public String userType;
    EditText inputUsername;
    EditText inputPassword;
    AppCompatButton btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getSupportActionBar().hide();

        getInputValue();

        TextView signUp1 = findViewById(R.id.signUp);
        signUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);

                startActivity(intent);
            }
        });

    }

    private void getInputValue(){

        inputUsername = findViewById(R.id.usernameTxt);
        inputPassword = findViewById(R.id.passwordTxt);
        btnLogIn = findViewById(R.id.logIn);


        RetrofitService retrofitService = new RetrofitService();
        AuthenticationService authenticationService = retrofitService.getRetrofit().create(AuthenticationService.class);

        btnLogIn.setOnClickListener(view -> {

            String username = inputUsername.getText().toString();
            String password = inputPassword.getText().toString();

            if (checkInputValue()){
                AuthenticationRequest request = new AuthenticationRequest();
                request.setUsername(username);
                request.setPassword(password);

                authenticationService.login(request).enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {


                        if (response.body() != null){
                            String token = response.body().getAccessToken();

                            try {
                                JWTUtils.decoded(token);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(LogInActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this, UserActivity.class);

                            startActivity(intent);

                        }else {
                            Toast.makeText(LogInActivity.this, "Incorrect username or password!", Toast.LENGTH_LONG).show();
                            inputPassword.setText("");
                            inputUsername.setText("");
                        }




                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Logger.getLogger(LogInActivity.class.getName()).log(Level.SEVERE,"Error", t);
                    }
                });


            }





        });

    }


    private boolean checkInputValue(){

        if (inputUsername.length() == 0) {
            inputUsername.setError("This field is required");
            return false;
        }

        if (inputPassword.length() == 0) {
            inputPassword.setError("This field is required");
            return false;
        }


        return true;
    }











}