package com.example.coltonteefy.moapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.coltonteefy.moapp.utils.CreateCustomToast;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;


public class SignInActivity extends AppCompatActivity {
    DialogPlus dialog;
    Button loginBtn, createBtn, createAccountBtn, haveAccountBtn;
    EditText userNameTxt, passwordTxt, signUpEmail, signUpUserName, signUpPassword, confirmSignUpPassword;
    int buttonId;
    String email, userName, password;
    Activity activity = SignInActivity.this;

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
            CreateCustomToast customToast = new CreateCustomToast();
            customToast.customToast(R.layout.toast_custom_invalid_user_password, activity);
        } else if (user.equals("") && !password.equals("")) {
            CreateCustomToast customToast = new CreateCustomToast();
            customToast.customToast(R.layout.toast_custom_invalid_user, activity);
        } else if (password.equals("") && !user.equals("")) {
            CreateCustomToast customToast = new CreateCustomToast();
            customToast.customToast(R.layout.toast_custom_invalid_password, activity);
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
}

