package com.ford.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.ford.bo.User;

/**
 * Created by abi on 8/15/2015.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){

        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);


    }


    public void storeUserData(User user){

        SharedPreferences.Editor spEditor = userLocalDatabase.edit();

        spEditor.putString("email1",user.getEmail1());
        spEditor.putString("password",user.getPassword());
        spEditor.putString("dob",user.getDob());
        spEditor.putString("gender",user.getGender());
        spEditor.putString("firstName",user.getFirstName());
        spEditor.putInt("id", user.getId());
        spEditor.commit();
    }


    public User getLoggedInuser(){

        String email1 = userLocalDatabase.getString("email1", "");
        String password = userLocalDatabase.getString("password","");
        String dob = userLocalDatabase.getString("dob","");
        String gender = userLocalDatabase.getString("gender","");
        String firstName = userLocalDatabase.getString("firstName","");
        int id = userLocalDatabase.getInt("id",-1);

        User storedUser = new User();
        storedUser.setEmail1(email1);
        storedUser.setGender(gender);
        storedUser.setDob(dob);

        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean isUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn", false)){
            return  true;
        } else {
            return  false;
        }

    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
