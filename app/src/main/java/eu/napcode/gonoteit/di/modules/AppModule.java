package eu.napcode.gonoteit.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.apollographql.apollo.ApolloClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.api.NoteAdapter;
import eu.napcode.gonoteit.app.GoNoteItApp;
import eu.napcode.gonoteit.type.CustomType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static eu.napcode.gonoteit.api.ApiConsts.API_URL;

@Module
public class AppModule {

    @Provides
    Context context(GoNoteItApp application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    ApolloClient provideGoNoteItClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        return ApolloClient.builder()
                .serverUrl(API_URL)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.GENERICSCALAR, new NoteAdapter())
                .build();
    }

    @Provides
    SharedPreferences provideSharedPrefs(Context context) {
        return context.getSharedPreferences("gonoteit", Context.MODE_PRIVATE);
    }

    @Provides
    ApolloRxHelper providesApolloRxHelper() {
        return new ApolloRxHelper();
    }
}
