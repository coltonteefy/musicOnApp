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
}
