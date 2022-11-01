package com.example.myapplication;

import static android.os.Environment.DIRECTORY_DCIM;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class VideoFragment extends Fragment implements View.OnClickListener{

    private InfoActivity infoActivity;
    private View view;
    private ImageView videoimageview;
    private ImageButton info_bt, videoenhancement_btn, videoamplification_btn, video_btn, videoload_btn,audiosaveandshare_btn;
    Context ct;
    VideoView videoview;

    private static String video = null;
    private static TextView textPath = null;
    AudioFromVideo mAudioFromVideo;
    //String VideoAudio = "/storage/emulated/0/Movies/temp.mp4";
    //String VideoAudio = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM) + "/Camera/Video.mp4";
    String VideoAudio = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM) + "/Video.mp4";
    String audio = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM) + "/output.pcm";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container,false);

        videoload_btn = (ImageButton)view.findViewById(R.id.VideoLoad_btn);
        video_btn = (ImageButton)view.findViewById(R.id.AudioRecord_btn);
        videoamplification_btn = (ImageButton)view.findViewById(R.id.VideoAmplification_btn);
        videoenhancement_btn = (ImageButton)view.findViewById(R.id.VideoEnhancement_btn);
        audiosaveandshare_btn = (ImageButton) view.findViewById(R.id.AudioSaveAndShare_btn);
        videoimageview = (ImageView) view.findViewById(R.id.VideoImageView);



        videoload_btn.setOnClickListener(this);
        video_btn.setOnClickListener(this);
        videoamplification_btn.setOnClickListener(this);
        videoenhancement_btn.setOnClickListener(this);
        audiosaveandshare_btn.setOnClickListener(this);

        ct = container.getContext();
        videoview = view.findViewById(R.id.ImageView);

        //비디오뷰 숨기고 이미지 출력
        videoview.setVisibility(View.INVISIBLE);
        return view;


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.Info_btn:
                infoActivity = new InfoActivity(getActivity(),"다이어로그에 들어갈 내용입니다.");
                infoActivity.show();
                break;


            case R.id.VideoLoad_btn: //비디오 불러오기
                //비디오뷰 출력
                videoimageview.setVisibility(View.INVISIBLE);
                videoview.setVisibility(View.VISIBLE);
                /*Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction("android.intent.action.GET_CONTENT");*/

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/*");

                this.startActivityForResult(intent, 101);
                break;

            case R.id.AudioRecord_btn:    //비디오 촬영
                Intent intent2 = new Intent(ct,VideoRecordActivity.class);
                startActivity(intent2);
                break;

            case R.id.VideoAmplification_btn: //비디오 음성 증폭
                break;

            case R.id.VideoEnhancement_btn: //비디오 잡음제거
                //Uri selectedMediaUri = data.getData();
                //szPath = getRealPathFromURI(mContext, selectedMediaUri);
                //video = FileUtils.getPath(ct, selectedMediaUri);
                Log.i(TAG, "비디오: "+video);
                if (video != null) {
                    mAudioFromVideo = new AudioFromVideo(video,audio,VideoAudio);
                    mAudioFromVideo.start();

                    /*MediaController mc = new MediaController((ct));
                    VideoView videoView = this.videoView;
                    videoView.setMediaController(mc);
                    videoView.setVideoPath(String.valueOf(video));
                    videoView.start();*/


                    //video = this.videoView;
                    //Intrinsics.checkNotNull(var10000);


                } else {
                    Toast.makeText(ct, "동영상을 먼저 불러와 주세요", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.AudioSaveAndShare_btn: //비디오 저장 및 공유유
                Intent intentsave = new Intent(Intent.ACTION_SEND);
                intentsave.setType("video/*");
                Uri screenshotUri = Uri.parse(VideoAudio);
                intentsave.putExtra(Intent.EXTRA_STREAM, screenshotUri);

                Intent shareIntent = Intent.createChooser(intentsave, "share");
                startActivity(shareIntent);
                break;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == -1) {
            MediaController mc = new MediaController((ct));
            VideoView videoView = this.videoview;
            videoView.setMediaController(mc);
            Uri fileUri = data.getData();
            video = FileUtils.getPath(ct, fileUri);
            //video = getRealPathFromURI(fileUri);
            //Intrinsics.checkNotNull(var10000);
            videoView.setVideoPath(String.valueOf(fileUri));
            //video = this.videoView;
            videoView.start();

        }
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


    public static String uri2path(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(0);
        Uri uri = Uri.fromFile(new File(path));

        cursor.close();
        return path;

    }

}
