package fr.epita.snapquest.validation;

import fr.epita.snapquest.model.Photo;

public interface ValidationRule {
    ValidationResult validate(Photo photo);
}
