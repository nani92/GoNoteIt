package eu.napcode.gonoteit.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eu.napcode.gonoteit.ui.login.LoginActivity;
import eu.napcode.gonoteit.ui.main.MainActivity;

@Module
public interface ActivityModule {

    @ContributesAndroidInjector
    MainActivity bindMainActivity();

    @ContributesAndroidInjector
    LoginActivity bindLoginActivity();
}
