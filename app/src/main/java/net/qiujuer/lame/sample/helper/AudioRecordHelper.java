package net.qiujuer.lame.sample.helper;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.util.Log;

import net.qiujuer.lame.Lame;
import net.qiujuer.lame.LameAsyncEncoder;
import net.qiujuer.lame.LameOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class AudioRecordHelper {
    private static final String TAG = AudioRecordHelper.class.getSimpleName();
    private static final int[] SAMPLE_RATES = new int[]{44100, 22050, 11025, 8000};
    private RecordCallback callback;
    private File tmpFile;
    private int minShortBufferSize;
    private boolean isDone;
    private boolean isCancel;

    public AudioRecordHelper(File tmpFile, RecordCallback callback) {
        this.tmpFile = tmpFile;
        this.callback = callback;
    }

    private AudioRecord initAudioRecord() {
        for (int rate : SAMPLE_RATES) {
            for (short audioFormat : new short[]{AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT}) {
                for (short channelConfig : new short[]{AudioFormat.CHANNEL_IN_STEREO, AudioFormat.CHANNEL_IN_MONO}) {
                    try {
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);
                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);
                            Log.e("MainActivity", "instantiate and have a success " +recorder.getState() );
                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                // Because the bufferSize is byte units
                                // short = byte * 2
                                minShortBufferSize = bufferSize / 2;
                                return recorder;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MainActivity", rate + "Exception, keep trying.", e);
                    }
                }
            }
        }
        return null;
    }

    private File initTmpFile() {
        if (tmpFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            tmpFile.delete();
        }
        try {
            if (tmpFile.createNewFile())
                return tmpFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void recordAsync() {
        new Thread() {
            @Override
            public void run() {
                record();
            }
        }.start();
    }

    public File record() {
        isCancel = false;
        isDone = false;
        Log.e("MainActivity", " AudioRecordHelper record() 1");
        AudioRecord audioRecorder;
        File file;
        if ((audioRecorder = initAudioRecord()) == null
        ) {
            return null;
        }
        Log.e("MainActivity", " AudioRecordHelper record() 1.1");
        if ((file = initTmpFile()) == null
        ) {
            return null;
        }

        Log.e("MainActivity", " AudioRecordHelper record() 2");
        BufferedOutputStream outputStream;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        Log.e("MainActivity", " AudioRecordHelper record() 3");
        final long startTime = SystemClock.uptimeMillis();
        final int shortBufferSize = minShortBufferSize;
        final RecordCallback callback = this.callback;

        Lame lame = new Lame(audioRecorder.getSampleRate(), audioRecorder.getChannelCount(),
                audioRecorder.getSampleRate());
        LameOutputStream lameOutputStream = new LameOutputStream(lame, outputStream, shortBufferSize);
        LameAsyncEncoder lameAsyncEncoder = new LameAsyncEncoder(lameOutputStream, shortBufferSize);

        int readSize;
        long endTime;

        callback.onRecordStart();
        audioRecorder.startRecording();

        while (true) {
            short[] buffer = lameAsyncEncoder.getFreeBuffer();

            readSize = audioRecorder.read(buffer, 0, shortBufferSize);
            if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                lameAsyncEncoder.push(buffer, readSize);
            }

            endTime = SystemClock.uptimeMillis();
            callback.onProgress(endTime - startTime);

            if (isDone) {
                break;
            }
        }

        audioRecorder.stop();
        audioRecorder.release();

        // wait lame encoder done.
        lameAsyncEncoder.awaitEnd();

        if (!isCancel) {
            callback.onRecordDone(file, endTime - startTime);
        }

        return file;
    }

    public void stop(boolean isCancel) {
        this.isCancel = isCancel;
        this.isDone = true;
    }

    public interface RecordCallback {
        void onRecordStart();

        void onProgress(long time);

        void onRecordDone(File file, long time);
    }

}
