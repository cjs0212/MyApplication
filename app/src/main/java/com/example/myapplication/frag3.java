package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.HearingText.HearingTest;

public class frag3 extends Fragment implements View.OnClickListener{


    private Button playBtn, stopBtn, hearingtest_bt;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private View view;
    Context ct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_frag3, container,false);

        //setContentView(R.layout.activity_main);

        hearingtest_bt = (Button) view.findViewById(R.id.HearingTest_bt);


        ct = container.getContext();

        hearingtest_bt.setOnClickListener(this);


        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.HearingTest_bt:
                Intent testIntent = new Intent(getActivity(), HearingTest.class);
                startActivity(testIntent);
                break;
        }


    }
}