package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.HearingTest.HearingTest;

public class frag3 extends Fragment implements View.OnClickListener{

    private InfoActivity infoActivity;
    private static final int RESULT_OK = 1;
    private Button info_bt, hearingtest_bt;
    private TextView txtResult;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private View view;
    Context ct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag3, container,false);

        //setContentView(R.layout.activity_main);

        txtResult = (TextView) view.findViewById(R.id.txtResult);
        hearingtest_bt = (Button) view.findViewById(R.id.HearingTest_bt);
        info_bt = (Button) view.findViewById(R.id.Info_bt);

        ct = container.getContext();

        hearingtest_bt.setOnClickListener(this);
        info_bt.setOnClickListener(this);


        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.HearingTest_bt:
                Intent intent = new Intent(getActivity(), HearingTest.class);
                startActivity(intent);
                break;

            case R.id.Info_bt:
                infoActivity = new InfoActivity(getActivity(),"다이어로그에 들어갈 내용입니다.");
                infoActivity.show();
                break;
        }




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==1){
            if(resultCode==RESULT_OK){ //데이터 받기
                String result = data.getStringExtra("result");
                txtResult.setText(result);
            }
        }
    }

}
