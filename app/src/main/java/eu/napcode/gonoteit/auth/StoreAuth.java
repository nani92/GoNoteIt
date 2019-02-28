package eu.napcode.gonoteit.auth;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import eu.napcode.gonoteit.app.GoNoteItApp;
import timber.log.Timber;

public class StoreAuth {

    private static final String HOST_NAME_KEY = "host";
    private static final String USER_NAME_KEY = "user";
    private static final String TOKEN_KEY = "token";

    private SharedPreferences sharedPreferences;

    @Inject
    public StoreAuth(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveHost(String host) {
        Timber.d("Storing host");

        sharedPreferences.edit()
                .putString(HOST_NAME_KEY, host)
                .apply();
    }

    public void saveToken(String token) {
        Timber.d("Storing token %s", token);
        sharedPreferences.edit()
                .putString(TOKEN_KEY, token)
                .apply();
    }

    public String getHost() {
        Timber.d("Retrieving host");

        return sharedPreferences.getString(HOST_NAME_KEY, "");
    }

    public String getUserName() {
        Timber.d("Retrieving user name");

        return sharedPreferences.getString(USER_NAME_KEY, "");
    }

    public String getToken() {
        Timber.d("Retrieving token");

        return sharedPreferences.getString(TOKEN_KEY, "");
    }
}
