package fr.epita.snapquest.data.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
public class NetworkStateRepository {

    private static final NetworkStateRepository INSTANCE = new NetworkStateRepository();

    private final MutableLiveData<Boolean> online = new MutableLiveData<>(true);

    private NetworkStateRepository() { }

    public static NetworkStateRepository get() {
        return INSTANCE;
    }

    public LiveData<Boolean> networkAvailable() {
        return online;
    }

    /** Called by ConnectivityReceiver on the main thread. */
    public void setOnline(boolean isOnline) {
        online.postValue(isOnline);
    }

}
