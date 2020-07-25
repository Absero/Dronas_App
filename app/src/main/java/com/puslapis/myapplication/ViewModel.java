package com.puslapis.myapplication;

import android.content.Context;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public class ViewModel extends BaseObservable {

    private Context kontekstas;

//    public final ObservableField<Objektas> mObjektas = new ObservableField<>();
    public final ObservableInt mLeftKampas = new ObservableInt(0);
    public final ObservableInt mLeftStr = new ObservableInt(0);
    public final ObservableInt mRightKampas = new ObservableInt(0);
    public final ObservableInt mRightStr = new ObservableInt(0);

    public ViewModel(Context context) {
        this.kontekstas = context;
    }

    public String kampas(int kampas){return String.valueOf(kampas);}
    public String strength(int strength){return String.valueOf(strength);}

}
