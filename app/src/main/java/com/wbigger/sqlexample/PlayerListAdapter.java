package com.wbigger.sqlexample;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbigger.sqlexample.data.TeamContract;

/**
 * Created by claudio on 5/11/17.
 */

class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {
    // Holds on to the cursor to display the team
    private Cursor mCursor;
    private Context mContext;

    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     * @param cursor the db cursor with team data to display
     */
    public PlayerListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.player_list_item, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        // Update the view holder with the information needed to display
        String name = mCursor.getString(mCursor.getColumnIndex(TeamContract.TeamEntry.COLUMN_PLAYER_NAME));
        int number = mCursor.getInt(mCursor.getColumnIndex(TeamContract.TeamEntry.COLUMN_PLAYER_NUMBER));
        long id = mCursor.getLong(mCursor.getColumnIndex(TeamContract.TeamEntry._ID));

        // Display the player name
        holder.nameTextView.setText(name);
        // Display the player number
        holder.playerNumberTextView.setText(String.valueOf(number));
        holder.itemView.setTag(id);
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class PlayerViewHolder extends RecyclerView.ViewHolder {

        // Will display the player name
        TextView nameTextView;
        // Will display the player number
        TextView playerNumberTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link PlayerViewHolder#onCreateViewHolder(ViewGroup, int)}
         */
        public PlayerViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            playerNumberTextView = (TextView) itemView.findViewById(R.id.player_number_text_view);
        }

    }
}
