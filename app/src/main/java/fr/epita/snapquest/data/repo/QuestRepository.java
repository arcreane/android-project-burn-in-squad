package fr.epita.snapquest.data.repo;

import fr.epita.snapquest.data.db.AppDatabase;
import fr.epita.snapquest.data.db.PhotoEntity;
import fr.epita.snapquest.data.db.QuestEntity;
import java.util.List;

public class QuestRepository {
    private AppDatabase db;

    public QuestRepository(AppDatabase db) {
        this.db = db;
    }

    public List<QuestEntity> getAllQuests() {
        return db.questDao().getAllQuests();
    }

    public QuestEntity getQuestById(int id) {
        return db.questDao().getQuestById(id);
    }

    public void savePhoto(PhotoEntity photo) {
        db.photoDao().insertPhoto(photo);
    }

    public PhotoEntity getPhotoForQuest(int questId) {
        return db.photoDao().getPhotoForQuest(questId);
    }

    public List<PhotoEntity> getAllPhotos() {
        return db.photoDao().getAllPhotos();
    }

    public void deletePhoto(int questId) {
        db.photoDao().deletePhotoForQuest(questId);
    }

    public void markQuestCompleted(int questId) {
        db.questDao().markQuestCompleted(questId);
    }

    public int getQuestCount() {
        return db.questDao().getQuestCount();
    }

    public int getPhotoCount() {
        return db.photoDao().getPhotoCount();
    }
}