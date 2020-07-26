package com.puslapis.myapplication;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableInt;

public class DronoValdymas {
    private final String TAG = "DronoValdymas";

    private Main context;
    private ViewModel mViewModel;

    private double currentMotorSTR_double = 0;

    private double mMotorFL_delta = 0;
    private double mMotorFR_delta = 0;
    private double mMotorBL_delta = 0;
    private double mMotorBR_delta = 0;


    public DronoValdymas(Context context, ViewModel viewModel) {
        this.context = (Main) context;
        this.mViewModel = viewModel;
    }

    public void ChangeAllMotorStr() {
        double delta = mViewModel.mLeftY.get() * Glob.maxPadidejimasPerCikla;
        double apskaiciuota = delta + currentMotorSTR_double;

        if (apskaiciuota > mViewModel.maxSpeed.get()) apskaiciuota = mViewModel.maxSpeed.get();
        if (apskaiciuota > Glob.maksimaliReiksme) apskaiciuota = Glob.maksimaliReiksme;
        if (apskaiciuota < 0) apskaiciuota = 0;
        currentMotorSTR_double = apskaiciuota;

        mViewModel.mMotorStr.set((int) apskaiciuota);
        mViewModel.mMotorFL.set((int) ((int) apskaiciuota + mMotorFL_delta + context.mCoeffFL.getValue()));
        mViewModel.mMotorFR.set((int) ((int) apskaiciuota + mMotorFR_delta + context.mCoeffFR.getValue()));
        mViewModel.mMotorBL.set((int) ((int) apskaiciuota + mMotorBL_delta + context.mCoeffBL.getValue()));
        mViewModel.mMotorBR.set((int) ((int) apskaiciuota + mMotorBR_delta + context.mCoeffBR.getValue()));
    }

    public void ChangeIndMotorStr() {
        double currentX = mViewModel.mRightX.get();
        double currentY = mViewModel.mRightY.get();

        mMotorFL_delta = CalculateMotorValue(currentX, currentY, "FL");
        mMotorFR_delta = CalculateMotorValue(currentX, currentY, "FR");
        mMotorBL_delta = CalculateMotorValue(currentX, currentY, "BL");
        mMotorBR_delta = CalculateMotorValue(currentX, currentY, "BR");

        mViewModel.mMotorFL.set((int) (currentMotorSTR_double + mMotorFL_delta + context.mCoeffFL.getValue()));
        mViewModel.mMotorFR.set((int) (currentMotorSTR_double + mMotorFR_delta + context.mCoeffFL.getValue()));
        mViewModel.mMotorBL.set((int) (currentMotorSTR_double + mMotorBL_delta + context.mCoeffFL.getValue()));
        mViewModel.mMotorBR.set((int) (currentMotorSTR_double + mMotorBR_delta + context.mCoeffFL.getValue()));
    }

    private int CalculateMotorValue(double x, double y, String motor) {
        int stiprumas = 0;
        switch (motor) {
            case "FL":
                //X+ Y-
                stiprumas = (int) ((x < 0 ? 0 : x) * Glob.maksimaliVienoMotoroAmplitude + (y > 0 ? 0 : -y) * Glob.maksimaliVienoMotoroAmplitude);
                break;
            case "FR":
                //X- Y-
                stiprumas = (int) ((x > 0 ? 0 : -x) * Glob.maksimaliVienoMotoroAmplitude + (y > 0 ? 0 : -y) * Glob.maksimaliVienoMotoroAmplitude);
                break;
            case "BL":
                //X+ Y+
                stiprumas = (int) ((x < 0 ? 0 : x) * Glob.maksimaliVienoMotoroAmplitude + (y < 0 ? 0 : y) * Glob.maksimaliVienoMotoroAmplitude);
                break;
            case "BR":
                //X- Y+
                stiprumas = (int) ((x > 0 ? 0 : -x) * Glob.maksimaliVienoMotoroAmplitude + (y < 0 ? 0 : y) * Glob.maksimaliVienoMotoroAmplitude);
                break;
            default:
        }
        return stiprumas;
    }

    public void ResetAll() {
        //reset locals
        currentMotorSTR_double = 0;
        mMotorFL_delta = 0;
        mMotorFR_delta = 0;
        mMotorBL_delta = 0;
        mMotorBR_delta = 0;

        //reset observables
        mViewModel.mMotorStr.set((int) currentMotorSTR_double);
        mViewModel.mMotorFL.set(0);
        mViewModel.mMotorFR.set(0);
        mViewModel.mMotorBL.set(0);
        mViewModel.mMotorBR.set(0);
    }


}
