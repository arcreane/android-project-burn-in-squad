package fr.epita.snapquest.ui.collection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import fr.epita.snapquest.R;
import fr.epita.snapquest.data.db.AppDatabase;
import fr.epita.snapquest.data.db.QuestEntity;
import fr.epita.snapquest.data.repo.QuestRepository;
import fr.epita.snapquest.model.QuestStatus;

import java.util.List;
public class CollectionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestListAdapter adapter;
    private QuestRepository repository;
    private QuestStatus currentStatus = QuestStatus.ALL;
    private List<QuestEntity> allQuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        recyclerView = findViewById(R.id.quest_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize repository and load quests
        AppDatabase db = AppDatabase.getInstance(this);
        repository = new QuestRepository(db);
        allQuests = repository.getAllQuests();

        // Create adapter
        adapter = new QuestListAdapter(allQuests, quest -> {
            // Handle quest click
        });
        recyclerView.setAdapter(adapter);

        // Set action bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Collection");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collection_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter_all) {
            currentStatus = QuestStatus.ALL;
            updateList();
            return true;
        } else if (item.getItemId() == R.id.filter_completed) {
            currentStatus = QuestStatus.COMPLETED;
            updateList();
            return true;
        } else if (item.getItemId() == R.id.filter_pending) {
            currentStatus = QuestStatus.PENDING;
            updateList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateList() {
        List<QuestEntity> filtered = allQuests;

        if (currentStatus == QuestStatus.COMPLETED) {
            filtered = allQuests.stream()
                    .filter(q -> q.completed)
                    .toList();
        } else if (currentStatus == QuestStatus.PENDING) {
            filtered = allQuests.stream()
                    .filter(q -> !q.completed)
                    .toList();
        }

        adapter.updateQuests(filtered);
    }
}
