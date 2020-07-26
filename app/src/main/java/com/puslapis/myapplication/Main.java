package com.puslapis.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.puslapis.myapplication.databinding.MainBinding;
import com.puslapis.myapplication.misc.Helper;
import com.puslapis.myapplication.other.HorizontalNumberPicker;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Main extends AppCompatActivity {
    private final String TAG = "Main";

    private ViewModel mViewModel;
    private DronoValdymas mDronoValdymas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region prazia ir lango nustatymai
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
        //endregion

        mViewModel = new ViewModel(this);
        mainBinding.setViewModel(mViewModel);
        mDronoValdymas = new DronoValdymas(this, mViewModel);

        final SeekBar mGreicioRibotuvas = findViewById(R.id.mainGreicioRibotuvas);
        mGreicioRibotuvas.setMax(Glob.maksimaliReiksme);
        mGreicioRibotuvas.setProgress(Glob.maksimaliReiksme / 2);
        ((ProgressBar)(findViewById(R.id.mainGreitis))).setMax(Glob.maksimaliReiksme);

        ((JoystickView) (findViewById(R.id.mainLeftJoystick))).setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                double[] temp = Helper.Pole2XY(strength, angle);
                mViewModel.mLeftX.set(temp[0]);
                mViewModel.mLeftY.set(temp[1]);

                mDronoValdymas.ChangeMotorStr();

            }
        }, 1000/Glob.daznis);

        ((JoystickView) (findViewById(R.id.mainRightJoystick))).setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                double[] temp = Helper.Pole2XY(strength, angle);
                mViewModel.mRightX.set(temp[0]);
                mViewModel.mRightY.set(temp[1]);
            }
        }, 1000/Glob.daznis);

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

    public void resetAll_Click(View view) {
        mDronoValdymas.ResetAll();
    }
}