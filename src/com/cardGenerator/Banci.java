package com.cardGenerator;

/**
 * Am folosit aceasta enumeratie pentru stocarea de informatii utile(precum cifrul bancii) in cadrul unor obiecte unice;
 * In cadrul unei enumeratii, constructorul este privat, astfel incat exista un singur obiect de acel tip(BCR, BT..), oricate
 * referinte ar fi spre acesta.

 */
public enum Banci {
    BCR(1.5,0.7,2.3,0.3,"RNCB",2650),
    BT(1.0,0.7,3.4,0.5,"BTRL",3200),
    ING(2.5,2.1,0.8,0.4,"INGB",3000),
    RAIFFEISEN(0.5,2.6,3.4,0.2,"RZBR",3550),
    BRD(2.3,1.1,1.7,0.1,"BRDE",2300);

    private double com_online;
    private double com_factura;
    private double com_transf;
    private double dobanda;
    private String cifru;
    private double limita;

    private Banci(double online,double factura,double transf,double dobanda,String cifru,double limita){
        this.com_online=online;
        this.com_factura=factura;
        this.com_transf=transf;
        this.dobanda=dobanda;
        this.cifru=cifru;
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

    public String getCifru(){
        return cifru;
    }

    public double getLimita() {
        return limita;
    }
}