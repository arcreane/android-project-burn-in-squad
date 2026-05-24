package fr.epita.snapquest.data.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quests")
public class QuestEntity {
    @PrimaryKey
    public int id;
    public String title;
    public String category;
    public String hint;
    public int points;
    public boolean completed = false;

    public QuestEntity() {}

    public QuestEntity(int id, String title, String category, String hint, int points) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.hint = hint;
        this.points = points;
    }
}