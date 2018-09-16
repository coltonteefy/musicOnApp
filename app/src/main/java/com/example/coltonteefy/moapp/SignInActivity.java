package com.example.coltonteefy.moapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coltonteefy.moapp.utils.CreateCustomToast;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class SignInActivity extends AppCompatActivity {
    DialogPlus dialog;
    Button loginBtn, createBtn, createAccountBtn, haveAccountBtn, forgotPasswordBtn;
    EditText userNameTxt, passwordTxt, signUpEmail, signUpUserName, signUpPassword, confirmSignUpPassword;
    int buttonId;
    String email, userName, password;
    Activity activity = SignInActivity.this;
    boolean userExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        loginBtn = findViewById(R.id.loginBtn);
        createBtn = findViewById(R.id.createBtn);
        userNameTxt = findViewById(R.id.userNameTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);

        userNameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

                            SignInActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (myResponse.equals("[]")) {
                                        userExist = false;
                                    } else {
                                        userExist = true;
                                    }
                                }
                            });
                        }
                    }
                });

//                HttpDataHandler dataHandler = new HttpDataHandler();
//                dataHandler.setActivity(activity);
//                dataHandler.checkExistingUser(s);
//                userExist = dataHandler.isExist();
//                if(userExist) {
//                    Toast.makeText(activity, "true", Toast.LENGTH_SHORT).show();
//                } else
//                    Toast.makeText(activity, "false", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //    login validation check
    public void loginClick(View view) {
        String user = String.valueOf(userNameTxt.getText());
        String password = String.valueOf(passwordTxt.getText());
        if (user.equals("") && password.equals("")) {
            CreateCustomToast customToast = new CreateCustomToast();
            customToast.customToast(R.layout.toast_custom_invalid_user_password, activity);
        } else if (user.equals("") && !password.equals("")) {
            CreateCustomToast customToast = new CreateCustomToast();
            customToast.customToast(R.layout.toast_custom_invalid_user, activity);
        } else if (password.equals("") && !user.equals("")) {
            CreateCustomToast customToast = new CreateCustomToast();
            customToast.customToast(R.layout.toast_custom_invalid_password, activity);
        } else if(!userExist) {
            CreateCustomToast customToast = new CreateCustomToast();
            customToast.customToast(R.layout.toast_custom_user_no_exist, activity);
        } else {
            Intent loginSuccess = new Intent(this, MainActivity.class);
            startActivity(loginSuccess);
        }
    }

    public void registrationView(View view) {
        userNameTxt.getText().clear();
        passwordTxt.getText().clear();

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
                    HttpDataHandler handler = new HttpDataHandler();
                    handler.setActivity(activity);
                    handler.postNewUser(email, userName, password);
                    buttonId = v.getId();
                    dialog.dismiss();
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


    public void getUser(String user) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://floating-citadel-31945.herokuapp.com/user";

        RequestBody body = new FormEncodingBuilder()
                .add("userName", user)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                e.printStackTrace();
                Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Toast.makeText(activity, "response " + myResponse, Toast.LENGTH_SHORT).show();


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }
        });
    }

    public void forgotClick(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"coltonteefy@yahoo.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Password Change");
        i.putExtra(Intent.EXTRA_TEXT   , "Did you forget your password?");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

