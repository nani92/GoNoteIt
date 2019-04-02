package eu.napcode.gonoteit.app

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import eu.napcode.gonoteit.BuildConfig
import eu.napcode.gonoteit.BuildConfig.SENTRY_DSN
import eu.napcode.gonoteit.di.components.DaggerAppComponent
import io.sentry.Sentry
import timber.log.Timber

class GoNoteItApp : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingAndroidFragmentInjector: DispatchingAndroidInjector<Fragment>


    override fun onCreate() {
        super.onCreate()

        setupDagger()
        setupTimber()

        Sentry.init(SENTRY_DSN)
    }

    private fun setupDagger() {
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    private fun setupTimber() {

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return this.dispatchingAndroidInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return this.dispatchingAndroidFragmentInjector
    }
}
