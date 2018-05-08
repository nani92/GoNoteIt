package eu.napcode.gonoteit.api;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloQueryWatcher;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;

import javax.annotation.Nonnull;

import io.reactivex.Observable;

public class ApolloRxHelper {

    public ApolloRxHelper() {
    }

    public <T> Observable<Response<T>> from(@Nonnull final ApolloQueryWatcher<T> watcher) {
        return Rx2Apollo.from(watcher);
    }

    @Nonnull public <T> Observable<Response<T>> from(@Nonnull final ApolloCall<T> call) {
        return Rx2Apollo.from(call);
    }
}
