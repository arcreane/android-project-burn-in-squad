package fr.epita.snapquest.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface QuestDao {
    @Insert
    void insertQuest(QuestEntity quest);

    @Query("SELECT * FROM quests ORDER BY id ASC")
    List<QuestEntity> getAllQuests();

    @Query("SELECT * FROM quests WHERE id = :id")
    QuestEntity getQuestById(int id);

    @Query("UPDATE quests SET completed = 1 WHERE id = :id")
    void markQuestCompleted(int id);

    @Query("SELECT COUNT(*) FROM quests")
    int getQuestCount();
}