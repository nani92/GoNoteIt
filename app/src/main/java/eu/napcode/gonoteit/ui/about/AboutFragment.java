package eu.napcode.gonoteit.ui.about;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.FragmentAboutBinding;

import static android.content.Intent.ACTION_VIEW;

public class AboutFragment extends Fragment implements GithubDescriptionDownloadAsyncTask.GithubDescriptionListener {

    @Inject
    AboutAnimationHelper animationHelper;

    private FragmentAboutBinding binding;
    private GithubDescriptionDownloadAsyncTask asyncTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AndroidSupportInjection.inject(this);

        setupViews();

        downloadGithubDescription();
    }

    private void setupViews() {
        animationHelper.setupLayoutTransitions(binding.constraintLayout);

        setupDevViews();
        binding.repoIncluded.githubImageView.setOnClickListener(v -> openGithub());

        binding.aboutCardView.setOnClickListener(v -> toggleAbout());
        binding.devCardView.setOnClickListener(v -> toggleDev());
        binding.repoCardView.setOnClickListener(v -> toggleRepo());
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
        animationHelper.toggleViews(
                binding.aboutIncluded.contentTextView,
                binding.aboutIncluded.constraintLayout,
                R.layout.about_card_about_expanded, R.layout.about_card_about);
    }

    private void toggleDev() {
        animationHelper.toggleViews(
                binding.devIncluded.expandGroup, binding.devIncluded.constraintLayout,
                R.layout.about_card_dev_expanded, R.layout.about_card_dev);
    }

    private void toggleRepo() {
        animationHelper.toggleViews(
                binding.repoIncluded.expandGroup, binding.repoIncluded.constraintLayout,
                R.layout.about_card_repo_expanded, R.layout.about_card_repo);
    }

    private void downloadGithubDescription() {
        asyncTask = new GithubDescriptionDownloadAsyncTask();
        asyncTask.attachListener(this);
        asyncTask.execute();
    }

    @Override
    public void onDescriptionReceived(String description) {
        binding.repoIncluded.descriptionTextView.setText(description);
    }

    @Override
    public void onStop() {
        super.onStop();

        asyncTask.attachListener(null);
    }
}
