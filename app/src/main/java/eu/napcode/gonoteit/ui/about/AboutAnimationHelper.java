package eu.napcode.gonoteit.ui.about;

import android.animation.LayoutTransition;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.transition.TransitionManager;
import android.view.View;

import javax.inject.Inject;

import eu.napcode.gonoteit.R;

import static android.view.View.GONE;

public class AboutAnimationHelper {

    private Context context;

    @Inject
    public AboutAnimationHelper(Context context) {
        this.context = context;
    }

    public void setupLayoutTransitions(ConstraintLayout constraintLayout) {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(getAnimationDuration());
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

        constraintLayout.setLayoutTransition(layoutTransition);
    }

    private int getAnimationDuration() {
        return context.getResources().getInteger(R.integer.anim_duration_medium);
    }

    public void toggleViews(View expandView, ConstraintLayout constraintLayout, int expandLayoutId, int collapseLayoutId) {

        if (shouldExpand(expandView)) {
            expandLayout(constraintLayout, expandLayoutId);
        } else {
            collapseLayout(constraintLayout, collapseLayoutId);
        }
    }

    private boolean shouldExpand(View view) {
        return view.getVisibility() == GONE;
    }

    private void expandLayout(ConstraintLayout layoutToExpand, int expandLayoutId) {
        layoutToChange(layoutToExpand, expandLayoutId);
    }

    private void collapseLayout(ConstraintLayout layoutToCollapse, int collapseLayoutId) {
        layoutToChange(layoutToCollapse, collapseLayoutId);
    }

    private void layoutToChange(ConstraintLayout constraintLayout, int layoutId) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(context, layoutId);
        constraintSet.applyTo(constraintLayout);

        TransitionManager.beginDelayedTransition(constraintLayout);
    }
}
