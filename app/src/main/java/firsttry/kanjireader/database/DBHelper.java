package firsttry.kanjireader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import firsttry.kanjireader.R;

public class DBHelper extends SQLiteOpenHelper {

    private Context context; // Context of the activity currently in
    private SQLiteDatabase mDb; // Reference to the database

    // Database info
    private static final String DATABASE_NAME = "KanjiReader.db";
    private static final int DATABASE_VERSION = 1;

    // User Table
    private static final String USER_TABLE_NAME = "User";
    private static final String USER_COLUMN_ID = "uid"; // PK, AUTOINCREMENT
    private static final String USER_COLUMN_NAME = "name"; // UNIQUE

    // Radical Table
    private static final String RADICAL_TABLE_NAME = "Radical";
    private static final String RADICAL_COLUMN_RADICAL = "radical"; // PK
    private static final String RADICAL_COLUMN_STROKES = "strokes"; // NOT NULL

    // Kanji Table
    private static final String KANJI_TABLE_NAME = "Kanji";
    private static final String KANJI_COLUMN_KANJI = "kanji"; // PK
    private static final String KANJI_COLUMN_RADICAL = "radical"; // FK Radical(radical)
    private static final String KANJI_COLUMN_STROKES = "strokes"; // NOT NULL
    private static final String KANJI_COLUMN_GRADE = "grade";
    private static final String KANJI_COLUMN_PARTS = "parts";
    private static final String KANJI_COLUMN_ONYOMI = "onyomi";
    private static final String KANJI_COLUMN_KUNYOMI = "kunyomi";
    private static final String KANJI_COLUMN_OFFICIALLIST = "officialList";
    private static final String KANJI_COLUMN_OLDFORM = "oldForm";

    // Checklist Table
    private static final String CHECKLIST_TABLE_NAME = "Checklist";
    private static final String CHECKLIST_COLUMN_USERID = "userId"; // FK User(uid), PK
    private static final String CHECKLIST_COLUMN_KANJI = "kanji"; // FK Kanji(kanji), PK
    private static final String CHECKLIST_COLUMN_LEARNED = "learned"; // NOT NULL, Default = 0

    // JEDict Table
//    private static final String JEDICT_TABLE_NAME = "JEDict";
//    private static final String JEDICT_COLUMN_WORD = "word"; // PK
//    private static final String JEDICT_COLUMN_EDEF = "eDef";
//    private static final String JEDICT_COLUMN_JDEF = "jDef";
//    private static final String JEDICT_COLUMN_READINGS = "readings";
//    private static final String JEDICT_COLUMN_PARTICLES = "particles";

    // Reading Table
    private static final String READING_TABLE_NAME = "Reading";
    private static final String READING_COLUMN_RID = "rid"; // PK, AUTOINCREMENT
    private static final String READING_COLUMN_TITLE = "title"; // NOT NULL
    private static final String READING_COLUMN_AUTHOR = "author"; // NOT NULL
    private static final String READING_COLUMN_PART = "part";
    private static final String READING_COLUMN_TEXT = "text"; // NOT NULL

    // ReadingContains Table
    private static final String READINGCONTAINS_TABLE_NAME = "ReadingContains";
    private static final String READINGCONTAINS_COLUMN_RID = "rid"; // FK Reading(rid), PK
    private static final String READINGCONTAINS_COLUMN_KANJI = "kanji"; // FK Kanji(kanji), PK

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        this.context = context;
        mDb = this.getWritableDatabase(); // This will call onCreate if db does not exist
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mDb = db;
        Log.i("DATABASE", "onCreate was hit");

        InputStream insertStream;
        BufferedReader insertReader;
        String stmt;

        try {
            // Open create_table_user
            insertStream = context.getResources().openRawResource(R.raw.create_table_user);
            insertReader = new BufferedReader(new InputStreamReader(insertStream));

            stmt = "";
            while (insertReader.ready()) {
                stmt += "\n" + insertReader.readLine();
            }
            mDb.execSQL(stmt);
            insertReader.close();
        }
        catch (IOException e) {
            Log.e("ERROR", "IOEXCEPTION when opening create_table_user.");
        }

        try {
            // Open create_table_radical
            insertStream = context.getResources().openRawResource(R.raw.create_table_radical);
            insertReader = new BufferedReader(new InputStreamReader(insertStream));

            stmt = "";
            while (insertReader.ready()) {
                stmt += "\n" + insertReader.readLine();
            }
            mDb.execSQL(stmt);
            insertReader.close();
        }
        catch (IOException e) {
            Log.e("ERROR", "IOEXCEPTION when opening create_table_radical.");
        }

        try {
            // Open create_table_kanji
            insertStream = context.getResources().openRawResource(R.raw.create_table_kanji);
            insertReader = new BufferedReader(new InputStreamReader(insertStream));

            stmt = "";
            while (insertReader.ready()) {
                stmt += "\n" + insertReader.readLine();
            }
            mDb.execSQL(stmt);
            insertReader.close();
        }
        catch (IOException e) {
            Log.e("ERROR", "IOEXCEPTION when opening create_table_kanji.");
        }

