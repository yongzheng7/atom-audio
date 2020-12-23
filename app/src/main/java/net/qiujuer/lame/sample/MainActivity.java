package net.qiujuer.lame.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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

    protected boolean checkPremissions(int code, String... permissions) {
        boolean hasPremissions = true;
        for (String permission : permissions) {
            hasPremissions = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        if (!hasPremissions) {
            ActivityCompat.requestPermissions(this, permissions, code);
            return false;
        }
        return true;
    }

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
                if (checkPremissions(1024,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                Log.e("MainActivity", "onStartRecord " + time);
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
