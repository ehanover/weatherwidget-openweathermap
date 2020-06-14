package com.github.ehanover.weatherwidget_openweathermap;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    final String TAG = "ASDF";
    final String PACKAGE = "com.github.ehanover.weatherwidget_openweathermap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences(PACKAGE, Context.MODE_PRIVATE);

        EditText zip = findViewById(R.id.main_edittext_zip);
        String s = prefs.getInt("zip", -1) + "";
        zip.setText(s);
        zip.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {}
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                SharedPreferences prefs = getSharedPreferences(PACKAGE, Context.MODE_PRIVATE);
                try {
                    int val = Integer.parseInt(cs.toString());
                    prefs.edit().putInt("zip", val).apply();
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });

        EditText key = findViewById(R.id.main_edittext_key);
        String k = prefs.getString("key", "No key");
        key.setText(k);
        key.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {}
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                SharedPreferences prefs = getSharedPreferences(PACKAGE, Context.MODE_PRIVATE);
                try {
                    prefs.edit().putString("key", cs.toString()).apply();
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });

    }
}
