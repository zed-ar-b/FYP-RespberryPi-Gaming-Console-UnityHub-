package com.unity3d.player;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class UnityPlayerActivity extends Activity implements IUnityPlayerLifecycleEvents {
    protected UnityPlayer mUnityPlayer;

    public void onUnityPlayerQuitted() {
    }

    /* access modifiers changed from: protected */
    public String updateUnityCommandLineArguments(String str) {
        return str;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        super.onCreate(bundle);
        getIntent().putExtra("unity", updateUnityCommandLineArguments(getIntent().getStringExtra("unity")));
        UnityPlayer unityPlayer = new UnityPlayer(this, this);
        this.mUnityPlayer = unityPlayer;
        setContentView(unityPlayer);
        this.mUnityPlayer.requestFocus();
    }

    public void onUnityPlayerUnloaded() {
        moveTaskToBack(true);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        this.mUnityPlayer.newIntent(intent);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mUnityPlayer.destroy();
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (MultiWindowSupport.getAllowResizableWindow(this)) {
            this.mUnityPlayer.pause();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (MultiWindowSupport.getAllowResizableWindow(this)) {
            this.mUnityPlayer.resume();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        MultiWindowSupport.saveMultiWindowMode(this);
        if (!MultiWindowSupport.getAllowResizableWindow(this)) {
            this.mUnityPlayer.pause();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!MultiWindowSupport.getAllowResizableWindow(this) || MultiWindowSupport.isMultiWindowModeChangedToTrue(this)) {
            this.mUnityPlayer.resume();
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.mUnityPlayer.lowMemory();
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        if (i == 15) {
            this.mUnityPlayer.lowMemory();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mUnityPlayer.configurationChanged(configuration);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        this.mUnityPlayer.windowFocusChanged(z);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 2) {
            return this.mUnityPlayer.injectEvent(keyEvent);
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return this.mUnityPlayer.injectEvent(keyEvent);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return this.mUnityPlayer.injectEvent(keyEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mUnityPlayer.injectEvent(motionEvent);
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        return this.mUnityPlayer.injectEvent(motionEvent);
    }
}
