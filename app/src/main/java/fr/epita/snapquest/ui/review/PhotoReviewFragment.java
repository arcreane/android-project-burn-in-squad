package fr.epita.snapquest.ui.review;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import fr.epita.snapquest.R;
import fr.epita.snapquest.data.db.AppDatabase;
import fr.epita.snapquest.data.db.PhotoEntity;
import fr.epita.snapquest.model.Photo;
import fr.epita.snapquest.validation.PhotoValidator;
import fr.epita.snapquest.validation.ValidationResult;

/**
 * Reusable fragment for photo review.
 * MODE_REVIEW: after camera capture — validates photo, shows Accept/Retake/Share.
 * MODE_VIEW: from collection — shows saved photo with Close/Share.
 * Communicates back to host via FragmentResult API.
 */
public class PhotoReviewFragment extends Fragment {

    public static final String ARG_PHOTO_PATH = "photoPath";
    public static final String ARG_QUEST_ID = "questId";
    public static final String ARG_MODE = "mode";
    public static final String MODE_REVIEW = "review";
    public static final String MODE_VIEW = "view";
    public static final String REQUEST_KEY = "photoReviewResult";
    public static final String RESULT_ACTION = "action";
    public static final String ACTION_ACCEPTED = "accepted";
    public static final String ACTION_RETAKE = "retake";
    public static final String ACTION_CLOSE = "close";

    private String photoPath;
    private int questId;
    private String mode;
    private boolean isValid;

    public static PhotoReviewFragment newInstance(String photoPath, int questId, String mode) {
        PhotoReviewFragment fragment = new PhotoReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_PATH, photoPath);
        args.putInt(ARG_QUEST_ID, questId);
        args.putString(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photoPath = getArguments().getString(ARG_PHOTO_PATH);
            questId = getArguments().getInt(ARG_QUEST_ID, 1);
            mode = getArguments().getString(ARG_MODE, MODE_REVIEW);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView photoImageView = view.findViewById(R.id.fragment_photo_image);
        TextView resultTextView = view.findViewById(R.id.fragment_validation_result);
        LinearLayout reviewButtons = view.findViewById(R.id.review_buttons);
        LinearLayout viewButtons = view.findViewById(R.id.view_buttons);

        if (photoPath != null) {
            Glide.with(this).load(photoPath).into(photoImageView);
        }

        if (MODE_REVIEW.equals(mode)) {
            reviewButtons.setVisibility(View.VISIBLE);
            viewButtons.setVisibility(View.GONE);

            Photo photo = new Photo(questId, photoPath, System.currentTimeMillis());
            ValidationResult result = new PhotoValidator().validate(photo);
            isValid = result.isValid();
            resultTextView.setText(result.getMessage());

            Button btnAccept = view.findViewById(R.id.fragment_btn_accept);
            Button btnRetake = view.findViewById(R.id.fragment_btn_retake);
            Button btnShare = view.findViewById(R.id.fragment_btn_share);

            btnAccept.setOnClickListener(v -> {
                if (isValid) {
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    db.photoDao().insertPhoto(new PhotoEntity(questId, photoPath, System.currentTimeMillis()));
                    db.questDao().markQuestCompleted(questId);
                    postResult(ACTION_ACCEPTED);
                } else {
                    Toast.makeText(requireContext(), "Photo validation failed", Toast.LENGTH_SHORT).show();
                }
            });

            btnRetake.setOnClickListener(v -> postResult(ACTION_RETAKE));
            btnShare.setOnClickListener(v -> sharePhoto());

        } else {
            reviewButtons.setVisibility(View.GONE);
            viewButtons.setVisibility(View.VISIBLE);
            resultTextView.setText(getString(R.string.quest_completed));

            Button btnClose = view.findViewById(R.id.fragment_btn_close);
            Button btnShareView = view.findViewById(R.id.fragment_btn_share_view);

            btnClose.setOnClickListener(v -> postResult(ACTION_CLOSE));
            btnShareView.setOnClickListener(v -> sharePhoto());
        }
    }

    private void postResult(String action) {
        Bundle result = new Bundle();
        result.putString(RESULT_ACTION, action);
        getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
    }

    private void sharePhoto() {
        if (photoPath == null) return;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + photoPath));
        startActivity(Intent.createChooser(shareIntent, "Share Photo"));
    }
}
