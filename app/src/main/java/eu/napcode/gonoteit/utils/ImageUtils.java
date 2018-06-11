package eu.napcode.gonoteit.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    public static String encodeBitmapToBase64(Bitmap imageBitmap, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        imageBitmap.compress(compressFormat, quality, byteArrayOS);

        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64ToBitmap(String input) {
        byte [] bytes = decodeBase64ToBytes(input);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static byte[] decodeBase64ToBytes(String input) {
        return Base64.decode(input, 0);
    }
}
