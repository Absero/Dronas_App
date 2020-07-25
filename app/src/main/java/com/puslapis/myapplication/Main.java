package com.puslapis.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.puslapis.myapplication.databinding.MainBinding;
import com.puslapis.myapplication.other.HorizontalNumberPicker;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Main extends AppCompatActivity {

    public ViewModel mViewModel;

    public SeekBar mGreicioRibotuvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        ((HorizontalNumberPicker) (findViewById(R.id.mainCoeffFL))).setValue(sharedPref.getInt("Coeff_FL", 0));
        ((HorizontalNumberPicker) (findViewById(R.id.mainCoeffBL))).setValue(sharedPref.getInt("Coeff_BL", 0));
        ((HorizontalNumberPicker) (findViewById(R.id.mainCoeffFR))).setValue(sharedPref.getInt("Coeff_FR", 0));
        ((HorizontalNumberPicker) (findViewById(R.id.mainCoeffBR))).setValue(sharedPref.getInt("Coeff_BR", 0));

        mViewModel = new ViewModel(this);
        mainBinding.setViewModel(mViewModel);

        mGreicioRibotuvas = findViewById(R.id.mainGreicioRibotuvas);
        mGreicioRibotuvas.setMax(Glob.maksimaliReiksme);
        mGreicioRibotuvas.setProgress(Glob.maksimaliReiksme / 2);

        ((JoystickView) (findViewById(R.id.mainLeftJoystick))).setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                mViewModel.mLeftKampas.set(angle);
                mViewModel.mLeftStr.set(strength);
            }
        }, 20);

        ((JoystickView) (findViewById(R.id.mainRightJoystick))).setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                mViewModel.mRightKampas.set(angle);
                mViewModel.mRightStr.set(strength);
            }
        }, 20);

    }

    //region funkcionavimo funkcijos


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Coeff_FL", ((HorizontalNumberPicker) (findViewById(R.id.mainCoeffFL))).getValue());
        editor.putInt("Coeff_BL", ((HorizontalNumberPicker) (findViewById(R.id.mainCoeffBL))).getValue());
        editor.putInt("Coeff_FR", ((HorizontalNumberPicker) (findViewById(R.id.mainCoeffFR))).getValue());
        editor.putInt("Coeff_BR", ((HorizontalNumberPicker) (findViewById(R.id.mainCoeffBR))).getValue());
        editor.apply();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    //endregion funkcionavimo funkcijos
}