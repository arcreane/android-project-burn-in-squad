package fr.epita.snapquest;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import fr.epita.snapquest.network.ConnectivityReceiver;
public class SnapQuestApp extends Application {

    private ConnectivityReceiver connectivityReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // onTerminate is only called in emulators, but unregister anyway for hygiene.
        if (connectivityReceiver != null) {
            unregisterReceiver(connectivityReceiver);
        }
    }

}
