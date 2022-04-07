package com.egortroyan.otpgenerator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class DeleteActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonYes;
    Button buttonNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_layout);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);

        buttonYes.setOnClickListener(this);
        buttonNo.setOnClickListener(this);


    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        Intent intent = getIntent();
        switch (view.getId()){
            case R.id.buttonYes:
                setResult(RESULT_OK,intent);
                break;
            case R.id.buttonNo:
                setResult(RESULT_CANCELED,intent);
                break;
        }
        finish();
    }

}