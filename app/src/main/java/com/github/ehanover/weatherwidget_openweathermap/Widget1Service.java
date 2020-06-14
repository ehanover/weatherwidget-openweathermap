package com.github.ehanover.weatherwidget_openweathermap;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class Widget1Service extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        Log.d(TAG, "onGetViewFactory");

        return new Widget1ServiceFactory(this.getApplicationContext(), intent);
    }


}
