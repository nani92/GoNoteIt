package eu.napcode.gonoteit.utils

import com.apollographql.apollo.ApolloClient
import eu.napcode.gonoteit.api.Note
import eu.napcode.gonoteit.api.NoteAdapter
import eu.napcode.gonoteit.auth.StoreAuth
import eu.napcode.gonoteit.type.CustomType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class ApiClientProvider(var storeAuth: StoreAuth) {

    private var apiClient: ApolloClient? = null

    fun invalidate() {
        apiClient = null
    }

    fun getApiClient() : ApolloClient {
        if (apiClient == null) {
            apiClient = createClient(storeAuth)
        }

        return apiClient!!
    }

    private fun createClient(storeAuth: StoreAuth): ApolloClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val builder = original.newBuilder()
                            .method(original.method(), original.body())
                    builder.header("Authorization", "JWT " + storeAuth.token)

                    chain.proceed(builder.build())
                }
                .build()

        var host = storeAuth.host
        if (host == "") {
            host = "http://gonote.it"
        }

        Timber.d("build api with host $host")

        return ApolloClient.builder()
                .serverUrl(host)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter<Note>(CustomType.GENERICSCALAR, NoteAdapter())
                .build()
    }
}