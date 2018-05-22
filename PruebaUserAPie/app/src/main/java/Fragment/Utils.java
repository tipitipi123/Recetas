package Fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {
    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService( Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity act) {
        if (act == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(act.findViewById(android.R.id.content).getWindowToken(), 0);
    }

    public static void showAlert(Context context, String message, String btn){

        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(btn, null)
                .show();
    }

}
