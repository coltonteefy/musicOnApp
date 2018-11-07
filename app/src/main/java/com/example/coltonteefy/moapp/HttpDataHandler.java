package com.example.coltonteefy.moapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpDataHandler {
    private Activity activity;
    private boolean userExists;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    public void postNewUser(String user, String password, String email) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://afternoon-waters-54974.herokuapp.com/register";

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("email", email)
                .add("username", user)
                .add("password", password);

        RequestBody requestBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String message = response.body().string();
                Log.i("why", message);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (message.equals("register success")) {
                            setExist(false);
                        } else {
                            setExist(true);
                        }
                    }
                });
            }
        });


    }


    public boolean getUserExists() {

        return userExists;
    }

    private void setExist(boolean exist) {

        userExists = exist;
    }
}
