package eu.napcode.gonoteit.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ActivityMainBinding;
import eu.napcode.gonoteit.databinding.DrawerHeaderBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.model.UserModel;
import eu.napcode.gonoteit.ui.about.AboutFragment;
import eu.napcode.gonoteit.ui.calendar.CalendarFragment;
import eu.napcode.gonoteit.ui.login.LoginActivity;
import eu.napcode.gonoteit.ui.notes.NotesFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    ViewModelFactory viewModelFactory;

    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;
    private DrawerHeaderBinding headerBinding;
    private ActionBarDrawerToggle drawerToggle;

    private Fragment fragmentToSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.headerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.drawer_header, binding.navigationView, false);

        AndroidInjection.inject(this);
        setupViewModel();
        setSupportActionBar(this.binding.toolbar);

        setupDrawer();
        setupUser();

        if ("android.intent.action.CALENDAR".equals(getIntent().getAction())) {
            displayCalendar();

            return;
        }

        if (savedInstanceState == null) {
            displayFirstScreen();
        }
    }

    private void setupViewModel() {
        this.mainViewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(MainViewModel.class);
    }

    private void setupDrawer() {
        this.binding.navigationView.addHeaderView(headerBinding.getRoot());

        this.drawerToggle = new ActionBarDrawerToggle(this,
                this.binding.drawerLayout, this.binding.toolbar,
                R.string.open_drawer, R.string.close_drawer);

        this.binding.drawerLayout.addDrawerListener(this.drawerToggle);
        this.drawerToggle.syncState();

        this.binding.navigationView.setNavigationItemSelectedListener(this);

        this.binding.drawerLayout.addDrawerListener(onDrawerClosedDrawerListener);
    }

    private OnDrawerClosedDrawerListener onDrawerClosedDrawerListener = new OnDrawerClosedDrawerListener(){

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            super.onDrawerClosed(drawerView);

            displayFragment();
        }
    };

    private void displayFragment() {
        fragmentToSet.setEnterTransition(getTransitionForFragment());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContainerFrame, fragmentToSet)
                .commit();
    }

    private Slide getTransitionForFragment() {
        Slide slide = new Slide();
        slide.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        return slide;
    }

    private void setupUser() {
        this.mainViewModel.getLoggedInUser().observe(this, this::processUser);
    }

    private void processUser(UserModel user) {

        if (user == null && mainViewModel.isUserLoggedIn() == false) {
            setViewsForNotLoggedInUser();

            return;
        }

        if (user == null) {
            return;
        }

        setViewsForLoggedInUser(user);
    }

    private void setViewsForNotLoggedInUser() {
        headerBinding.usernameTextView.setText(R.string.not_logged_in);
        headerBinding.usernameTextView.setCompoundDrawables(null, null, null, null);
        headerBinding.loginButton.setVisibility(VISIBLE);
        headerBinding.loginButton.setOnClickListener(v -> startLoginActivity());

        binding.navigationView.getMenu().findItem(R.id.logout).setVisible(false);

        showLoginSnackbar();
    }

    private void startLoginActivity (){
        Intent intent = new Intent(this, LoginActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this);

        startActivity(intent, options.toBundle());
    }

    private void showLoginSnackbar() {
        Snackbar snackbar = Snackbar.make(binding.drawerLayout, R.string.not_logged_in, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.login, v -> startLoginActivity());

        snackbar.show();
    }

    private void setViewsForLoggedInUser(UserModel user) {
        headerBinding.usernameTextView.setText(user.getUserName());
        headerBinding.usernameTextView.setCompoundDrawables(null, null, getDrawable(R.drawable.ic_edit_24px), null);
        headerBinding.loginButton.setVisibility(GONE);

        binding.navigationView.getMenu().findItem(R.id.logout).setVisible(true);
    }

    private void displayFirstScreen() {
        fragmentToSet = NotesFragment.Companion.newInstance(false);
        displayFragment();
        this.binding.navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void displayCalendar() {
        fragmentToSet = new CalendarFragment();
        displayFragment();
        this.binding.navigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        this.drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (this.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        this.binding.drawerLayout.closeDrawer(Gravity.START);

        switch (item.getItemId()) {
            case R.id.notes:
                fragmentToSet = NotesFragment.Companion.newInstance(false);

                return true;
            case R.id.fav_notes:
                fragmentToSet = NotesFragment.Companion.newInstance(true);

                return true;

            case R.id.calendar:
                fragmentToSet = new CalendarFragment();

                return true;
            case R.id.about:
                fragmentToSet = new AboutFragment();

                return true;
            case R.id.logout:
                displayLogoutDialogFragment();

                return true;
            default:
                return false;
        }
    }

    private void displayLogoutDialogFragment() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.logout, (dialog1, which) -> {
                    mainViewModel.logoutUser();
                    displayFirstScreen();
                })
                .setNegativeButton(android.R.string.cancel, (dialog12, which) -> {
                })
                .create();

        dialog.show();
    }

    public void showProgressBar() {
        binding.progressBar.setVisibility(VISIBLE);
    }

    public void hideProgressBar() {
        binding.progressBar.setVisibility(GONE);
    }

    public void displayMessage(String message) {
        Snackbar.make(binding.mainContainerFrame, message, Snackbar.LENGTH_LONG).show();
    }
}
