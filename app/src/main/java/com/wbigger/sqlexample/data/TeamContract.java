package com.wbigger.sqlexample.data;

import android.provider.BaseColumns;

/**
 * Created by claudio on 5/11/17.
 */

public class TeamContract {

    public static final class TeamEntry implements BaseColumns {
        public static final String TABLE_NAME = "team";
        public static final String COLUMN_PLAYER_NAME = "playerName";
        public static final String COLUMN_PLAYER_NUMBER = "playerNumber";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
