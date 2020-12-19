package com.example.discountapp;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public class Mechanism1 {
    static final String ALL_ITEMS = "ALL_Items";
    private static final String TAG = "okay_Mechanism1";
    ArrayList<String> states;
    Map<Integer, String> listBeaconMap;
    String currentState = ALL_ITEMS;
    Handler updateStates;
    Runnable runnable;

    public Mechanism1(Map<Integer, String> listBeaconMap) {
        this.listBeaconMap = listBeaconMap;
        this.states = new ArrayList<>();
        // we are going to maintain last three states
        this.states.add(ALL_ITEMS);
        this.states.add(ALL_ITEMS);
        this.states.add(ALL_ITEMS);
        this.states.add(ALL_ITEMS);
//        this.states.add(ALL_ITEMS);
//         every 3 seconds it will add All_items state if there is no add state function called
        updateStates = new Handler();
        runnable =  new Runnable() {
            @Override
            public void run() {
                AddState(states.get(states.size()-1));
                Log.d(TAG, "run: called, adding last added element");
            }
        };
        updateStates.postDelayed(runnable,3000);
    }

    public void AddState(String state){
        Log.d(TAG, "AddState: called with state=>"+state);
        // cancel the handler
        updateStates.removeCallbacks(runnable);
        states.remove(0);
        states.add(state);
        if (CheckForConsecutiveAreSame()) currentState = state;
        //start the handler
        updateStates.postDelayed(runnable,3000);
        // printing the states
        Log.d(TAG, "AddState: at the end states=>"+states);
    }

    private boolean CheckForConsecutiveAreSame() {
        // checking if all value on states are same;
        boolean isConsecutive = true;
        for(int i=1;i<states.size();i++){
            Log.d(TAG, "CheckForConsecutiveAreSame: "+(i-1)+"=>"+states.get(i-1)+" "+i+"=>"+states.get(i));
            if (!states.get(i-1).equals(states.get(i))) return false;
        }
        return isConsecutive;
    }


    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
}
