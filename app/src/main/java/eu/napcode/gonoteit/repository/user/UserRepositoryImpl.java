package eu.napcode.gonoteit.repository.user;

import android.annotation.SuppressLint;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;

import javax.inject.Inject;

import eu.napcode.gonoteit.AuthenticateMutation;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.model.UserModel;
import io.reactivex.Observable;

public class UserRepositoryImpl implements UserRepository {

    private ApolloClient apolloClient;
    private StoreAuth storeAuth;
    private ApolloRxHelper apolloRxHelper;

    @Inject
    public UserRepositoryImpl(ApolloClient apolloClient, StoreAuth storeAuth, ApolloRxHelper apolloRxHelper) {
        this.apolloClient = apolloClient;
        this.storeAuth = storeAuth;
        this.apolloRxHelper = apolloRxHelper;
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<Response<AuthenticateMutation.Data>> authenticateUser(String login, String password) {
        ApolloCall<AuthenticateMutation.Data> authMutation = apolloClient
                .mutate(new AuthenticateMutation(login, password));

        return apolloRxHelper.from(authMutation);
    }

    @Override
    public void saveUserAuthData(String login, String token) {
        storeAuth.saveToken(token);
        storeAuth.saveName(login);
    }

    @Override
    public UserModel getLoggedInUser() {

        if (isUserLoggedIn()) {
            return new UserModel(storeAuth.getUserName());
        }

        return null;
    }

    private boolean isUserLoggedIn() {
        String token = storeAuth.getToken();

        return token != null && token.length() > 0;
    }
}
