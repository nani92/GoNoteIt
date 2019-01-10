package eu.napcode.gonoteit.utils;

import android.content.SharedPreferences;

import javax.inject.Inject;

import timber.log.Timber;

public class TimestampStore {

    private static final String TIMESTAMP_KEY = "timestamp";

    private SharedPreferences sharedPreferences;

    @Inject
    public TimestampStore(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveTimestamp(Long timestamp) {
        Timber.d("Storing timestamp %d", timestamp);
        sharedPreferences.edit()
                .putLong(TIMESTAMP_KEY, timestamp)
                .apply();
    }

    public Long getTimestamp() {
        Timber.d("Retrieving timestamp");

        return sharedPreferences.getLong(TIMESTAMP_KEY, 0);
    }

    public boolean hasTimestamp() {
        Timber.d("Has timestamp");

        return sharedPreferences.contains(TIMESTAMP_KEY);
    }

    public void removeTimestamp() {
        Timber.d("Delete timestamp");
        sharedPreferences.edit()
                .remove(TIMESTAMP_KEY)
                .apply();
    }
}
