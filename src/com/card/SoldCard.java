package com.card;

import javax.swing.*;
import java.awt.*;

/**
 * Interfata grafica prin intermediul careia afisez datele stocate pe card, precum sold curent, moneda actuala, limita de operatiuni etc
 * In metodele de mai jos si prin intermediul constructorului se initializeaza fiecare panel din cadrul Frame-ului principal, in cadrul
 * carora se regasesc multiple label-uri pentru afisarea de informatii
 */
public class SoldCard {
    private Card card;
    private JFrame display;
    private JPanel sold;
    private JPanel descriere;
    private JPanel com_dobanda;

    public SoldCard(Card card){
        this.card=card;
        this.display=new JFrame("Sold curent");
        this.display.getContentPane().setBackground(new Color(56,56,56));
        this.display.setLayout(new BoxLayout(display.getContentPane(),BoxLayout.PAGE_AXIS));
        this.display.setSize(new Dimension(400,400));
        this.display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initSold();
        initInfo();
        initComisioane();
        this.display.pack();
        this.display.setResizable(false);
        this.display.setLocationRelativeTo(null);
        this.display.setVisible(true);
    }

    private void initSold(){
        sold=new JPanel(new GridLayout(3,2));
        sold.setPreferredSize(new Dimension(400,150));
        JLabel suma=new JLabel("Suma card:");
        suma.setForeground(new Color(255,255,255));
        JLabel sumas=new JLabel();
        sumas.setForeground(new Color(0,255,0));
        sumas.setText(""+card.getSuma());
        JLabel moneda=new JLabel("Moneda: ");
        moneda.setForeground(new Color(255,255,255));
        JLabel monedas=new JLabel();
        monedas.setForeground(new Color(0,255,0));
        monedas.setText(card.getMoneda());
        JLabel limita=new JLabel("Limita operatii: ");
        limita.setForeground(new Color(255,255,255));
        JLabel limitas=new JLabel();
        limitas.setForeground(new Color(0,255,0));
        if(card.getLim_transfer()==-1)
            limitas.setText("Infinity");
        else
            limitas.setText(card.getLim_transfer()+"");

        sold.add(suma); sold.add(sumas);
        sold.add(moneda); sold.add(monedas);
        sold.add(limita); sold.add(limitas);
        sold.setBackground(new Color(56,56,56));

        display.add(sold);
    }

    private void initInfo(){
        descriere=new JPanel(new FlowLayout(FlowLayout.CENTER));
        descriere.setBackground(new Color(223,80,32));
        descriere.setPreferredSize(new Dimension(400,25));
        JLabel info=new JLabel("Comisioane si dobanda");
        info.setForeground(new Color(0,100,100));
        descriere.add(info);
        display.add(descriere);
    }

    /**
     * Metoda in cadrul careia se initializeaza si extrag datele specifice(comisioane si dobanzi) din cardul bancar si care se vor
     * depune in label-uri specifice.
     */
    private void initComisioane(){
        com_dobanda=new JPanel(new GridLayout(4,2));
        com_dobanda.setBackground(new Color(56,56,56));
        com_dobanda.setPreferredSize(new Dimension(400,150));
        JLabel com_online,com_transfer,com_factura,dobanda;
        JLabel com_os,com_ts,com_fs,dobandas;

        com_online=new JLabel("Comision online: ");
        com_online.setForeground(new Color(255,255,255));
        com_dobanda.add(com_online);

        com_os=new JLabel();
        com_os.setForeground(new Color(0,255,0));
        com_os.setText(card.getCom_online()+"");
        com_dobanda.add(com_os);

        com_transfer=new JLabel("Comision transfer: ");
        com_transfer.setForeground(new Color(255,255,255));
        com_dobanda.add(com_transfer);

        com_ts=new JLabel();
        com_ts.setForeground(new Color(0,255,0));
        com_ts.setText(card.getCom_transfer()+"");
        com_dobanda.add(com_ts);

        com_factura=new JLabel("Comision factura: ");
        com_factura.setForeground(new Color(255,255,255));
        com_dobanda.add(com_factura);

        com_fs=new JLabel();
        com_fs.setForeground(new Color(0,255,0));
        com_fs.setText(card.getCom_factura()+"");
        com_dobanda.add(com_fs);

        dobanda=new JLabel("Dobanda: ");
        dobanda.setForeground(new Color(255,255,255));
        com_dobanda.add(dobanda);

        dobandas=new JLabel();
        dobandas.setForeground(new Color(0,255,0));
        dobandas.setText(card.getDobanda()+"");
        com_dobanda.add(dobandas);

        display.add(com_dobanda);
    }
}
