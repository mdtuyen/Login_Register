package com.example.yoshiki.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogout;
    private EditText etName, etAge, etUsername;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etAge  = (EditText) findViewById(R.id.etAge);
        etUsername = (EditText) findViewById(R.id.etUsername);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true){
            displayUserDetails();
        }else{
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();
        etUsername.setText(user.username);
        etName.setText(user.name);
        etAge.setText(user.age + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}
