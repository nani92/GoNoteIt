package eu.napcode.gonoteit.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.apollographql.apollo.api.Error;

import static eu.napcode.gonoteit.repository.Resource.Status.ERROR;
import static eu.napcode.gonoteit.repository.Resource.Status.LOADING;
import static eu.napcode.gonoteit.repository.Resource.Status.SUCCESS;

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final String message;

    @Nullable
    public final T data;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(Throwable throwable) {
        return new Resource<>(ERROR, null, throwable.getLocalizedMessage());
    }

    public static <T> Resource<T> error(Error error) {
        return new Resource<>(ERROR, null, error.message());
    }

    public static <T> Resource<T> error(String message) {
        return new Resource<>(ERROR, null, message);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}
