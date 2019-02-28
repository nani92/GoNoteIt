package eu.napcode.gonoteit.di.modules

import android.content.Context
import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import eu.napcode.gonoteit.auth.StoreAuth
import eu.napcode.gonoteit.utils.ApiClientProvider
import eu.napcode.gonoteit.utils.GlideBase64Loader
import javax.inject.Singleton

@Module
class UtilsModule {

    @Provides
    @Singleton
    internal fun provideApiProvider(storeAuth: StoreAuth): ApiClientProvider {
        return ApiClientProvider(storeAuth)
    }
}
