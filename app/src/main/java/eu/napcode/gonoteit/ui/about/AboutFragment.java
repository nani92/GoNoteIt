package eu.napcode.gonoteit.ui.about;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
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

import static android.content.Intent.ACTION_VIEW;
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

        setupDevViews();
        binding.repoIncluded.githubImageView.setOnClickListener(v -> openGithub());

        binding.aboutCardView.setOnClickListener(v -> toggleAbout());
        binding.devCardView.setOnClickListener(v -> toggleDev());
        binding.repoCardView.setOnClickListener(v -> toggleRepo());
    }

    private void setupLayoutTransitions() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        binding.constraintLayout.setLayoutTransition(layoutTransition);
    }

    private void setupDevViews() {
        binding.devIncluded.napcodeImageView.setOnClickListener(v -> openNapcodeWeb());
        binding.devIncluded.webImageView.setOnClickListener(v -> openNapcodeWeb());
        binding.devIncluded.playstoreImageView.setOnClickListener(v -> openPlayStore());
    }

    private void openNapcodeWeb() {
        Intent intent = new Intent(ACTION_VIEW, Uri.parse(getString(R.string.napcode_web)));
        startActivity(intent);
    }

    private void openPlayStore() {
        Intent marketIntent = new Intent(ACTION_VIEW, Uri.parse(getString(R.string.playstore_market)));
        Intent webIntent = new Intent(ACTION_VIEW, Uri.parse(getString(R.string.playstore_web)));

        try {
            startActivity(marketIntent);
        } catch (Exception e) {
            startActivity(webIntent);
        }
    }

    private void openGithub() {
        Intent intent = new Intent(ACTION_VIEW, Uri.parse(getString(R.string.repo_github)));
        startActivity(intent);
    }

    private void toggleAbout() {

        if (shouldExpand(binding.aboutIncluded.contentTextView)) {
            expandLayout(binding.aboutIncluded.constraintLayout, R.layout.about_card_about_expanded);
        } else {
            collapseLayout(binding.aboutIncluded.constraintLayout, R.layout.about_card_about);
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
        constraintSet.clone(getContext(), layoutId);
        constraintSet.applyTo(constraintLayout);


        TransitionManager.beginDelayedTransition(constraintLayout);
    }

    private void toggleDev() {

        if (shouldExpand(binding.devIncluded.expandGroup)) {
            expandLayout(binding.devIncluded.constraintLayout, R.layout.about_card_dev_expanded);
        } else {
            collapseLayout(binding.devIncluded.constraintLayout, R.layout.about_card_dev);
        }
    }

    private void toggleRepo() {

        if (shouldExpand(binding.repoIncluded.expandGroup)) {
            expandLayout(binding.repoIncluded.constraintLayout, R.layout.about_card_repo_expanded);
        } else {
            collapseLayout(binding.repoIncluded.constraintLayout, R.layout.about_card_repo);
        }
    }
}
