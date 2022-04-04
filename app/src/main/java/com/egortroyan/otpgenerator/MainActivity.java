package com.egortroyan.otpgenerator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private EPass ePass;
    private TPass tPass;
    private EditText textView;
    private int mCounter;
    private final String keyE = "96539f95f5d1734d5c1adf9d06e7a47fd5d8614d";
    private final String keyT = "66792d996c512653085a589d9c30eadf8f53411febcbc11face12520d523d0c9";
    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Button getOTPEvent = findViewById(R.id.generate_button);
        Button getOTPTime = findViewById(R.id.generate_button_t);
        textView = findViewById(R.id.editTextNumber);
        Gson gson = new Gson();
        if (mSettings.contains("EPassObject")) {
            ePass = gson.fromJson(mSettings.getString("EPassObject",""), EPass.class);
        } else {
            ePass = new EPass(mCounter, keyE);
        }
        tPass = new TPass(keyT);
        getOTPEvent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "ResourceType"})
            @Override
            public void onClick(View view) {
                String s = ePass.getOTP();
                textView.setText(s);
            }
        });
        getOTPTime.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "ResourceType"})
            @Override
            public void onClick(View view) {
                String s = tPass.getOTP();
                textView.setText(s);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Gson gson = new Gson();
        String json = gson.toJson(ePass);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("EPassObject", json);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.contains("EPassObject")) {
            Gson gson = new Gson();
            ePass = gson.fromJson(mSettings.getString("EPassObject",""), EPass.class);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Gson gson = new Gson();
        String json = gson.toJson(ePass);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("EPassObject", json);
        editor.apply();
    }
}