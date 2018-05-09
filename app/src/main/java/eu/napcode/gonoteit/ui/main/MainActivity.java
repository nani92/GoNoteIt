package eu.napcode.gonoteit.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.GetNotesQuery;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ActivityMainBinding;
import eu.napcode.gonoteit.api.Note;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.model.NoteModel;
import eu.napcode.gonoteit.type.Type;
import eu.napcode.gonoteit.ui.login.LoginActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    ViewModelFactory viewModelFactory;

    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle drawerToggle;

    @Inject
    ApolloClient apolloClient;

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

        graphQLTry();
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

    private void graphQLTry() {
        CompositeDisposable disposables = new CompositeDisposable();

        ApolloCall<GetNotesQuery.Data> notesQuery = apolloClient.
                query(new GetNotesQuery());

        disposables.add(Rx2Apollo.from(notesQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<GetNotesQuery.Data>>() {

                                   @Override
                                   public void onNext(Response<GetNotesQuery.Data> dataResponse) {
                                       Log.d("Natalia", "ok ");

                                       for (GetNotesQuery.AllEntity allEntity : dataResponse.data().allEntities()) {
                                           Log.d("Natalia dupa", allEntity.type().rawValue() + allEntity.data());
                                           Type type = allEntity.type();
                                           Log.d("Natali kupa", ((NoteModel)((Note)allEntity.data()).parseNote(type)).getMsg());
                                       }

                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       Log.e("Natalia", e.getMessage(), e);
                                   }

                                   @Override
                                   public void onComplete() {

                                   }
                               }
                ));
    }
}
