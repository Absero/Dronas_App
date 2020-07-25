package com.puslapis.myapplication;

import android.content.Context;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

public class ViewModel extends BaseObservable {

    private Context kontekstas;

//    public final ObservableField<Objektas> mObjektas = new ObservableField<>();
    public final ObservableBoolean mMatomas = new ObservableBoolean(true);

    public ViewModel(Context context) {
        this.kontekstas = context;
    }

    public int matomasUzrasas(boolean matomas){
        if(matomas)
            return View.VISIBLE;
        else
            return View.INVISIBLE;
    }
}
