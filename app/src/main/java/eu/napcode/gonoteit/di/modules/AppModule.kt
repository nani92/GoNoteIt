package eu.napcode.gonoteit.di.modules

import android.content.Context
import android.content.SharedPreferences

import com.apollographql.apollo.ApolloClient

import dagger.Module
import dagger.Provides
import eu.napcode.gonoteit.api.ApolloRxHelper
import eu.napcode.gonoteit.app.GoNoteItApp
import eu.napcode.gonoteit.utils.ApiClientProvider
import eu.napcode.gonoteit.utils.NetworkHelper

import android.content.Context.MODE_PRIVATE

@Module
class AppModule {

    @Provides
    internal fun context(application: GoNoteItApp): Context {
        return application.applicationContext
    }

    @Provides
    internal fun provideGoNoteItClient(apiClientProvider: ApiClientProvider): ApolloClient {
        return apiClientProvider.getApiClient()
    }

    @Provides
    internal fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("gonoteit", MODE_PRIVATE)
    }

    @Provides
    internal fun providesApolloRxHelper(): ApolloRxHelper {
        return ApolloRxHelper()
    }

    @Provides
    internal fun providesNetworkHelper(context: Context): NetworkHelper {
        return NetworkHelper(context)
    }
}
