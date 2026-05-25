package fr.epita.snapquest.validation;

import java.io.File;

public class SizeRule implements ValidationRule {

    @Override
    public boolean validate(File file) {
        return file.exists() && file.length() > 0;
    }
}