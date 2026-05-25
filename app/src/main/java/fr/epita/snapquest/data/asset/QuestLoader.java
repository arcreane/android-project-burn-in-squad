package fr.epita.snapquest.data.asset;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.epita.snapquest.data.db.AppDatabase;
import fr.epita.snapquest.data.db.QuestEntity;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class QuestLoader {
    public static void loadQuestsIfNeeded(Context context, AppDatabase db) {
        if (db.questDao().getQuestCount() > 0) {
            return;
        }
        try {
            String json = readAssetFile(context, "quests.json");
            JsonObject obj = new Gson().fromJson(json, JsonObject.class);
            JsonArray questsArray = obj.getAsJsonArray("quests");
            for (int i = 0; i < questsArray.size(); i++) {
                JsonObject questObj = questsArray.get(i).getAsJsonObject();
                QuestEntity quest = new QuestEntity(
                        questObj.get("id").getAsInt(),
                        questObj.get("title").getAsString(),
                        questObj.get("category").getAsString(),
                        questObj.get("hint").getAsString(),
                        questObj.get("points").getAsInt()
                );
                db.questDao().insertQuest(quest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readAssetFile(Context context, String filename)
            throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(filename))
        );
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}