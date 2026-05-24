package fr.epita.snapquest.validation;

import java.io.File;

public class ExifFreshnessRule implements ValidationRule {

    @Override
    public boolean validate(File file) {
        return file.exists();
    }
}