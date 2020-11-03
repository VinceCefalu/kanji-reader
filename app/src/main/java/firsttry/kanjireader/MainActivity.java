package firsttry.kanjireader;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

//import org.liquidplayer.javascript.JSContext;

import firsttry.kanjireader.database.DBHelper;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int uid; // The id of the user currently "logged in"
    private DBHelper dbHelper;

    // variables for switching themes
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    private boolean useDarkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.AppThemeDark_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab the id from the LoginActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getInt("uid");
        }

        dbHelper = new DBHelper(getApplicationContext());

        // Start on text fragment
        SwitchToText();
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.actionText: {
                                SwitchToText();
                                return true;
                            }
                            case R.id.actionKanji: {
                                KanjiFragment frag = (KanjiFragment) getFragmentManager().findFragmentByTag("kanji");
                                if (frag == null || !frag.isVisible()) {
                                    SwitchToKanji();
                                }
                                return true;
                            }
                            case R.id.actionSettings: {
                                SettingsFragment frag = (SettingsFragment) getFragmentManager().findFragmentByTag("settings");
                                if (frag == null || !frag.isVisible()){
                                    SwitchToSettings();
                                }
                                return true;
                            }
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Re-open the database
        dbHelper = new DBHelper(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        dbHelper.close();
        super.onBackPressed();
    }

    public void SwitchToText(){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        TextFragment fragment = new TextFragment();

        // Create the bundle to send the userId
        Bundle extras = new Bundle();
        extras.putInt("uid", uid);
        fragment.setArguments(extras);

        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "text");
        fragmentTransaction.commit();

    }

    public void SwitchToKanji(){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        KanjiFragment fragment = new KanjiFragment();

        // Create the bundle to send the userId
        Bundle extras = new Bundle();
        extras.putInt("uid", uid);
        fragment.setArguments(extras);

        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "kanji");
        fragmentTransaction.commit();
    }

//    public void SwitchToDictionary(){
//        fragmentManager = getFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        DictionaryFragment fragment = new DictionaryFragment();
//
//        // Create the bundle to send the userId
//        Bundle extras = new Bundle();
//        extras.putInt("uid", uid);
//        fragment.setArguments(extras);
//
//        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "dictionary");
//        fragmentTransaction.commit();
//    }

    public void SwitchToSettings(){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "settings");
        fragmentTransaction.commit();
    }

    public void ToggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }

    public boolean getDark(){
        return useDarkTheme;
    }

}
