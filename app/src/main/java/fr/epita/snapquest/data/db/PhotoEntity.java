package fr.epita.snapquest.data.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos",
        foreignKeys = @ForeignKey(entity = QuestEntity.class,
                parentColumns = "id",
                childColumns = "questId",
                onDelete = ForeignKey.CASCADE))
public class PhotoEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int questId;
    public String filePath;
    public long timestamp;
    public double latitude;
    public double longitude;

    public PhotoEntity() {}

    public PhotoEntity(int questId, String filePath, long timestamp) {
        this.questId = questId;
        this.filePath = filePath;
        this.timestamp = timestamp;
    }
}