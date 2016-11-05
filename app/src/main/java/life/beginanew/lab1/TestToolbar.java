package life.beginanew.lab1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    private static final String LOG = "TestToolbar";
    String snackbarMessage = "You selected item 1.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lab 8-8: Change the string message to something that you have written.
                Snackbar.make(view, "This is the Snackbar message.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // Lab 8-5: In the TestToolbar class, write the public boolean onCreateOptionsMenu (Menu m) function.
    // The purpose of this function is to create your toolbar by inflating it from your xml file:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu );
        return true;
    }

    // Lab 8-6: The last part is to respond to one of the items being selected. Write the method
    // public boolean onOptionsItemSelected(MenuItem mi).
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.menu_item_1:
                Log.d(LOG, "Dining menu selected.");
                // Lab 8-8: Set the case statements for your first menu item to display a Snackbar.
                Snackbar.make(findViewById(R.id.test_toolbar), snackbarMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.menu_item_2:
                Log.d(LOG, "Gas Station menu selected.");
                // Lab 8-9: you must add your own String resources for the Title, the Positive Button text,
                // and the Negative button text. The title should be “Do you want to go back?” and
                // if the user selects the positive button, then finish the current activity.
                // If they select the negative button then do nothing.
                // Set this to be the code for selecting on the second toolbar item.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Do you want to go back?");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.menu_item_3:
                Log.d(LOG, "Pharmacy menu selected.");
                // Lab 8-10: Use the custom layout given in the example, but download another free icon
                // from the website mentioned in step 4.
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.new_message);

                dialogBuilder.setPositiveButton(R.string.change_message, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        snackbarMessage = edt.getText().toString();
                        Snackbar.make(findViewById(android.R.id.content), "Message is changed.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                });
                dialogBuilder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        onBackPressed();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
                break;
            // Lab 8-7: make it show a Toast saying “Version 1.0, by Your name”,
            // with your actual name in the string.
            case R.id.menu_about:
                Toast.makeText(TestToolbar.this, "Version 1.0, by Byung Seon Kim", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
