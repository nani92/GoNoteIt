package eu.napcode.gonoteit.api;

import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;

import javax.annotation.Nonnull;

public class UUIDAdapter implements CustomTypeAdapter<String> {

    @Override
    public String decode(@Nonnull CustomTypeValue value) {
        return value.value.toString();
    }

    @Nonnull
    @Override
    public CustomTypeValue encode(@Nonnull String value) {
        return CustomTypeValue.fromRawValue(value);
    }
}
