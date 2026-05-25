package fr.epita.snapquest.ui.collection;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import fr.epita.snapquest.R;
import fr.epita.snapquest.data.db.QuestEntity;
import java.util.List;

public class QuestListAdapter extends RecyclerView.Adapter<QuestViewHolder>{
    private List<QuestEntity> quests;
    private OnQuestClickListener listener;

    public interface OnQuestClickListener {
        void onQuestClick(QuestEntity quest);
    }

    public QuestListAdapter(List<QuestEntity> quests, OnQuestClickListener listener) {
        this.quests = quests;
        this.listener = listener;
    }

    @Override
    public QuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quest, parent, false);
        return new QuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestViewHolder holder, int position) {
        QuestEntity quest = quests.get(position);

        holder.titleText.setText(quest.title);
        holder.categoryText.setText(quest.category);
        holder.pointsText.setText("+" + quest.points);
        holder.statusIcon.setText(quest.completed ? "✓" : "○");

        // Load placeholder thumbnail with Glide
        Glide.with(holder.itemView.getContext())
                .load(R.drawable.ic_launcher_foreground)
                .override(80, 80)  // Fixed size to prevent OOM
                .centerCrop()
                .into(holder.thumbnailImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onQuestClick(quest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    public void updateQuests(List<QuestEntity> newQuests) {
        this.quests = newQuests;
        notifyDataSetChanged();
    }

}
