package com.github.ehanover.weatherwidget_openweathermap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import top.defaults.drawabletoolbox.DrawableBuilder;

//https://github.com/commonsguy/cw-advandroid/blob/master/AppWidget/LoremWidget/src/com/commonsware/android/appwidget/lorem/LoremViewsFactory.java
public class Widget1ServiceFactory implements RemoteViewsService.RemoteViewsFactory {
    final String TAG = "ASDF";
    final String PACKAGE = "com.github.ehanover.weatherwidget_openweathermap";

    static SimpleDateFormat sdfDayCompare = new SimpleDateFormat("d", Locale.ENGLISH);
    static Map<String, Integer> iconNamesToR;

    private Context context;
    final int numItems = 5;
    WeatherData[] items = new WeatherData[numItems];

    Widget1ServiceFactory(Context context, Intent intent) {
        Log.d(TAG, "service factory constructor");
        this.context = context;
        //this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        iconNamesToR = new HashMap<String, Integer>();
        String[] icons = new String[]{"01d", "01n", "02d", "02n", "03d", "03n", "04d", "04n", "09d", "09n", "10d", "10n", "11d", "11n", "13d", "13n", "50d", "50n"}; // excludes night pictures
        //int[] rs = new int[]{R.drawable.w_01d, R.drawable.w_01n, R.drawable.w_02d, R.drawable.w_02n, R.drawable.w_03d, R.drawable.w_03n, R.drawable.w_04d, R.drawable.w_04n, R.drawable.w_09d, R.drawable.w_09n, R.drawable.w_10d, R.drawable.w_10n, R.drawable.w_11d, R.drawable.w_11n, R.drawable.w_13d, R.drawable.w_13n, R.drawable.w_50d, R.drawable.w_50n};
        int[] rs = new int[]{R.drawable.w_01d, R.drawable.w_01d, R.drawable.w_02d, R.drawable.w_02d, R.drawable.w_03d, R.drawable.w_03d, R.drawable.w_04d, R.drawable.w_04d, R.drawable.w_09d, R.drawable.w_09d, R.drawable.w_10d, R.drawable.w_10d, R.drawable.w_11d, R.drawable.w_11d, R.drawable.w_13d, R.drawable.w_13d, R.drawable.w_50d, R.drawable.w_50d};
        for(int i=0; i<rs.length; i++){
            iconNamesToR.put(icons[i], rs[i]);
        }

    }

    @Override
    public RemoteViews getViewAt(int position) {
        //Log.d("ASDF", "getViewAt update " + position + ". WD: " + items[position].getTemp());
        RemoteViews rvsRow = new RemoteViews(context.getPackageName(), R.layout.widget1_row);

        try {
            WeatherData wd = items[position];

            rvsRow.setTextViewText(R.id.row_textview_date, wd.getDate());
            rvsRow.setTextViewText(R.id.row_textview_temperature, wd.getTemp());
            rvsRow.setTextViewText(R.id.row_textview_cloud, wd.getCloud());
            rvsRow.setTextViewText(R.id.row_textview_precip, wd.getPrecip());
            //rvsRow.setTextViewText(R.id.row_textview_condition, wd.getCondition());
            rvsRow.setImageViewResource(R.id.row_imageview_condition, wd.getMiddleIconR());

            double weeksAvg = 0;//WeatherData.average(wd.temps);
            for (int i=0; i<numItems; i++) {
                weeksAvg += WeatherData.average(items[i].temps);
            }
            weeksAvg /= numItems;

            double weekMax = WeatherData.getMax(wd.temps);
            double weekMin = WeatherData.getMin(wd.temps);
            int maxTempDiff = 12;
            int colorL = Color.rgb(map(weekMax-weeksAvg, -maxTempDiff, maxTempDiff, 150, 255), 60, 65);
            int colorR = Color.rgb(50, 20, 255-map(weeksAvg-weekMin, -maxTempDiff, maxTempDiff, 0, 95)); // 255 to 160 backwards.
//            int colorL = Color.rgb(map((float) WeatherData.getMax(wd.temps), 40, 100, 125, 255), 60, 90); // red  0xfff6ee19, 0xff115ede
//            int colorR = Color.rgb(60, 10, map((float) WeatherData.getMin(wd.temps), 0, 50, 75, 255)); // blue

//            GradientDrawable gradient = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{colorL, colorR});
//            gradient.setSize(200, 200);
//            rvsRow.setImageViewBitmap(R.id.row_imageview_background, drawableToBitmap(gradient));

            int w = convertDpToPx((int) context.getResources().getDimension(R.dimen.widget1_row_background_width));
            int h = convertDpToPx((int) context.getResources().getDimension(R.dimen.widget1_row_height));
//            int w = (int) context.getResources().getDimension(R.dimen.widget1_row_background_width);
//            int h = (int) context.getResources().getDimension(R.dimen.widget1_row_height);

//            Drawable drawableBuilt = new DrawableBuilder().rectangle().rounded().solidColor(Color.CYAN).width(w).height(h).build(); // https://github.com/duanhong169/DrawableToolbox
            Drawable drawableBuilt = new DrawableBuilder().rectangle().rounded().gradient().linearGradient().angle(0).startColor(colorL).endColor(colorR).width(w).height(h).build();
            rvsRow.setImageViewBitmap(R.id.row_imageview_background, drawableToBitmap(drawableBuilt));

//            Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.id., null);
//            rvsRow.setImageViewBitmap(R.id.row_imageview_background, drawableToBitmap(drawable));

//            RoundedBitmapDrawable rbd = RoundedBitmapDrawableFactory.create(context.getResources(), drawableToBitmap(gradient));
//            rbd.setCircular(true);
//            rbd.setCornerRadius(100);
//            rvsRow.setImageViewBitmap(R.id.row_imageview_background, rbd.getBitmap());

        } catch(Exception e){
            Log.e(TAG, "getViewAtError: " + e);
        }

        return rvsRow;
    }


