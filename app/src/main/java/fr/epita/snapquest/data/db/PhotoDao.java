package fr.epita.snapquest.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface PhotoDao {
    @Insert
    void insertPhoto(PhotoEntity photo);

    @Query("SELECT * FROM photos WHERE questId = :questId")
    PhotoEntity getPhotoForQuest(int questId);

    @Query("SELECT * FROM photos ORDER BY timestamp DESC")
    List<PhotoEntity> getAllPhotos();

    @Query("DELETE FROM photos WHERE questId = :questId")
    void deletePhotoForQuest(int questId);

    @Delete
    void deletePhoto(PhotoEntity photo);

    @Query("SELECT COUNT(*) FROM photos")
    int getPhotoCount();
}