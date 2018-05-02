package eu.napcode.gonoteit.main;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.GetNotesQuery;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.rx.RxSchedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidInjection.inject(this);

        this.mainViewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(MainViewModel.class);

        graphQLTry();
    }

    private void graphQLTry() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl("http://10.0.0.105:8000/graphql/")
                .okHttpClient(okHttpClient)
                .build();

        CompositeDisposable disposables = new CompositeDisposable();

        ApolloCall<GetNotesQuery.Data> notesQuery = apolloClient.
                query(new GetNotesQuery());

        disposables.add(Rx2Apollo.from(notesQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<GetNotesQuery.Data>>() {

                                   @Override
                                   public void onNext(Response<GetNotesQuery.Data> dataResponse) {
                                       Log.d("Natalia", "ok ");

                                       for (GetNotesQuery.AllEntity allEntity : dataResponse.data().allEntities()) {
                                           Log.d("Natalia dupa", allEntity.type().rawValue());
                                       }

                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       Log.e("Natalia", e.getMessage(), e);
                                   }

                                   @Override
                                   public void onComplete() {

                                   }
                               }
                ));

    }
}
