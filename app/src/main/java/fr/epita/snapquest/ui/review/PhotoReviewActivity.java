package fr.epita.snapquest.ui.review;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import fr.epita.snapquest.R;
public class PhotoReviewActivity extends AppCompatActivity {
    private PhotoReviewViewModel viewModel;
    private ImageView photoImageView;
    private TextView resultTextView;
    private Button acceptBtn, retakeBtn, shareBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_review);
        viewModel = new ViewModelProvider(this).get(PhotoReviewViewModel.class);
        photoImageView = findViewById(R.id.photo_image);
        resultTextView = findViewById(R.id.validation_result);
        acceptBtn = findViewById(R.id.btn_accept);
        retakeBtn = findViewById(R.id.btn_retake);
        shareBtn = findViewById(R.id.btn_share);
        Intent intent = getIntent();
        String photoPath = intent.getStringExtra("photoPath");
        int questId = intent.getIntExtra("questId", 1);
        viewModel.setPhotoPath(photoPath);
        viewModel.setQuestId(questId);
        if (photoPath != null) {
            Glide.with(this).load(photoPath).into(photoImageView);
            resultTextView.setText("Photo received!");
        }
        acceptBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Photo accepted!", Toast.LENGTH_SHORT).show();
            finish();
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