        try {
            // Open create_table_checklist
            insertStream = context.getResources().openRawResource(R.raw.create_table_checklist);
            insertReader = new BufferedReader(new InputStreamReader(insertStream));

            stmt = "";
            while (insertReader.ready()) {
                stmt += "\n" + insertReader.readLine();
            }
            mDb.execSQL(stmt);
            insertReader.close();
        }
        catch (IOException e) {
            Log.e("ERROR", "IOEXCEPTION when opening create_table_checklist.");
        }

        try {
            // Open create_table_jedict
            insertStream = context.getResources().openRawResource(R.raw.create_table_jedict);
            insertReader = new BufferedReader(new InputStreamReader(insertStream));

            stmt = "";
            while (insertReader.ready()) {
                stmt += "\n" + insertReader.readLine();
            }
            mDb.execSQL(stmt);
            insertReader.close();
        }
        catch (IOException e) {
            Log.e("ERROR", "IOEXCEPTION when opening create_table_jedict.");
        }

        try {
            // Open create_table_reading
            insertStream = context.getResources().openRawResource(R.raw.create_table_reading);
            insertReader = new BufferedReader(new InputStreamReader(insertStream));

            stmt = "";
            while (insertReader.ready()) {
                stmt += "\n" + insertReader.readLine();
            }
            mDb.execSQL(stmt);
            insertReader.close();
        }
        catch (IOException e) {
            Log.e("ERROR", "IOEXCEPTION when opening create_table_reading.");
        }

        try {
            // Open create_table_readingcontains
            insertStream = context.getResources().openRawResource(R.raw.create_table_readingcontains);
            insertReader = new BufferedReader(new InputStreamReader(insertStream));

            stmt = "";
            while (insertReader.ready()) {
                stmt += "\n" + insertReader.readLine();
            }
            mDb.execSQL(stmt);
            insertReader.close();
        }
        catch (IOException e) {
            Log.e("ERROR", "IOEXCEPTION when opening create_table_readingcontains.");
        }

        try {
            // Open populate_table_radical
            insertStream = context.getResources().openRawResource(R.raw.populate_table_radical);
            insertReader = new BufferedReader(new InputStreamReader(insertStream));

            stmt = "";
            while (insertReader.ready()) {
                stmt += "\n" + insertReader.readLine();
            }
            mDb.execSQL(stmt);
            insertReader.close();
        }
        catch (IOException e) {
            Log.e("ERROR", "IOEXCEPTION when opening populate_table_radical.");
        }

        try {
            // Open populate_table_kanji
            insertStream = context.getResources().openRawResource(R.raw.populate_table_kanji);
            insertReader = new BufferedReader(new InputStreamReader(insertStream));

            stmt = "";
            while (insertReader.ready()) {
                stmt += "\n" + insertReader.readLine();
            }
            mDb.execSQL(stmt);
            insertReader.close();
        }
        catch (IOException e) {
            Log.e("ERROR", "IOEXCEPTION when opening populate_table_kanji.");
        }

        try {
            // Open populate_table_jedict
            insertStream = context.getResources().openRawResource(R.raw.populate_table_jedict);
            insertReader = new BufferedReader(new InputStreamReader(insertStream));

            stmt = "";
            while (insertReader.ready()) {
                stmt += "\n" + insertReader.readLine();
            }
            mDb.execSQL(stmt);
            insertReader.close();
        }
        catch (IOException e) {
            Log.e("ERROR", "IOEXCEPTION when opening populate_table_jedict.");
        }

        // Add readings to test the findKanjiContained method
        insertReading( "こころ", "夏目漱石", "上　先生と私　一",
                        "　私はその人を常に先生と呼んでいた。だからここでもただ先生と書くだけで本名は打ち明けない。これは世間を憚かる遠慮というよりも、その方が私にとって自然だからである。私はその人の記憶を呼び起すごとに、すぐ「先生」といいたくなる。筆を執っても心持は同じ事である。よそよそしい頭文字などはとても使う気にならない。\n" +
                        "　私が先生と知り合いになったのは鎌倉である。その時私はまだ若々しい書生であった。暑中休暇を利用して海水浴に行った友達からぜひ来いという端書を受け取ったので、私は多少の金を工面して、出掛ける事にした。私は金の工面に二、三日を費やした。ところが私が鎌倉に着いて三日と経たないうちに、私を呼び寄せた友達は、急に国元から帰れという電報を受け取った。電報には母が病気だからと断ってあったけれども友達はそれを信じなかった。友達はかねてから国元にいる親たちに勧まない結婚を強いられていた。彼は現代の習慣からいうと結婚するにはあまり年が若過ぎた。それに肝心の当人が気に入らなかった。それで夏休みに当然帰るべきところを、わざと避けて東京の近くで遊んでいたのである。彼は電報を私に見せてどうしようと相談をした。私にはどうしていいか分らなかった。けれども実際彼の母が病気であるとすれば彼は固より帰るべきはずであった。それで彼はとうとう帰る事になった。せっかく来た私は一人取り残された。\n" +
                        "　学校の授業が始まるにはまだ大分日数があるので鎌倉におってもよし、帰ってもよいという境遇にいた私は、当分元の宿に留まる覚悟をした。友達は中国のある資産家の息子で金に不自由のない男であったけれども、学校が学校なのと年が年なので、生活の程度は私とそう変りもしなかった。したがって一人ぼっちになった私は別に恰好な宿を探す面倒ももたなかったのである。\n" +
                        "　宿は鎌倉でも辺鄙な方角にあった。玉突きだのアイスクリームだのというハイカラなものには長い畷を一つ越さなければ手が届かなかった。車で行っても二十銭は取られた。けれども個人の別荘はそこここにいくつでも建てられていた。それに海へはごく近いので海水浴をやるには至極便利な地位を占めていた。\n" +
                        "　私は毎日海へはいりに出掛けた。古い燻ぶり返った藁葺の間を通り抜けて磯へ下りると、この辺にこれほどの都会人種が住んでいるかと思うほど、避暑に来た男や女で砂の上が動いていた。ある時は海の中が銭湯のように黒い頭でごちゃごちゃしている事もあった。その中に知った人を一人ももたない私も、こういう賑やかな景色の中に裹まれて、砂の上に寝そべってみたり、膝頭を波に打たしてそこいらを跳ね廻るのは愉快であった。\n" +
                        "　私は実に先生をこの雑沓の間に見付け出したのである。その時海岸には掛茶屋が二軒あった。私はふとした機会からその一軒の方に行き慣れていた。長谷辺に大きな別荘を構えている人と違って、各自に専有の着換場を拵えていないここいらの避暑客には、ぜひともこうした共同着換所といった風なものが必要なのであった。彼らはここで茶を飲み、ここで休息する外に、ここで海水着を洗濯させたり、ここで鹹はゆい身体を清めたり、ここへ帽子や傘を預けたりするのである。海水着を持たない私にも持物を盗まれる恐れはあったので、私は海へはいるたびにその茶屋へ一切を脱ぎ棄てる事にしていた。");

