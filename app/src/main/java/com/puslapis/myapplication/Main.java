package com.puslapis.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.puslapis.myapplication.databinding.MainBinding;
import com.puslapis.myapplication.misc.Glob;
import com.puslapis.myapplication.misc.Helper;
import com.puslapis.myapplication.other.HorizontalNumberPicker;
import com.puslapis.myapplication.other.Stopwatch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.github.controlwear.virtual.joystick.android.JoystickView;
/* naudingi linkai
    https://stackoverflow.com/questions/2695646/declaring-a-custom-android-ui-element-using-xml kaip sukurti custom elementa su nustatymais xml faile
 */

/* TODO:
 *      kai nera nauju reiksmiu galima siust tiesiog HOLD komanda (reikia deliot visur kad pasikeite reiksme) (gal vis delto reik ziuret ar desinys joystickas nulinej padety)
 *      koeficientu gal nereikia siust kartu susumuotai su viskuo, gal reik irasyt paciam drone? (galima tiesiog atminusuot pries siunciant)
 *      paciam drone galima padaryt fifo su dinaminiu masyvu kuriam dedami paketai arba tiesiog kiekvienai komandai paskirt vieta
 *      *
 *      apdoroti gautus TCP duomenis
 */

public class Main extends AppCompatActivity {
    private final String TAG = "Main";

    private ViewModel mViewModel;
    private DronoValdymas mDronoValdymas;

    public TcpClient mTcpClient;

    public boolean mDataChangedFlag = false;

    HorizontalNumberPicker mCoeffFL, mCoeffFR, mCoeffBL, mCoeffBR;

    private Timer mSiuntimoTaimeris;

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

        //region Joystickai
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
        //endregion

        //region switchas pradziai
        ((Switch) findViewById(R.id.mainStartWorkSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Switch sw = (Switch) buttonView;
                if (isChecked) {
                    //paleisti taimeri siuntima
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage_thread("2".getBytes());

                        Log.d(TAG, "onCheckedChanged: " + Glob.periodas_ms);

                        mSiuntimoTaimeris = new Timer();
                        mSiuntimoTaimeris.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Log.d(TAG, "run: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
                                mTcpClient.sendMessage_noThread((LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")) + "\n").getBytes());
                            }
                        }, Glob.periodas_ms, Glob.periodas_ms);

                    } else {
                        sw.setChecked(false);
                    }
                } else {
                    //stabdyti taimeri ir siuntima
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage_thread("1".getBytes());
                        mSiuntimoTaimeris.cancel();
                        mSiuntimoTaimeris.purge();
                    }
                }
            }
        });
        //endregion switchas pradziai
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

    public void connect_click(View view) {
        Button b = (Button) view;
        if (b.getText().toString().equals(getString(R.string.disconnect))) {
            if (mTcpClient != null) {
                mTcpClient.stopClient();
            }
            b.setText(R.string.connect);
            b.setBackgroundColor(getColor(R.color.zalia));
            if (mTcpClient != null)
                mTcpClient.stopClient();
        } else {
            new ConnectTask().execute();

            Executor mExecutor = Executors.newSingleThreadExecutor();
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    Stopwatch s = new Stopwatch();
                    s.start();
                    boolean fail = false;
                    while (mTcpClient == null) {
                        if (s.getElapsedTime() > 500) {
                            fail = true;
                            break;
                        }
                    }
                    if (!fail) {
                        s.start();
                        while (!mTcpClient.isConnected) {
                            if (s.getElapsedTime() > 500) {
                                fail = true;
                                break;
                            }
                        }
                        if (!fail) {
                            Button b = findViewById(R.id.mainButtonConnect);
                            b.setText(R.string.disconnect);
                            b.setBackgroundColor(getColor(R.color.raudona));
                        }
                    }
                }
            });
        }
    }

    public byte[] packData(){
        return new byte[]{0};
    }

    /**
     * For connecting to server and receiving messages
     */
    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

//            if (message[0].equals("connect")) {
//                mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
//                    @Override
//                    public void messageReceived(String message) {
//                        OnTcpMessageReceived(message.getBytes());
//                    }
//                });
//                mTcpClient.run();
//            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....
            OnTcpMessageReceived(values[0].getBytes());
        }
    }

    private void OnTcpMessageReceived(byte[] message) {
    }
}
