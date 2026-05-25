package fr.epita.snapquest.validation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import fr.epita.snapquest.BuildConfig;

public class GeminiValidationService {

    private static final String ENDPOINT =
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=";

    public String validate(String photoPath, String questTitle, String questHint) throws Exception {
        // Decode and scale down bitmap to reduce payload size
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        if (bitmap == null) throw new Exception("Cannot decode image file");
        bitmap = scaleBitmap(bitmap, 512);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        String base64Image = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);

        String prompt = "Does this photo show: \"" + questTitle + "\"? " +
                "Hint: " + questHint + ". " +
                "Reply with YES or NO on the first line, then one sentence explaining why.";

        // Build Gemini request JSON
        JSONObject inlineData = new JSONObject()
                .put("mime_type", "image/jpeg")
                .put("data", base64Image);

        JSONObject requestBody = new JSONObject()
                .put("contents", new JSONArray().put(
                        new JSONObject().put("parts", new JSONArray()
                                .put(new JSONObject().put("inline_data", inlineData))
                                .put(new JSONObject().put("text", prompt))
                        )
                ));

        // HTTP POST
        URL url = new URL(ENDPOINT + BuildConfig.GEMINI_API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(15000);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        InputStream is = (code == 200) ? conn.getInputStream() : conn.getErrorStream();
        StringBuilder sb = new StringBuilder();
        byte[] buf = new byte[4096];
        int n;
        while ((n = is.read(buf)) != -1) {
            sb.append(new String(buf, 0, n, StandardCharsets.UTF_8));
        }
        is.close();

        if (code != 200) {
            throw new Exception("Gemini API error " + code + ": " + sb);
        }

        // Parse response text
        JSONObject response = new JSONObject(sb.toString());
        return response
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
                .trim();
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int maxSize) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale = Math.min((float) maxSize / w, (float) maxSize / h);
        if (scale >= 1f) return bitmap;
        return Bitmap.createScaledBitmap(bitmap, Math.round(w * scale), Math.round(h * scale), true);
    }
}
