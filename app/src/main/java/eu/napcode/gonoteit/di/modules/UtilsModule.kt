package eu.napcode.gonoteit.di.modules

import dagger.Module
import dagger.Provides
import eu.napcode.gonoteit.auth.StoreAuth
import eu.napcode.gonoteit.utils.ApiClientProvider
import javax.inject.Singleton

@Module
class UtilsModule {

    @Provides
    @Singleton
    internal fun provideApiProvider(storeAuth: StoreAuth): ApiClientProvider {
        return ApiClientProvider(storeAuth)
    }
}
