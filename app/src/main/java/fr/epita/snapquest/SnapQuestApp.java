package fr.epita.snapquest;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import fr.epita.snapquest.data.asset.QuestLoader;
import fr.epita.snapquest.data.db.AppDatabase;
import fr.epita.snapquest.network.ConnectivityReceiver;

public class SnapQuestApp extends Application {

    private ConnectivityReceiver connectivityReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        // Register network receiver for entire app lifetime
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);

        // Seed 20 quests from assets/quests.json on first launch
        AppDatabase db = AppDatabase.getInstance(this);
        QuestLoader.loadQuestsIfNeeded(this, db);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (connectivityReceiver != null) {
            unregisterReceiver(connectivityReceiver);
        }
    }
}
