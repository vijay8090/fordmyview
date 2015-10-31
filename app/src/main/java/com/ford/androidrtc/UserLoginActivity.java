package com.ford.androidrtc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ford.bo.User;
import com.ford.network.GetUserCallback;
import com.ford.network.ServerRequests;
import com.ford.storage.UserLocalStore;

public class UserLoginActivity extends Activity implements View.OnClickListener {


    Button bLogin, bSignup;
    EditText etEmail, etPassword;
    TextView tvRegister;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);

        bSignup = (Button) findViewById(R.id.bSignup);
        bSignup.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogin:


                String email1 = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User();

                user.setEmail1(email1);
                user.setPassword(password);
                authenticate(user);

//                userLocalStore.storeUserData(user);
//                userLocalStore.setUserLoggedIn(true);
                break;
            case R.id.bSignup:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void authenticate(User user) {
        ServerRequests serverRequests = new ServerRequests(this);

        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(boolean result, User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    loguserin(returnedUser);
                }
            }
        });
    }

    private void loguserin(User returnedUser) {

        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, UserHomeActivity.class));
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserLoginActivity.this);
        dialogBuilder.setMessage("Incorrect User Details");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();

    }
}
