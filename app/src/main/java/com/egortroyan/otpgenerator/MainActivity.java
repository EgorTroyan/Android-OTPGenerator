package com.egortroyan.otpgenerator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.egortroyan.otpgenerator.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;

    private List<EPass> ePassList;
    private List<TPass> tPassList;
    private ArrayList<Pass> passList;
    Type eTypeList = new TypeToken<List<EPass>>(){}.getType();
    Type tTypeList = new TypeToken<List<TPass>>(){}.getType();
    public static final String APP_PREFERENCES = "settings";
    private SharedPreferences mSettings;
    ImageButton button;
    ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        button = findViewById(R.id.fab);
        button.setOnClickListener(this);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ePassList = new ArrayList<>();
        tPassList = new ArrayList<>();
        passList = new ArrayList<>();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        listAdapter.clear();
        ePassList.clear();
        tPassList.clear();
        passList.clear();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mSettings.contains("allEPass")) {
            ePassList.clear();
            getPassListFromSettings(eTypeList);
        }
        if(mSettings.contains("allTPass")){
            tPassList.clear();
            getPassListFromSettings(tTypeList);
        }
        passList.addAll(ePassList);
        passList.addAll(tPassList);
        listAdapter = new ListAdapter(MainActivity.this, passList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener((adapterView, view, i, l) -> {

            TextView textView = view.findViewById(R.id.get_otp);
            textView.setText(passList.get(i).getOTP());
        });

        binding.listview.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
            intent.putExtra("position", String.valueOf(i));
            startActivityForResult(intent, 2);
            return false;
        });
    }

    private void saveListToSettings(){
        Gson gson = new Gson();
        ePassList.clear();
        tPassList.clear();
        for(Pass p : passList){
            if (p instanceof EPass){
                ePassList.add((EPass) p);
            } else if (p instanceof TPass) {
                tPassList.add((TPass) p);
            }
        }
        String jsonE = gson.toJson(ePassList, eTypeList);
        String jsonT = gson.toJson(tPassList, tTypeList);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("allEPass", jsonE);
        editor.putString("allTPass", jsonT);
        editor.apply();
    }

    private void getPassListFromSettings(Type type){
        Gson gson = new Gson();


        if(type.equals(eTypeList)){
            ePassList.clear();
            ePassList = gson.fromJson(mSettings.getString("allEPass",""), type);
        } else if (type.equals(tTypeList)) {
            tPassList.clear();
            tPassList = gson.fromJson(mSettings.getString("allTPass",""), type);
        }
    }
//
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab) {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        if (requestCode == 1) {

            String jsonE = data.getStringExtra("objectE");
            String jsonT = data.getStringExtra("objectT");

            Gson gson = new Gson();
            if (jsonE != null) {
                EPass pass = gson.fromJson(jsonE, EPass.class);
                ePassList.add(pass);
            }
            if (jsonT != null) {
                TPass pass = gson.fromJson(jsonT, TPass.class);
                tPassList.add(pass);
            }
            saveListToSettings();
        }
        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                int position = Integer.parseInt(data.getStringExtra("position"));
                Pass pass = listAdapter.getItem(position);
                listAdapter.remove(pass);
                passList.remove(pass);
                saveListToSettings();
                listAdapter.notifyDataSetChanged();
            }
        }
    }
}