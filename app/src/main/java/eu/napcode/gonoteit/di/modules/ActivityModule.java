package eu.napcode.gonoteit.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eu.napcode.gonoteit.main.MainActivity;

@Module
public interface ActivityModule {

    @ContributesAndroidInjector
    MainActivity bindMainActivity();
}
