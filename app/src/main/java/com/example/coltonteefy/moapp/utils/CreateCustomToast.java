package com.example.coltonteefy.moapp.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class CreateCustomToast {

    public void customToast(int layout, Activity context) {

        View toastView = context.getLayoutInflater().inflate(layout, null);

        Toast toast = new Toast(context);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
