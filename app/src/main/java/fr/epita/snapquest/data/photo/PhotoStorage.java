package fr.epita.snapquest.data.photo;

import android.content.Context;
import android.os.Environment;
import java.io.File;

public class PhotoStorage {
    public static File getPicturesDir(Context context) {
        File picturesDir = context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        if (picturesDir != null && !picturesDir.exists()) {
            picturesDir.mkdirs();
        }
        return picturesDir;
    }

    public static void deletePhoto(File photoFile) {
        if (photoFile != null && photoFile.exists()) {
            photoFile.delete();
        }
    }
}