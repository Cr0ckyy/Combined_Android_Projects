package com.myapplicationdev.android.c347_l10_ps_enhanced_getting_my_location;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;

public class MusicService extends Service {

    MediaPlayer player = new MediaPlayer();

    // TODO: Empty Constructor is needed for Service
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    // execution of service will start on calling this method
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            File file = new File(
                    Environment
                            .getExternalStorageDirectory()
                            .getAbsolutePath() + "/MyFolder",
                    "Something Happened on the Way to Heaven.mp3");


            // specify the path of the audio file
            player.setDataSource(file.getPath());
            player.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // providing the boolean value as true to play the audio on loop
        player.setLooping(true);

        // starting the process
        player.start();

        // returns the status of the program
        return START_STICKY;
    }

    @Override
// execution of the service will stop on calling this method
    public void onDestroy() {
        super.onDestroy();

        // stopping the process
        player.stop();
    }
}