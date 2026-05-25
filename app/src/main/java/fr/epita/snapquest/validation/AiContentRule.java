package fr.epita.snapquest.validation;

import fr.epita.snapquest.model.Photo;

public class AiContentRule implements ValidationRule {

    private final String questTitle;
    private final String questHint;

    public AiContentRule(String questTitle, String questHint) {
        this.questTitle = questTitle;
        this.questHint = questHint != null ? questHint : "";
    }

    @Override
    public ValidationResult validate(Photo photo) {
        try {
            GeminiValidationService service = new GeminiValidationService();
            String response = service.validate(photo.filePath, questTitle, questHint);
            boolean isYes = response.toUpperCase().startsWith("YES");
            return new ValidationResult(isYes, (isYes ? "✓ " : "✗ ") + response);
        } catch (Exception e) {
            return new ValidationResult(false, "✗ AI validation error: " + e.getMessage());
        }
    }
}
