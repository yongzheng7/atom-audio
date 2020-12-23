package net.qiujuer.lame.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.qiujuer.lame.sample.helper.AudioPlayHelper;
import net.qiujuer.lame.sample.helper.AudioRecordHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private AudioRecordHelper mAudioRecordHelper;
    private AudioPlayHelper<Object> mAudioPlayHelper;
    private TextView mStatusText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusText = (TextView) findViewById(R.id.txt_status);

        Button record = (Button) findViewById(R.id.btn_record);
        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        onStartRecord();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        onCancelRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        onStopRecord();
                        break;
                }
                return true;
            }
        });

        File tmp = new File(getCacheDir(), "tmp.mp3");

        mAudioPlayHelper = new AudioPlayHelper<>(new AudioPlayHelper.RecordPlayListener<Object>() {
            @Override
            public void onPlayStart(Object o) {
                Log.e("MainActivity", "onPlayStart ");
            }

            @Override
            public void onPlayStop(Object o) {
                Log.e("MainActivity", "onPlayStop ");
            }

            @Override
            public void onPlayError(Object o) {
                Log.e("MainActivity", "onPlayError ");
                showStatus("Play error!!!");
            }
        });

        mAudioRecordHelper = new AudioRecordHelper(tmp, new AudioRecordHelper.RecordCallback() {
            @Override
            public void onRecordStart() {
                Log.e("MainActivity", "onRecordStart ");
                showStatus("Recording, release to play!");
            }

            @Override
            public void onProgress(long time) {
                Log.e("MainActivity", "onStartRecord "+time);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onRecordDone(final File file, final long time) {
                showStatus("Record done!\n\rFile size:" + file.length() + "B Time:" + time + "ms");
                mAudioPlayHelper.trigger(MainActivity.this, file.getAbsolutePath());
            }
        });
    }


    private void onStartRecord() {
        Log.e("MainActivity", "onStartRecord ");
        mAudioPlayHelper.stop();
        mAudioRecordHelper.recordAsync();
    }

    private void onStopRecord() {
        Log.e("MainActivity", "onStopRecord ");
        mAudioRecordHelper.stop(false);
    }

    private void onCancelRecord() {
        mAudioRecordHelper.stop(true);
    }


    private void showStatus(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusText.setText(str);
            }
        });
    }

}
