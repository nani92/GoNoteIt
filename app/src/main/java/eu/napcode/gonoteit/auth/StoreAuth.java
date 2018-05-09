package eu.napcode.gonoteit.auth;

import android.content.SharedPreferences;

import javax.inject.Inject;

import timber.log.Timber;

public class StoreAuth {

    private static final String USER_NAME_KEY = "user";
    private static final String TOKEN_KEY = "token";

    private SharedPreferences sharedPreferences;

    @Inject
    public StoreAuth(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveName(String name) {
        Timber.d("Storing name %s", name);
        sharedPreferences.edit()
                .putString(USER_NAME_KEY, name)
                .apply();
    }

    public void saveToken(String token) {
        Timber.d("Storing token %s", token);
        sharedPreferences.edit()
                .putString(TOKEN_KEY, token)
                .apply();
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
