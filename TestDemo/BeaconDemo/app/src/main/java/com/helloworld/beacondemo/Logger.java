package com.helloworld.beacondemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Logger {
    static ArrayList<String> logs;
    static Activity activity;

    public Logger(Activity activity) {
        this.logs = new ArrayList<>();
        this.activity = activity;
    }

     void Log(String tag, String str){
        String s ="";
        Date d = new Date();
        s += d.toString() + "-->"+tag+"-->"+str;
        logs.add(s);
    }

     void SendLogs(){
        new SendLog(logs.toString()).execute();
    }

     class SendLog extends AsyncTask<Void, Void, Void> {
        String log;
        String result;
        boolean isSuccessful = true;
        public SendLog(String l) { this.log = l; }
        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            String url_str = "http://206.81.0.65:3000/api/v1/write";
            RequestBody formBody = new FormBody.Builder()
                    .add("str", this.log)
                    .build();
            Request request = new Request.Builder()
                    .url(url_str)
                    .post(formBody)
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
            super.onPostExecute(aVoid);
            if (isSuccessful){
                Toast.makeText(activity, "log sent, successful", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity, "log sent, failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
