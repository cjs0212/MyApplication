package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class frag1 extends Fragment implements View.OnClickListener{

    private View view;
    Context ct;
    VideoView videoView;
    private static String video = null;
    private static TextView textPath = null;
    AudioFromVideo mAudioFromVideo;
    String VideoAudio = "/storage/emulated/0/Movies/temp.mp4";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag1, container,false);

        Button btn1 = (Button)view.findViewById(R.id.btn1);
        Button btn2 = (Button)view.findViewById(R.id.btn2);
        Button btn3 = (Button)view.findViewById(R.id.btn3);
        Button btn4 = (Button)view.findViewById(R.id.btn4);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);


        ct = container.getContext();



        videoView = view.findViewById(R.id.videoView);






        return view;


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.btn1: //비디오 불러오기
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                this.startActivityForResult(intent, 101);
                break;

            case R.id.btn2:
                Intent intent2 = new Intent(ct,VideoRecordActivity.class);
                startActivity(intent2);
                break;

            case R.id.btn4: //비디오 잡음제거

                String audio = Environment.getExternalStorageDirectory() + "/testoutput2.pcm";
                //Uri selectedMediaUri = data.getData();
                //szPath = getRealPathFromURI(mContext, selectedMediaUri);
                //video = FileUtils.getPath(ct, selectedMediaUri);

                Log.i(TAG, "비디오: "+video);
                if (video != null) {
                    mAudioFromVideo = new AudioFromVideo(video,audio,VideoAudio);
                    mAudioFromVideo.start();
                        /*try {
                            PlayShortAudioFileViaAudioTrack(audio);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/

                    MediaController mc = new MediaController((ct));
                    VideoView videoView = this.videoView;

                    videoView.setMediaController(mc);





                    videoView.setVideoPath(String.valueOf(video));
                    //video = this.videoView;
                    //Intrinsics.checkNotNull(var10000);
                    videoView.start();



                } else {
                    Toast.makeText(ct, "동영상을 먼저 불러와 주세요", Toast.LENGTH_LONG).show();
                }





                /*Intent intent4 = new Intent( );
                intent4.setType("video/*");
                intent4.setAction("android.intent.action.GET_CONTENT");
                this.startActivityForResult(intent4, 100);*/


                break;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == -1) {
            MediaController mc = new MediaController((ct));
            VideoView videoView = this.videoView;


            videoView.setMediaController(mc);


            Uri fileUri = data.getData();
            //video = FileUtils.getPath(ct, fileUri);
            video = getRealPathFromURI(fileUri);

            //Intrinsics.checkNotNull(var10000);

            videoView.setVideoPath(String.valueOf(fileUri));
            //video = this.videoView;

            videoView.start();

        }

        /*if (requestCode == 100 && resultCode == -1)//잡음제거
        {
            String audio = Environment.getExternalStorageDirectory() + "/testoutput2.pcm";
            //Uri selectedMediaUri = data.getData();
            //szPath = getRealPathFromURI(mContext, selectedMediaUri);
            //video = FileUtils.getPath(ct, selectedMediaUri);

            Log.i(TAG, "비디오: "+video);
                    if (video != null) {
                        mAudioFromVideo = new AudioFromVideo(video,audio);
                        mAudioFromVideo.start();
                        *//*try {
                            PlayShortAudioFileViaAudioTrack(audio);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*//*
                    } else {
                        textPath.setText("Please select video file!");
                    }

        }*/
    }

    /*Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String audio = Environment.getExternalStorageDirectory() + "/output.pcm";
            try {
                PlayShortAudioFileViaAudioTrack(audio);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    Thread thread = new Thread(runnable);*/

    private void PlayShortAudioFileViaAudioTrack(String filePath) throws IOException
    {
// We keep temporarily filePath globally as we have only two sample sounds now..
        if (filePath==null)
            return;

//Reading the file..
        byte[] byteData = null;
        File file = null;
        file = new File(filePath); // for ex. path= "/sdcard/samplesound.pcm" or "/sdcard/samplesound.wav"
        byteData = new byte[(int) file.length()];
        FileInputStream in = null;
        try {
            in = new FileInputStream( file );
            in.read( byteData );
            in.close();

        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        int sampleRate = 44100;
        // AudioTrack(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode)
        AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);
        if (at!=null) {
            at.play();
// Write the byte array to the track
            at.write(byteData, 0, byteData.length);
            at.stop();
            at.release();
        }
        else
            Log.d("TCAudio", "audio track is not initialised ");

    }

    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }

        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = ct.getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

}