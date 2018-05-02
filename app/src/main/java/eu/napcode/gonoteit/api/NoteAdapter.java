package eu.napcode.gonoteit.api;

import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;
import com.google.gson.Gson;
import javax.annotation.Nonnull;

import eu.napcode.gonoteit.model.NoteModel;

public class NoteAdapter implements CustomTypeAdapter<NoteModel> {

    @Override
    public NoteModel decode(@Nonnull CustomTypeValue value) {
        Gson gson = new Gson();
        String stringValue = value.value.toString();

        return gson.fromJson(String.valueOf(stringValue), NoteModel.class);
    }

    @Nonnull
    @Override
    public CustomTypeValue encode(@Nonnull NoteModel value) {
        Gson gson = new Gson();

        return CustomTypeValue.fromRawValue(gson.toJson(value));
    }
}
