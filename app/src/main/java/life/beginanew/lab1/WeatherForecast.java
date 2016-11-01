package life.beginanew.lab1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    // for displaying error message
    private static final String ACTIVITY_NAME = "WeatherForecast";
    // Lab 6-6 We will be using a web server to tell us what the weather is in Ottawa. The URL to use is:
    private static final String URL_STRING =
            "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
    private static final String URL_IMAGE = "http://openweathermap.org/img/w/";

    ImageView weatherImage;
    TextView temperature;
    TextView lowTemperature;
    TextView highTemperature;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weatherImage = (ImageView) findViewById(R.id.weather_icon);
        temperature = (TextView) findViewById(R.id.temperature);
        lowTemperature = (TextView) findViewById(R.id.temperature_low);
        highTemperature = (TextView) findViewById(R.id.temperature_high);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Lab6-4 In the onCreate() function, write code to set the progressBar’s visibility
        // to View.Visible so that the progress bar will show.
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);

        new ForecastQuery().execute(null, null, null);
    }

    // Create an inner class in WeatherForecast, called ForecastQuery,
    // which extends AsyncTask<String, Integer, String>.
    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        // The class should have 3 string variables for the min, max, and current temperature.
        // There should also be a Bitmap variable to store the picture for the current weather.
        private double temperatureValue; // <temperature value="13.76" min="12.78" max="14.44" unit="metric"/>
        private double temperatureMin;
        private double temperatureMax;
        private String iconFilename = null;
        private Bitmap weatherIcon;

        // Lab 6-7
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection;
            InputStream inputStream = null;
            URL url = null;

            try {
                // download URL
                url = new URL(URL_STRING);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000); // milliseconds
                connection.setConnectTimeout(15000); // milliseconds
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
                return null;
            }

            try {
                // feed xml parser
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);

                String tagName = null;
                String attribute = null;
                int count = 0;
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        Log.i(ACTIVITY_NAME, "Start document");
                    } else if (eventType == XmlPullParser.START_TAG) {
                        // Lab 6-8
                        tagName = parser.getName();
                        Log.i(ACTIVITY_NAME, "Start tag " + tagName);
                        if (tagName.equalsIgnoreCase("temperature")) {
                            temperatureValue = Double.parseDouble(parser.getAttributeValue(null, "value"));
                            Log.i(ACTIVITY_NAME, "Attribute value: " + parser.getAttributeValue(null, "value"));
                            // Lab 6-9
                            publishProgress(25);
                            temperatureMin = Double.parseDouble(parser.getAttributeValue(null, "min"));
                            Log.i(ACTIVITY_NAME, "Attribute min: " + parser.getAttributeValue(null, "min"));
                            publishProgress(50);
                            temperatureMax = Double.parseDouble(parser.getAttributeValue(null, "max"));
                            Log.i(ACTIVITY_NAME, "Attribute max: " + parser.getAttributeValue(null, "max"));
                            publishProgress(75);
                        } else if (tagName.equalsIgnoreCase("weather")) {
                            iconFilename = parser.getAttributeValue(null, "icon") + ".png";
                            File file = getFileStreamPath(iconFilename );
                            if (!file.exists()) {
                                findImage(iconFilename);
                            } else {
                                Log.i(ACTIVITY_NAME, "Saved icon, " + iconFilename + " is displayed.");
                                try {
                                    FileInputStream in = new FileInputStream(file);
                                    weatherIcon = BitmapFactory.decodeStream(in);
                                } catch (FileNotFoundException e) {
                                    Log.i(ACTIVITY_NAME, "Saved icon, " + iconFilename + " is not found.");
                                }
                            }
                            publishProgress(100);
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        Log.i(ACTIVITY_NAME, "End tag " + parser.getName());
                    } else if (eventType == XmlPullParser.TEXT) {
                        Log.i(ACTIVITY_NAME, "Text " + parser.getText());
                    }
                    eventType = parser.next();
                }
            } catch (XmlPullParserException | IOException e) {
                Log.i(ACTIVITY_NAME, "XmlPullParserException: " + e.getMessage());
            } finally {
                if (inputStream != null) {
                    try { inputStream.close(); } catch (IOException e) {}
                }
                connection.disconnect();
                return null;
            }
        }

        // Lab 6-10
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        // Lab 6-12
        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            temperature.setText(String.format("%.2f", temperatureValue));
            lowTemperature.setText(String.format("%.2f", temperatureMin));
            highTemperature.setText(String.format("%.2f", temperatureMax));
            weatherImage.setImageBitmap(weatherIcon);
            super.onPostExecute(s);
        }

        // Lab 6-11
        private void findImage(String fname) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) new URL(URL_IMAGE + fname).openConnection();
                connection.setReadTimeout(10000); // milliseconds
                connection.setConnectTimeout(15000); // milliseconds
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    weatherIcon = BitmapFactory.decodeStream(connection.getInputStream());
                    FileOutputStream outputStream = openFileOutput(fname , Context.MODE_PRIVATE);
                    weatherIcon.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.i(ACTIVITY_NAME, "Weather icon, " + fname + " is downloaded and displayed.");
                } else {
                    Log.i(ACTIVITY_NAME, "Can't connect to the weather icon for downloading.");
                }
            } catch (IOException e) {
                Log.i(ACTIVITY_NAME, "weather icon download error: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}
