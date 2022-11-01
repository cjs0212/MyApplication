package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class InfoActivity extends  Dialog {
    private TextView txt_contents;
    private Button shutdownClick;

    public InfoActivity(@NonNull Context context, String contents) {
        super(context);
        setContentView(R.layout.activity_info);

        txt_contents = findViewById(R.id.txt_contents);
        //txt_contents.setText(contents);
        shutdownClick = findViewById(R.id.btn_shutdown);
        shutdownClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}