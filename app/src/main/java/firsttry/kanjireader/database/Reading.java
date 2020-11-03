package firsttry.kanjireader.database;

public class Reading {

    private int rid;
    private String title;
    private String author;
    private String part;

    public Reading(int rid, String title, String author, String part) {
        this.rid = rid;
        this.title = title;
        this.author = author;
        this.part = part;
    }

    public int getRid() {
        return rid;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPart() {
        return part;
    }
}