        insertReading("こころ", "夏目漱石", "上　先生と私　二",
                        "　私がその掛茶屋で先生を見た時は、先生がちょうど着物を脱いでこれから海へ入ろうとするところであった。私はその時反対に濡れた身体を風に吹かして水から上がって来た。二人の間には目を遮る幾多の黒い頭が動いていた。特別の事情のない限り、私はついに先生を見逃したかも知れなかった。それほど浜辺が混雑し、それほど私の頭が放漫であったにもかかわらず、私がすぐ先生を見付け出したのは、先生が一人の西洋人を伴れていたからである。\n" +
                        "　その西洋人の優れて白い皮膚の色が、掛茶屋へ入るや否や、すぐ私の注意を惹いた。純粋の日本の浴衣を着ていた彼は、それを床几の上にすぽりと放り出したまま、腕組みをして海の方を向いて立っていた。彼は我々の穿く猿股一つの外何物も肌に着けていなかった。私にはそれが第一不思議だった。私はその二日前に由井が浜まで行って、砂の上にしゃがみながら、長い間西洋人の海へ入る様子を眺めていた。私の尻をおろした所は少し小高い丘の上で、そのすぐ傍がホテルの裏口になっていたので、私の凝としている間に、大分多くの男が塩を浴びに出て来たが、いずれも胴と腕と股は出していなかった。女は殊更肉を隠しがちであった。大抵は頭に護謨製の頭巾を被って、海老茶や紺や藍の色を波間に浮かしていた。そういう有様を目撃したばかりの私の眼には、猿股一つで済まして皆なの前に立っているこの西洋人がいかにも珍しく見えた。\n" +
                        "　彼はやがて自分の傍を顧みて、そこにこごんでいる日本人に、一言二言何かいった。その日本人は砂の上に落ちた手拭を拾い上げているところであったが、それを取り上げるや否や、すぐ頭を包んで、海の方へ歩き出した。その人がすなわち先生であった。\n" +
                        "　私は単に好奇心のために、並んで浜辺を下りて行く二人の後姿を見守っていた。すると彼らは真直に波の中に足を踏み込んだ。そうして遠浅の磯近くにわいわい騒いでいる多人数の間を通り抜けて、比較的広々した所へ来ると、二人とも泳ぎ出した。彼らの頭が小さく見えるまで沖の方へ向いて行った。それから引き返してまた一直線に浜辺まで戻って来た。掛茶屋へ帰ると、井戸の水も浴びずに、すぐ身体を拭いて着物を着て、さっさとどこへか行ってしまった。\n" +
                        "　彼らの出て行った後、私はやはり元の床几に腰をおろして烟草を吹かしていた。その時私はぽかんとしながら先生の事を考えた。どうもどこかで見た事のある顔のように思われてならなかった。しかしどうしてもいつどこで会った人か想い出せずにしまった。\n" +
                        "　その時の私は屈托がないというよりむしろ無聊に苦しんでいた。それで翌日もまた先生に会った時刻を見計らって、わざわざ掛茶屋まで出かけてみた。すると西洋人は来ないで先生一人麦藁帽を被ってやって来た。先生は眼鏡をとって台の上に置いて、すぐ手拭で頭を包んで、すたすた浜を下りて行った。先生が昨日のように騒がしい浴客の中を通り抜けて、一人で泳ぎ出した時、私は急にその後が追い掛けたくなった。私は浅い水を頭の上まで跳かして相当の深さの所まで来て、そこから先生を目標に抜手を切った。すると先生は昨日と違って、一種の弧線を描いて、妙な方向から岸の方へ帰り始めた。それで私の目的はついに達せられなかった。私が陸へ上がって雫の垂れる手を振りながら掛茶屋に入ると、先生はもうちゃんと着物を着て入れ違いに外へ出て行った。");

