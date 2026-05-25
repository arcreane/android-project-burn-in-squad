package fr.epita.snapquest.validation;

import android.graphics.BitmapFactory;
import fr.epita.snapquest.model.Photo;

public class SizeRule implements ValidationRule {
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;

    @Override
    public ValidationResult validate(Photo photo) {
        if (photo == null || photo.filePath == null) {
            return new ValidationResult(false, "Photo is null");
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photo.filePath, options);

        int width = options.outWidth;
        int height = options.outHeight;

        if (width >= MIN_WIDTH && height >= MIN_HEIGHT) {
            return new ValidationResult(true, "✓ Size valid (" + width + "x" + height + ")");
        }
        return new ValidationResult(false,
            "✗ Photo too small: " + width + "x" + height + " (need " + MIN_WIDTH + "x" + MIN_HEIGHT + ")");
    }
}
