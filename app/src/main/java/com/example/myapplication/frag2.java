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
import android.widget.Toast;

import java.io.IOException;


public class frag2 extends Fragment implements View.OnClickListener{
    private Button playBtn, stopBtn, recordBtn;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private View view;
    Context ct;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_frag2, container,false);

        //setContentView(R.layout.activity_main);

        playBtn = (Button) view.findViewById(R.id.playBtn);
        stopBtn = (Button) view.findViewById(R.id.stopBtn);
        recordBtn = (Button) view.findViewById(R.id.recordBtn);

        ct = container.getContext();

        playBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        recordBtn.setOnClickListener(this);

        stopBtn.setEnabled(false);
        playBtn.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myAudioRecorder.setOutputFile(outputFile);
        // Inflate the layout for this fragment
        return view;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //출근버튼
            case R.id.recordBtn:
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                recordBtn.setEnabled(false);
                stopBtn.setEnabled(true);
                Toast.makeText(ct, "Recording started", Toast.LENGTH_LONG).show();
                break;
            case R.id.stopBtn:
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;
                recordBtn.setEnabled(false);
                stopBtn.setEnabled(false);
                playBtn.setEnabled(true);
                Toast.makeText(ct, "Audio Recorder successfully", Toast.LENGTH_LONG).show();
                break;
            //퇴근 버튼
            case R.id.playBtn:
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(ct, "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // make something
                }
                break;
        }
    }
}