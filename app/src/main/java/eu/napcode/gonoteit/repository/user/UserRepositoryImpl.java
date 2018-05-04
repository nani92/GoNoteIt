package eu.napcode.gonoteit.repository.user;

import com.apollographql.apollo.ApolloClient;

import javax.inject.Inject;

import eu.napcode.gonoteit.AuthenticateMutation;

public class UserRepositoryImpl implements UserRepository {

    private ApolloClient apolloClient;

    @Inject
    public UserRepositoryImpl(ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
    }

    @Override
    public void authenticateUser(String login, String password) {
        apolloClient.mutate(new AuthenticateMutation(login, password));
    }
}
