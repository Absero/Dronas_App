package com.puslapis.myapplication;

public class Glob {
    public static final int daznis = 50;
    public static final double maksimalausGreicioLaikas = 2;
    public static final int maksimaliReiksme = 500;
    public static final int maksimaliVienoMotoroAmplitude = 50;

    public static final int maxPadidejimasPerCikla = (int) ((double) maksimaliReiksme / daznis / maksimalausGreicioLaikas);
}
