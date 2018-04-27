package eu.napcode.gonoteit.di.modules;

import dagger.Module;
import dagger.Provides;
import eu.napcode.gonoteit.rx.AppSchedulers;
import eu.napcode.gonoteit.rx.RxSchedulers;

@Module
public class RxModule {

    @Provides
    RxSchedulers provideRxSchedulers() {
        return new AppSchedulers();
    }
}
