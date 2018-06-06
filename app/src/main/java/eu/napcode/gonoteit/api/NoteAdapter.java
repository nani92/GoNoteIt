package eu.napcode.gonoteit.api;

import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;
import javax.annotation.Nonnull;

public class NoteAdapter implements CustomTypeAdapter<Note> {

    @Override
    public Note decode(@Nonnull CustomTypeValue value) {
        return new Note(value.value.toString());
    }

    @Nonnull
    @Override
    public CustomTypeValue encode(@Nonnull Note note) {
        return CustomTypeValue.fromRawValue(note.getNoteDataString());
    }
}
