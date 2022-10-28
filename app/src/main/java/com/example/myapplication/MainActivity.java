package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    VideoView videoView;
    /*Button btn2;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*videoView = findViewById(R.id.videoView);*/
        /*btn2 = findViewById(R.id.btn2);*/
        bottomNavigationView = findViewById(R.id.bottomNavy);


        /*btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),VideoRecordActivity.class);
                startActivity(intent);
            }
        });*/

        //처음화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new frag1()).commit();

        //FrameLayout에 fragment.xml 띄우기
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.item_fragment1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new frag1()).commit();
                        break;
                    case R.id.item_fragment2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new frag2()).commit();
                        break;
                    case R.id.item_fragment3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new frag3()).commit();
                        break;
                    case R.id.item_fragment4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new frag4()).commit();
                        break;
                }
                return true;
            }
        });
    }





   /* public void bt1(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        this.startActivityIfNeeded(intent, 101);
    }*/

    /*public void bt2(View view) {
        Intent intent = new Intent(getApplicationContext(),VideoRecordActivity.class);
        startActivity(intent);

    }*/

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == -1) {
            MediaController mc = new MediaController((Context) this);
            VideoView video = this.videoView;

            //Intrinsics.checkNotNull(var10000);
            video.setMediaController(mc);

            //Intrinsics.checkNotNull(data);
            Uri fileUri = data.getData();

            video = this.videoView;
            //Intrinsics.checkNotNull(var10000);

            video.setVideoPath(String.valueOf(fileUri));
            video = this.videoView;
            //Intrinsics.checkNotNull(var10000);
            video.start();

        }
    }*/
}