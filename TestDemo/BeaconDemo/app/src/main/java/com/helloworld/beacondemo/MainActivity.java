package com.helloworld.beacondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private BeaconRegion region;
    private static Map<String, List<String>> PLACES_BY_BEACONS;
    int count = 1;
    Logger logger;
    TextView nearest_tv,output_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logger =  new Logger(this);
        nearest_tv = findViewById(R.id.nearest_tv);
        output_tv = findViewById(R.id.output_tv);

        Map<Integer, String> listBeaconMap = new HashMap<>();
        listBeaconMap.put(49427,"B1");
        listBeaconMap.put(26535,"A100");
        listBeaconMap.put(7518,"D7");
        listBeaconMap.put(49357,"D8");
        beaconManager = new BeaconManager(this);
        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        findViewById(R.id.send_log_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.SendLogs();
            }
        });

        // add this below:
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Log.d("demo","Count is : "+count++);
                    logger.Log("demo","Count is : "+count++);
//                    Log.d("demo","---------------------");
                    String output = "";
                    for(Beacon beacon: list){
                        Log.d("demo",listBeaconMap.get(beacon.getMajor()) +" -> "+beacon.getRssi());
                        logger.Log("demo",listBeaconMap.get(beacon.getMajor()) +" -> "+beacon.getRssi());
                        output += listBeaconMap.get(beacon.getMajor()) +" -> "+beacon.getRssi()+"\n";
                    }
                    output_tv.setText(output);
                    Log.d("demo","---------------------");
                    Beacon nearestBeacon = list.get(0);
                    nearest_tv.setText(listBeaconMap.get(nearestBeacon.getMajor()));
                    Log.d("demo","Nearest Beacon" +" -> "+listBeaconMap.get(nearestBeacon.getMajor()));
                    logger.Log("demo","Nearest Beacon" +" -> "+listBeaconMap.get(nearestBeacon.getMajor()));
                    Log.d("demo","---------------------");
                    logger.Log("demo","---------------------");
                    List<String> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
//                    Log.d("Airport", "Nearest places: " + places);
                }
            }
        });


            Map<String, List<String>> placesByBeacons = new HashMap<>();
            placesByBeacons.put("B1", new ArrayList<String>() {{
                add("B1");
                // read as: "Heavenly Sandwiches" is closest
                // to the beacon with major 22504 and minor 48827
                add("Green & Green Salads");
                // "Green & Green Salads" is the next closest
                add("Mini Panini");
                // "Mini Panini" is the furthest away
            }});
            placesByBeacons.put("A100", new ArrayList<String>() {{
                add("A100");
                add("Green & Green Salads");
                add("Heavenly Sandwiches");
            }});
        placesByBeacons.put("D7", new ArrayList<String>() {{
            add("D7");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        placesByBeacons.put("D8", new ArrayList<String>() {{
            add("D8");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
            PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
        }


    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }

    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }

}