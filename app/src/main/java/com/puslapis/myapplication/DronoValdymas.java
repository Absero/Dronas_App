package com.puslapis.myapplication;

import android.content.Context;

import com.puslapis.myapplication.misc.Glob;

public class DronoValdymas {
    private final String TAG = "Main -> DronoValdymas";

    private Main context;
    private ViewModel mViewModel;

    private double mCurrentMotorSTR_double = 0;

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
        double apskaiciuota = delta + mCurrentMotorSTR_double;

        if (apskaiciuota > mViewModel.maxSpeed.get()) apskaiciuota = mViewModel.maxSpeed.get();
        if (apskaiciuota > Glob.maksimaliReiksme) apskaiciuota = Glob.maksimaliReiksme;
        if (apskaiciuota < 0) apskaiciuota = 0;
        mCurrentMotorSTR_double = apskaiciuota;

        mViewModel.mMotorStr.set((int) apskaiciuota);
        UpdateAllMotorValues();
    }

    public void ChangeIndMotorStr() {
        double x = mViewModel.mRightX.get();
        double y = mViewModel.mRightY.get();

        //X+ Y-
        mMotorFL_delta = (int) (((x < 0 ? 0 : x) + (y > 0 ? 0 : -y)) * Glob.maksimaliVienoMotoroAmplitude);
        //X- Y-
        mMotorFR_delta = (int) (((x > 0 ? 0 : -x) + (y > 0 ? 0 : -y)) * Glob.maksimaliVienoMotoroAmplitude);
        //X+ Y+
        mMotorBL_delta = (int) (((x < 0 ? 0 : x) + (y < 0 ? 0 : y)) * Glob.maksimaliVienoMotoroAmplitude);
        //X- Y+
        mMotorBR_delta = (int) (((x > 0 ? 0 : -x) + (y < 0 ? 0 : y)) * Glob.maksimaliVienoMotoroAmplitude);

        UpdateAllMotorValues();
    }

    private void UpdateAllMotorValues() {
        mViewModel.mMotorFL.set(CalculateIndMotorValue("FL"));
        mViewModel.mMotorFR.set(CalculateIndMotorValue("FR"));
        mViewModel.mMotorBL.set(CalculateIndMotorValue("BL"));
        mViewModel.mMotorBR.set(CalculateIndMotorValue("BR"));
    }

    private int CalculateIndMotorValue(String motor) {
        double delta = 0;
        int coeff = 0;

        switch (motor) {
            case "FL":
                delta = mMotorFL_delta;
                coeff = context.mCoeffFL.getValue();
                break;
            case "FR":
                delta = mMotorFR_delta;
                coeff = context.mCoeffFR.getValue();
                break;
            case "BL":
                delta = mMotorBL_delta;
                coeff = context.mCoeffBL.getValue();
                break;
            case "BR":
                delta = mMotorBR_delta;
                coeff = context.mCoeffBR.getValue();
                break;
            default:
        }
        return (int) mCurrentMotorSTR_double + (int) delta + coeff;

    }

    public void ResetAll() {
        //reset locals
        mCurrentMotorSTR_double = 0;
        mMotorFL_delta = 0;
        mMotorFR_delta = 0;
        mMotorBL_delta = 0;
        mMotorBR_delta = 0;

        //reset observables
        mViewModel.mMotorStr.set((int) mCurrentMotorSTR_double);
        mViewModel.mMotorFL.set(0);
        mViewModel.mMotorFR.set(0);
        mViewModel.mMotorBL.set(0);
        mViewModel.mMotorBR.set(0);
    }
}
