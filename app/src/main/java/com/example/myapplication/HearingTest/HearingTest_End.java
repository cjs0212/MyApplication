package com.example.myapplication.HearingTest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class HearingTest_End extends AppCompatActivity {
    String[] age =new String[]{"75","60","50", "40","38","33","27","22","17","13","10","5","0"};
    PieChart chart1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearing_test_end);

        TextView resulttext= (TextView)findViewById(R.id.ResultText);
        Button end = (Button)findViewById(R.id.End);
        chart1 = (PieChart) findViewById(R.id.Chart);

        Bundle bundle = getIntent().getExtras();
        int i = bundle.getInt("index_i");

        i--;
        Log.d("", "onCreate: " + i);
        int age_int = Integer.parseInt(age[i]);


        chart1.getInnerValueUnit();
        chart1.clearChart();
        chart1.addPieSlice(new PieModel("건강 지수", 80 - age_int, Color.parseColor("#00b2ed")));
        chart1.addPieSlice(new PieModel("위험 지수", age_int, Color.parseColor("#ff3399")));
        chart1.startAnimation();


        if(i <= 1) resulttext.setText(age[i] +  "세 청각 장애가 의심됩니다.");
        else if( i > 1 && i < 10) resulttext.setText("청각나이는 "+ age[i] + "세 입니다");
        else if(i == 10) resulttext.setText("개의 청력을 가지셨습니다.");
        else if(i >= 11) resulttext.setText("안들리는거 알고 있습니다!");
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent testIntent = new Intent(HearingTest_End.this, MainActivity.class);
                startActivity(testIntent);

            }
        });
    }










}