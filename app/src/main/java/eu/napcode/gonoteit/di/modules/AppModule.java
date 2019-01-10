package eu.napcode.gonoteit.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.apollographql.apollo.ApolloClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.api.NoteAdapter;
import eu.napcode.gonoteit.api.UUIDAdapter;
import eu.napcode.gonoteit.app.GoNoteItApp;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.utils.NetworkHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

import static android.content.Context.MODE_PRIVATE;
import static eu.napcode.gonoteit.api.ApiConsts.API_URL;
import static eu.napcode.gonoteit.type.CustomType.GENERICSCALAR;
import static eu.napcode.gonoteit.type.CustomType.UUID;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@Module
public class AppModule {

    @Provides
    Context context(GoNoteItApp application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    ApolloClient provideGoNoteItClient(StoreAuth storeAuth) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder()
                            .method(original.method(), original.body());
                    builder.header("Authorization", "JWT " + storeAuth.getToken());

                    return chain.proceed(builder.build());
                })
                .build();

        return ApolloClient.builder()
                .serverUrl(API_URL)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(GENERICSCALAR, new NoteAdapter())
                .addCustomTypeAdapter(UUID, new UUIDAdapter())
                .build();
    }

    @Provides
    SharedPreferences provideSharedPrefs(Context context) {
        return context.getSharedPreferences("gonoteit", MODE_PRIVATE);
    }

    @Provides
    ApolloRxHelper providesApolloRxHelper() {
        return new ApolloRxHelper();
    }

    @Provides
    NetworkHelper providesNetworkHelper(Context context) {
        return new NetworkHelper(context);
    }
}
