package ru.goodibunakov.testaximedia;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GooDi on 09.10.2017.
 */

public class Rect implements Parcelable {
    private PointF originRect;
    private PointF currentRect;

    public Rect(PointF origin) {
        originRect = origin;
        currentRect = origin;
    }

    public PointF getCurrentRect() {
        return currentRect;
    }

    public void setCurrentRect(PointF currentRect) {
        this.currentRect = currentRect;
    }

    public PointF getOriginRect() {
        return originRect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        originRect.writeToParcel(dest, flags);
        currentRect.writeToParcel(dest, flags);
    }

    // координаты Rect из Parcel-объекта в методе createFromParcel
    private Rect(Parcel in) {
        originRect.readFromParcel(in);
        currentRect.readFromParcel(in);
    }

    public static final Parcelable.Creator<Rect> CREATOR = new Parcelable.Creator<Rect>() {

        // Возвращает новый Rect из parcel
        public Rect createFromParcel(Parcel in) {
            Rect b = new Rect(in);
            return b;
        }

        // Возвращает массив объектов Rect
        public Rect[] newArray(int size) {
            return new Rect[size];
        }
    };
}