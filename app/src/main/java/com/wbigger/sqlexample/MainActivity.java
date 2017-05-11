package com.wbigger.sqlexample;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.wbigger.sqlexample.data.TeamContract;
import com.wbigger.sqlexample.data.TeamDbHelper;

public class MainActivity extends AppCompatActivity {

    private PlayerListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private EditText mNewPlayerNameEditText;
    private EditText mNewPlayerNumberEditText;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView playersRecyclerView;

        // Set local attributes to corresponding views
        playersRecyclerView = (RecyclerView) this.findViewById(R.id.all_players_list_view);
        mNewPlayerNameEditText = (EditText) this.findViewById(R.id.person_name_edit_text);
        mNewPlayerNumberEditText = (EditText) this.findViewById(R.id.player_number_edit_text);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        playersRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Create a DB helper (this will create the DB if run for the first time)
        TeamDbHelper dbHelper = new TeamDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding players
        mDb = dbHelper.getWritableDatabase();

        // Get all players info from the database and save in a cursor
        Cursor cursor = getAllPlayers();

        // Create an adapter for that cursor to display the data
        mAdapter = new PlayerListAdapter(this, cursor);

        // Link the adapter to the RecyclerView
        playersRecyclerView.setAdapter(mAdapter);


        // Create an item touch helper to handle swiping items off the list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //get the id of the item being swiped
                long id = (long) viewHolder.itemView.getTag();
                //remove from DB
                removePlayer(id);
                //update the list
                mAdapter.swapCursor(getAllPlayers());
            }

        }).attachToRecyclerView(playersRecyclerView);

    }

    /**
     * This method is called when user clicks on the Add to team button
     *
     * @param view The calling view (button)
     */
    public void addToTeam(View view) {
        if (mNewPlayerNameEditText.getText().length() == 0 ||
                mNewPlayerNumberEditText.getText().length() == 0) {
            return;
        }
        //default player number to 10
        int playerNumber = 10;
        try {
            //mNewPlayerNumberEditText inputType="number", so this should always work
            playerNumber = Integer.parseInt(mNewPlayerNumberEditText.getText().toString());
        } catch (NumberFormatException ex) {
            Log.e(LOG_TAG, "Failed to parse player number text to number: " + ex.getMessage());
        }

        // Add player info to mDb
        addNewPlayer(mNewPlayerNameEditText.getText().toString(), playerNumber);

        // Update the cursor in the adapter to trigger UI to display the new list
        mAdapter.swapCursor(getAllPlayers());

        //clear UI text fields
        mNewPlayerNumberEditText.clearFocus();
        mNewPlayerNameEditText.getText().clear();
        mNewPlayerNumberEditText.getText().clear();
    }



    /**
     * Query the mDb and get all players from the team table
     *
     * @return Cursor containing the list of players
     */
    private Cursor getAllPlayers() {
        return mDb.query(
                TeamContract.TeamEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TeamContract.TeamEntry.COLUMN_TIMESTAMP
        );
    }

    /**
     * Adds a new player to the mDb including the player number and the current timestamp
     *
     * @param name  Player's name
     * @param number Player number
     * @return id of new record added
     */
    private long addNewPlayer(String name, int number) {
        ContentValues cv = new ContentValues();
        cv.put(TeamContract.TeamEntry.COLUMN_PLAYER_NAME, name);
        cv.put(TeamContract.TeamEntry.COLUMN_PLAYER_NUMBER, number);
        return mDb.insert(TeamContract.TeamEntry.TABLE_NAME, null, cv);
    }


    /**
     * Removes the record with the specified id
     *
     * @param id the DB id to be removed
     * @return True: if removed successfully, False: if failed
     */
    private boolean removePlayer(long id) {
        return mDb.delete(TeamContract.TeamEntry.TABLE_NAME, TeamContract.TeamEntry._ID + "=" + id, null) > 0;
    }

}
