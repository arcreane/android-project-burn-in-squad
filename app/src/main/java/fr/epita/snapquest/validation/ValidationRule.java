package fr.epita.snapquest.validation;

import java.io.File;

public interface ValidationRule {
    boolean validate(File file);
}
