package eu.napcode.gonoteit.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ActivityMainBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    ViewModelFactory viewModelFactory;

    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        AndroidInjection.inject(this);

        this.mainViewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(MainViewModel.class);

        setSupportActionBar(this.binding.toolbar);

        setupDrawer();
    }

    private void setupDrawer() {
        this.drawerToggle = new ActionBarDrawerToggle(this,
                this.binding.drawerLayout, this.binding.toolbar,
                R.string.open_drawer, R.string.close_drawer);

        this.binding.drawerLayout.addDrawerListener(this.drawerToggle);
        this.drawerToggle.syncState();

        this.binding.navigationView.setNavigationItemSelectedListener(this);
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
                //TODO display main board with notes
                return true;
            case R.id.fav_notes:
                //TODO display favorite notes
                return true;
            case R.id.about:
                //TODO display about
                return true;
            case R.id.logout:
                //TODO display logout ensure dialog
                return true;
            default:
                return false;
        }
    }
}
