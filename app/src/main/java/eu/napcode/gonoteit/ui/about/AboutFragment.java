package eu.napcode.gonoteit.ui.about;

import android.animation.LayoutTransition;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.FragmentAboutBinding;

import static android.view.View.GONE;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews();
    }

    private void setupViews() {
        setupLayoutTransitions();

        binding.aboutCardView.setOnClickListener(v -> toggleAbout());
    }

    private void setupLayoutTransitions() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        binding.constraintLayout.setLayoutTransition(layoutTransition);

    }

    private void toggleAbout() {

        if (binding.aboutIncluded.contentTextView.getVisibility() == GONE) {
            expandLayout(binding.aboutIncluded.constraintLayout, R.layout.about_card_about_expanded);
        } else {
            collapseLayout(binding.aboutIncluded.constraintLayout, R.layout.about_card_about);
        }
    }

    private void expandLayout(ConstraintLayout layoutToExpand, int expandLayoutId) {
        ConstraintSet expandedConstraintSet = new ConstraintSet();
        expandedConstraintSet.clone(getContext(), expandLayoutId);
        expandedConstraintSet.applyTo(layoutToExpand);

        TransitionManager.beginDelayedTransition(layoutToExpand);
    }

    private void collapseLayout(ConstraintLayout layoutToCollapse, int collapseLayoutId) {
        ConstraintSet expandedConstraintSet = new ConstraintSet();
        expandedConstraintSet.clone(getContext(), collapseLayoutId);
        expandedConstraintSet.applyTo(layoutToCollapse);

        TransitionManager.beginDelayedTransition(layoutToCollapse);
    }
}
