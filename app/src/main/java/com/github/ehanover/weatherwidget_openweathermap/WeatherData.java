package com.github.ehanover.weatherwidget_openweathermap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherData {
    final String TAG = "ASDF";
    
    //static SimpleDateFormat sdfWeekly = new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH);
    static SimpleDateFormat sdfWeekly = new SimpleDateFormat("EEE M/d", Locale.ENGLISH);

    Date date;
    List<Double> temps; // consider making these floats to be more efficient?
    List<Double> clouds;
    List<Double> rains;
    List<Double> snows;
    List<String> conditions;
    List<String> iconNames;

    WeatherData(Date da, List<Double> te, List<Double> cl, List<Double> ra, List<Double> sn, List<String> co, List<String> ic) {
        this.date = da;
        this.temps = new ArrayList<>(te);
        this.clouds = new ArrayList<>(cl);
        this.rains = new ArrayList<>(ra);
        this.snows = new ArrayList<>(sn);
        this.conditions = new ArrayList<>(co);
        this.iconNames = new ArrayList<>(ic);
        //Log.d(TAG, "creating new WeatherData" + temps);
    }

    String getDate() {
        return sdfWeekly.format(date)+":";
    }

    String getTemp() {
        //return (int)(average(temps))+"°";
        return (int)getMax(temps) + "°/" + (int)getMin(temps) + "°";
    }

    String getCloud() {
        return (int)(average(clouds))+"%";
    }

    String getPrecip() {
        return (int)(average(rains) + average(snows))+" in.";
        //return (int)(average(rain)) + "/" + (int)(average(snow)) + " in.";
    }

//    String getMiddleCondition() {
//        return (conditions.get(conditions.size()/2)); // the middle condition
//    }

    int getMiddleIconR() {
        String name = (iconNames.get(iconNames.size()/2)); // the middle icon TODO: maybe add rain, clouds, etc. to pick the icon?
        return Widget1ServiceFactory.iconNamesToR.get(name);
    }

    public String toString() {
        return "WeatherData date=" + getDate();
    }

    static double average(List<Double> l) {
        double sum = 0;
        for(double t : l){
            sum += t;
        }
        return sum/((double)l.size());
    }
    
    static double getMax(List<Double> l) {
        double max = Double.MIN_VALUE;
        for(int i=0; i<l.size(); i++){
            if(l.get(i) > max){
                max = l.get(i);
            }
        }
        return max;
    }
    
    static double getMin(List<Double> l) {
        double min = Double.MAX_VALUE;
        for(int i=0; i<l.size(); i++){
            if(l.get(i) < min){
                min = l.get(i);
            }
        }
        return min;
    }

}
