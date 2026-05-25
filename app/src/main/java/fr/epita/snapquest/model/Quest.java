package fr.epita.snapquest.model;

public class Quest {
    public int id;
    public String title;
    public String category;
    public String hint;
    public int points;
    public boolean completed;

    public Quest() {}

    public Quest(int id, String title, String category, String hint, int points) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.hint = hint;
        this.points = points;
        this.completed = false;
    }
}