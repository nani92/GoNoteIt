package eu.napcode.gonoteit.repository.user;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;

import javax.inject.Inject;

import eu.napcode.gonoteit.AuthenticateMutation;
import io.reactivex.Observable;

public class UserRepositoryImpl implements UserRepository {

    private ApolloClient apolloClient;

    @Inject
    public UserRepositoryImpl(ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
    }

    @Override
    public Observable<Response<AuthenticateMutation.Data>> authenticateUser(String login, String password) {
        ApolloCall<AuthenticateMutation.Data> authMutation = apolloClient
                .mutate(new AuthenticateMutation(login, password));

        return Rx2Apollo.from(authMutation);
    }
}
