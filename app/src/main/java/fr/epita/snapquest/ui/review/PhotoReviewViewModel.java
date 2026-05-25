package fr.epita.snapquest.ui.review;

import androidx.lifecycle.ViewModel;

public class PhotoReviewViewModel extends ViewModel {
    private String photoPath;
    private int questId;

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
}
