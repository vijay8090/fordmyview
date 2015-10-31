package com.ford.androidrtc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ford.bo.User;
import com.ford.storage.UserLocalStore;
import com.ford.util.customview.DateDisplayPicker;


public class UserHomeActivity extends Activity implements View.OnClickListener {
    Button bLogout;
    EditText etEmail, etPassword, etConfirmPassword;
    DateDisplayPicker etDob;
    RadioGroup rgGender;
    UserLocalStore userLocalStore;
    RadioButton rbMale, rbFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etDob  = (DateDisplayPicker) findViewById(R.id.etDob);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);

        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);

        bLogout = (Button) findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(authenticateUser()){
            //displayDetails();
            startActivity(new Intent(UserHomeActivity.this,RtcActivity.class));
        } else {
            startActivity(new Intent(UserHomeActivity.this, UserLoginActivity.class));
        }
    }

    private boolean authenticateUser(){

        return userLocalStore.isUserLoggedIn();
    }

    private void displayDetails(){

        User loggedUser = userLocalStore.getLoggedInuser();
        etEmail.setText(loggedUser.getEmail1());

       /* etDob.onDateSet(new DatePicker(null), 2015, 6,
        4);*/
        etDob.setText(loggedUser.getDob());

        if("male".equalsIgnoreCase(loggedUser.getGender())){
            rbMale.setChecked(true);
            rbFemale.setChecked(false);
        }else {
            rbMale.setChecked(false);
            rbFemale.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.bLogout :

                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, UserLoginActivity.class));

                break;
        }
    }
}
