package com.helloworld.beacondemo;

import java.util.ArrayList;
import java.util.Map;

import android.os.Handler;
import android.util.Log;

public class Algo1 {
    private static final String ALL_ITEMS = "ALL_States";
    private static final String TAG = "okay_Algo1";
    ArrayList<String> states;
    Map<Integer, String> listBeaconMap;
    String currentState = ALL_ITEMS;
    Handler updateStates;
    Runnable runnable;

    public Algo1(Map<Integer, String> listBeaconMap) {
        this.listBeaconMap = listBeaconMap;
        this.states = new ArrayList<>();
        // we are going to maintain last five states
        this.states.add(ALL_ITEMS);
        this.states.add(ALL_ITEMS);
        this.states.add(ALL_ITEMS);
        this.states.add(ALL_ITEMS);
        // every 2 seconds it will add All_items state if there is no add state function called
        updateStates = new Handler();
        runnable =  new Runnable() {
            @Override
            public void run() {
                AddState(ALL_ITEMS);
                Log.d(TAG, "run: called");
            }
        };
        updateStates.postDelayed(runnable,2000);
    }

    public void AddState(String state){
        Log.d(TAG, "AddState: called with state=>"+state);
        // cancel the handler
        updateStates.removeCallbacks(runnable);
        states.remove(0);
        states.add(state);
        if (CheckForConsecutiveAreSame()) currentState = state;
        //start the handler
        updateStates.postDelayed(runnable,2000);
        // printing the states
        Log.d(TAG, "AddState: at the end states=>"+state);
    }

    private boolean CheckForConsecutiveAreSame() {
        // checking if all value on states are same;
        boolean isConsecutive = true;
        for(int i=1;i<states.size();i++){
            if (!states.get(i-1).equals(states.get(i))) return false;
        }
        return isConsecutive;
    }

    public String getCurrentState() {
        return currentState;
    }
}
