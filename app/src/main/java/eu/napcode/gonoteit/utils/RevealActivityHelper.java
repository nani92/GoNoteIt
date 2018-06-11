package eu.napcode.gonoteit.utils;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;

import eu.napcode.gonoteit.R;

import static android.view.View.INVISIBLE;

public class RevealActivityHelper {

    public static final String REVEAL_X_KEY = "reveal x";
    public static final String REVEAL_Y_KEY = "reveal y";

    private View rootViewToReveal;
    private Context context;
    private int revealX, revealY;

    public RevealActivityHelper(Context context, View rootViewToReveal, Intent intent) {
        this.context = context;
        this.rootViewToReveal = rootViewToReveal;

        revealX = intent.getIntExtra(REVEAL_X_KEY, 0);
        revealY = intent.getIntExtra(REVEAL_Y_KEY, 0);

        rootViewToReveal.setVisibility(INVISIBLE);
        setupViewTreeObserver(rootViewToReveal.getViewTreeObserver());
    }

    private void setupViewTreeObserver(ViewTreeObserver observer) {

        if (observer.isAlive()) {

            observer.addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            reveal();
                            rootViewToReveal.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
        }
    }

    public void reveal() {
        float finalRadius = (float) (Math.max(rootViewToReveal.getWidth(), rootViewToReveal.getHeight()) * 1.1);

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootViewToReveal, revealX, revealY, 0, finalRadius);
        circularReveal.setDuration(context.getResources().getInteger(R.integer.anim_duration_medium));
        circularReveal.setInterpolator(new AccelerateInterpolator());

        rootViewToReveal.setVisibility(View.VISIBLE);
        circularReveal.start();
    }
}
