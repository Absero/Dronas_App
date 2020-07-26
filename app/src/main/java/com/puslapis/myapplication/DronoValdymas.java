package com.puslapis.myapplication;

import android.content.Context;

public class DronoValdymas {
    private Context context;
    private ViewModel mViewModel;
    private double currentMotorSTR_double=0;

    public DronoValdymas(Context context, ViewModel viewModel) {
        this.context = context;
        this.mViewModel = viewModel;
    }

    public void ChangeMotorStr() {
        double delta = mViewModel.mLeftY.get() * Glob.maxPadidejimasPerCikla;
        double apskaiciuota=delta+currentMotorSTR_double;

        if (apskaiciuota > Glob.maksimaliReiksme) apskaiciuota=Glob.maksimaliReiksme;
        if (apskaiciuota < 0) apskaiciuota=0;
        currentMotorSTR_double=apskaiciuota;

        mViewModel.mMotorStr.set((int)apskaiciuota);
    }

    public void ResetAll(){
        //reset locals
        currentMotorSTR_double=0;

        //reset observables
        mViewModel.mMotorStr.set((int)currentMotorSTR_double);
    }


}
