package fr.epita.snapquest.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import fr.epita.snapquest.data.repo.NetworkStateRepository;
public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String TAG = "ConnectivityReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean online = isOnline(context);
        Log.d(TAG, online ? "Connected" : "Disconnected");
        NetworkStateRepository.get().setOnline(online);
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo active = cm.getActiveNetworkInfo();
        return active != null && active.isConnected();
    }

}
