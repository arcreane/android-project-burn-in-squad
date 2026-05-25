package fr.epita.snapquest.ui.camera;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;

import fr.epita.snapquest.R;
import fr.epita.snapquest.data.photo.PhotoStorage;
import fr.epita.snapquest.ui.review.PhotoReviewFragment;
import fr.epita.snapquest.util.PermissionUtils;

public class CameraActivity extends AppCompatActivity {

    private PreviewView previewView;
    private Button btnCapture;
    private TextView questTitleView;
    private TextView questHintView;
    private ImageCapture imageCapture;
    private CameraViewModel viewModel;
    private String questTitle;
    private String questHint;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                } else {
                    Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        viewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        previewView = findViewById(R.id.previewView);
        btnCapture = findViewById(R.id.btnCapture);
        questTitleView = findViewById(R.id.quest_title_overlay);
        questHintView = findViewById(R.id.quest_hint_overlay);

        int questId = getIntent().getIntExtra("questId", 1);
        questTitle = getIntent().getStringExtra("questTitle");
        questHint = getIntent().getStringExtra("questHint");
        viewModel.setCurrentQuestId(questId);

        if (questTitle != null) questTitleView.setText(questTitle);
        if (questHint != null) questHintView.setText("Hint: " + questHint);

        btnCapture.setOnClickListener(v -> takePhoto());

        getSupportFragmentManager().setFragmentResultListener(
                PhotoReviewFragment.REQUEST_KEY, this, (requestKey, result) -> {
                    String action = result.getString(PhotoReviewFragment.RESULT_ACTION);
                    if (PhotoReviewFragment.ACTION_ACCEPTED.equals(action)) {
                        finish();
                    } else if (PhotoReviewFragment.ACTION_RETAKE.equals(action)) {
                        getSupportFragmentManager().popBackStack();
                        previewView.setVisibility(View.VISIBLE);
                        btnCapture.setVisibility(View.VISIBLE);
                        questTitleView.setVisibility(View.VISIBLE);
                        questHintView.setVisibility(View.VISIBLE);
                    }
                });

        if (PermissionUtils.hasCameraPermission(this)) {
            startCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                imageCapture = new ImageCapture.Builder().build();
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture);
            } catch (Exception e) {
                Toast.makeText(this, "Camera failed to start", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) {
            Toast.makeText(this, "Camera is not ready yet", Toast.LENGTH_SHORT).show();
            return;
        }

        File photoFile = new File(PhotoStorage.getPicturesDir(this), System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults results) {
                        String photoPath = photoFile.getAbsolutePath();
                        viewModel.setPhotoPath(photoPath);
                        previewView.setVisibility(View.GONE);
                        btnCapture.setVisibility(View.GONE);
                        questTitleView.setVisibility(View.GONE);
                        questHintView.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, PhotoReviewFragment.newInstance(
                                        photoPath, viewModel.getCurrentQuestId(),
                                        PhotoReviewFragment.MODE_REVIEW, questTitle, questHint))
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(CameraActivity.this, "Photo capture failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
