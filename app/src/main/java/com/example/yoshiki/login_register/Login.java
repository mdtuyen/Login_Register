package com.example.yoshiki.login_register;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogin;
    private EditText etUsername, etPassword;
    private TextView tvRegisterLink;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin   = (Button) findViewById(R.id.btnLogin);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);

        btnLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(username, password);

                authenticate(user);

                userLocalStore.storeUserData(user);
                userLocalStore.setUserLoggedIn(true);
                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if(returnedUser == null){
                    showErrorMessage();
                }else{
                    logUserIn(returnedUser);
                }
            }
        });

    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect use details");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser){
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this, MainActivity.class));
    }
}
