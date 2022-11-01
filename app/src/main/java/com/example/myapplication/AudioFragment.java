package com.example.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;


public class AudioFragment extends Fragment implements View.OnClickListener{
    private ImageButton audioplayresults_btn,audiosaveandshare_btn,audioamplification_btn, audioenhancement_btn, audiorecord_btn,audiooriginalPlay_btn;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private View view;
    Context ct;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_audio, container,false);

        //setContentView(R.layout.activity_main);

        audioamplification_btn = (ImageButton) view.findViewById(R.id.AudioAmplification_btn);
        audioenhancement_btn = (ImageButton) view.findViewById(R.id.AudioEnhancement_btn);
        audiorecord_btn = (ImageButton) view.findViewById(R.id.AudioRecord_btn);
        audiooriginalPlay_btn = (ImageButton) view.findViewById(R.id.AudioOriginalPlay_btn);
        audiosaveandshare_btn = (ImageButton) view.findViewById(R.id.AudioSaveAndShare_btn);
        audioplayresults_btn = (ImageButton) view.findViewById(R.id.AudioPlayResults_btn);

        ct = container.getContext();

        audioamplification_btn.setOnClickListener(this);
        audioenhancement_btn.setOnClickListener(this);
        audiorecord_btn.setOnClickListener(this);
        audiooriginalPlay_btn.setOnClickListener(this);
        audiosaveandshare_btn.setOnClickListener(this);
        audioplayresults_btn.setOnClickListener(this);

        return view;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //음성증폭
            case R.id.AudioAmplification_btn:

                break;
            //음성향상
            case R.id.AudioEnhancement_btn:

                break;
            //오디오 녹음
            case R.id.AudioRecord_btn:

                break;
            //오디오 원본 재생
            case R.id.AudioOriginalPlay_btn:

                break;
            //오디오 저장및 공유
            case R.id.AudioSaveAndShare_btn:

                break;
            //오디오 음성향상본 실행
            case R.id.AudioPlayResults_btn:

                break;
        }
    }
}