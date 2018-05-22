package Fragment;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class ScreenNavigator {
    public static void openDialogFragment(FragmentActivity activity, DialogFragment fragment){
        Utils.hideKeyboard(activity);

        // show dialog share
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag(Common.FRAGMENT_TAG_DIALOG);
        if (prev != null) {
            fm.beginTransaction().remove(prev);
        }
        fm.beginTransaction().addToBackStack(null); //si no se añade, al abrir un nuevo fragment, el actual se destuye y no se peude volver atrás

        fragment.show(fm, Common.FRAGMENT_TAG_DIALOG);
    }
}
