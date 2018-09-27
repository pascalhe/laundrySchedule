package zhaw.ch.laundryschedule.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;

public class ProgressView {
    private static final ProgressView ourInstance = new ProgressView();

    public static ProgressView getInstance() {
        return ourInstance;
    }

    private ProgressView() {
    }

    /**
     * Shows the progress UI and hides passed form
     * @param show Boolean if True shows the progressView
     * @param formView View to hide
     * @param progressView View to show
     * @param context Context
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void show(final boolean show, final View formView, final View progressView, Context context) {
        int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        formView.setVisibility(show ? View.GONE : View.VISIBLE);
        formView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                formView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
