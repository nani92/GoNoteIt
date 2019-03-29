package eu.napcode.gonoteit.repository.user

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.api.Response
import com.nhaarman.mockitokotlin2.mock

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

import eu.napcode.gonoteit.AuthenticateMutation
import eu.napcode.gonoteit.MockRxSchedulers
import eu.napcode.gonoteit.api.ApolloRxHelper
import eu.napcode.gonoteit.auth.StoreAuth
import eu.napcode.gonoteit.dao.user.UserDao
import eu.napcode.gonoteit.data.notes.NotesLocal
import eu.napcode.gonoteit.data.user.UserRemote
import eu.napcode.gonoteit.repository.Resource
import eu.napcode.gonoteit.rx.RxSchedulers
import eu.napcode.gonoteit.utils.ApiClientProvider
import eu.napcode.gonoteit.utils.TimestampStore
import io.reactivex.Observable
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner.Silent::class)
class UserRepositoryTest {

    @Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    internal var apolloClient: ApolloClient = mock()

    @Mock
    internal var storeAuth: StoreAuth = mock()

    @Mock
    internal var response: Response<AuthenticateMutation.Data> = mock()

    @Mock
    internal var apolloMutationCall: ApolloMutationCall<AuthenticateMutation.Data> = mock()

    @Mock
    internal var apolloRxHelper: ApolloRxHelper = mock()

    @Mock
    internal var timestampStore: TimestampStore = mock()

    @Mock
    internal var notesLocal: NotesLocal = mock()

    @Mock
    internal var userRemote: UserRemote = mock()

    @Mock
    internal var userDao: UserDao = mock()

    internal var rxSchedulers: RxSchedulers = MockRxSchedulers()

    private var userRepository: UserRepository = mock()

    @Mock
    internal var mutableLiveData: MutableLiveData<Resource<AuthenticateMutation.Data>> = mock()

    @Mock
    internal var apiProvider: ApiClientProvider = mock()

    @Before
    fun initial() {
        userRepository = UserRepositoryImpl(storeAuth, apolloRxHelper, timestampStore, notesLocal,
                userRemote, userDao, rxSchedulers, apiProvider)

        Mockito.`when`<ApolloMutationCall<AuthenticateMutation.Data>>(apolloClient!!.mutate(Mockito.any(AuthenticateMutation::class.java)))
                .thenReturn(apolloMutationCall)

        Mockito.`when`<Observable<Response<AuthenticateMutation.Data>>>(apolloRxHelper!!.from(Mockito.any<Any>() as ApolloMutationCall<AuthenticateMutation.Data>))
                .thenReturn(Observable.just<Response<AuthenticateMutation.Data>>(response!!))
    }

    @Test
    fun testAuthenticateIsCalled() {
        userRepository.authenticateUser("", "", "", mock())

        Mockito.verify<ApolloClient>(apolloClient).mutate(Mockito.any(AuthenticateMutation::class.java))
    }

//    @Test
//    fun testAuthenticationWithProperValues() {
//        val hostName = "host"
//        val userName = "login"
//        val password = "password"
//
//        userRepository.authenticateUser(hostName, userName, password, mock())
//
//        Mockito.verify<ApolloClient>(apolloClient)
//                .mutate(Mockito.argThat<Mutation<Operation.Data, Any, Operation.Variables>>(
//                        //verify(myClass).setItems(argThat { size == 2 } )
//                ))
//    }


//    private fun getAuthenticateMatcher(userName: String, password: String): ArgumentMatcher<Mutation<Operation.Data, Any, Operation.Variables>> {
//        return ArgumentMatcher { it.variables().valueMap() == userName && it.variables().password() == password }
//    }

    //    @Test
    //    public void testStoreUserName() {
    //        String userName = "login";
    //        TestObserver<Response<AuthenticateMutation.Data>> subscriber = new TestObserver<>();
    //
    //        userRepository
    //                .authenticateUser(userName, "", mutableLiveData)
    //                .subscribe(subscriber);
    //
    //        subscriber.assertSubscribed();
    //
    //        Mockito.verify(storeAuth).saveName(userName);
    //    }

    //    @Test
    //    public void testStoreToken() {
    //        userRepository
    //                .authenticateUser("", "", mutableLiveData)
    //                .subscribe();
    //
    //        Mockito.verify(storeAuth).saveToken(Mockito.any());
    //    }

    //    @Test
    //    public void shouldReturnNotLoggedInUser() {
    //        Mockito.when(storeAuth.getToken()).thenReturn("");
    //
    //        Assert.assertEquals(null, userRepository.getLoggedInUser());
    //    }

    @Test
    fun shouldReturnLoggedInUser() {
        val username = "name"
        Mockito.`when`(storeAuth!!.token).thenReturn("token")
        Mockito.`when`(storeAuth!!.userName).thenReturn(username)

        //TODO
        //Assert.assertEquals(username, userRepository.getLoggedInUser());
    }

    @Test
    fun testLogoutUser() {
        userRepository!!.logoutUser()
                .subscribe()

        Mockito.verify<StoreAuth>(storeAuth).saveToken(null)
    }
}