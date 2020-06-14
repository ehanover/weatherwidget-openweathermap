package com.github.ehanover.weatherwidget_openweathermap;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class JSONTask extends AsyncTask<String, Void, String> {
    final String TAG = "ASDF";
    
    private String urlString;

    public JSONTask(String url){
        this.urlString = url;
    }

    protected String doInBackground(String... text) { 
        // https://www.tanelikorri.com/tutorial/android/http-request-tutorial/
        try {
            //Log.d(TAG, "JSONTask doInBackground start");
            //URL url = new URL(text[0]);
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "JSONTask response code not OK");
                return "error";
            }

            InputStream is = connection.getInputStream();
            //String content = convertInputStream(is, "UTF-8");
            String content = convertInputStream(is);
            is.close();

            //Log.d(TAG, "JSONTask doInBackground finish");
            return content;
        } catch (IOException error) {
            Log.e(TAG, "JSONTask IOException error: " + error);
        }

        return "error";
    }

    protected void onPreExecute() {
        //Log.d("ASDF", "JSONTask onPreExecute");
    }

    protected void onPostExecute(String result) {
        //Log.d(TAG, "JSONTask onPostExecute");
    }

    private String convertInputStream(InputStream is) {
        Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

}


// Other attempts?
/*
    private static class WeatherDataTask extends AsyncTask<String, Void, String> {
        AppWidgetManager awm;
        RemoteViews rvs;
        int wid;

        WeatherDataTask(AppWidgetManager awm, RemoteViews rvs, int wid){
            this.awm = awm;
            this.rvs = rvs;
            this.wid = wid;
        }

        protected String doInBackground(String... text){ // https://www.tanelikorri.com/tutorial/android/http-request-tutorial/
            try {
                URL url = new URL(text[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();

                if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "response code is not good");
                }

                InputStream is = connection.getInputStream();
                String content = convertInputStream(is, "UTF-8");
                is.close();

                //Log.d(TAG, "doinbackground finished. result: " + content);

                return content;
            } catch (IOException error) {
                Log.e(TAG, "IOException error in WeatherDataTask: " + error);
            }

            return "error";
        }

        protected void onPreExecute(){
            Log.d(TAG, "pre execute asynctask");
        }

        protected void onPostExecute(String result) {
            try {
                //Log.d(TAG, "post execute asynctask. result: " + result);
                JSONArray data = new JSONObject(result).getJSONArray("list");

                for(int i=0; i<5; i++){
                    JSONObject day = data.getJSONObject(i);
                    JSONObject main = day.getJSONObject("main");

                    long dateSeconds = Integer.parseInt(day.getString("dt"));
                    Date date = new Date(dateSeconds*1000);

                    double temp = main.getDouble("temp");
                    int clouds = day.getJSONObject("clouds").getInt("all");
                    String condition = day.getJSONArray("weather").getJSONObject(0).getString("description");

                    //Log.d(TAG, "condition on " + i + ", " + sdfWeekly.format(date) + ": " + condition);

                    //String updateTime = "Last updated " + sdfUpdated.format(new Date());
                    //rvs.setTextViewText(R.id.textView_updatetime, updateTime);

                    //awm.updateAppWidget(wid, rvs);
                }



            } catch(JSONException error) {
                Log.e(TAG, "jsonexception in onpostexecute: " + error);
            }
        }

        private String convertInputStream(InputStream is, String encoding) {
            Scanner scanner = new Scanner(is, encoding).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }

    }
*/

/*
    public class AsyncTest extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... text){ // https://www.tanelikorri.com/tutorial/android/http-request-tutorial/
            Log.d("ASDF", "doInBackground start");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e){
                Log.e("ASDF", "DIB error: " + e);
            }

            return "data";
        }

        protected void onPreExecute(){
            //Log.d(TAG, "pre execute asynctask");
        }

        protected void onPostExecute(String result) {
            Random r = new Random();
            items = new String[]{"A"+r.nextInt(100), "B"+r.nextInt(100), "C"+r.nextInt(100)};
            running = false;
            //onDataSetChanged();
        }
    } */
