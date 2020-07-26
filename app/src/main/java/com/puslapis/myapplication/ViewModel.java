package com.puslapis.myapplication;

import android.content.Context;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import java.util.Locale;

public class ViewModel extends BaseObservable {

    private Context kontekstas;


    /**
     * svarbiausios vertes
     */
    public final ObservableInt mMotorStr = new ObservableInt(0);

    /**
     * misc kintamieji
     */
    public final ObservableDouble mLeftX = new ObservableDouble(0);
    public final ObservableDouble mLeftY = new ObservableDouble(0);
    public final ObservableDouble mRightX = new ObservableDouble(0);
    public final ObservableDouble mRightY = new ObservableDouble(0);
    public final ObservableInt maxSpeed = new ObservableInt(0);

    public ViewModel(Context context) {
        this.kontekstas = context;
    }

    /**
     * funkcijos atvaizdavimui
     */
    public String coordX(double x){return String.format(Locale.ENGLISH,"%.2f", x);}
    public String coordY(double y){return String.format(Locale.ENGLISH,"%.2f", y);}
    public int getMotorStr(double str){return (int)str;}

}
