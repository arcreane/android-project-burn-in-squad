package  fr.epita.snapquest.ui.collection;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import fr.epita.snapquest.R;

public class QuestViewHolder extends RecyclerView.ViewHolder {

    public TextView titleText;
    public TextView categoryText;
    public ImageView thumbnailImage;

    public TextView statusIcon;
    public TextView pointsText;

    public QuestViewHolder(View view) {
        super(view);
        titleText = view.findViewById(R.id.quest_title);
        categoryText = view.findViewById(R.id.quest_category);
        thumbnailImage = view.findViewById(R.id.quest_thumbnail);
        statusIcon = view.findViewById(R.id.quest_status);
        pointsText = view.findViewById(R.id.quest_points);
    }
}