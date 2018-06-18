package com.lfjmgs.networkutils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class LoadingDialog {

    private static AlertDialog sDialog;

    public static AlertDialog createDialog(Context context, boolean cancelable,
                                           DialogInterface.OnCancelListener onCancelListener) {
        return new AlertDialog.Builder(context, R.style.dialog_custom)
                .setCancelable(cancelable)
                .setOnCancelListener(onCancelListener)
                .create();
    }

    public static void dismiss() {
        if (!isShowing()) {
            sDialog = null;
            return;
        }
        try {
            sDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sDialog = null;
    }


    public static boolean isShowing() {
        return sDialog != null && sDialog.isShowing();
    }


    public static AlertDialog show(Context context) {
        return show(context, true, null);
    }

    public static AlertDialog show(Context context,
                                   DialogInterface.OnCancelListener onCancelListener) {
        return show(context, true, onCancelListener);
    }

    public static AlertDialog show(Context context, boolean cancelable,
                                   DialogInterface.OnCancelListener onCancelListener) {
        if (isShowing() && sDialog.getContext() == context) {
            return sDialog;
        }

        dismiss();

        try {
            sDialog = createDialog(context, cancelable, onCancelListener);
            sDialog.show();
            sDialog.setContentView(R.layout.common_loading);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sDialog;

    }

}
