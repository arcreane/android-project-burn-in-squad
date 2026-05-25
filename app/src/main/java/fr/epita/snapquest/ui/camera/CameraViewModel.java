package fr.epita.snapquest.ui.camera;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CameraViewModel extends ViewModel {
    private final MutableLiveData<String> lastPhotoPath = new MutableLiveData<>();
    private int currentQuestId;

    public void setPhotoPath(String path) {
        lastPhotoPath.setValue(path);
    }

    public LiveData<String> getLastPhotoPath() {
        return lastPhotoPath;
    }

    public void setCurrentQuestId(int id) {
        this.currentQuestId = id;
    }

    public int getCurrentQuestId() {
        return currentQuestId;
    }
}
