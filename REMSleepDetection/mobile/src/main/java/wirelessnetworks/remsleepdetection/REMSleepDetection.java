package wirelessnetworks.remsleepdetection;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


public class REMSleepDetection extends Activity implements SensorEventListener {
    public final int ACCELEROMETER_DATA_LENGTH = 200;

    public AccelerometerData accelerometerData;
    public int accelerometerCount;
    Sensor accelerometer;
    SensorManager sm;
    TextView acceleration;
    TextView tds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       this.accelerometerCount = 0;
       this.accelerometerData = new AccelerometerData(ACCELEROMETER_DATA_LENGTH);

        // Check status of Google Play Services


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remsleep_detection);
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_NORMAL);



       acceleration=(TextView)findViewById(R.id.acceleration);
       tds = (TextView)findViewById(R.id.totalDifferenceFromStillness);
        /*Fitness.SensorsApi.findDataSources(client, new DataSourcesRequest.Builder()
                // At least one datatype must be specified.
                .setDataTypes(DataTypes.LOCATION_SAMPLE)
                        // Can specify whether data type is raw or derived.
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(DataSourcesResult dataSourcesResult) {
                        Log.v("tag", "Result: " + dataSourcesResult.getStatus().toString());
                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                            Log.v("tag", "Data source found: " + dataSource.toString());
                            Log.v("tag", "Data Source type: " + dataSource.getDataType().getName());

                            //Let's register a listener to receive Activity data!
                            /*if (dataSource.getDataType().equals(DataTypes.LOCATION_SAMPLE)
                                    && mListener == null) {
                                Log.i("tag", "Data source for LOCATION_SAMPLE found!  Registering.");
                                registerFitnessDataListener(dataSource, DataTypes.LOCATION_SAMPLE);
                            }
                        }
                    }
                });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.remsleep_detection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
       try {
           acceleration.setText("X: " + sensorEvent.values[0] + "\nY: " + sensorEvent.values[1] + "\nZ: " + sensorEvent.values[2]);
           accelerometerData.x[accelerometerCount] = sensorEvent.values[0];
           accelerometerData.y[accelerometerCount] = sensorEvent.values[1];
           accelerometerData.z[accelerometerCount] = sensorEvent.values[2];
           accelerometerCount++;
           //Log.v("tag", String.valueOf(accelerometerCount));
           if (accelerometerCount == ACCELEROMETER_DATA_LENGTH) {
               double totalDifferenceFromStillness= accelerometerData.classify();
               tds.setText("Total Difference From Stillness: " + totalDifferenceFromStillness);
               Log.v("IMPORTANTDATA", String.valueOf(totalDifferenceFromStillness));
               accelerometerCount = 0;
               try {
//                   FileOutputStream fos = openFileOutput("OutPutData",
//                           Context.MODE_APPEND);
//                   fos.write(String.valueOf(totalDifferenceFromStillness).getBytes());
//                   fos.close();
//
//                   String storageState = Environment.getExternalStorageState();
//                   if (storageState.equals(Environment.MEDIA_MOUNTED)) {
////                       File file = new File(getExternalFilesDir(null),
////                               "OutPutData");
//                       File file = new File("/Download/outputdata.txt");
//                       FileOutputStream fos2 = new FileOutputStream(file);
//                       fos2.write(String.valueOf(totalDifferenceFromStillness).getBytes());
//                       Log.v("tag", "Wrote to file");
//                       fos2.close();
//                   }


//                   OutputStreamWriter outputStream = new OutputStreamWriter(openFileOutput("output.txt", Context.MODE_APPEND));
//                   outputStream.write(String.valueOf(totalDifferenceFromStillness));
//                   outputStream.close();

                   String filename = "outputfile";
                   String string = String.valueOf(totalDifferenceFromStillness);
                   FileOutputStream outputStream;

                   try {
                       outputStream = openFileOutput(filename, Context.MODE_WORLD_READABLE);
                       outputStream.write(string.getBytes());

                       outputStream.close();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   FileInputStream in = openFileInput("outputfile");
                   InputStreamReader inputStreamReader = new InputStreamReader(in);
                   BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                   StringBuilder sb = new StringBuilder();
                   String line = bufferedReader.readLine();
                   //Log.v("DOES THIS WORK????", line);


               } catch (Exception e) {
                   e.printStackTrace();
               }

           }

       }
       catch(Exception e)
       {
           System.out.println(e);
       }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
