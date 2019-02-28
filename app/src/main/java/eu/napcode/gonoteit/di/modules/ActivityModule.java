package eu.napcode.gonoteit.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eu.napcode.gonoteit.ui.create.CreateActivity;
import eu.napcode.gonoteit.ui.login.LoginActivity;
import eu.napcode.gonoteit.ui.main.MainActivity;
import eu.napcode.gonoteit.ui.note.NoteActivity;
import eu.napcode.gonoteit.ui.splash.SplashActivity;

@Module
public interface ActivityModule {

    @ContributesAndroidInjector
    MainActivity bindMainActivity();

    @ContributesAndroidInjector
    LoginActivity bindLoginActivity();

    @ContributesAndroidInjector
    CreateActivity bindCreateActivity();

    @ContributesAndroidInjector
    NoteActivity bindNoteActivity();

    @ContributesAndroidInjector
    SplashActivity bindSplashActivity();
}
