package com.cardGenerator;

public enum Currency {
    //Buy and sell currency;
    EURO(0,0),DOLAR(0,0),LIRA(0,0);

    private double cumparare;
    private double vanzare;

    private Currency(double v1,double v2){
        cumparare=v1;
        vanzare=v2;
    }

    public void setCurrency(double valoare){
        valoare = Math.round(valoare * 100) / 100.0;
        cumparare = valoare;
        vanzare = Math.round((valoare + 0.2) * 100) / 100.0;
    }

    public double convertDinLei(double suma){
        return Math.round((suma/vanzare)*100)/100.0;
    }

    public double convertInLei(double suma){
        return Math.round((suma*cumparare)*100)/100.0;
    }

    public double convertBetween(double suma){
        return Math.round((suma*vanzare)*100)/100.0;
    }

    public double forTransfer(double suma){
        return Math.round((suma/cumparare)*100)/100.0;
    }

    public double getCumparare(){
        return this.cumparare;
    }

    public double getVanzare(){
        return this.vanzare;
    }
}
