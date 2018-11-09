package com.example.coltonteefy.moapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.coltonteefy.moapp.utils.CreateCustomToast;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignInActivity extends AppCompatActivity {
    private final String TAG = "SignInActivity";
    DialogPlus dialog;
    Button loginBtn, createBtn, createAccountBtn, haveAccountBtn, forgotPasswordBtn;
    EditText userNameInputTxt, passwordInputTxt, signUpEmail, signUpUserName, signUpPassword, confirmSignUpPassword;
    int buttonId;
    String email, userName, password;
    Activity activity = SignInActivity.this;
    ProgressBar loginProgressBar;
    boolean userExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        loginBtn = findViewById(R.id.loginBtn);
        createBtn = findViewById(R.id.createBtn);
        userNameInputTxt = findViewById(R.id.userNameTxt);
        passwordInputTxt = findViewById(R.id.passwordTxt);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        loginProgressBar = findViewById(R.id.loginProgressBar);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setVisibility(View.GONE);
                loginProgressBar.setVisibility(View.VISIBLE);

                OkHttpClient client = new OkHttpClient();
                final String userInput = userNameInputTxt.getText().toString();
                final String pwInput = passwordInputTxt.getText().toString();

                String url = "https://afternoon-waters-54974.herokuapp.com/login";

                FormBody.Builder formBuilder = new FormBody.Builder()
                        .add("username", userInput)
                        .add("password", pwInput);

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

                        if (response.isSuccessful()) {

                            final String message = response.body().string();

                            activity.runOnUiThread(new Runnable() {
                                CreateCustomToast customToast = new CreateCustomToast();
                                @Override
                                public void run() {
                                    if (userInput.equals("") || pwInput.equals("")) {
                                        customToast.customToast(R.layout.toast_custom_empty_user_password, activity);
                                        loginBtn.setVisibility(View.VISIBLE);
                                        loginProgressBar.setVisibility(View.GONE);
                                    } else {
                                        switch (message) {
                                            case "success":
                                                Intent loginSuccess = new Intent(SignInActivity.this, MainActivity.class);
                                                startActivity(loginSuccess);
                                                break;
                                            case "Invalid password":
                                                Log.i(TAG, "wrong password");
                                                customToast.customToast(R.layout.toast_custom_user_no_exist, activity);
                                                loginBtn.setVisibility(View.VISIBLE);
                                                loginProgressBar.setVisibility(View.GONE);
                                                break;
                                            case "Not a valid user":
                                                Log.i(TAG, "no username");
                                                customToast.customToast(R.layout.toast_custom_user_no_exist, activity);
                                                loginBtn.setVisibility(View.VISIBLE);
                                                loginProgressBar.setVisibility(View.GONE);
                                                break;
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void registrationView(View view) {
        userNameInputTxt.getText().clear();
        passwordInputTxt.getText().clear();

        dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.layout_registration))
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)  // or any custom width ie: 300
                .setContentHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentBackgroundResource(R.color.darkPurple)
                .setMargin(0, 0, 0, 0)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        if (buttonId == R.id.createAccountBtn) {
                            CreateCustomToast customToast = new CreateCustomToast();
                            customToast.customToast(R.layout.toast_custom_success, activity);
                        }
                    }
                })
                .create();

        dialog.show();

        signUpEmail = findViewById(R.id.signUpEmail);
        signUpUserName = findViewById(R.id.signUpUserName);
        signUpPassword = findViewById(R.id.signUpPassword);
        confirmSignUpPassword = findViewById(R.id.confirmSignUpPassword);

        createAccountBtn = findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (signUpEmail.getText().toString().equals("") || signUpUserName.getText().toString().equals("") || signUpPassword.getText().toString().equals("") || confirmSignUpPassword.getText().toString().equals("")) {
                    CreateCustomToast customToast = new CreateCustomToast();
                    customToast.customToast(R.layout.toast_custom_fill_all, activity);
                } else if (!signUpPassword.getText().toString().equals(confirmSignUpPassword.getText().toString())) {
                    CreateCustomToast customToast = new CreateCustomToast();
                    customToast.customToast(R.layout.toast_custom_password_no_match, activity);
                } else {
                    email = String.valueOf(signUpEmail.getText());
                    userName = String.valueOf(signUpUserName.getText());
                    password = String.valueOf(signUpPassword.getText());
                    buttonId = v.getId();


                    OkHttpClient client = new OkHttpClient();

                    String url = "https://afternoon-waters-54974.herokuapp.com/register";

                    FormBody.Builder formBuilder = new FormBody.Builder()
                            .add("email", email)
                            .add("username", userName)
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

                            Log.i(TAG, "Response message: " + message);

                            activity.runOnUiThread(new Runnable() {
                                CreateCustomToast customToast = new CreateCustomToast();

                                @Override
                                public void run() {
                                    switch (message) {
                                        case "register success":
                                            dialog.dismiss();
                                            break;
                                        case "Username or email already exists":
                                            customToast.customToast(R.layout.toast_custom_user_already_exists, activity);
                                            break;
                                    }
                                }
                            });
                        }
                    });





                }
            }
        });

        haveAccountBtn = findViewById(R.id.haveAccountBtn);
        haveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonId = v.getId();
                dialog.dismiss();
            }
        });
    }


    public void forgotPasswordView(View view) {
        userNameInputTxt.getText().clear();
        passwordInputTxt.getText().clear();

        dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.layout_forgot_password_email))
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)  // or any custom width ie: 300
                .setContentHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentBackgroundResource(R.color.darkPurple)
                .setMargin(0, 0, 0, 0)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {

                    }
                })
                .create();

        dialog.show();

        final EditText resetEmail = findViewById(R.id.resetEmail);

        Button resetPassBtn = findViewById(R.id.resetPassBtn);
        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 10/10/18 finish server side to reset user password

//                OkHttpClient client = new OkHttpClient();
//
//                String url = "https://localhost:5000/userByEmail/";
//
//                Request request = new Request.Builder()
//                        .url(url.concat(resetEmail.getText().toString()))
//                        .build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(Response response) throws IOException {
//
//                        if (response.isSuccessful()) {
//                            final String myResponse = response.body().string();
//
//                            activity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (myResponse.equals("[]")) {
//                                        Toast.makeText(activity, resetEmail.getText() + " does not exist", Toast.LENGTH_SHORT).show();
//
//                                    } else {
//                                        Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });
            }
        });

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


//    public void getUser(String user) {
//        OkHttpClient client = new OkHttpClient();
//
//        String url = "https://floating-citadel-31945.herokuapp.com/user";
//
//        RequestBody body = new FormEncodingBuilder()
//                .add("userName", user)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//                e.printStackTrace();
//                Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//
//                if (response.isSuccessful()) {
//                    final String myResponse = response.body().string();
//                    Toast.makeText(activity, "response " + myResponse, Toast.LENGTH_SHORT).show();
//
//
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                        }
//                    });
//                }
//            }
//        });
//    }

    public void forgotClick() {
        // TODO: 10/10/18 finish server side to reset user password
//        OkHttpClient client = new OkHttpClient();
//
//        String url = "https://floating-citadel-31945.herokuapp.com/forgotPassEmail";
//
//        RequestBody body = new FormEncodingBuilder()
//                .add("userEmail", userEmail)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//                e.printStackTrace();
//                Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//
//                if (response.isSuccessful()) {
//                    final String myResponse = response.body().string();
//                    Toast.makeText(activity, "response " + myResponse, Toast.LENGTH_SHORT).show();
//
//
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                        }
//                    });
//                }
//            }
//        });
    }
}

