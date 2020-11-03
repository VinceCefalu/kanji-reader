package firsttry.kanjireader;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import firsttry.kanjireader.conversion.JapaneseCharacter;
import firsttry.kanjireader.database.ChecklistItem;
import firsttry.kanjireader.database.DBHelper;


public class KanjiFragment extends Fragment
        implements AdapterView.OnItemSelectedListener {

    private DBHelper dbHelper;
    private GridView kanjiGrid;
    private TextView txtTotal;
    private EditText txtSearch;
    private int uid;
    private ArrayAdapter<ChecklistItem> gridAdapter;
    private int learned, total;
    private int cellBackground;

    public KanjiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = this.getArguments();
        if (extras != null) {
            uid = extras.getInt("uid");
        }

        // check to see if the dark theme is already enabled
        if (((MainActivity) getActivity()).getDark())
            cellBackground = R.color.materialDark;
        else
            cellBackground = R.color.colorTransparent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kanji, container, false);

        // Instantiate member vars
        kanjiGrid = (GridView) view.findViewById(R.id.gridKanji);
        txtTotal = (TextView)view.findViewById(R.id.txtTotal);
        txtSearch = (EditText)view.findViewById(R.id.txtSearch);
        dbHelper = new DBHelper(view.getContext());

        // kanjiGrid click listener
        kanjiGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView)view.findViewById(android.R.id.text1);

                // Check if it is one of the sort markers
                ChecklistItem item = gridAdapter.getItem(position);
                if (item == null || item.isLearned() == -1)
                    return;

                // Figure out whether it is already learned or not
                if (item.isLearned() == 1) {
                    // Rather than updating the whole view, which is not very responsive and
                    // resets the scrollbar, just update the db and manually set the color here
                    dbHelper.setKanjiLearned(uid, item.getKanji(), false);
                    item.setLearned(0);
                    view.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                            cellBackground, null));
                    // Updating the numbers directly makes it more responsive
                    learned--;
                    updateTotal();
                }
                else {
                    dbHelper.setKanjiLearned(uid, item.getKanji(), true);
                    item.setLearned(1);
                    view.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                            R.color.colorLearned, null));
                    // Updating the numbers directly makes it more responsive
                    learned++;
                    updateTotal();

                }
            }
        });

        // kanjiGrid long click listener
        kanjiGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Check if it is one of the sort markers
                ChecklistItem item = gridAdapter.getItem(position);
                if (item == null || item.isLearned() == -1)
                    return false;

                showDialog(view, item);
                return true;
            }
        });

        // populate the spinner dropdown with choice elements
        Spinner spinSort = (Spinner) view.findViewById(R.id.spinSort);

        spinSort.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sortOptions, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinSort.setAdapter(adapter);

        // Initial numbers
        learned = dbHelper.getNumKanjiLearned(uid);
        total = dbHelper.getTotalNumKanji();

        updateTotal();

        return view;
    }

    // Search the checklist for kanji that match the pronounciation entered
    public ArrayList<ChecklistItem> searchChecklist(String searchText) {
        // Convert search (if possible) to both hiragana and katakana
        String katakana = "", hiragana = "";
        boolean isHiragana = false, isKatakana = false;
        if (JapaneseCharacter.isHiragana(searchText.charAt(0))) {
            isHiragana = true;
            hiragana = null;
        }
        else if (JapaneseCharacter.isKatakana(searchText.charAt(0))) {
            isKatakana = true;
            katakana = null;
        }

        for (int i = 0; i < searchText.length(); i++) {
            char c = searchText.charAt(i);

            if (isHiragana) {
                katakana = String.valueOf(JapaneseCharacter.toKatakana(c));
            } else if (isKatakana) {
                hiragana += String.valueOf(JapaneseCharacter.toHiragana(c));
            } else if (JapaneseCharacter.isRomaji(c)) {
                katakana += String.valueOf(JapaneseCharacter.toKatakana(c));
                hiragana += String.valueOf(JapaneseCharacter.toHiragana(c));
            }
        }

        return dbHelper.getKanjiSearchResults(katakana, hiragana, uid);
    }

    public void updateTotal() {
        // Set the txtTotal numbers
        txtTotal.setText(getResources().getString(R.string.txtTotal) + " " +
                String.valueOf(learned) + "/" + String.valueOf(total));
    }

    public void fillGridView(View view, final ArrayList<ChecklistItem> array) {
        gridAdapter = new ArrayAdapter<ChecklistItem>(view.getContext(),
                android.R.layout.simple_list_item_1, array) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(array.get(position).getKanji());
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setTextSize(20);

                int color; // Color of the cell

                // If not a kanji (-1), then make grey
                if (array.get(position).isLearned() == -1)
                    color = R.color.colorGrey;
                // Make green if learned
                else if (array.get(position).isLearned() == 1)
                    color = R.color.colorLearned;
                // Make (keep) transparent if not learned
                else
                    color = cellBackground;

                view.setBackgroundColor(ResourcesCompat.getColor(getResources(), color, null));

                return view;
            }
        };

        kanjiGrid.setAdapter(gridAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        fillGridView(this.getView(), dbHelper.getAllChecklistKanjiSorted(uid, position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void showDialog(View v, ChecklistItem item) {
        FragmentManager manager = getFragmentManager();
        KanjiInfoFragment kanjiInfoFragment = new KanjiInfoFragment();

        // Create the bundle to send the userId
        Bundle extras = new Bundle();
        extras.putString("kanji", item.getKanji());
        kanjiInfoFragment.setArguments(extras);

        kanjiInfoFragment.show(manager, "info");

    }


}
