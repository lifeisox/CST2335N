package life.beginanew.lab1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    // Lab 4-4, add variables for the ListView, EditText
    ListView chatListView;
    EditText chatEditText;
    Button sendButton;
    ArrayList<String> chatMessage;
    // Lab 4-10
    ChatAdapter messageAdapter;
    // Lab 5-5
    //ChatDatabaseHelper dbHelper;
    //SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        // Lab 4-4, initialize these variables using findViewById() for each of your objects.
        chatMessage = new ArrayList<>();
        chatListView = (ListView) findViewById(R.id.chatListView);
        chatEditText = (EditText) findViewById(R.id.textInput);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMessage.add(chatEditText.getText().toString());
                /* Lab 5
                ContentValues contentValues = new ContentValues(); // Step 6 for Lab 5
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, chatEditText.getText().toString()); // Step 6 for Lab 5
                db.insert(ChatDatabaseHelper.CHAT_TABLE, "", contentValues); // Step 6 for Lab 5
                */
                // Lab 4-11: The last part is to update the listView whenever there is new data to display
                messageAdapter.notifyDataSetChanged(); // This restarts the process of getCount() / getView()
                chatEditText.setText("");
            }
        });

        // Lab 4-10: set the data source of the listView to be a new ChatAdapter object
        // in this case, “this” is the ChatWindow, which is-A Context object ChatAdapter
        messageAdapter = new ChatAdapter(this);
        chatListView.setAdapter(messageAdapter);

        /*
         // Step 5 of Lab 5
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
         */

    }

    /*
    // Step 8 for Lab 5
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            db.close();
            dbHelper.close();
        } catch (Exception e) {
        }
    }
     */

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
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }

}
