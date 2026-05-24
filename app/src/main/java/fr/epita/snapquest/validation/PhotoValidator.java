package fr.epita.snapquest.validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoValidator {

    private final List<ValidationRule> rules = new ArrayList<>();

    public void addRule(ValidationRule rule) {
        rules.add(rule);
    }

    public boolean validate(File file) {

        for (ValidationRule rule : rules) {
            if (!rule.validate(file)) {
                return false;
            }
        }

        return true;
    }
}