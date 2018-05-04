package eu.napcode.gonoteit.di.components;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import eu.napcode.gonoteit.app.GoNoteItApp;
import eu.napcode.gonoteit.di.modules.ActivityModule;
import eu.napcode.gonoteit.di.modules.RepositoryModule;
import eu.napcode.gonoteit.di.modules.RxModule;
import eu.napcode.gonoteit.di.modules.StorageModule;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        ActivityModule.class,
        RepositoryModule.class,
        RxModule.class,
        StorageModule.class,
        ViewModelModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(GoNoteItApp application);

        AppComponent build();
    }

    void inject(GoNoteItApp application);
}
