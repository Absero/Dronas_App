package com.puslapis.myapplication.misc;

public class Glob {
    public static final int daznis = 50;
    public static final double maksimalausGreicioLaikas = 2;
    public static final int maksimaliReiksme = 500;
    public static final int maksimaliVienoMotoroAmplitude = maksimaliReiksme/5;

    public static final double periodas = 1.0/daznis;
    public static final int periodas_ms = (int)(periodas*1000);
    public static final int maxPadidejimasPerCikla = (int) ((double) maksimaliReiksme / daznis / maksimalausGreicioLaikas);
}
