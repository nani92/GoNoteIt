package eu.napcode.gonoteit.app;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasFragmentInjector;
import eu.napcode.gonoteit.BuildConfig;
import eu.napcode.gonoteit.di.components.DaggerAppComponent;
import timber.log.Timber;

public class GoNoteItApp extends Application implements HasActivityInjector, HasFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidFragmentInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        setupDagger();
        setupTimber();
    }

    private void setupDagger() {
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }

    private void setupTimber() {

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return this.dispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return this.dispatchingAndroidFragmentInjector;
    }
}
