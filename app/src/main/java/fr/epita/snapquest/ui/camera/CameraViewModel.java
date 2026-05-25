package fr.epita.snapquest.ui.camera;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CameraViewModel extends ViewModel {
    private final MutableLiveData<String> lastPhotoPath = new MutableLiveData<>();
    public void setLastPhotoPath(String path) {
        lastPhotoPath.setValue(path);
    }
    public LiveData<String> getLastPhotoPath() {
        return lastPhotoPath;
    }
}