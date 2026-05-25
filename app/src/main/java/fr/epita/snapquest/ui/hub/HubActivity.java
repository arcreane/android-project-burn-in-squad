package fr.epita.snapquest.ui.hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fr.epita.snapquest.R;
import fr.epita.snapquest.data.db.AppDatabase;
import fr.epita.snapquest.data.db.QuestEntity;
import fr.epita.snapquest.data.repo.NetworkStateRepository;
import fr.epita.snapquest.data.repo.QuestRepository;
import fr.epita.snapquest.ui.camera.CameraActivity;
import fr.epita.snapquest.ui.collection.CollectionActivity;
import fr.epita.snapquest.ui.setting.SettingsActivity;

public class HubActivity extends AppCompatActivity {

    private LinearLayout networkBadge;
    private TextView scoreTextView;
    private QuestRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        repository = new QuestRepository(AppDatabase.getInstance(this));
        networkBadge = findViewById(R.id.network_badge);
        scoreTextView = findViewById(R.id.score_text);

        Button startHuntingBtn = findViewById(R.id.btn_start_hunting);
        Button viewCollectionBtn = findViewById(R.id.btn_view_collection);
        Button settingsBtn = findViewById(R.id.btn_settings);

        startHuntingBtn.setOnClickListener(v -> startNextPendingQuest());
        viewCollectionBtn.setOnClickListener(v -> startActivity(new Intent(this, CollectionActivity.class)));
        settingsBtn.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        NetworkStateRepository.get().networkAvailable().observe(this, isConnected ->
                networkBadge.setVisibility(isConnected ? View.GONE : View.VISIBLE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateScore();
    }

    private void startNextPendingQuest() {
        List<QuestEntity> quests = repository.getAllQuests();
        QuestEntity next = null;
        for (QuestEntity q : quests) {
            if (!q.completed) {
                next = q;
                break;
            }
        }
        if (next == null) {
            Toast.makeText(this, "All quests completed!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("questId", next.id);
        intent.putExtra("questTitle", next.title);
        intent.putExtra("questHint", next.hint);
        startActivity(intent);
    }

    private void updateScore() {
        List<QuestEntity> quests = repository.getAllQuests();
        int completed = 0;
        int totalPoints = 0;
        for (QuestEntity q : quests) {
            if (q.completed) {
                completed++;
                totalPoints += q.points;
            }
        }
        scoreTextView.setText(completed + "/" + quests.size() + " quests · " + totalPoints + " pts");
    }
}
