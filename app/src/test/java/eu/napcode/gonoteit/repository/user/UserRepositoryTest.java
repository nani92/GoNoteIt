package eu.napcode.gonoteit.repository.user;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloMutationCall;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import eu.napcode.gonoteit.AuthenticateMutation;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    @Mock
    ApolloClient apolloClient;

    @Mock
    ApolloCall<AuthenticateMutation.Data> authenticateMutationApolloCall;

    private UserRepository userRepository;

    @Before
    public void initial() {
        userRepository = new UserRepositoryImpl(apolloClient);
    }

    @Test
    public void testAuthenticateIsCalled() {
        userRepository.authenticateUser("", "");

        Mockito.verify(apolloClient).mutate(Mockito.any(AuthenticateMutation.class));
    }
}