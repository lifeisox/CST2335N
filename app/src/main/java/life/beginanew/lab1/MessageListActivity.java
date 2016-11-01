package life.beginanew.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import life.beginanew.lab1.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Messages. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MessageDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MessageListActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "ChatWindow";
    // Lab 4-4, add variables for the ListView, EditText
    ListView chatListView;
    EditText chatEditText;
    Button sendButton;
    ArrayList<String> chatMessage;
    // Lab 4-10
    ChatAdapter messageAdapter;
    // Lab 5-5
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.message_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Lab 7-6: copy and paste the code from ChatWindow.java -----------------------------------
        // Lab 5-5 it creates a temporary ChatDatabaseHelper object, which then gets a writeable
        // database and stores that as an instance variable.
        dbHelper = new ChatDatabaseHelper(MessageListActivity.this); // Step 5 of Lab 5
        db = dbHelper.getWritableDatabase(); // Step 5 of Lab 5

        // Lab 4-4, initialize these variables using findViewById() for each of your objects.
        chatMessage = new ArrayList<>();
        chatListView = (ListView) findViewById(R.id.chatListView);
        chatEditText = (EditText) findViewById(R.id.textInput);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMessage.add(chatEditText.getText().toString());
                // Lab 5-6 Modify the sendButton’s onClickListener callback function so that whenever
                //va message is added to the ArrayList, it also inserts the new message into the database.
                // Use a ContentValues object to put the new message.
                ContentValues contentValues = new ContentValues(); // Step 6 for Lab 5
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, chatEditText.getText().toString()); // Step 6 for Lab 5
                db.insert(ChatDatabaseHelper.TABLE_NAME, "", contentValues); // Step 6 for Lab 5

                // Lab 4-11: The last part is to update the listView whenever there is new data to display
                messageAdapter.notifyDataSetChanged(); // This restarts the process of getCount() / getView()
                chatEditText.setText("");
            }
        });

        // Lab 4-10: set the data source of the listView to be a new ChatAdapter object
        // in this case, “this” is the ChatWindow, which is-A Context object ChatAdapter
        messageAdapter = new ChatAdapter(this);
        chatListView.setAdapter(messageAdapter);

        // Lab 5-5 execute a query for any existing chat messages and add them into the ArrayList
        // of messages that was created in Lab 4.
        Cursor cursor;
        cursor = db.rawQuery(ChatDatabaseHelper.READALL_CHAT_TABLE, null);
        int messageIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            chatMessage.add(cursor.getString(messageIndex));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(messageIndex));
            cursor.moveToNext();
        }
        // Print an information message about the Cursor
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
        // Then use a for loop to print out the name of each column returned by the cursor.
        for (int colIndex = 0; colIndex < cursor.getColumnCount(); colIndex++) {
            Log.i(ACTIVITY_NAME, "Column name of " + colIndex + " = " + cursor.getColumnName(colIndex));
        }
        //------------------------------------------------------------------------------------------

    }

    // Lab 7-7: Also copy over the ChatAdapter inner class into the MessageListActivity class.
    // Lab4-5: Create an inner class of ChatWindow, called ChatAdapter, and it should extend ArrayAdapter<String>
    private class ChatAdapter extends ArrayAdapter<String> {

        // Lab 4-5: Create a constructor for ChatAdapter that takes a Context parameter,
        // and passes it to the parent constructor (ArrayAdapter).
        public ChatAdapter(Context context) {
            super(context, 0);
        }

        // Lab 4-6a: This returns the number of rows that will be in your listView.
        // In your case, it should be the number of strings in the array list object ( return list.size() ).
        public int getCount() {
            return chatMessage.size();
        }

        // Lab 4-6b: This returns the item to show in the list at the specified position: ( return list.get(position) )
        public String getItem(int position) {
            return chatMessage.get(position);
        }

        // Lab 4-6c: this returns the layout that will be positioned at the specified position in the list.
        public View getView(int position, View convertView, ViewGroup parent) {
            // Lab 4-9: Go to the function: getView(int position, View convertView, ViewGroup parent)
            // in ChatWindow.java and create a LayoutInflater object
            LayoutInflater inflater = MessageListActivity.this.getLayoutInflater();
            View result;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            TextView message = (TextView) result.findViewById(R.id.message_text);


            // Lab 7-9: Instead, create a variable outside the setOnClickListener() callback:
            // Lab 7-10: The message.setText(messageText) should replace message.setText(getItem(position));
            final String messageText = getItem(position);
            message.setText(messageText);
            // Lab 7-8: In the getView() function, add the line above the return statement:
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        // Lab 7-10: Now that the text for that row is stored in the variable messageText:
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, messageText);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        // Lab 7-10: Now that the text for that row is stored in the variable messageText:
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, messageText);

                        context.startActivity(intent);
                    }
                }
            });

            return result;
        }
    }
}