    @Override
    public void onDataSetChanged() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PACKAGE, Context.MODE_PRIVATE);
            int zip = prefs.getInt("zip", -1);
            String key = prefs.getString("key", "NO_KEY");

            Log.d(TAG, "onDataSetChanged start with zip=" + zip + " and key=" + key);

            String url = String.format("https://api.openweathermap.org/data/2.5/forecast?zip=%s,us&appid=%s&units=imperial", zip, key);
            String result = new JSONTask(url).execute().get(); // https://openweathermap.org/forecast5

            JSONObject data = new JSONObject(result);
            JSONArray list = data.getJSONArray("list");

            int currentDayIndex = 0;
            Date dateCurrent = null;

            List<Double> dTemps = new ArrayList<>();
            List<Double> dClouds = new ArrayList<>();
            List<Double> dRains = new ArrayList<>();
            List<Double> dSnows = new ArrayList<>();
            List<String> dConditions = new ArrayList<>();
            List<String> dIconNames = new ArrayList<>(); // other available data: wind, pressure, humidity

            for(int i=0; i<data.getInt("cnt"); i++){
                
                JSONObject day = list.getJSONObject(i);
                JSONObject main = day.getJSONObject("main");

                long dateSeconds = Integer.parseInt(day.getString("dt"));
                Date date = new Date(dateSeconds*1000);

                if(dateCurrent == null) // sets the first date
                    dateCurrent = date;

                if(isDateDifferent(dateCurrent, date)){ // we've finished gathering data for a week, so dump the data to items[] and reset for the next week
                    items[currentDayIndex] = new WeatherData(dateCurrent, dTemps, dClouds, dRains, dSnows, dConditions, dIconNames);
                    dateCurrent = date;
                    currentDayIndex++;

                    dTemps.clear();
                    dClouds.clear();
                    dRains.clear();
                    dSnows.clear();
                    dConditions.clear();
                }

                double temp = main.getDouble("temp");
                double cloud = (double) day.getJSONObject("clouds").getInt("all");
                String condition = day.getJSONArray("weather").getJSONObject(0).getString("description");
                String iconName = day.getJSONArray("weather").getJSONObject(0).getString("icon");
                double rain = tryParse(day, "rain"); // sometimes the json does not contain rain/snow data, so use special parsing function
                double snow = tryParse(day, "snow");

                dTemps.add(temp);
                dClouds.add(cloud);
                dRains.add(rain);
                dSnows.add(snow);
                dConditions.add(condition);
                dIconNames.add(iconName);
            }

        } catch(InterruptedException | ExecutionException | JSONException error) {
            Log.e(TAG, "onDataSetChanged error: " + error);
        }

    }

    private int convertDpToPx(int dp) {
        return Math.round(dp*(context.getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }

    static int map(double value, float istart, float istop, float ostart, float ostop) {
        int val = (int) ( ostart + (ostop - ostart) * ((value - istart) / (istop - istart)) );
        return Math.min( Math.max((int)ostart, val), 255);
    }

    static boolean isDateDifferent(Date a, Date b) {
        int da = Integer.parseInt(sdfDayCompare.format(a));
        int db = Integer.parseInt(sdfDayCompare.format(b));
        return (db != da);
    }

    static double tryParse(JSONObject day, String cat) {
        try {
            return day.getJSONObject(cat).getDouble("3h");
        } catch(JSONException e){
            //Log.d(TAG, "tryParse error but probably ok: " + e);
            return 0;
        }
    }

    static Bitmap drawableToBitmap (Drawable drawable) {
        //https://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    // Should these be overridden of they're not doing anything?
    @Override
    public void onCreate() {}

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
