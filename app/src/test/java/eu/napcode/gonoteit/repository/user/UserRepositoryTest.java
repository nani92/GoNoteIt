package eu.napcode.gonoteit.repository.user;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloMutationCall;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
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

import eu.napcode.gonoteit.AuthenticateMutation;
import eu.napcode.gonoteit.MockRxSchedulers;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.auth.StoreAuth;
import io.reactivex.Observable;

@RunWith(MockitoJUnitRunner.class)
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

    private UserRepository userRepository;

    @Before
    public void initial() {
        userRepository = new UserRepositoryImpl(apolloClient, storeAuth, new MockRxSchedulers(), apolloRxHelper);
        Mockito.when(apolloClient.mutate(Mockito.any(AuthenticateMutation.class)))
                .thenReturn(apolloMutationCall);
        Mockito.when(apolloRxHelper.from((ApolloMutationCall<AuthenticateMutation.Data>) Mockito.any()))
                .thenReturn(Observable.just(response));
    }

    @Test
    public void testAuthenticateIsCalled() {
        userRepository.authenticateUser("", "");

        Mockito.verify(apolloClient).mutate(Mockito.any(AuthenticateMutation.class));
    }

    @Test
    public void testAuthenticationWithProperValues() {
        String userName = "login";
        String password = "password";

        userRepository.authenticateUser(userName, password);

        Mockito.verify(apolloClient).mutate(Mockito.<Mutation<Operation.Data, Object, Operation.Variables>>argThat(getAuthenticateMatcher(userName, password)));
    }


    private ArgumentMatcher getAuthenticateMatcher(final String userName, final String password) {
        return (ArgumentMatcher<AuthenticateMutation>) authenticateMutation -> authenticateMutation.variables().userName().equals(userName) &&
                authenticateMutation.variables().password().equals(password);
    }

    @Test
    public void testStoreUserName() {
        String userName = "login";
        Mockito.when(response.data())
                .thenReturn(new AuthenticateMutation.Data(new AuthenticateMutation.TokenAuth("", "")));


        userRepository.authenticateUser(userName, "");

        Mockito.verify(storeAuth, Mockito.after(1000)).saveName(userName);
    }

    @Test
    public void testStoreToken() {
        String token = "VeryRandomStringToken";
        Mockito.when(response.data())
                .thenReturn(new AuthenticateMutation.Data(new AuthenticateMutation.TokenAuth("", token)));

        userRepository.authenticateUser("", "");

        Mockito.verify(storeAuth, Mockito.after(100)).saveToken(token);
    }
}