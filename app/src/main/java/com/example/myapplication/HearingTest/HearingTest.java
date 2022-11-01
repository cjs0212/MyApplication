package com.example.myapplication.HearingTest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.example.myapplication.R;

public class HearingTest extends AppCompatActivity {
    int setting = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearing_test);

        ImageFilterView TestStart_btn = (ImageFilterView)findViewById(R.id.TestStart_btn);


        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(radioGroupClickListener);


        TestStart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(HearingTest.this, HearingTest2.class);
                Intent.putExtra("setting",setting);
                startActivity(Intent);
            }
        });
    }

    RadioGroup.OnCheckedChangeListener radioGroupClickListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (i == R.id.radiobutton_left) {   //왼쪽 테스트 버튼
                setting = 0;
            } else if (i == R.id.radiobutton_right) { //오른쪽 테스트 버튼
                setting = 1;
            } else if (i == R.id.radiobutton){ //양쪽 테스트 버튼
                setting = 2;
            }
        }
    };
}