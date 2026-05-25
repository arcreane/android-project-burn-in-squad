package fr.epita.snapquest.ui.review;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import fr.epita.snapquest.R;
import fr.epita.snapquest.data.db.AppDatabase;
import fr.epita.snapquest.data.db.PhotoEntity;
import fr.epita.snapquest.model.Photo;
import fr.epita.snapquest.validation.PhotoValidator;
import fr.epita.snapquest.validation.ValidationResult;

public class PhotoReviewActivity extends AppCompatActivity {
    private PhotoReviewViewModel viewModel;
    private ImageView photoImageView;
    private TextView resultTextView;
    private Button acceptBtn, retakeBtn, shareBtn;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_review);

        viewModel = new ViewModelProvider(this).get(PhotoReviewViewModel.class);
        db = AppDatabase.getInstance(this);

        photoImageView = findViewById(R.id.photo_image);
        resultTextView = findViewById(R.id.validation_result);
        acceptBtn = findViewById(R.id.btn_accept);
        retakeBtn = findViewById(R.id.btn_retake);
        shareBtn = findViewById(R.id.btn_share);

        String photoPath = getIntent().getStringExtra("photoPath");
        int questId = getIntent().getIntExtra("questId", 1);

        viewModel.setPhotoPath(photoPath);
        viewModel.setQuestId(questId);

        if (photoPath != null) {
            Glide.with(this).load(photoPath).into(photoImageView);

            Photo photo = new Photo(questId, photoPath, System.currentTimeMillis());
            ValidationResult result = new PhotoValidator().validate(photo);

            viewModel.setIsValid(result.isValid());
            viewModel.setValidationMessage(result.getMessage());
            resultTextView.setText(result.getMessage());
        }

        acceptBtn.setOnClickListener(v -> {
            if (viewModel.isValid()) {
                PhotoEntity photoEntity = new PhotoEntity(
                        viewModel.getQuestId(),
                        viewModel.getPhotoPath(),
                        System.currentTimeMillis()
                );
                db.photoDao().insertPhoto(photoEntity);
                db.questDao().markQuestCompleted(viewModel.getQuestId());
                Toast.makeText(this, "Photo saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Photo validation failed", Toast.LENGTH_SHORT).show();
            }
        });

        retakeBtn.setOnClickListener(v -> finish());

        shareBtn.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse("file://" + viewModel.getPhotoPath()));
            startActivity(Intent.createChooser(shareIntent, "Share Photo"));
        });
    }
}
