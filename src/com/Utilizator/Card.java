package com.Utilizator;

public class Card {
    private double suma;
    private double lim_transfer;
    private String moneda;
    private double com_online;
    private double com_factura;
    private double com_transfer;
    private double dobanda;

    public Card(){

    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {

        this.suma = Math.round(suma*100)/100.0;
    }

    public double getLim_transfer() {
        return lim_transfer;
    }

    public void setLim_transfer(double lim_transfer) {
        this.lim_transfer = lim_transfer;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public double getCom_online() {
        return com_online;
    }

    public void setCom_online(double com_online) {

        this.com_online = Math.round(com_online*100)/100.0;
    }

    public double getCom_factura() {
        return com_factura;
    }

    public void setCom_factura(double com_factura) {

        this.com_factura = Math.round(com_factura*100)/100.0;
    }

    public double getCom_transfer() {
        return com_transfer;
    }

    public void setCom_transfer(double com_transfer) {

        this.com_transfer = Math.round(com_transfer*100)/100.0;
    }

    public void setDobanda(double dobanda){
        this.dobanda=dobanda;
    }

    public double getDobanda(){return this.dobanda;}

}
