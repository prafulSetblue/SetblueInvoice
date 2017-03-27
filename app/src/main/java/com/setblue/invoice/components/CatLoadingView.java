package com.setblue.invoice.components;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.setblue.invoice.R;
import com.setblue.invoice.utils.CommonVariables;


public class CatLoadingView extends DialogFragment {

    public CatLoadingView() {
    }

    Animation operatingAnim;
    Dialog mDialog;
    View mouse;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            try {
                mDialog = new Dialog(getActivity(), R.style.cart_dialog);
                mDialog.setContentView(R.layout.custom_progress);
                mDialog.setCanceledOnTouchOutside(true);
                mDialog.getWindow().setGravity(Gravity.CENTER);
                operatingAnim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                operatingAnim.setRepeatCount(Animation.INFINITE);
                operatingAnim.setDuration(2000);

                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);

                View view = mDialog.getWindow().getDecorView();

                mouse = view.findViewById(R.id.mouse);
            } catch (Exception e) {
                e.printStackTrace();Log.e(CommonVariables.TAG,"Exception: "+e.getMessage());
            }
        }
        return mDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (mouse != null)
                mouse.setAnimation(operatingAnim);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(CommonVariables.TAG,"Exception: "+e.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            operatingAnim.reset();
            mouse.clearAnimation();
        } catch (Exception e) {
            e.printStackTrace();Log.e(CommonVariables.TAG,"Exception: "+e.getMessage());
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
            mDialog = null;
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();Log.e(CommonVariables.TAG,"Exception: "+e.getMessage());
        }
    }
}