        insertReading("上を向いて歩こう", "坂本 九", "１９６１年",
                "上を向いて歩こう\n" +
                "涙がこぼれないように\n" +
                "思い出す春の日\n" +
                "一人ぼっちの夜\n\n" +
                "上を向いて歩こう\n" +
                "にじんだ星を数えて\n" +
                "思い出す夏の日\n" +
                "一人ぼっちの夜\n\n" +
                "幸せは雲の上に\n" +
                "幸せは空の上に\n\n" +
                "上を向いて歩こう\n" +
                "涙がこぼれないように\n" +
                "泣きながら歩く\n" +
                "一人ぼっちの夜\n\n" +
                "思い出す秋の日\n" +
                "一人ぼっちの夜\n\n" +
                "悲しみは星の影に\n" +
                "悲しみは月の影に\n\n" +
                "上を向いて歩こう\n" +
                "涙がこぼれないように\n" +
                "泣きながら歩く\n" +
                "一人ぼっちの夜\n\n" +
                "一人ぼっちの夜");

        insertReading("今年たくさん売れた本を発表", "NHK NEWS WEB EASY", "１２月０４日 １１時３０分",
                "日本出版販売は、今年たくさん売れた本を発表しました。\n\n" +
                "１番売れた本は、佐藤愛子さんが書いた「九十歳。何がめでたい」というエッセーで、１０５万冊以上売れました。佐藤さんは９４歳です。この本は、年をとることはめでたいことばかりではないと面白く書いてあって、人気になりました。５０歳から７０歳ぐらいの女性がたくさん読んでいます。\n\n" +
                "２番目は、「ざんねんないきもの事典」という子どものための本です。パンダが食べるササにはほとんど栄養がないことなど、動物の残念なことを子どもたちに紹介しています。\n\n" +
                "３番目は、恩田陸さんの小説「蜜蜂と遠雷」です。この小説は今年２つの賞をもらいました。\n\n" +
                "４番目は、子どもが漢字の練習をする「うんこ漢字ドリル」です。全部の例文に、糞という意味の「うんこ」ということばが入っています。\n\n" +
                "５番目は、村上春樹さんの小説「騎士団長殺し」です。\n\n" +
                "日本出版販売は、「子どもの本や勉強の本がこんなに売れるのは、とても珍しいです」と言っています。");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Write code for updating database
        onCreate(db);
    }


    ////////////////////////////////
    //       User operations      //
    ////////////////////////////////
    public boolean insertUser(final String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_NAME, name);

        if (mDb.insert(USER_TABLE_NAME, null, contentValues) == -1L) {
            return false;
        }
        else {
            // Create the checklist
            int uid = getUidByName(name);
            createChecklist(uid);
        }

        return true;
    }

    public ArrayList<String> getAllUserNames() {
        ArrayList<String> users = new ArrayList<>();

        Cursor res =  mDb.rawQuery("SELECT " + USER_COLUMN_NAME + " FROM " + USER_TABLE_NAME, null);
        res.moveToFirst();

        while(!res.isAfterLast()){
            users.add(res.getString(res.getColumnIndex(USER_COLUMN_NAME)));
            res.moveToNext();
        }
        res.close();
        return users;
    }

    public int getUidByName(String name) {
        Cursor res =  mDb.rawQuery("SELECT " + USER_COLUMN_ID + " FROM " + USER_TABLE_NAME +
                " WHERE " + USER_COLUMN_NAME + " = '" + name + "'", null);
        res.moveToFirst();

        int uid = res.getInt(res.getColumnIndex(USER_COLUMN_ID));
        res.close();
        return uid;
    }

    public String getNameByUid(int uid) {
        Cursor res =  mDb.rawQuery("SELECT " + USER_COLUMN_NAME + " FROM " + USER_TABLE_NAME +
                " WHERE " + USER_COLUMN_ID + " = " + uid, null);
        res.moveToFirst();

        String name = res.getString(res.getColumnIndex(USER_COLUMN_NAME));
        res.close();
        return name;
    }

    public boolean deleteUser(String name) {
        int uid = getUidByName(name);
        return mDb.delete(USER_TABLE_NAME, USER_COLUMN_ID + " = " + uid, null) > 0;
    }


    ////////////////////////////////
    //     Radical operations     //
    ////////////////////////////////
    public ArrayList<String> getAllRadicals() {
        ArrayList<String> radicals = new ArrayList<>();

        Cursor res = mDb.rawQuery("SELECT DISTINCT " + RADICAL_COLUMN_RADICAL +
                " FROM " + RADICAL_TABLE_NAME +
                " ORDER BY " + RADICAL_COLUMN_STROKES + " ASC", null);
        res.moveToFirst();

        while(!res.isAfterLast()){
            radicals.add(res.getString(res.getColumnIndex(RADICAL_COLUMN_RADICAL)));
            res.moveToNext();
        }
        res.close();
        return radicals;
    }


    ////////////////////////////////
    //      Kanji operations      //
    ////////////////////////////////
    private ArrayList<String> getAllKanji() {
        ArrayList<String> kanji = new ArrayList<String>();

        Cursor res = mDb.rawQuery("SELECT " + KANJI_COLUMN_KANJI +
                " FROM " + KANJI_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            kanji.add(res.getString(res.getColumnIndex(KANJI_COLUMN_KANJI)));
            res.moveToNext();
        }

        return kanji;
    }

    public Kanji getKanjiDetails(String kanji) {

        Cursor res =  mDb.rawQuery("SELECT * FROM " + KANJI_TABLE_NAME + " WHERE " +
                                    KANJI_COLUMN_KANJI + " = '" + kanji + "'", null);
        res.moveToFirst();

        String k = res.getString(res.getColumnIndex(KANJI_COLUMN_KANJI));
        String radical = res.getString(res.getColumnIndex(KANJI_COLUMN_RADICAL));
        int strokes = res.getInt(res.getColumnIndex(KANJI_COLUMN_STROKES));
        int grade = res.getInt(res.getColumnIndex(KANJI_COLUMN_GRADE));
        String parts = res.getString(res.getColumnIndex(KANJI_COLUMN_PARTS));
        String onyomi = res.getString(res.getColumnIndex(KANJI_COLUMN_ONYOMI));
        String kunyomi = res.getString(res.getColumnIndex(KANJI_COLUMN_KUNYOMI));
        String officialList = res.getString(res.getColumnIndex(KANJI_COLUMN_OFFICIALLIST));
        String oldForm = res.getString(res.getColumnIndex(KANJI_COLUMN_OLDFORM));

        Kanji Kan = new Kanji(k, radical, strokes, grade, parts, onyomi, kunyomi, officialList, oldForm);
        res.close();
        return Kan;
    }

    public int getTotalNumKanji() {
        int num = 0;

        Cursor res =  mDb.rawQuery("SELECT COUNT(" + KANJI_COLUMN_KANJI + ") AS count FROM " +
                KANJI_TABLE_NAME, null);
        res.moveToFirst();

        while(!res.isAfterLast()){
            num = res.getInt(res.getColumnIndex("count"));
            res.moveToNext();
        }
        res.close();
        return num;
    }


    ////////////////////////////////
    //    Checklist operations    //
    ////////////////////////////////
    private void createChecklist(int userId) {

        Cursor res =  mDb.rawQuery("SELECT " + KANJI_COLUMN_KANJI + " FROM " + KANJI_TABLE_NAME, null);
        res.moveToFirst();

        String kanji;
        while(!res.isAfterLast()){
            kanji = res.getString(res.getColumnIndex(KANJI_COLUMN_KANJI));
            ContentValues contentValues = new ContentValues();
            contentValues.put(CHECKLIST_COLUMN_USERID, userId);
            contentValues.put(CHECKLIST_COLUMN_KANJI, kanji);
            contentValues.put(CHECKLIST_COLUMN_LEARNED, 0);
            mDb.insert(CHECKLIST_TABLE_NAME, null, contentValues);
            res.moveToNext();
        }
        res.close();
    }

    public int getNumKanjiLearned(int userId) {
        int num = 0;

        Cursor res =  mDb.rawQuery("SELECT COUNT(" + CHECKLIST_COLUMN_KANJI + ") AS count FROM " +
                CHECKLIST_TABLE_NAME + " WHERE " + CHECKLIST_COLUMN_LEARNED + " = 1 AND "
                + CHECKLIST_COLUMN_USERID + " = " + userId, null);
        res.moveToFirst();

        while(!res.isAfterLast()){
            num = res.getInt(res.getColumnIndex("count"));
            res.moveToNext();
        }
        res.close();
        return num;
    }

    public ArrayList<String> getKanjiNotLearned(int userId) {
        ArrayList<String> kanji = new ArrayList<>();

        Cursor res =  mDb.rawQuery("SELECT * FROM " + CHECKLIST_TABLE_NAME +
                    " WHERE " + CHECKLIST_COLUMN_LEARNED + " = 0 AND "
                    + CHECKLIST_COLUMN_USERID + " = " + userId, null);
        res.moveToFirst();

        while(!res.isAfterLast()){
            kanji.add(res.getString(res.getColumnIndex(KANJI_COLUMN_KANJI)));
            res.moveToNext();
        }
        res.close();
        return kanji;
    }

    public ArrayList<String> getKanjiLearned(int userId) {
        ArrayList<String> kanji = new ArrayList<>();

        Cursor res =  mDb.rawQuery("SELECT * FROM " + CHECKLIST_TABLE_NAME +
                " WHERE " + CHECKLIST_COLUMN_LEARNED + " = 1 AND "
                + CHECKLIST_COLUMN_USERID + " = " + userId, null);
        res.moveToFirst();

        while(!res.isAfterLast()){
            kanji.add(res.getString(res.getColumnIndex(KANJI_COLUMN_KANJI)));
            res.moveToNext();
        }
        res.close();
        return kanji;
    }

    public ArrayList<ChecklistItem> getAllChecklistKanjiSorted(int userId, int sortMethod) {
        ArrayList<ChecklistItem> checklistKanji = new ArrayList<>();
        switch (sortMethod) {
            case 0: // Grade
            {
                ArrayList<Integer> grades = new ArrayList<>();

                // Get the possible values for strokes
                Cursor res = mDb.rawQuery("SELECT DISTINCT " + KANJI_COLUMN_GRADE +
                        " FROM " + KANJI_TABLE_NAME +
                        " ORDER BY " + KANJI_COLUMN_GRADE + " ASC", null);
                res.moveToFirst();

                while (!res.isAfterLast()) {
                    grades.add(res.getInt(res.getColumnIndex(KANJI_COLUMN_GRADE)));
                    res.moveToNext();
                }

                // Grab all kanji, sorted by strokes, then by radical
                for (int i = 0; i < grades.size(); i++) {
                    res =  mDb.rawQuery("SELECT c." + CHECKLIST_COLUMN_KANJI +
                            ", c." + CHECKLIST_COLUMN_LEARNED + " FROM " +
                            CHECKLIST_TABLE_NAME + " AS c, " + KANJI_TABLE_NAME + " AS k" +
                            " WHERE k." + KANJI_COLUMN_GRADE + " = " + grades.get(i) + " AND k." +
                            KANJI_COLUMN_KANJI + " = c." + CHECKLIST_COLUMN_KANJI + " AND c." +
                            CHECKLIST_COLUMN_USERID + " = " + userId +
                            " ORDER BY k." + KANJI_COLUMN_STROKES + " ASC, k." +
                            KANJI_COLUMN_RADICAL + " ASC", null);
                    res.moveToFirst();

                    // Add a separator to the list
                    // It will not do anything when clicked or long-pressed
                    checklistKanji.add(new ChecklistItem(String.valueOf(grades.get(i)), -1));
                    String kanji;
                    int learned;
                    while(!res.isAfterLast()) {
                        kanji = res.getString(res.getColumnIndex(CHECKLIST_COLUMN_KANJI));
                        learned = res.getInt(res.getColumnIndex(CHECKLIST_COLUMN_LEARNED));
                        checklistKanji.add(new ChecklistItem(kanji, learned));
                        res.moveToNext();
                    }
                    res.close();
                }
                break;
            }
            case 1: // Strokes
            {
                ArrayList<Integer> strokes = new ArrayList<>();

                // Get the possible values for strokes
                Cursor res = mDb.rawQuery("SELECT DISTINCT " + KANJI_COLUMN_STROKES +
                        " FROM " + KANJI_TABLE_NAME +
                        " ORDER BY " + KANJI_COLUMN_STROKES + " ASC", null);
                res.moveToFirst();

                while (!res.isAfterLast()) {
                    strokes.add(res.getInt(res.getColumnIndex(KANJI_COLUMN_STROKES)));
                    res.moveToNext();
                }

                // Grab all kanji, sorted by strokes, then by radical
                for (int i = 0; i < strokes.size(); i++) {
                    res = mDb.rawQuery("SELECT c." + CHECKLIST_COLUMN_KANJI +
                            ", c." + CHECKLIST_COLUMN_LEARNED + " FROM " +
                            CHECKLIST_TABLE_NAME + " AS c, " + KANJI_TABLE_NAME + " AS k" +
                            " WHERE k." + KANJI_COLUMN_STROKES + " = " + strokes.get(i) + " AND k." +
                            KANJI_COLUMN_KANJI + " = c." + CHECKLIST_COLUMN_KANJI + " AND c." +
                            CHECKLIST_COLUMN_USERID + " = " + userId +
                            " ORDER BY k." + KANJI_COLUMN_RADICAL + " ASC", null);
                    res.moveToFirst();

                    // Add a separator to the list
                    // It will not do anything when clicked or long-pressed
                    checklistKanji.add(new ChecklistItem(String.valueOf(strokes.get(i)), -1));
                    String kanji;
                    int learned;
                    while (!res.isAfterLast()) {
                        kanji = res.getString(res.getColumnIndex(CHECKLIST_COLUMN_KANJI));
                        learned = res.getInt(res.getColumnIndex(CHECKLIST_COLUMN_LEARNED));
                        checklistKanji.add(new ChecklistItem(kanji, learned));
                        res.moveToNext();
                    }
                    res.close();
                }
                break;
            }
            case 2: // Radical
            {
                ArrayList<String> radicals = getAllRadicals();
                Cursor res;

                // Grab all kanji, sorted by radical, then by strokes
                for (int i = 0; i < radicals.size(); i++) {
                    res = mDb.rawQuery("SELECT c." + CHECKLIST_COLUMN_KANJI +
                            ", c." + CHECKLIST_COLUMN_LEARNED + " FROM " +
                            CHECKLIST_TABLE_NAME + " AS c, " + KANJI_TABLE_NAME + " AS k" +
                            " WHERE k." + KANJI_COLUMN_RADICAL + " = '" + radicals.get(i) + "' AND k." +
                            KANJI_COLUMN_KANJI + " = c." + CHECKLIST_COLUMN_KANJI + " AND c." +
                            CHECKLIST_COLUMN_USERID + " = " + userId +
                            " ORDER BY k." + KANJI_COLUMN_STROKES + " ASC", null);
                    res.moveToFirst();

                    // Add a separator to the list
                    // It will not do anything when clicked or long-pressed
                    checklistKanji.add(new ChecklistItem(String.valueOf(radicals.get(i)), -1));
                    String kanji;
                    int learned;
                    while (!res.isAfterLast()) {
                        kanji = res.getString(res.getColumnIndex(CHECKLIST_COLUMN_KANJI));
                        learned = res.getInt(res.getColumnIndex(CHECKLIST_COLUMN_LEARNED));
                        checklistKanji.add(new ChecklistItem(kanji, learned));
                        res.moveToNext();
                    }
                    res.close();
                }
                break;
            }
            default:
            {
                break;
            }
        }
        return checklistKanji;
    }

    public ArrayList<ChecklistItem> getKanjiSearchResults(String katakana, String hiragana, int uid) {
        ArrayList<ChecklistItem> list = new ArrayList<>();
        Cursor res;
        // Only search hiragana
        if (katakana == null) {
            res =  mDb.rawQuery("SELECT c." + CHECKLIST_COLUMN_KANJI +
                    ", c." + CHECKLIST_COLUMN_LEARNED + " FROM " +
                    CHECKLIST_TABLE_NAME + " AS c, " + KANJI_TABLE_NAME + " AS k" +
                    " WHERE k." + KANJI_COLUMN_KUNYOMI + " LIKE '%" + hiragana + "%' AND " +
                    CHECKLIST_COLUMN_USERID + " = " + uid, null);
        }
        // Only search katakana
        else if (hiragana == null) {
            res =  mDb.rawQuery("SELECT c." + CHECKLIST_COLUMN_KANJI +
                    ", c." + CHECKLIST_COLUMN_LEARNED + " FROM " +
                    CHECKLIST_TABLE_NAME + " AS c, " + KANJI_TABLE_NAME + " AS k" +
                    " WHERE k." + KANJI_COLUMN_ONYOMI + " LIKE '%" + katakana + "%' AND " +
                    CHECKLIST_COLUMN_USERID + " = " + uid, null);
        }
        // Search both, do not add dupes
        else {
            res =  mDb.rawQuery("SELECT DISTINCT c." + CHECKLIST_COLUMN_KANJI +
                    ", c." + CHECKLIST_COLUMN_LEARNED + " FROM " +
                    CHECKLIST_TABLE_NAME + " AS c, " + KANJI_TABLE_NAME + " AS k" +
                    " WHERE (k." + KANJI_COLUMN_KUNYOMI + " LIKE '%" + hiragana + "%' OR " +
                    KANJI_COLUMN_ONYOMI + " LIKE '%" + katakana + "%') AND " +
                    CHECKLIST_COLUMN_USERID + " = " + uid, null);
        }

        res.moveToFirst();
        while (!res.isAfterLast()) {
            String kanji = res.getString(res.getColumnIndex(CHECKLIST_COLUMN_KANJI));
            int learned = res.getInt(res.getColumnIndex(CHECKLIST_COLUMN_LEARNED));
            ChecklistItem next = new ChecklistItem(kanji, learned);
            list.add(next);
            res.moveToNext();
        }

        res.close();
        return list;
    }

    public void setKanjiLearned(final int userId, final String kanji, final boolean learned) {
        new AsyncTask<Void, Void, Boolean>() {
            protected Boolean doInBackground(Void... v) {
                String whereClause = CHECKLIST_COLUMN_KANJI + " = '" + kanji +
                        "' AND " + CHECKLIST_COLUMN_USERID + " = " + userId;
                ContentValues values = new ContentValues();

                if (learned)
                    values.put(CHECKLIST_COLUMN_LEARNED, 1);
                else
                    values.put(CHECKLIST_COLUMN_LEARNED, 0);

                mDb.update(CHECKLIST_TABLE_NAME, values, whereClause, null);
                // Escape early if cancel() is called
                return !isCancelled();
            }
        }.execute();
    }


    ////////////////////////////////
    //     Reading operations     //
    ////////////////////////////////
    private int getReadingRowid(String title, String author, String part) {
        int rid = -1;

        Cursor res = mDb.rawQuery("SELECT " + READING_COLUMN_RID + " FROM " + READING_TABLE_NAME +
                " WHERE " + READING_COLUMN_TITLE + " = '" + title + "' AND " +
                READING_COLUMN_AUTHOR + " = '" + author + "' AND " +
                READING_COLUMN_PART + " = '" + part + "'", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            rid = res.getInt(res.getColumnIndex(READING_COLUMN_RID));
            res.moveToNext();
        }
        res.close();

        return rid;
    }

    public boolean insertReading(String title, String author, String part, String text) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(READING_COLUMN_TITLE, title);
        contentValues.put(READING_COLUMN_AUTHOR, author);
        contentValues.put(READING_COLUMN_PART, part);
        contentValues.put(READING_COLUMN_TEXT, text);
        if (mDb.insert(READING_TABLE_NAME, null, contentValues) != -1L) {
            findKanjiContained(getReadingRowid(title, author, part));
            return true;
        }
        return false;
    }

    public ArrayList<Reading> getAllReadings() {
        ArrayList<Reading> readings = new ArrayList<>();

        Cursor res =  mDb.rawQuery("SELECT " + READING_COLUMN_RID + ", "
                + READING_COLUMN_TITLE + ", "
                + READING_COLUMN_AUTHOR + ", "
                + READING_COLUMN_PART +
                " FROM " + READING_TABLE_NAME, null);
        res.moveToFirst();

        String title = "", author = "", part = "";
        int rid = 0;
        while(!res.isAfterLast()){
            rid = res.getInt(res.getColumnIndex(READING_COLUMN_RID));
            title = res.getString(res.getColumnIndex(READING_COLUMN_TITLE));
            author = res.getString(res.getColumnIndex(READING_COLUMN_AUTHOR));
            part = res.getString(res.getColumnIndex(READING_COLUMN_PART));
            readings.add(new Reading(rid, title, author, part));
            res.moveToNext();
        }
        res.close();

        return readings;
    }

    public String getReadingText(int rid) {
        String text = "";

        Cursor res =  mDb.rawQuery("SELECT " + READING_COLUMN_TEXT +
                " FROM " + READING_TABLE_NAME +
                " WHERE " + READING_COLUMN_RID + " = " + rid, null);
        res.moveToFirst();

        while(!res.isAfterLast()) {
            text = res.getString(res.getColumnIndex(READING_COLUMN_TEXT));
            res.moveToNext();
        }
        res.close();
        return text;
    }

    public boolean deleteReading(int rid){
        return mDb.delete(READING_TABLE_NAME, READING_COLUMN_RID + " = " + rid, null) > 0;
    }


    ////////////////////////////////
    // ReadingContains operations //
    ////////////////////////////////
    private void findKanjiContained(int rid) {

        // Kana list to check against text before trying to go to database for info
        ArrayList<String> allKanji = getAllKanji();

        // Get the reading
        String text = "";
        Cursor res = mDb.rawQuery("SELECT " + READING_COLUMN_TEXT + " FROM " + READING_TABLE_NAME +
                " WHERE rowid = " + rid, null);
        res.moveToFirst();
        while(!res.isAfterLast()) {
            text = res.getString(res.getColumnIndex(READING_COLUMN_TEXT));
            res.moveToNext();
        }

        // Move through the reading and check for kanji, generatine a list of unique kanji
        ArrayList<String> kanjiContained = new ArrayList<>();

        // This will likely be slow... but it's only done once pre text, so it may be fine
        for (int i = 0; i < text.length(); i++) {
            String temp = String.valueOf(text.charAt(i));
            if (allKanji.contains(temp) && !kanjiContained.contains(temp))
                kanjiContained.add(temp);
        }

        // Log the array
        Log.i("ARRAY", kanjiContained.toString());

        // Add kanji to database
        for (int i = 0; i < kanjiContained.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(READINGCONTAINS_COLUMN_RID, rid);
            contentValues.put(READINGCONTAINS_COLUMN_KANJI, kanjiContained.get(i));
            mDb.insert(READINGCONTAINS_TABLE_NAME, null, contentValues);
        }

        getKanjiContained(rid);
    }

    public ArrayList<String> getKanjiContained(int rid) {
        ArrayList<String> kanji = new ArrayList<>();

        Cursor res = mDb.rawQuery("SELECT " + READINGCONTAINS_COLUMN_KANJI +
                " FROM " + READINGCONTAINS_TABLE_NAME +
                " WHERE " + READINGCONTAINS_COLUMN_RID + " = " + rid, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            kanji.add(res.getString(res.getColumnIndex(READINGCONTAINS_COLUMN_KANJI)));
            res.moveToNext();
        }
        res.close();

        return kanji;
    }

    public ArrayList<ChecklistItem> getKanjiContainedForUser(int rid, int userId) {
        ArrayList<ChecklistItem> kanjiContained = new ArrayList<>();

        // Grab all kanji, sorted by strokes, then by radical
        Cursor res =  mDb.rawQuery("SELECT c." + CHECKLIST_COLUMN_KANJI + ", c." + CHECKLIST_COLUMN_LEARNED +
                " FROM " + CHECKLIST_TABLE_NAME + " AS c, " + READINGCONTAINS_TABLE_NAME + " AS r," +
                KANJI_TABLE_NAME + " AS k" + " WHERE r." + READINGCONTAINS_COLUMN_KANJI +
                " = c." + CHECKLIST_COLUMN_KANJI + " AND c." + CHECKLIST_COLUMN_USERID + " = " + userId +
                " AND k." + KANJI_COLUMN_KANJI + " = c." + CHECKLIST_COLUMN_KANJI +
                " AND r." + READINGCONTAINS_COLUMN_RID + " = " + rid + " ORDER BY k." +
                KANJI_COLUMN_STROKES + " ASC, k." + KANJI_COLUMN_RADICAL + " ASC", null);
        res.moveToFirst();

        // Add a separator to the list
        // It will not do anything when clicked or long-pressed
        String kanji;
        int learned;
        while(!res.isAfterLast()) {
            kanji = res.getString(res.getColumnIndex(CHECKLIST_COLUMN_KANJI));
            learned = res.getInt(res.getColumnIndex(CHECKLIST_COLUMN_LEARNED));
            kanjiContained.add(new ChecklistItem(kanji, learned));
            res.moveToNext();
        }
        res.close();

        return kanjiContained;
    }

    public int getNumKanjiUnknown(int userId, int rid) {
        ArrayList<String> kanjiLearned = getKanjiLearned(userId);
        ArrayList<String> readingKanji = getKanjiContained(rid);
        int numUnknown = 0;

        for (String k : readingKanji) {
            if (!kanjiLearned.contains(k))
                numUnknown++;
        }
        return numUnknown;
    }
}
