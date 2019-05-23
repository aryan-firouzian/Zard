package com.experiment.zard;

// This code is licensed under MIT license
// Author, Aryan Firouzian
// Personal use without permission to share
// App contains audio files, which are taken from 50Languages website

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int random = 0;
    String filename = "";
    MediaPlayer mediaPlayer = new MediaPlayer();
    String directory = "1";
    boolean secondTrack = false;
    int counter = 0;
    float voiceVolume = 0;
    int end = 20;
    boolean repeat = false;

    Button button;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button_id);
        editText = (EditText) findViewById(R.id.plain_text_input);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    end = Integer.parseInt(editText.getText().toString());
                    playVoice();
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(!secondTrack&&!repeat){
                    try { Thread.sleep(5000); }
                    catch (InterruptedException ex) { }
                }
                playVoice();
            }
        });

    }

    private void playVoice(){
        int maxVolume = 100;

        if(counter<end){
            if(!repeat&&!secondTrack){
                directory = "1";
                random = new Random().nextInt(2000) + 1;
                filename = String.format("%04d", random);
                voiceVolume = (float) (Math.log(maxVolume - 70) / Math.log(maxVolume));
                secondTrack = true;
            }
            else if(!repeat&&secondTrack){
                directory = "2";
                voiceVolume = (float) (Math.log(maxVolume - 40) / Math.log(maxVolume));
                secondTrack = false;
                repeat = true;
            }
            else if(repeat&&!secondTrack){
                directory = "1";
                voiceVolume = (float) (Math.log(maxVolume - 70) / Math.log(maxVolume));
                secondTrack = true;
            }
            else{
                directory = "2";
                voiceVolume = (float) (Math.log(maxVolume - 40) / Math.log(maxVolume));
                secondTrack = false;
                repeat = false;

                counter ++;
            }

            try {
                AssetFileDescriptor a = getAssets().openFd( directory+"/"+ filename+".mp3");
                mediaPlayer.reset();
                mediaPlayer.setDataSource(a.getFileDescriptor(), a.getStartOffset(),a.getLength());
                mediaPlayer.setVolume(voiceVolume, voiceVolume);
                a.close();
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }
        else{
            this.finish();
            System.exit(0);
        }
    }
}
