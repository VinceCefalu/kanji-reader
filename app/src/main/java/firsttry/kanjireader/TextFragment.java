package firsttry.kanjireader;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import java.util.ArrayList;

import firsttry.kanjireader.conversion.JapaneseCharacter;
import firsttry.kanjireader.database.ChecklistItem;
import firsttry.kanjireader.database.DBHelper;
import firsttry.kanjireader.database.Kanji;
import firsttry.kanjireader.database.Reading;

public class TextFragment extends Fragment {

    private int uid;
    private DBHelper dbHelper;
    private ListView listText;
    private WebView webView;
    private Scene listScene;
    private Scene webScene;
    private Scene kanjiScene;
    private Scene editScene;
    private EditText readingTitle;
    private EditText readingAuthor;
    private EditText readingPart;
    private EditText readingText;
    private GridView kanjiGrid;
    private int cellBackground;

    public TextFragment() {
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);

        // create the scenes used in this fragment
        listScene = Scene.getSceneForLayout((ViewGroup)view.findViewById(R.id.rootContainer), R.layout.layout_reading_list, getActivity().getApplicationContext());
        webScene = Scene.getSceneForLayout((ViewGroup)view.findViewById(R.id.rootContainer), R.layout.layout_web_view, getActivity().getApplicationContext());
        kanjiScene = Scene.getSceneForLayout((ViewGroup)view.findViewById(R.id.rootContainer), R.layout.fragment_kanji, getActivity().getApplicationContext());
        editScene = Scene.getSceneForLayout((ViewGroup)view.findViewById(R.id.rootContainer), R.layout.layout_edit_reading, getActivity().getApplicationContext());

        // default to the listScene
        listScene.enter();

        // create the database
        dbHelper = new DBHelper(view.getContext());

        // change the colors of the titles if it's in dark theme
        if (((MainActivity) getActivity()).getDark()) {
            // create the textviews for listScene
            TextView lblTitle = (TextView)listScene.getSceneRoot().findViewById(R.id.lblTitle);
            TextView lblAuthor = (TextView)listScene.getSceneRoot().findViewById(R.id.lblAuthor);
            TextView lblUnknown = (TextView)listScene.getSceneRoot().findViewById(R.id.lblUnknown);
            lblTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
            lblAuthor.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
            lblUnknown.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        }

        // populate listview
        listText = (ListView) listScene.getSceneRoot().findViewById(R.id.listText);

        listText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Reading reading = (Reading)listText.getItemAtPosition(i);

