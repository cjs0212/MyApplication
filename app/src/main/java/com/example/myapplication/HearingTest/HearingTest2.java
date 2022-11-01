package com.example.myapplication.HearingTest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Timer;

public class HearingTest2 extends AppCompatActivity {
    Timer timer;
    MediaPlayer mediaPlayer;
    int i=0;
    SoundPool m_soundPool;
    int m_soundpool_id;

    ImageButton next_btn,end_btn,replay_btn;

    String[] Hz =new String[]{ "8000hz", "10000hz","12000hz","14080hz","14918hz","15805hz","16746hz","17742hz","18798hz","19916hz","21101hz","22357hz"};

    ArrayList<Integer> playlist;

    // 시작버튼
    Button startButton;
    //종료버튼
    Button stopButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearing_test2);

        // id 매핑
        TextView  hztext= (TextView)findViewById(R.id.hztext);
        next_btn = (ImageButton)findViewById(R.id.Hear_btn);
        end_btn = (ImageButton)findViewById(R.id.End_btn);
        replay_btn = (ImageButton)findViewById(R.id.Replay);

        Bundle bundle = getIntent().getExtras();
        int sound_settings = bundle.getInt("setting");

        m_soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC,0);

        //첫 사운드 듣기전 종료버튼과 다시듣기 버튼 비활성화
        replay_btn.setEnabled(false);
        end_btn.setEnabled(false);
        //mediaPlayer = MediaPlayer.create(HearingTest2.this,R.raw.hz8000);
        //mediaPlayer.start();
        playlist = new ArrayList<>();
        hztext.setText(Hz[i]);
        playlist.add(R.raw.hz8000);
        playlist.add(R.raw.hz10000);
        playlist.add(R.raw.hz12000);
        playlist.add(R.raw.hz14080);
        playlist.add(R.raw.hz14918);
        playlist.add(R.raw.hz15805);
        playlist.add(R.raw.hz16746);
        playlist.add(R.raw.hz17742);
        playlist.add(R.raw.hz18798);
        playlist.add(R.raw.hz19916);
        playlist.add(R.raw.hz21101);
        playlist.add(R.raw.hz22357);



        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replay_btn.setEnabled(true);
                end_btn.setEnabled(true);
                //마지막 인덱스까지 간 경우 결과 화면으로
                if(i == 12)
                {
                    Intent testIntent = new Intent(HearingTest2.this, HearingTest_End.class);
                    testIntent.putExtra("index_i",i);
                    startActivity(testIntent);
                    return;
                }

                m_soundpool_id = m_soundPool.load(HearingTest2.this,playlist.get(i),1);
                soundplay(sound_settings);
                //연타 방지를 위한 버튼 비활성화 2초지연
                btn_off();

                hztext.setText(Hz[i++]);
            }
        });

        replay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_soundpool_id = m_soundPool.load(HearingTest2.this,playlist.get(i-1),1);
                soundplay(sound_settings);
                //연타 방지를 위한 버튼 비활성화 2초지연
                btn_off();

            }
        });


        end_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_soundPool.release();
                Intent testIntent = new Intent(HearingTest2.this, HearingTest_End.class);
                testIntent.putExtra("index_i",i);
                startActivity(testIntent);

            }
        });

    }


    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        timer.cancel();
        super.onDestroy();
    }
    public void soundplay(int sound_settings){
        m_soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                // soundId, leftVolumn, rightVolumn, priority, loop, rate
                if(sound_settings == 0)
                    m_soundPool.play(m_soundpool_id,1,0,0,0,1);
                if(sound_settings == 1)
                    m_soundPool.play(m_soundpool_id,0,1,0,0,1);
                if(sound_settings == 2)
                    m_soundPool.play(m_soundpool_id,1,1,0,0,1);

            }
        });

    }

    public void btn_off(){

        next_btn.setClickable(false);
        replay_btn.setClickable(false);
        //2초 지연후 버튼 활성화
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                replay_btn.setClickable(true);
                next_btn.setClickable(true);
            }
        },2000);	//700밀리 초 동안 딜레이


    }
}