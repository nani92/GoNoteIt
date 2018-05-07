package eu.napcode.gonoteit.repository.user;

import android.annotation.SuppressLint;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;

import javax.inject.Inject;

import eu.napcode.gonoteit.AuthenticateMutation;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.rx.RxSchedulers;
import io.reactivex.Observable;

public class UserRepositoryImpl implements UserRepository {

    private ApolloClient apolloClient;
    private StoreAuth storeAuth;
    private RxSchedulers rxSchedulers;

    @Inject
    public UserRepositoryImpl(ApolloClient apolloClient, StoreAuth storeAuth, RxSchedulers rxSchedulers) {
        this.apolloClient = apolloClient;
        this.storeAuth = storeAuth;
        this.rxSchedulers = rxSchedulers;
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<Response<AuthenticateMutation.Data>> authenticateUser(String login, String password) {
        ApolloCall<AuthenticateMutation.Data> authMutation = apolloClient
                .mutate(new AuthenticateMutation(login, password));

        Observable<Response<AuthenticateMutation.Data>> authObservable = Rx2Apollo.from(authMutation);
        authObservable.subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.androidMainThread())
                .subscribe(
                        dataResponse -> {
                            storeAuth.saveToken(dataResponse.data().tokenAuth().token());
                            storeAuth.saveName(login);
                        });

        return authObservable;
    }
}