                if (reading.getTitle().equals(getString(R.string.NewReading))) {
                    // Open new reading dialog
                    Transition t = new Slide();
                    TransitionManager.go(editScene, t);

                    // apply dark theme to scene elements if needed
                    if (((MainActivity) getActivity()).getDark()) {
                        darkThemeEdit(view);
                    }

                    final Button btnSubmit = (Button) editScene.getSceneRoot().findViewById(R.id.btnSubmit);
                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(addReading(v)){
                                TransitionManager.go(listScene, new Slide());
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Error adding reading");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                     dialog.cancel();
                                    }
                                });
                            }
                        }
                    });
                }
                else { // Open the webView and build the html for the selected reading
                    Transition t = new Slide();
                    TransitionManager.go(webScene, t);
                    webView = (WebView) webScene.getSceneRoot().findViewById(R.id.webview);
                    if (((MainActivity) getActivity()).getDark()) {
                        webView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.materialDark, null));
                    }
                    WebSettings settings = webView.getSettings();
                    settings.setDefaultTextEncodingName("utf-8");
                    webView.loadData(buildHtml(uid, reading), "text/html; charset=utf-8", null);
                }
            }
        });

        // Long click will eventually bring up a dialog with some options
        // for details about kanji contained and option to delete
        listText.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                final int rid = ((Reading) listText.getItemAtPosition(position)).getRid();

                // If it is the Create new reading item
                if (rid == -1)
                    return true; // Makes the long click fail

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Reading Options");

                // Set up the buttons
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                CharSequence items[] = new CharSequence[] {"Kanji List", "Edit", "Remove"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            // opens kanji list for reading
                            case 0: {
                                // Transition to kanjiScene
                                Transition t = new Slide();
                                TransitionManager.go(kanjiScene, t);

                                // Grab the grid
                                kanjiGrid = (GridView)kanjiScene.getSceneRoot().findViewById(R.id.gridKanji);

                                // Move guideline
                                Guideline guideLine = (Guideline)kanjiScene.getSceneRoot().findViewById(R.id.guideGridTop);
                                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
                                params.guidePercent = 0.05f;
                                guideLine.setLayoutParams(params);

                                // Hide unused elements
                                kanjiScene.getSceneRoot().findViewById(R.id.txtSearch).setVisibility(View.INVISIBLE);
                                kanjiScene.getSceneRoot().findViewById(R.id.txtTotal).setVisibility(View.INVISIBLE);
                                kanjiScene.getSceneRoot().findViewById(R.id.txtSortBy).setVisibility(View.INVISIBLE);
                                kanjiScene.getSceneRoot().findViewById(R.id.spinSort).setVisibility(View.INVISIBLE);

                                // Fill the grid with readingContains kanji
                                fillKanjiGridView(view, dbHelper.getKanjiContainedForUser(rid, uid));
                                break;
                            }
                            // Opens the editing dialog
                            case 1: {

                                // transition to the editScene
                                Transition t = new Slide();
                                TransitionManager.go(editScene, t);

                                // IT REALLY HELPS WHEN YOU DO THIS AFTER THE SCENE IS CREATED
                                if (((MainActivity) getActivity()).getDark()){
                                    darkThemeEdit(view);
                                }

                                readingTitle = (EditText)editScene.getSceneRoot().findViewById(R.id.editTitle);
                                readingAuthor = (EditText)editScene.getSceneRoot().findViewById(R.id.editAuthor);
                                readingPart = (EditText)editScene.getSceneRoot().findViewById(R.id.editPart);
                                readingText = (EditText)editScene.getSceneRoot().findViewById(R.id.editReading);

                                Reading reading = (Reading)listText.getItemAtPosition(position);

                                // populate the edit texts with db values
                                readingTitle.setText(reading.getTitle());
                                readingAuthor.setText(reading.getAuthor());
                                readingPart.setText(reading.getPart());
                                readingText.setText(dbHelper.getReadingText(reading.getRid()));

                                final Button btnSubmit = (Button) editScene.getSceneRoot().findViewById(R.id.btnSubmit);
                                btnSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(true){ // TODO: Fix this
                                            TransitionManager.go(listScene, new Slide());
                                        }
                                        else{
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setTitle("Error editing reading");

                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                        }
                                    }
                                });

                                break;
                            }
                            // remove text from the database
                            case 2: {
                                // Make sure the user really wants to remove it
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Confirm removal?");

                                // Set up the buttons
                                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbHelper.deleteReading(rid);
                                        fillReadingListView(view, dbHelper.getAllReadings());
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                                break;
                            }
                        }
                    }
                });
                builder.show();

                return true;
            }
        });
        fillReadingListView(view, dbHelper.getAllReadings());

        return view;
    }

    public void fillKanjiGridView(View view, final ArrayList<ChecklistItem> array) {
        ArrayAdapter <ChecklistItem> gridAdapter = new ArrayAdapter<ChecklistItem>(view.getContext(),
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

    public void fillReadingListView(View view, final ArrayList<Reading> readings) {
        readings.add(new Reading(-1, getString(R.string.NewReading), "", ""));
        ArrayAdapter<Reading> adapter1 = new ArrayAdapter<Reading>(view.getContext(),
                android.R.layout.simple_list_item_1, readings) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater)getContext().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.list_item, null);
                int textSize = 20;

                // If it is create reading item
                if (readings.get(position).getTitle().equals(getString(R.string.NewReading))) {
                    TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
                    txtTitle.setText(readings.get(position).getTitle());
                    txtTitle.setTextSize(textSize);

                    // Set the rest to empty strings
                    TextView txtPart = (TextView) view.findViewById(R.id.txtPart);
                    txtPart.setText("");

                    TextView txtAuthor = (TextView) view.findViewById(R.id.txtAuthor);
                    txtAuthor.setText("");

                    TextView txtUnknownKanji = (TextView) view.findViewById(R.id.txtUnknownKanji);
                    txtUnknownKanji.setText("");
                }
                else { // if it a normal reading item
                    TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
                    txtTitle.setText(readings.get(position).getTitle());
                    txtTitle.setTextSize(textSize);

                    TextView txtPart = (TextView) view.findViewById(R.id.txtPart);
                    txtPart.setText(readings.get(position).getPart());
                    txtPart.setTextSize(12);

                    TextView txtAuthor = (TextView) view.findViewById(R.id.txtAuthor);
                    txtAuthor.setText(readings.get(position).getAuthor());
                    txtAuthor.setTextSize(textSize-2);

                    // Color the text of the unknown kanji number depending on how big it is
                    TextView txtUnknownKanji = (TextView) view.findViewById(R.id.txtUnknownKanji);
                    int numUnknown = dbHelper.getNumKanjiUnknown(uid, readings.get(position).getRid());
                    txtUnknownKanji.setText(String.valueOf(numUnknown));
                    int color;
                    if (numUnknown > 15)
                        color = R.color.colorRed;
                    else if (numUnknown > 10)
                        color = R.color.colorYellow;
                    else
                        color = R.color.colorGreen;
                    txtUnknownKanji.setTextColor(ResourcesCompat.getColor(getResources(), color, null));
                    txtUnknownKanji.setTextSize(textSize + 5);
                }

                return view;
            }
        };
        listText.setAdapter(adapter1);
    }

    public void darkThemeEdit(View view){
        // create the elements to dark
        // this way of doing it probably sucks but I can't think of another way how to do it by the presentation tomorrow

        Button btnSubmit = (Button)editScene.getSceneRoot().findViewById(R.id.btnSubmit);

        TextView lblTitle = (TextView)editScene.getSceneRoot().findViewById(R.id.lblTitle);
        TextView lblAuthor = (TextView)editScene.getSceneRoot().findViewById(R.id.lblAuthor);
        TextView lblPart = (TextView)editScene.getSceneRoot().findViewById(R.id.lblPart);
        TextView lblText = (TextView)editScene.getSceneRoot().findViewById(R.id.lblText);

        EditText readingTitle = (EditText)editScene.getSceneRoot().findViewById(R.id.editTitle);
        EditText readingAuthor = (EditText)editScene.getSceneRoot().findViewById(R.id.editAuthor);
        EditText readingPart = (EditText)editScene.getSceneRoot().findViewById(R.id.editPart);
        EditText readingText = (EditText)editScene.getSceneRoot().findViewById(R.id.editReading);

        btnSubmit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));

        lblTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        lblAuthor.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        lblPart.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        lblText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));

        readingTitle.setHintTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        readingTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        readingAuthor.setHintTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        readingAuthor.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        readingPart.setHintTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        readingPart.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        readingText.setHintTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));
        readingText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrey, null));

    }

    // Create the html view for the text adding furigana as necessary based on known kanji
    public String buildHtml(int uid, Reading reading) {
        // Starting tags
        String html = "<!doctype html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"utf-8\">";

        // Title
        html += "<title>" + reading.getTitle() + "</title>";

        // Author
        html += "<meta name=\"author\" content=\"" + reading.getAuthor() + "\">";

        // End head tag, start body with title, author, and part (if exists)
        // also apply dark theme if needed
        if (((MainActivity) getActivity()).getDark()) { // Uses R.color.colorGrey
            html += "</head><body style=\"font-size: 150%;line-height: 50px; color: #B3B3B3\"><h1>" + reading.getTitle() + "</h1><h5>" + reading.getAuthor() + "</h5>";
        }
        else{
            html += "</head><body style=\"font-size: 150%;line-height: 50px;\"><h1>" + reading.getTitle() + "</h1><h5>" + reading.getAuthor() + "</h5>";
        }

        // Part may not exist
        if (reading.getPart() != null) {
            html += "<h3>" + reading.getPart() + "</h3><br>";
        }

        // Put the text in the body, adding in furigana when necessary
        String text = dbHelper.getReadingText(reading.getRid());
        String lines[] = text.split("\\r?\\n");
        final String rubyOpenTag = "<ruby>";
        final String rubyCloseTag = "</ruby>";
        final String rtOpenTag = "<rt>";
        final String rtCloseTag = "</rt>";

        // Create the tokenizer (kuromoji) to parse the sentences
        Tokenizer tokenizer = Tokenizer.builder().build();
        ArrayList<String> unknown = dbHelper.getKanjiNotLearned(uid);

        // Tokenize the text and then check each word for unknown kanji
        // Insert the token readings when at least one kanji in word is unknown
        for (String line : lines) {
            for (Token token : tokenizer.tokenize(line)) {
                boolean needsReading = false;

                // For each token, inspect the characters
                String t = token.getSurfaceForm();
                for (int i = 0; i < t.length(); i++) {

                    char c = t.charAt(i);
                    // If not kanji move to next token
                    if (JapaneseCharacter.isKanji(c)) {
                        // Search for each kanji in unknown (list). If there is one unknown,
                        // then add readings to the html
                        if (unknown.contains(String.valueOf(c))) {
                            needsReading = true;
                            break;
                        }
                    }
                } // End for

                // Add the reading if necessary
                if (needsReading) {
                    // Convert the reading to hiragana
                    String tokenReading = token.getReading();
                    if (tokenReading != null) {
                        String newReading = "";
                        for (int i = 0; i < tokenReading.length(); i++) {
                            char c = tokenReading.charAt(i);
                            newReading += JapaneseCharacter.toHiragana(c);
                        }

                        // Check for okurigana
                        int okuriganaCount = 0;
                        for (int k = t.length() - 1; k >= 0; k--) {
                            char c = t.charAt(k);
                            if (JapaneseCharacter.isHiragana(c) || JapaneseCharacter.isKatakana(c)) {
                                okuriganaCount++;
                            }
                            else if (JapaneseCharacter.isKanji(c)) {
                                break; // Break at the first kanji
                            }
                        }

                        // If okurigana exists, chop it off the reading, because it is redundant and confusing
                        if (okuriganaCount != 0) {
                            newReading = newReading.substring(0, newReading.length() - okuriganaCount);
                        }

                        html += rubyOpenTag + token.getSurfaceForm() + rtOpenTag + newReading + rtCloseTag + rubyCloseTag;
                    }
                }
                else {
                    // Just add the token
                    html += token.getSurfaceForm();
                }

            } // End for
            html += "<br>";
        } // End for

        // Add closing tags for body + html
        html += "</body></html>";

        return html;
    }

    public boolean addReading(View view){

        // get the text from the EditTexts
        // create the EditTexts for the reading input
        readingTitle = (EditText)editScene.getSceneRoot().findViewById(R.id.editTitle);
        readingAuthor = (EditText)editScene.getSceneRoot().findViewById(R.id.editAuthor);
        readingPart = (EditText)editScene.getSceneRoot().findViewById(R.id.editPart);
        readingText = (EditText)editScene.getSceneRoot().findViewById(R.id.editReading);

        String title = readingTitle.getText().toString();
        String author = readingAuthor.getText().toString();
        String part = readingPart.getText().toString();
        String text = readingText.getText().toString();

        return dbHelper.insertReading(title, author, part, text);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
