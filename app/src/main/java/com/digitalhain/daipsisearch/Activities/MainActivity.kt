package com.digitalhain.daipsisearch.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.digitalhain.daipsisearch.R;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup) findViewById(R.id.groupradio);


        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);

                        switch (checkedId) {
                            case R.id.radia_eng:
                                Log.d("RADIO_BTN", "radia_eng Clicked");
                                break;
                            case R.id.radia_med:
                                Log.d("RADIO_BTN", "radia_med Clicked");
                                break;
                            case R.id.radia_comm:
                                Log.d("RADIO_BTN", "radia_comm Clicked");
                                break;
                            case R.id.radia_gvt_exam:
                                Log.d("RADIO_BTN", "radia_gvt_exam Clicked");
                                break;
                        }
                    }
                });
    }

    public void gotoNext(View view) {
        Intent intent = new Intent(MainActivity.this, searchedItemActivity.class);
        startActivity(intent);
    }
}