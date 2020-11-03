package firsttry.kanjireader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import firsttry.kanjireader.database.DBHelper;

public class LoginActivity extends Activity {

    private DBHelper dbHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(getApplicationContext());

        listView = (ListView)findViewById(R.id.listUsers);

        fillListView();

        // Handle the listView clicks
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String name = listView.getItemAtPosition(position).toString();

                // Check the database for the user string and open the next page with their data
                if (name.equals(getString(R.string.create_user))) {
                    // Open create user dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle(getString(R.string.create_user));

                    // Set up the input
                    final EditText input = new EditText(LoginActivity.this);

                    // Specify the type of input expected
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           final ProgressDialog progress;

                            progress = new ProgressDialog(LoginActivity.this);
                            progress.setTitle("Creating User...");
                            progress.setMessage("Please wait...");
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progress.setCanceledOnTouchOutside(false);
                            progress.setCancelable(false);
                            progress.show();

                            final String name = input.getText().toString();

                            // create the async task while the user is being created
                            new AsyncTask<Void, Void, Boolean>() {
                                protected Boolean doInBackground(Void... v) {
                                    dbHelper.insertUser(name);
                                    // Escape early if cancel() is called
                                    return !isCancelled();
                                }

                                @Override
                                protected void onPostExecute(Boolean aBoolean) {
                                    progress.dismiss();
                                    fillListView();
                                }
                            }.execute();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                else {
                    // Start the activity for the user selected
                    // passing the uid along to the MainActivity
                    int uid = dbHelper.getUidByName(name);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("uid", uid);
                    dbHelper.close();
                    startActivity(i);
                }
            }
        }); // end onClick

        // long click will give option to delete the selected user
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                final String name = listView.getItemAtPosition(position).toString();

                // Do not allow deletion of "Create New User" item
                if (name.equals(getResources().getString(R.string.create_user)))
                    return false; // Makes the long click act as a short click

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle(getString(R.string.delete_user));

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteUser(name);
                        fillListView();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Re-open the database
        dbHelper = new DBHelper(getApplicationContext());
    }

    private void fillListView() {

        ArrayList<String> users = dbHelper.getAllUserNames();
        Collections.sort(users);

        // Add the 'create user' user
        users.add(getString(R.string.create_user));

        // Create the adapter for the listView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, users);
        listView.setAdapter(adapter);

    }
}
