package eu.napcode.gonoteit.di.modules;

import dagger.Binds;
import dagger.Module;
import eu.napcode.gonoteit.repository.NotesRepository;
import eu.napcode.gonoteit.repository.NotesRepositoryImpl;

@Module
public interface RepositoryModule {

    @Binds
    NotesRepository recipesRepository(NotesRepositoryImpl recipesRepository);
}
