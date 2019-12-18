package com.cardGenerator;

public enum Currency {
    //Buy and sell currency;
    EURO(4.7,4.82),DOLAR(4.24,4.4),LIRA(5.52,5.75);

    private double cumparare;
    private double vanzare;

    private Currency(double v1,double v2){
        cumparare=v1;
        vanzare=v2;
    }

    public double convertInLei(double suma){
        return suma/cumparare;
    }

    public double convertDinLei(double suma){
        return suma*vanzare;
    }
}
