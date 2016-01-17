package com.example.yoshiki.login_register;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import org.json.JSONObject;

/**
 * Created by Yoshiki on 2016/01/02.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://192.168.1.11:3000";
    public static final String TAG = "mytag";

    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait ...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callback){
        progressDialog.show();
        new fetchUserDataAsyncTask(user, callback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {

        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback){
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RequestBody requestbody = new FormEncodingBuilder()
                    .add("name", user.name)
                    .add("age", user.age + "")
                    .add("username", user.username)
                    .add("password", user.password)
                    .build();

            Request request = new Request.Builder()
                    .url(SERVER_ADDRESS + "/registers")
                    .post(requestbody)
                    .build();

            OkHttpClient client = new OkHttpClient();

            try {

                Response response = client.newCall(request).execute();
                String result = response.body().toString();

            } catch (java.net.MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            userCallback.done(null);
        }
    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {

        User user;
        GetUserCallback userCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallback){
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {

            RequestBody requestbody = new FormEncodingBuilder()
                    .add("username", user.username)
                    .add("password", user.password)
                    .build();

            Request request = new Request.Builder()
                    .url(SERVER_ADDRESS + "/registers/fetch")
                    .post(requestbody)
                    .build();

            OkHttpClient client = new OkHttpClient();

            User returnedUser = null;
            try {

                Response response = client.newCall(request).execute();
                String result = response.body().string();
                JSONObject jObject = new JSONObject(result);

                if(jObject.length() == 0){
                    user = null;
                }else{

                    String name = jObject.getString("name");
                    int age = jObject.getInt("age");
                    returnedUser = new User(user.username, user.password, name, age);
                }

            } catch (java.net.MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

            return returnedUser;

        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            userCallback.done(returnedUser);
        }

    }
}
