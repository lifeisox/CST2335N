package life.beginanew.lab1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        setContentView(R.layout.activity_start);

        // Lab 3-6 Store a reference to the Button by calling findViewById()
        Button onlyButton = (Button) findViewById(R.id.button);
        // Lab 3-6 Write the clickHandler
        onlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 5);
            }
        });

        // Lab 4-2 add a callback handler so that when you click on the Start Chat button,
        // it writes information to the debug window: Log.i(ACTIVITY_NAME, “User clicked Start Chat”);
        Button chatButton = (Button) findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                // Lab 4-12: Add code to start a new ChatWindow Activity when the button is clicked.
                // Lab 7-15: Instead of launching a ChatWindow activity, change it to MessageListActivity.class.
                Intent intent = new Intent(StartActivity.this, MessageListActivity.class);
                startActivity(intent);
            }
        });

        // Lab 6-2 Add a button to the StartActivity page, which should launch the WeatherForecast
        // activity when the user clicks on the button.
        Button weatherButton = (Button) findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Weather Button");
                Intent intent = new Intent(StartActivity.this, WeatherForecast.class);
                startActivity(intent);
            }
        });
    }

    // Lab 3-6 Write the function: onActivityResult...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5) {
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
            // Lab 3-11 Android will look in the "extra data" ...
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, getString(R.string.take_message) + data.getStringExtra("Response"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
