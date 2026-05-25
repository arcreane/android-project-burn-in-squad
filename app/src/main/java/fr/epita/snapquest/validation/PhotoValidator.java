package fr.epita.snapquest.validation;

import fr.epita.snapquest.model.Photo;
import java.util.ArrayList;
import java.util.List;

public class PhotoValidator {
    private final List<ValidationRule> rules = new ArrayList<>();

    public PhotoValidator() {
        rules.add(new SizeRule());
        rules.add(new ExifFreshnessRule());
    }

    public ValidationResult validate(Photo photo) {
        for (ValidationRule rule : rules) {
            ValidationResult result = rule.validate(photo);
            if (!result.isValid()) {
                return result;
            }
        }
        return new ValidationResult(true, "✓ Photo is valid!");
    }
}
