package com.womandroid.we.chatSDK.core.audio;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import com.womandroid.we.chatSDK.core.session.ChatSDK;
import timber.log.Timber;

/**
 * Created by ben on 9/28/17.
 */

public class AudioRecorder {

    public static String AudioMessageDirectory = "AudioMessages" + File.separator;

    private static final AudioRecorder instance = new AudioRecorder();

    public static AudioRecorder shared () {
        return instance;
    }

    private MediaRecorder recorder;
    private long startTime = 0;

    public File record(String name) {

        stopRecording();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + AudioMessageDirectory + File.separator;

        File file = new File(path);
        file.mkdir();

        path += name;

        Timber.v("Recording to: " + path);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(path);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            recorder.prepare();
        } catch (IOException e) {
            ChatSDK.logError(e);
        }

        recorder.start();
        startTime = System.currentTimeMillis();

        return new File (path);
    }

    public int stopRecording() {
        long duration = duration();
        if(recorder != null) {
            try {
                recorder.stop();
            }
            catch (IllegalStateException e) {
                ChatSDK.logError(e);
            }
            recorder.release();
            recorder = null;
        }
        return (int) duration;
    }

    public long duration () {
        return System.currentTimeMillis() - startTime;
    }

}
