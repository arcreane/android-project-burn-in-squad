package fr.epita.snapquest.validation;

import androidx.exifinterface.media.ExifInterface;
import fr.epita.snapquest.model.Photo;

public class ExifFreshnessRule implements ValidationRule {
    private static final long MAX_AGE_SECONDS = 30;

    @Override
    public ValidationResult validate(Photo photo) {
        try {
            if (photo == null || photo.filePath == null) {
                return new ValidationResult(false, "Photo is null");
            }

            ExifInterface exif = new ExifInterface(photo.filePath);
            long photoTimeMs = exif.getDateTime();

            if (photoTimeMs == -1) {
                return new ValidationResult(false, "✗ Could not read EXIF timestamp");
            }

            long ageSeconds = (System.currentTimeMillis() - photoTimeMs) / 1000;

            if (ageSeconds <= MAX_AGE_SECONDS) {
                return new ValidationResult(true, "✓ Photo is fresh (" + ageSeconds + "s old)");
            }
            return new ValidationResult(false,
                "✗ Photo too old: " + ageSeconds + "s (max " + MAX_AGE_SECONDS + "s)");
        } catch (Exception e) {
            return new ValidationResult(false, "✗ Cannot read EXIF: " + e.getMessage());
        }
    }
}
