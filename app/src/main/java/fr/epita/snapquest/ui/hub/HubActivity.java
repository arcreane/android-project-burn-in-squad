package fr.epita.snapquest.ui.hub;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import fr.epita.snapquest.R;
import fr.epita.snapquest.ui.camera.CameraActivity;
import fr.epita.snapquest.ui.collection.CollectionActivity;
import fr.epita.snapquest.ui.setting.SettingsActivity;
public class HubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        Button startHuntingBtn = findViewById(R.id.btn_start_hunting);
        Button viewCollectionBtn = findViewById(R.id.btn_view_collection);
        Button settingsBtn = findViewById(R.id.btn_settings);
        startHuntingBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, CameraActivity.class));
        });
        viewCollectionBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, CollectionActivity.class));
        });
        settingsBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }
}