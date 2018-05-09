package eu.napcode.gonoteit.di.modules.viewmodel;

import android.arch.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.Binds;
import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.IntoMap;
import eu.napcode.gonoteit.ui.login.LoginActivity;
import eu.napcode.gonoteit.ui.login.LoginViewModel;
import eu.napcode.gonoteit.ui.main.MainViewModel;

@Module
public interface ViewModelModule {

    @Documented
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }

    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel mainViewModel(MainViewModel viewModel);

    @IntoMap
    @Binds
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel loginViewModel(LoginViewModel viewModel);
}
