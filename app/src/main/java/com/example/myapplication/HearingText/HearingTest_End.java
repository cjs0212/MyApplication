package com.example.myapplication.HearingText;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class HearingTest_End extends AppCompatActivity {
    String[] age =new String[]{"75","50", "50","38","33","27","22","17","13","10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearing_test_end);

        TextView resulttext= (TextView)findViewById(R.id.ResultText);
        Button end = (Button)findViewById(R.id.End);

        Bundle bundle = getIntent().getExtras();
        int i = bundle.getInt("index_i");


        if(i <= 1) resulttext.setText("청각 장애가 의심됩니다.");
        else if( i > 1 && i < 10) resulttext.setText("청각나이는 "+ age[i] + "세 입니다");
        else if(i == 10) resulttext.setText("개의 청력을 가지셨습니다.");
        else if(i == 11) resulttext.setText("불가능");
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent testIntent = new Intent(HearingTest_End.this, MainActivity.class);
                startActivity(testIntent);

            }
        });
    }
}