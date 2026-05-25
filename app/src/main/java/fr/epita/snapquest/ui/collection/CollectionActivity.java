package fr.epita.snapquest.ui.collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fr.epita.snapquest.R;
import fr.epita.snapquest.data.db.AppDatabase;
import fr.epita.snapquest.data.db.PhotoEntity;
import fr.epita.snapquest.data.db.QuestEntity;
import fr.epita.snapquest.data.repo.NetworkStateRepository;
import fr.epita.snapquest.data.repo.QuestRepository;
import fr.epita.snapquest.model.QuestStatus;
import fr.epita.snapquest.ui.camera.CameraActivity;
import fr.epita.snapquest.ui.review.PhotoReviewFragment;

public class CollectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuestListAdapter adapter;
    private QuestRepository repository;
    private LinearLayout networkBadge;
    private QuestStatus currentFilter = QuestStatus.ALL;
    private String currentSort = "default";
    private List<QuestEntity> allQuests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        networkBadge = findViewById(R.id.network_badge);
        recyclerView = findViewById(R.id.quest_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getInstance(this);
        repository = new QuestRepository(db);
        allQuests = new ArrayList<>(repository.getAllQuests());

        adapter = new QuestListAdapter(allQuests, quest -> {
            if (quest.completed) {
                // View saved photo
                PhotoEntity photo = repository.getPhotoForQuest(quest.id);
                if (photo != null) {
                    showPhotoFragment(photo.filePath, quest.id);
                }
            } else {
                // Open camera to complete this quest
                Intent intent = new Intent(this, CameraActivity.class);
                intent.putExtra("questId", quest.id);
                intent.putExtra("questTitle", quest.title);
                intent.putExtra("questHint", quest.hint);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.collection_title));
        }

        NetworkStateRepository.get().networkAvailable().observe(this, isConnected ->
                networkBadge.setVisibility(isConnected ? View.GONE : View.VISIBLE));

        getSupportFragmentManager().setFragmentResultListener(
                PhotoReviewFragment.REQUEST_KEY, this, (requestKey, result) -> {
                    String action = result.getString(PhotoReviewFragment.RESULT_ACTION);
                    if (PhotoReviewFragment.ACTION_CLOSE.equals(action)) {
                        getSupportFragmentManager().popBackStack();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        allQuests = new ArrayList<>(repository.getAllQuests());
        applyFilterAndSort();
    }

    private void showPhotoFragment(String photoPath, int questId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, PhotoReviewFragment.newInstance(
                        photoPath, questId, PhotoReviewFragment.MODE_VIEW))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collection_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter_all) {
            currentFilter = QuestStatus.ALL;
        } else if (id == R.id.filter_completed) {
            currentFilter = QuestStatus.COMPLETED;
        } else if (id == R.id.filter_pending) {
            currentFilter = QuestStatus.PENDING;
        } else if (id == R.id.sort_by_points) {
            currentSort = "points";
        } else if (id == R.id.sort_by_category) {
            currentSort = "category";
        } else if (id == R.id.sort_by_status) {
            currentSort = "status";
        } else {
            return super.onOptionsItemSelected(item);
        }
        applyFilterAndSort();
        return true;
    }

    private void applyFilterAndSort() {
        List<QuestEntity> filtered;
        if (currentFilter == QuestStatus.COMPLETED) {
            filtered = allQuests.stream().filter(q -> q.completed).collect(Collectors.toList());
        } else if (currentFilter == QuestStatus.PENDING) {
            filtered = allQuests.stream().filter(q -> !q.completed).collect(Collectors.toList());
        } else {
            filtered = new ArrayList<>(allQuests);
        }

        switch (currentSort) {
            case "points":
                Collections.sort(filtered, (a, b) -> b.points - a.points);
                break;
            case "category":
                Collections.sort(filtered, (a, b) -> a.category.compareTo(b.category));
                break;
            case "status":
                Collections.sort(filtered, (a, b) -> Boolean.compare(a.completed, b.completed));
                break;
        }

        adapter.updateQuests(filtered);
    }
}
