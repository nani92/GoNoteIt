package eu.napcode.gonoteit.auth;

import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;

public class StoreAuth {

    private static final String USER_NAME_KEY = "user";
    private static final String TOKEN_KEY = "token";

    private SharedPreferences sharedPreferences;

    @Inject
    public StoreAuth(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveName(String name) {
        sharedPreferences.edit()
                .putString(USER_NAME_KEY, name)
                .apply();
    }

    public void saveToken(String token) {
        sharedPreferences.edit()
                .putString(TOKEN_KEY, token)
                .apply();
    }
}
