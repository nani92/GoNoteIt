package eu.napcode.gonoteit.main;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ActivityMainBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;

public class MainActivity extends AppCompatActivity {

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
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }
}
