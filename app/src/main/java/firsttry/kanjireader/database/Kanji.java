package firsttry.kanjireader.database;

public class Kanji {

    private String kanji;
    private String radical;
    private int strokes;
    private int grade;
    private String parts;
    private String onyomi;
    private String kunyomi;
    private String officialList;
    private String oldForm;

    public Kanji(String kanji, String radical, int strokes, int grade, String parts, String onyomi,
                 String kunyomi, String officialList, String oldForm)
    {
        this.kanji = kanji;
        this.radical = radical;
        this.strokes = strokes;
        this.grade = grade;
        this.parts = parts;

        if (onyomi == null)
            this.onyomi = "";
        else
            this.onyomi = onyomi;

        if (kunyomi == null)
            this.kunyomi = "";
        else
            this.kunyomi = kunyomi;

        this.officialList = officialList;
        this.oldForm = oldForm;
    }

    public String getKanji() {
        return kanji;
    }

    public String getRadical() {
        return radical;
    }

    public int getStrokes() {
        return strokes;
    }

    public int getGrade() {
        return grade;
    }

    public String getParts() {
        return parts;
    }

    public String getOnyomi() {
        return onyomi;
    }

    public String getKunyomi() {
        return kunyomi;
    }

    public String getOfficialList() {
        return officialList;
    }

    public String getOldForm() {
        return oldForm;
    }

    public String toString(){
        String out = "Kanji: " + kanji;
        return out;
    }
}
