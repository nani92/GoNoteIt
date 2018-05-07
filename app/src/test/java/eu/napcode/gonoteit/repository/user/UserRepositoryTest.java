package eu.napcode.gonoteit.repository.user;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloMutationCall;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import eu.napcode.gonoteit.AuthenticateMutation;
import eu.napcode.gonoteit.MockRxSchedulers;
import eu.napcode.gonoteit.auth.StoreAuth;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    @Mock
    ApolloClient apolloClient;

    @Mock
    StoreAuth storeAuth;

    @Mock
    Response<AuthenticateMutation.Data> response;

    @Mock
    ApolloMutationCall<AuthenticateMutation.Data> apolloMutationCall;

    private UserRepository userRepository;

    @Before
    public void initial() {
        userRepository = new UserRepositoryImpl(apolloClient, storeAuth, new MockRxSchedulers());
        Mockito.when(apolloClient.mutate(Mockito.any(AuthenticateMutation.class)))
                .thenReturn(apolloMutationCall);
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

        userRepository.authenticateUser(userName, "");

        Mockito.verify(storeAuth).saveName(userName);
    }

    @Test
    public void testStoreToken() {
        //TODO think of way of creating a response by hand :(
        userRepository.authenticateUser("havk", "havkhavk");

        Mockito.verify(storeAuth).saveToken(Mockito.anyString());
    }
}