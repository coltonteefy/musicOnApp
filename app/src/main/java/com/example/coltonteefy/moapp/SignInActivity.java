package com.example.coltonteefy.moapp;

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
    Button loginBtn, createBtn, createAccountBtn, haveAccountBtn;
    EditText userNameTxt, passwordTxt, signUpEmail, signUpUserName, signUpPassword, confirmSignUpPassword;
    int buttonId;
    String email,userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        loginBtn = findViewById(R.id.loginBtn);
        createBtn = findViewById(R.id.createBtn);
        userNameTxt = findViewById(R.id.userNameTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
    }

    //    login validation check
    public void loginClick(View view) {
        String user = String.valueOf(userNameTxt.getText());
        String password = String.valueOf(passwordTxt.getText());
        if (user.equals("") && password.equals("")) {
            createNewAlertDialog(R.layout.dialog_invalid_user_and_password);
        } else if (user.equals("") && !password.equals("")) {
            createNewAlertDialog(R.layout.dialog_invalid_user);
        } else if (password.equals("") && !user.equals("")) {
            createNewAlertDialog(R.layout.dialog_invalid_password);
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
                            createNewAlertDialog(R.layout.dialog_success);
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
                    Toast.makeText(SignInActivity.this, "Fill out all fields", Toast.LENGTH_SHORT).show();
                } else if (!signUpPassword.getText().toString().equals(confirmSignUpPassword.getText().toString())) {
                    Toast.makeText(SignInActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                } else {
                    email = String.valueOf(signUpEmail.getText());
                    userName = String.valueOf(signUpUserName.getText());
                    password = String.valueOf(signUpPassword.getText());
                    HttpDataHandler handler = new HttpDataHandler();
                    handler.setActivity(SignInActivity.this);
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

    //  set new custom dialog layout
    public void createNewAlertDialog(int r) {
        DialogPlus dialog = DialogPlus.newDialog(SignInActivity.this)
                .setGravity(Gravity.BOTTOM)
                .setContentHolder(new ViewHolder(r))
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setExpanded(true, 250)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        dialog.show();
    }
}

