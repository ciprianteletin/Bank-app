package com.cardGenerator;

public enum TipCard {
    //a student is basically a student, no job or another income source, except his parents;
    Debit(0.1,0,0.7,1.5,400),
    Calator(0.3,0.1,0.1,0.2,2500),
    Depozit(0.2,0.9,0.6,2,-1),
    Salarial(0.1,0.5,0.2,2.2,2000),
    Student(0,0,0,5,1000);

    private double com_online;
    private double com_factura;
    private double com_transf;
    private double dobanda;
    private double limita;

    private TipCard(double online,double factura, double transf, double dobanda,double limita){
        this.com_online=online;
        this.com_factura=factura;
        this.com_transf=transf;
        this.dobanda=dobanda;
        this.limita=limita;
    }

    public String toString(){
        return this.name();
    }

    public double getCom_online() {
        return com_online;
    }

    public double getCom_factura() {
        return com_factura;
    }

    public double getCom_transf() {
        return com_transf;
    }

    public double getDobanda() {
        return dobanda;
    }

    public double getLimita(){return limita;}
}
