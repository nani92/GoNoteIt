package eu.napcode.gonoteit.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import eu.napcode.gonoteit.ui.about.AboutFragment
import eu.napcode.gonoteit.ui.calendar.CalendarFragment
import eu.napcode.gonoteit.ui.notes.NotesFragment

@Module
interface FragmentModule {

    @ContributesAndroidInjector
    fun bindNotesFragment(): NotesFragment

    @ContributesAndroidInjector
    fun bindAboutFragment(): AboutFragment

    @ContributesAndroidInjector
    fun bindCalendarFragment(): CalendarFragment
}
