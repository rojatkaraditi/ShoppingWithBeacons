package com.example.discountapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "okay_mainActivity";
    ArrayList<Item> items =  new ArrayList<>();
    Gson gson =  new Gson();
    RecyclerView rv;
    TextView indicator,produce_tv,lifestyle_tv,grocery_tv;
    ItemAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv_in_main);
        indicator = findViewById(R.id.region_indicator);
        produce_tv = findViewById(R.id.tv_produce_in_main);
        lifestyle_tv = findViewById(R.id.tv_lifeStyle_in_main);
        grocery_tv = findViewById(R.id.tv_grocery_in_main);
//        layoutManager =  new LinearLayoutManager(this);
//        layoutManager = new GridLayoutManager(this,2);
        layoutManager =  new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(items,this);
        rv.setAdapter(adapter);

        new GetItems(null).execute();

        findViewById(R.id.lifesytle_btn).setOnClickListener(v -> {
            ShowIndicatorOn(lifestyle_tv);
            new GetItems(getString(R.string.life_style_tag)).execute();
        });

        findViewById(R.id.produce_btn).setOnClickListener(v -> {
            ShowIndicatorOn(produce_tv);
            new GetItems(getString(R.string.produce_tag)).execute();
        });

        findViewById(R.id.grocery_btn).setOnClickListener(v -> {
            ShowIndicatorOn(grocery_tv);
            new GetItems(getString(R.string.grocery_tag)).execute();
        });

    }

//    public void GetItems(View view) {
//        new GetItems(view.getTag().toString()).execute();
//    }

    class GetItems extends AsyncTask<Void,Void,Void> {
        String type = "";
        String result = "";
        boolean isSuccessful = true;

        public GetItems(String type) {
            if (type!=null) this.type = type;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            String url_str = getString(R.string.get_item_url);
            if (!type.isEmpty()) url_str += "region="+type;
            Request request = new Request.Builder()
                    .url(url_str)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                result = response.body().string();
                if (!response.isSuccessful()) isSuccessful = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "onPostExecute: result of getItems=>"+result);
            super.onPostExecute(aVoid);
            if (isSuccessful){
                try {
                    JSONObject root = new JSONObject(result);
                    JSONArray arr = root.getJSONArray("result");
                    adapter.notifyItemRangeRemoved(0,items.size());
                    items.clear();
                    for(int i=0; i<arr.length();i++){
                        Item item = gson.fromJson(arr.getString(i),Item.class);
                        items.add(item);
                        Log.d(TAG, "onPostExecute: item at "+i+"=>"+item);
                    }
                    adapter.notifyItemRangeInserted(0,items.size());
//                    Log.d(TAG, "onPostExecute: items=>"+items);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onPostExecute: error parsing from Json");
                }

            }else{

            }
        }
    }

    void ShowIndicatorOn(TextView region){
        if(indicator!=null){
            int length = region.getWidth();
            int height = region.getHeight();
            float x = region.getX();
            float y = region.getY();
            indicator.setWidth(length);
            Log.d(TAG, "ShowIndicatorOn: indicator width=>"+indicator.getWidth());
            indicator.animate().x(x-15).alpha((float) 1.0).start();
        }
    }

    void HideIndicator(){
        indicator.animate().alpha((float) 0.0).start();
    }

}