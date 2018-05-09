package eu.napcode.gonoteit.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eu.napcode.gonoteit.ui.notes.NotesFragment;

@Module
public interface FragmentModule {

    @ContributesAndroidInjector
    NotesFragment bindNotesFragment();
}
