package eu.napcode.gonoteit.repository.user;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloMutationCall;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import eu.napcode.gonoteit.AuthenticateMutation;
import eu.napcode.gonoteit.MockRxSchedulers;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.dao.user.UserDao;
import eu.napcode.gonoteit.data.notes.NotesLocal;
import eu.napcode.gonoteit.data.user.UserRemote;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.rx.RxSchedulers;
import eu.napcode.gonoteit.ui.login.UserValidator;
import eu.napcode.gonoteit.utils.TimestampStore;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserRepositoryTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    ApolloClient apolloClient;

    @Mock
    StoreAuth storeAuth;

    @Mock
    Response<AuthenticateMutation.Data> response;

    @Mock
    ApolloMutationCall<AuthenticateMutation.Data> apolloMutationCall;

    @Mock
    ApolloRxHelper apolloRxHelper;

    @Mock
    TimestampStore timestampStore;

    @Mock
    NotesLocal notesLocal;

    @Mock
    UserRemote userRemote;

    @Mock
    UserDao userDao;

    RxSchedulers rxSchedulers = new MockRxSchedulers();

    @Mock
    UserValidator userValidator;

    private UserRepository userRepository;

    @Mock
    MutableLiveData<Resource<AuthenticateMutation.Data>> mutableLiveData;

    @Before
    public void initial() {
        userRepository = new UserRepositoryImpl(apolloClient, storeAuth,
                apolloRxHelper, timestampStore, notesLocal, userRemote,
                userDao, rxSchedulers, userValidator);

        Mockito.when(apolloClient.mutate(Mockito.any(AuthenticateMutation.class)))
                .thenReturn(apolloMutationCall);

        Mockito.when(apolloRxHelper.from((ApolloMutationCall<AuthenticateMutation.Data>) Mockito.any()))
                .thenReturn(Observable.just(response));
    }

    @Test
    public void testAuthenticateIsCalled() {
        userRepository.authenticateUser("", "", mutableLiveData);

        Mockito.verify(apolloClient).mutate(Mockito.any(AuthenticateMutation.class));
    }

    @Test
    public void testAuthenticationWithProperValues() {
        String userName = "login";
        String password = "password";

        userRepository.authenticateUser(userName, password, mutableLiveData);

        Mockito.verify(apolloClient).mutate(Mockito.<Mutation<Operation.Data, Object, Operation.Variables>>argThat(getAuthenticateMatcher(userName, password)));
    }


    private ArgumentMatcher getAuthenticateMatcher(final String userName, final String password) {
        return (ArgumentMatcher<AuthenticateMutation>) authenticateMutation -> authenticateMutation.variables().userName().equals(userName) &&
                authenticateMutation.variables().password().equals(password);
    }

//    @Test
//    public void testStoreUserName() {
//        String userName = "login";
//        TestObserver<Response<AuthenticateMutation.Data>> subscriber = new TestObserver<>();
//
//        userRepository
//                .authenticateUser(userName, "", mutableLiveData)
//                .subscribe(subscriber);
//
//        subscriber.assertSubscribed();
//
//        Mockito.verify(storeAuth).saveName(userName);
//    }

//    @Test
//    public void testStoreToken() {
//        userRepository
//                .authenticateUser("", "", mutableLiveData)
//                .subscribe();
//
//        Mockito.verify(storeAuth).saveToken(Mockito.any());
//    }

//    @Test
//    public void shouldReturnNotLoggedInUser() {
//        Mockito.when(storeAuth.getToken()).thenReturn("");
//
//        Assert.assertEquals(null, userRepository.getLoggedInUser());
//    }

    @Test
    public void shouldReturnLoggedInUser() {
        String username = "name";
        Mockito.when(storeAuth.getToken()).thenReturn("token");
        Mockito.when(storeAuth.getUserName()).thenReturn(username);

        //TODO
        //Assert.assertEquals(username, userRepository.getLoggedInUser());
    }

    @Test
    public void testLogoutUser() {
        userRepository.logoutUser()
                .subscribe();

        Mockito.verify(storeAuth).saveToken(null);
    }
}