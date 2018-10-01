package com.adam.app.demofloatwindowapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Messenger;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    public static final int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ask floating window permission
        if (!Settings.canDrawOverlays(this)) {
            askPermissions();
        }

    }

    /**
     * Start Demo when press the button
     * @param v
     */
    public void onStartDemo(View v) {
        Utils.inFo(this, "onStartDemo enter");
        // ask floating window permission
        if (!Settings.canDrawOverlays(this)) {
            askPermissions();
        } else {
            Utils.inFo(this, "start floating window");
            Intent intent = new Intent();
            FloatingViewService.startJobService(this, intent);
        }

    }

    private void askPermissions() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
        this.startActivityForResult(intent, REQUEST_PERMISSION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.inFo(this, "onActivityResult enter");
        if (requestCode == REQUEST_PERMISSION_CODE) {
            Utils.inFo(this, "resultCode = " + resultCode);
        }
    }
}
