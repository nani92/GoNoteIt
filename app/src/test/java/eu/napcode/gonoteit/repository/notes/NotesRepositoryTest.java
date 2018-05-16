package eu.napcode.gonoteit.repository.notes;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.GetNotesQuery;
import eu.napcode.gonoteit.MockRxSchedulers;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.type.Type;
import io.reactivex.Observable;
import io.reactivex.subscribers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class NotesRepositoryTest {

    private NotesRepository notesRepository;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    ApolloClient apolloClient;

    @Mock
    StoreAuth storeAuth;

    @Mock
    ApolloRxHelper apolloRxHelper;

    @Mock
    ApolloQueryCall<GetNotesQuery.Data> apolloGetNotesCall;

    @Mock
    Response<GetNotesQuery.Data> getNotesResponse;

    @Before
    public void init() {
        this.notesRepository = new NotesRepositoryImpl(apolloClient, storeAuth, apolloRxHelper);

        Mockito.when(apolloClient.query(Mockito.any(GetNotesQuery.class)))
                .thenReturn(apolloGetNotesCall);

        Mockito.when(apolloRxHelper.from(apolloGetNotesCall))
                .thenReturn(Observable.just(getNotesResponse));
    }

    @Test
    public void testCallGetNotesQuery() {
        notesRepository.getNotes();

        Mockito.verify(apolloClient).query(Mockito.any(GetNotesQuery.class));
    }

    @Test
    public void testCallIntoRx() {
        notesRepository.getNotes();

        Mockito.verify(apolloRxHelper).from(apolloGetNotesCall);
    }

    @Test
    public void testReturnNotes() {
        TestSubscriber<List<NoteModel>> notesSubscriber = new TestSubscriber<>();
        notesRepository.getNotes().subscribe(notesSubscriber);

        notesSubscriber.assertSubscribed();
    }

    @Test
    public void testCallNoteMutation() {
        notesRepository.createNote(new NoteModel());

        Mockito.verify(apolloClient).mutate(Mockito.any(CreateNoteMutation.class));
    }

    @Test
    public void testCreateNoteSendingProvidedContentValue() {
        NoteModel noteModel = new NoteModel();
        noteModel.setTitle("test title");
        noteModel.setContent("test content");

        notesRepository.createNote(noteModel);

        Mockito.verify(apolloClient).mutate(
                Mockito.argThat((ArgumentMatcher<CreateNoteMutation>) argument -> {
                    Input<String> content = argument.variables().content();

                    return content.defined && content.value.equals(noteModel.getContent());
                }));
    }

    @Test
    public void testCreateNoteSendingProvidedTitleValue() {
        NoteModel noteModel = new NoteModel();
        noteModel.setTitle("test title");
        noteModel.setContent("test content");

        notesRepository.createNote(noteModel);

        Mockito.verify(apolloClient).mutate(
                Mockito.argThat((ArgumentMatcher<CreateNoteMutation>) argument -> {
                    Input<String> title = argument.variables().title();

                    return title.defined && title.value.equals(noteModel.getTitle());
                }));
    }
}