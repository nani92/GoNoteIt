package eu.napcode.gonoteit.di.modules;

import com.apollographql.apollo.ApolloClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.napcode.gonoteit.api.NoteAdapter;
import eu.napcode.gonoteit.type.CustomType;
import okhttp3.OkHttpClient;

import static eu.napcode.gonoteit.api.ApiConsts.API_URL;

@Module
public class AppModule {

    @Singleton
    @Provides
    ApolloClient provideGoNoteItClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        return ApolloClient.builder()
                .serverUrl(API_URL)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.GENERICSCALAR, new NoteAdapter())
                .build();
    }
}
