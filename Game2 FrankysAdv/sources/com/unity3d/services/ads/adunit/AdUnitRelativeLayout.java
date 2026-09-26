package com.unity3d.services.ads.adunit;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Iterator;

public class AdUnitRelativeLayout extends RelativeLayout {
    private int _maxEvents = 10000;
    private final ArrayList<AdUnitMotionEvent> _motionEvents = new ArrayList<>();
    private boolean _shouldCapture = false;

    public AdUnitRelativeLayout(Context context) {
        super(context);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        super.onInterceptTouchEvent(motionEvent);
        if (!this._shouldCapture || this._motionEvents.size() >= this._maxEvents) {
            return false;
        }
        boolean z = (motionEvent.getFlags() & 1) != 0;
        synchronized (this._motionEvents) {
            ArrayList<AdUnitMotionEvent> arrayList = this._motionEvents;
            AdUnitMotionEvent adUnitMotionEvent = r5;
            AdUnitMotionEvent adUnitMotionEvent2 = new AdUnitMotionEvent(motionEvent.getActionMasked(), z, motionEvent2.getToolType(0), motionEvent.getSource(), motionEvent.getDeviceId(), motionEvent2.getX(0), motionEvent2.getY(0), motionEvent.getEventTime(), motionEvent2.getPressure(0), motionEvent2.getSize(0));
            arrayList.add(adUnitMotionEvent);
        }
        return false;
    }

    public void startCapture(int i) {
        this._maxEvents = i;
        this._shouldCapture = true;
    }

    public void endCapture() {
        this._shouldCapture = false;
    }

    public void clearCapture() {
        synchronized (this._motionEvents) {
            this._motionEvents.clear();
        }
    }

    public int getMaxEventCount() {
        return this._maxEvents;
    }

    public int getCurrentEventCount() {
        int size;
        synchronized (this._motionEvents) {
            size = this._motionEvents.size();
        }
        return size;
    }

    public SparseArray<SparseArray<AdUnitMotionEvent>> getEvents(SparseArray<ArrayList<Integer>> sparseArray) {
        SparseIntArray sparseIntArray = new SparseIntArray();
        SparseArray<SparseArray<AdUnitMotionEvent>> sparseArray2 = new SparseArray<>();
        synchronized (this._motionEvents) {
            Iterator<AdUnitMotionEvent> it = this._motionEvents.iterator();
            while (it.hasNext()) {
                AdUnitMotionEvent next = it.next();
                ArrayList arrayList = sparseArray.get(next.getAction());
                if (arrayList != null) {
                    int intValue = ((Integer) arrayList.get(0)).intValue();
                    if (sparseIntArray.get(next.getAction(), 0) == intValue) {
                        if (sparseArray2.get(next.getAction()) == null) {
                            sparseArray2.put(next.getAction(), new SparseArray());
                        }
                        sparseArray2.get(next.getAction()).put(intValue, next);
                        arrayList.remove(0);
                    }
                    sparseIntArray.put(next.getAction(), sparseIntArray.get(next.getAction()) + 1);
                }
            }
        }
        return sparseArray2;
    }

    public SparseIntArray getEventCount(ArrayList<Integer> arrayList) {
        SparseIntArray sparseIntArray = new SparseIntArray();
        synchronized (this._motionEvents) {
            Iterator<AdUnitMotionEvent> it = this._motionEvents.iterator();
            while (it.hasNext()) {
                AdUnitMotionEvent next = it.next();
                Iterator<Integer> it2 = arrayList.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    Integer next2 = it2.next();
                    if (next.getAction() == next2.intValue()) {
                        sparseIntArray.put(next2.intValue(), sparseIntArray.get(next2.intValue(), 0) + 1);
                        break;
                    }
                }
            }
        }
        return sparseIntArray;
    }
}
