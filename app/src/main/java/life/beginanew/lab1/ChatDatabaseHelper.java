package life.beginanew.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lifei on 10/5/2016.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Chats.db";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "tbl_chat";
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "message";

    public static final String CREATE_CHAT_TABLE = "CREATE TABLE " + TABLE_NAME
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MESSAGE + " TEXT NOT NULL)";

    public static final String DROP_CHAT_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String READALL_CHAT_TABLE = "SELECT " + KEY_ID + ", " + KEY_MESSAGE
            + " FROM " + TABLE_NAME;
    private static final String ACTIVITY_NAME = "ChatDatabaseHelper"; // Lab 5-7


    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHAT_TABLE);
        Log.i(ACTIVITY_NAME, "Calling onCreate"); // Lab 5-7
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_CHAT_TABLE);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVertion=" + oldVersion + " newVersion=" + newVersion); // Lab 5-7
    }
}
