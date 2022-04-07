package com.egortroyan.otpgenerator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    EditText editId;
    EditText editSeed;
    Button button;
    RadioGroup selector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editId = findViewById(R.id.editID);
        editSeed = findViewById(R.id.editTextTextMultiLine);
        button = findViewById(R.id.addButton);
        selector = findViewById(R.id.rgType);

        button.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if(editId.getText().toString().isEmpty() || editSeed.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(MainActivity2.this, "Поля не могут быть пустыми", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        Pass pass;
        String id = editId.getText().toString();
        String key = editSeed.getText().toString();
        if(selector.getCheckedRadioButtonId() == R.id.rbEventType){
            pass = new EPass(0, id, key);
        } else {
            pass = new TPass(id, key);
        }
        try {
            pass.getOTP();
        } catch (Exception e){
            Toast toast = Toast.makeText(MainActivity2.this, "Проверьте правильность заполнения полей", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        Gson gson = new Gson();
        String json;
        if(pass instanceof EPass){
            json = gson.toJson(pass, EPass.class);
            intent.putExtra("objectE", json);
        } else {
            json = gson.toJson(pass, TPass.class);
            intent.putExtra("objectT", json);
        }

        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}