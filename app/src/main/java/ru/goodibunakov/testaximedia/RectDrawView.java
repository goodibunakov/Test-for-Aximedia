package ru.goodibunakov.testaximedia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.goodibunakov.testaximedia.DrawActivity.wasDrawing;

/**
 * Created by GooDi on 09.10.2017.
 */

public class RectDrawView extends View {

    private Rect currentRect;
    private List<Rect> rects = new ArrayList<>();
    private Paint rectPaint, backgroundPaint;
    private final String PARENT_STATE_KEY = "1";
    private final String BOXEN_KEY = "2";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                wasDrawing = true;
                action = "ACTION_DOWN";
                //сброс текущего состояния
                currentRect = new Rect(current);
                rects.add(currentRect);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (currentRect != null){
                    currentRect.setCurrentRect(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                currentRect = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                currentRect = null;
                break;
        }
        Log.d("draw", "x=" + current.x + "  y=" + current.y);
        return true;
    }

    public RectDrawView(Context context) {
        super(context, null);
    }

    public RectDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //прямоугольники рисуются полупрозрачным cehsv цветом
        rectPaint = new Paint();
        rectPaint.setColor(getResources().getColor(R.color.colorRect));

        //фон закрашивается серовато-белым цветом
        backgroundPaint = new Paint();
        backgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //заполнение фона
        //canvas.drawPaint(backgroundPaint);

        for (Rect rect : rects){
            float left = Math.min(rect.getOriginRect().x, rect.getCurrentRect().x);
            float right = Math.max(rect.getOriginRect().x, rect.getCurrentRect().x);
            float top = Math.min(rect.getOriginRect().y, rect.getCurrentRect().y);
            float bottom = Math.max(rect.getOriginRect().y, rect.getCurrentRect().y);
            canvas.drawRect(left, top, right, bottom, rectPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {

        Parcelable parentState = super.onSaveInstanceState();

        Bundle bundle = new Bundle();
        bundle.putParcelable(PARENT_STATE_KEY, parentState);
        bundle.putParcelableArray(BOXEN_KEY, rects.toArray(new Rect[rects.size()]));
Log.d("onSaveInstanceState", bundle.toString());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state != null && state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            super.onRestoreInstanceState(bundle.getParcelable(PARENT_STATE_KEY));

            Rect[] rects1 = (Rect[]) bundle.getParcelableArray(BOXEN_KEY);
            rects = new ArrayList<>(Arrays.asList(rects1));
        } else {
            super.onRestoreInstanceState(state);
        }
        Log.d("onRestoreInstanceState", state.toString());
    }
}
