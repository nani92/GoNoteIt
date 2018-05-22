package eu.napcode.gonoteit.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static eu.napcode.gonoteit.dao.NoteEntity.TABLE_NAME;

public class NotesContentProvider extends ContentProvider {

    public static final String AUTHORITY = "eu.napcode.gonoteit.provider";

    public static final Uri URI_NOTES= Uri.parse(
            "content://" + AUTHORITY + "/" + TABLE_NAME);

    private static final int CODE_NOTES = 100;
    private static final int CODE_NOTE = 101;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_NAME, CODE_NOTES);
        MATCHER.addURI(AUTHORITY, TABLE_NAME + "/*", CODE_NOTE);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
