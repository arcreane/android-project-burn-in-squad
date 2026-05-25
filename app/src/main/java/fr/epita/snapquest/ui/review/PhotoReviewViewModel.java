package fr.epita.snapquest.ui.review;

import androidx.lifecycle.ViewModel;

public class PhotoReviewViewModel extends ViewModel {
    private String photoPath;
    private int questId;
    private boolean valid;
    private String validationMessage;

    public void setPhotoPath(String path) {
        this.photoPath = path;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }

    public int getQuestId() {
        return questId;
    }

    public void setIsValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValidationMessage(String message) {
        this.validationMessage = message;
    }

    public String getValidationMessage() {
        return validationMessage;
    }
}
