package com.puslapis.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.puslapis.myapplication.databinding.MainBinding;
import com.puslapis.myapplication.misc.Helper;
import com.puslapis.myapplication.other.HorizontalNumberPicker;

import io.github.controlwear.virtual.joystick.android.JoystickView;
/* naudingi linkai
    https://stackoverflow.com/questions/2695646/declaring-a-custom-android-ui-element-using-xml kaip sukurti custom elementa su nustatymais xml faile
 */

/* TODO:
 *      kai nera nauju reiksmiu galima siust tiesiog HOLD komanda (reikia deliot visur kad pasikeite reiksme) (gal vis delto reik ziuret ar desinys joystickas nulinej padety)
 *      koeficientu gal nereikia siust kartu susumuotai su viskuo, gal reik irasyt paciam drone? (galima tiesiog atminusuot pries siunciant)
 *      paciam drone galima padaryt fifo su dinaminiu masyvu kuriam dedami paketai arba tiesiog kiekvienai komandai paskirt vieta
 */

public class Main extends AppCompatActivity {
    private final String TAG = "Main";

    private ViewModel mViewModel;
    private DronoValdymas mDronoValdymas;
    public boolean mDataChangedFlag = false;

    HorizontalNumberPicker mCoeffFL, mCoeffFR, mCoeffBL, mCoeffBR;

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

        mCoeffFL = findViewById(R.id.mainCoeffFL);
        mCoeffFL.setMax(Glob.maksimaliReiksme);
        mCoeffFL.setMin(-Glob.maksimaliReiksme);
        mCoeffFL.setOnChangeListener(new HorizontalNumberPicker.OnChangeListener() {
            @Override
            public void onMove() {
                mDronoValdymas.ChangeAllMotorStr();
            }
        });
        mCoeffFR = findViewById(R.id.mainCoeffFR);
        mCoeffFR.setMax(Glob.maksimaliReiksme);
        mCoeffFR.setMin(-Glob.maksimaliReiksme);
        mCoeffFR.setOnChangeListener(new HorizontalNumberPicker.OnChangeListener() {
            @Override
            public void onMove() {
                mDronoValdymas.ChangeAllMotorStr();
            }
        });
        mCoeffBL = findViewById(R.id.mainCoeffBL);
        mCoeffBL.setMax(Glob.maksimaliReiksme);
        mCoeffBL.setMin(-Glob.maksimaliReiksme);
        mCoeffBL.setOnChangeListener(new HorizontalNumberPicker.OnChangeListener() {
            @Override
            public void onMove() {
                mDronoValdymas.ChangeAllMotorStr();
            }
        });
        mCoeffBR = findViewById(R.id.mainCoeffBR);
        mCoeffBR.setMax(Glob.maksimaliReiksme);
        mCoeffBR.setMin(-Glob.maksimaliReiksme);
        mCoeffBR.setOnChangeListener(new HorizontalNumberPicker.OnChangeListener() {
            @Override
            public void onMove() {
                mDronoValdymas.ChangeAllMotorStr();
            }
        });

        ((ProgressBar) findViewById(R.id.mainFL)).setMax(Glob.maksimaliReiksme);
        ((ProgressBar) findViewById(R.id.mainFR)).setMax(Glob.maksimaliReiksme);
        ((ProgressBar) findViewById(R.id.mainBL)).setMax(Glob.maksimaliReiksme);
        ((ProgressBar) findViewById(R.id.mainBR)).setMax(Glob.maksimaliReiksme);

        //endregion

        mViewModel = new ViewModel(this);
        mainBinding.setViewModel(mViewModel);
        mDronoValdymas = new DronoValdymas(this, mViewModel);

        final SeekBar mGreicioRibotuvas = findViewById(R.id.mainGreicioRibotuvas);
        mGreicioRibotuvas.setMax(Glob.maksimaliReiksme);
        mGreicioRibotuvas.setProgress(Glob.maksimaliReiksme / 2);
        ((ProgressBar) (findViewById(R.id.mainGreitis))).setMax(Glob.maksimaliReiksme);

        ((JoystickView) (findViewById(R.id.mainLeftJoystick))).setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                double[] temp = Helper.Pole2XY(strength, angle);
                mViewModel.mLeftX.set(temp[0]);
                mViewModel.mLeftY.set(temp[1]);

                mDronoValdymas.ChangeAllMotorStr();

            }
        }, 1000 / Glob.daznis);

        ((JoystickView) (findViewById(R.id.mainRightJoystick))).setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                double[] temp = Helper.Pole2XY(strength, angle);
                mViewModel.mRightX.set(temp[0]);
                mViewModel.mRightY.set(temp[1]);

                mDronoValdymas.ChangeIndMotorStr();
            }
        }, 1000 / Glob.daznis);

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
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
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
        } else {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    //endregion funkcionavimo funkcijos

    public void resetAll_Click(View view) {
        mDronoValdymas.ResetAll();
    }
}