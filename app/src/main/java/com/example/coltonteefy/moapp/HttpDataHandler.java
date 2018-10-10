package com.example.coltonteefy.moapp;

import android.app.Activity;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class HttpDataHandler {
    private Activity activity;
    private boolean exist;

    public void setActivity(Activity activity) {

        this.activity = activity;
    }




    public void postNewUser(String email, String user, String password) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://floating-citadel-31945.herokuapp.com/addUser";

        RequestBody body = new FormEncodingBuilder()
                .add("userEmail", email)
                .add("userName", user)
                .add("userPassword", password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        });
    }


    public void checkExistingUser(CharSequence s) {

        OkHttpClient client = new OkHttpClient();

        String url = "https://floating-citadel-31945.herokuapp.com/user/";

        Request request = new Request.Builder()
                .url(url.concat(s.toString()))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (myResponse.equals("[]")) {
                                setExist(false);
                            } else {
                                setExist(true);
                            }
                        }
                    });
                }
            }
        });
    }


    public void checkExistingEmail(String email) {

        // TODO: 10/10/18 finish server side to reset user password

//        OkHttpClient client = new OkHttpClient();
//
//        String url = "https://localhost:5000/userByEmail/";
//
//        Request request = new Request.Builder()
//                .url(url.concat(email.toString()))
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//
//                if (response.isSuccessful()) {
//                    final String myResponse = response.body().string();
//
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (myResponse.equals("[]")) {
//                                setExist(false);
//                            } else {
//                                setExist(true);
//                            }
//                        }
//                    });
//                }
//            }
//        });
    }


    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
