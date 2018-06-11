package eu.napcode.gonoteit.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

public class GlideBase64Loader {

    private Context context;
    private String base64ImageString = "";

    @Inject
    public GlideBase64Loader(Context context) {
        this.context = context;
    }

    public void loadBase64IntoView(String base64ImageString, ImageView view) {

        if (this.base64ImageString.equals(base64ImageString)) {
            return;
        }

        this.base64ImageString = base64ImageString;

        Glide.with(context)
                .load(ImageUtils.decodeBase64ToBitmap(base64ImageString))
                .into(view);
    }
}
