package eu.napcode.gonoteit.di.modules;

import dagger.Binds;
import dagger.Module;
import eu.napcode.gonoteit.repository.calendar.CalendarRepository;
import eu.napcode.gonoteit.repository.calendar.CalendarRepositoryImpl;
import eu.napcode.gonoteit.repository.notes.NotesRepository;
import eu.napcode.gonoteit.repository.notes.NotesRepositoryImpl;
import eu.napcode.gonoteit.repository.user.UserRepository;
import eu.napcode.gonoteit.repository.user.UserRepositoryImpl;

@Module
public interface RepositoryModule {

    @Binds
    NotesRepository notesRepository(NotesRepositoryImpl notesRepository);

    @Binds
    UserRepository userRepository(UserRepositoryImpl userRepository);


    @Binds
    CalendarRepository calendarRepository(CalendarRepositoryImpl calendarRepository);
}
