package com.puslapis.myapplication;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableInt;

import com.puslapis.myapplication.misc.Glob;

import java.util.Locale;

public class ViewModel extends BaseObservable {
    private final String TAG = "Main -> ViewModel";
    private Main kontekstas;

    public ViewModel(Context context) {
        this.kontekstas = (Main) context;
        mMotorFL.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                DataChangedFlag_Set();
            }
        });
        mMotorFR.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                DataChangedFlag_Set();
            }
        });
        mMotorBL.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                DataChangedFlag_Set();
            }
        });
        mMotorBR.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                DataChangedFlag_Set();
            }
        });
    }

    /**
     * svarbiausios vertes
     */
    public final ObservableInt mMotorStr = new ObservableInt(0);
    public final ObservableInt mMotorFL = new ObservableInt(0);
    public final ObservableInt mMotorFR = new ObservableInt(0);
    public final ObservableInt mMotorBL = new ObservableInt(0);
    public final ObservableInt mMotorBR = new ObservableInt(0);

    /**
     * misc kintamieji
     */
    public final ObservableDouble mLeftX = new ObservableDouble(0);
    public final ObservableDouble mLeftY = new ObservableDouble(0);
    public final ObservableDouble mRightX = new ObservableDouble(0);
    public final ObservableDouble mRightY = new ObservableDouble(0);
    public final ObservableInt maxSpeed = new ObservableInt(Glob.maksimaliReiksme / 2);


    /**
     * funkcijos atvaizdavimui
     */
    public String coordX(double x) {
        return String.format(Locale.ENGLISH, "X: %.4f", x);
    }

    public String coordY(double y) {
        return String.format(Locale.ENGLISH, "Y: %.4f", y);
    }

    public int getMotorStr(double str) {
        return (int) str;
    }

    /**
     * onPropertychangedCallbacks
     */
    private void DataChangedFlag_Set() {
        kontekstas.mDataChangedFlag = true;
    }
}
