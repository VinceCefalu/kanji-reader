package firsttry.kanjireader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import firsttry.kanjireader.database.DBHelper;
import firsttry.kanjireader.database.Kanji;


public class KanjiInfoFragment extends android.app.DialogFragment {

    // create db stuff
    private String kanji;

    public KanjiInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getArguments();
        if (extras != null) {
            kanji = extras.getString("kanji");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kanji_info, container, false);

        populateFields(view, kanji);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void populateFields(View v, String kanji){

        DBHelper dbHelper = new DBHelper(v.getContext());
        Kanji kanjiItem = dbHelper.getKanjiDetails(kanji);

        // create the textviews
        TextView lblKanji = (TextView) v.findViewById(R.id.txtKanji);
        TextView lblOldForm = (TextView) v.findViewById(R.id.txtOldForm);
        TextView lblOnyomi = (TextView) v.findViewById(R.id.lblOnyomi);
        TextView lblKunyomi = (TextView) v.findViewById(R.id.lblKunyomi);
        TextView lblStrokes = (TextView) v.findViewById(R.id.lblStrokes);
        TextView lblRadical = (TextView) v.findViewById(R.id.lblRadical);
        TextView lblParts = (TextView) v.findViewById(R.id.lblParts);
        TextView lblGrade = (TextView) v.findViewById(R.id.lblGrade);

        // change the labels to appropriate information in the kanji info fragment
        lblKanji.setText(kanjiItem.getKanji());
        lblOldForm.setText(kanjiItem.getOldForm());
        String temp = "Onyomi: " + "  " + kanjiItem.getOnyomi();
        lblOnyomi.setText(temp);
        lblKunyomi.setText("Kunyomi: \t" + kanjiItem.getKunyomi());
        lblStrokes.setText(getResources().getString(R.string.Strokes) + "   " + String.valueOf(kanjiItem.getStrokes()));
        lblRadical.setText(getResources().getString(R.string.Radical) + "   " + kanjiItem.getRadical());
        lblParts.setText(getResources().getString(R.string.Part) + kanjiItem.getParts());
        lblGrade.setText(getResources().getString(R.string.Grade) + "  " + String.valueOf(kanjiItem.getGrade()));
    }
}
