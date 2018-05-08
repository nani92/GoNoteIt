package eu.napcode.gonoteit.di.modules;

import dagger.Module;
import dagger.Provides;
import eu.napcode.gonoteit.ui.login.UserValidator;

@Module
public class UtilsModule {

    @Provides
    UserValidator providesUserValidator() {
        return new UserValidator();
    }
}
