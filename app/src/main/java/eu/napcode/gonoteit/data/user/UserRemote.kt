package eu.napcode.gonoteit.data.user

import com.apollographql.apollo.ApolloClient
import eu.napcode.gonoteit.GetUserQuery
import eu.napcode.gonoteit.api.ApolloRxHelper
import eu.napcode.gonoteit.auth.StoreAuth
import eu.napcode.gonoteit.rx.RxSchedulers
import eu.napcode.gonoteit.utils.ApiClientProvider
import eu.napcode.gonoteit.utils.TimestampStore
import io.reactivex.Single
import javax.inject.Inject

class UserRemote() {

    private lateinit var apolloClient: ApolloClient
    private lateinit var storeAuth: StoreAuth
    private lateinit var apolloRxHelper: ApolloRxHelper
    private lateinit var rxSchedulers: RxSchedulers
    private lateinit var timestampStore: TimestampStore
    private lateinit var apiClientProvider: ApiClientProvider

    @Inject
    constructor (apolloClient: ApolloClient, storeAuth: StoreAuth, apolloRxHelper: ApolloRxHelper,
                 rxSchedulers: RxSchedulers, timestampStore: TimestampStore, apiClientProvider: ApiClientProvider) : this() {
        this.apolloClient = apolloClient
        this.storeAuth = storeAuth
        this.apolloRxHelper = apolloRxHelper
        this.rxSchedulers = rxSchedulers
        this.timestampStore = timestampStore
        this.apiClientProvider = apiClientProvider
    }

    fun getUser(): Single<GetUserQuery.User?> {
        val userQuery = apiClientProvider.getApiClient().query(GetUserQuery())

        return apolloRxHelper.from(userQuery)
                .map { dataResponse -> dataResponse.data()!!.user() }
                .singleOrError()
    }
}