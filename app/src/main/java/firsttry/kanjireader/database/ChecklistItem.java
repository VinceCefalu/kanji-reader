package firsttry.kanjireader.database;

public class ChecklistItem {

    private String kanji;
    private int learned;

    public ChecklistItem(String kanji, int learned) {
        this.kanji = kanji;
        this.learned = learned;
    }

    public String getKanji() {
        return kanji;
    }

    public int isLearned() {
        return learned;
    }

    // For use in checklist gridview
    public void setLearned(int learned) {
        if (learned == 0 || learned == 1)
            this.learned = learned;
    }

    @Override
    public String toString() {
        return kanji;
    }
}
