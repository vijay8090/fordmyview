package com.ford.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ford.bo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by abi on 8/16/2015.
 */
public class ServerRequests {

    ProgressDialog progressDialog;

    public static int CONNECTION_TIME_OUT = 20* 1000;

    public static String SERVER_ADDRESS = "http://usha.hi5trends.com/";

    public ServerRequests(Context context){

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");

    }

    public void storeUserDataInBackground(User user, GetUserCallback callback){
        progressDialog.show();
        new StoreUserDataAsyncTask( user,  callback).execute();
}

    public void fetchUserDataInBackground(User user, GetUserCallback callback){
        progressDialog.show();
        new FetchUserDataAsyncTask( user,  callback).execute();
    }

    private class StoreUserDataAsyncTask extends AsyncTask<Void,Void,Void> {

        User user;
        GetUserCallback callback;
        public StoreUserDataAsyncTask(User user, GetUserCallback callback) {

            this.user = user;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {


            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("username", user.getEmail1());
                jsonObject.put("password", user.getPassword());
                jsonObject.put("btn_action", "save");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String jsonResponse =  performPostCall(SERVER_ADDRESS+"controller/M03LoginControllerExt.php",
                    jsonObject );

            try {
                JSONObject json = new JSONObject(jsonResponse);
                json.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }




        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.done(true,user);
            super.onPostExecute(aVoid);
        }
    }


    private class FetchUserDataAsyncTask extends AsyncTask<Void,Void,User> {

        User user;
        GetUserCallback callback;
        public FetchUserDataAsyncTask(User user, GetUserCallback callback) {

            this.user = user;
            this.callback = callback;
        }

        @Override
        protected User doInBackground(Void... params) {

            Map<String, String> postDataParams = new HashMap<String,String>();



            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("username", user.getEmail1());
                jsonObject.put("password", user.getPassword());
                jsonObject.put("btn_action", "login");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String jsonResponse =  performPostCall(SERVER_ADDRESS+"controller/M03LoginControllerExt.php",
                    jsonObject );

            User returnedUser = new User();

            try {
                JSONObject json = new JSONObject(jsonResponse);

                if(json.length()  == 0){
                    returnedUser = null;
                }  else {
                    if(json.getString("message").equalsIgnoreCase("success")) {
                        returnedUser.setEmail1("hi user");
                        returnedUser.setGender("Female");
                        returnedUser.setDob("2/19/2012");
                        json.getString("message");
                    } else{
                        returnedUser = null;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //user.setEmail1();
            return returnedUser;
        }




        @Override
        protected void onPostExecute(User user) {
            progressDialog.dismiss();
            boolean result = false;
            if(user != null){
                result = true;
            }
            callback.done(result,user);
            super.onPostExecute(user);
        }
    }



    private String  performPostCall(String requestURL,
                                    JSONObject jsonObject) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(CONNECTION_TIME_OUT);
            conn.setConnectTimeout(CONNECTION_TIME_OUT);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
           /* conn.setFixedLengthStreamingMode(jsonObject.toString().getBytes().length);

            //make some HTTP header nicety
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");*/


               /* conn.setHeader("Accept", "application/json");
                conn.setHeader("Content-type", "application/json");*/
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonObject.toString());

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }

                response = response.substring(1, response.length()-1);

                response = convertStandardJSONString(response);
            }
            else {
                response="";

                throw new Exception(responseCode+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private  String convertStandardJSONString(String data_json){
        data_json = data_json.replace("\\", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        return data_json;
    }

    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
