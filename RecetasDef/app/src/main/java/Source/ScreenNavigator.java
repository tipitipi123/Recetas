package Source;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.tipix1998.recetasfinal.R;

public class ScreenNavigator {

    public enum AnimationView {
        NONE,
        FADE,
        SLIDE,
        SLIDE_UP
    }

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

    public static void openFragment(FragmentActivity activity, Fragment f, boolean destroyPrev, AnimationView anim){

        Utils.hideKeyboard(activity);

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        switch (anim){
            case FADE:
                ft.setCustomAnimations(R.anim.screen_fade_in,
                        R.anim.screen_fade_out,
                        R.anim.screen_fade_in,
                        R.anim.screen_fade_out);
                break;
            case SLIDE:
                ft.setCustomAnimations(R.anim.slide_left_in,
                        R.anim.slide_left_out,
                        R.anim.slide_right_in,
                        R.anim.slide_right_out);
                break;
            case SLIDE_UP:
                ft.setCustomAnimations(R.anim.slide_up_in,
                        R.anim.slide_up_out,
                        R.anim.slide_up_in,
                        R.anim.slide_up_out);
                break;

            case NONE:
                ft.setCustomAnimations(0,
                        R.anim.slide_left_out,
                        0,
                        R.anim.slide_right_out);
                break;
        }

        if(!destroyPrev) {
            ft.add(R.id.fragmentContainer, f, f.getClass().getSimpleName());
            ft.addToBackStack(null); // si no se añade, al abrir un nuevo fragment, el actual se destuye y no se peude volver atrás
        }else{
            ft.replace(R.id.fragmentContainer, f, f.getClass().getSimpleName());
        }
        // Start the animated transition.
        ft.commitAllowingStateLoss();

    }
}
