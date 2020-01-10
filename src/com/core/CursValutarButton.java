package com.core;

import com.cardGenerator.Currency;

import javax.swing.*;
import java.awt.*;

/**
 * Interfata are ca si scop afisarea cursului valutar curent din ziua respectiva, fiind formata din label-uri, fiecare coloana
 * din cadrul campului avand cate o semnificatie(lucru precizat pe primul rand al interfetei)
 */
public class CursValutarButton extends JFrame {
    private JLabel vandDolar, vandLira, vanzare, euro, dolar;
    private JLabel lira, cumparEuro, cumparDolar, cumparLira, vandEuro, cumparare;

    public CursValutarButton() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(56,56,56));
        setVisible(true);
        setResizable(false);
        setLabels();
    }

    private void initComponents() {
        cumparare = new JLabel("Cumparare");
        cumparare.setForeground(new Color(255,255,255));
        vanzare = new JLabel("Vanzare");
        vanzare.setForeground(new Color(255,255,255));
        euro = new JLabel("Euro:");
        euro.setForeground(new Color(255,255,255));
        dolar = new JLabel("Dolar:");
        dolar.setForeground(new Color(255,255,255));
        lira = new JLabel("Lira:");
        lira.setForeground(new Color(255,255,255));
        cumparEuro = new JLabel("");
        cumparEuro.setForeground(new Color(255,255,255));
        cumparDolar = new JLabel("");
        cumparDolar.setForeground(new Color(255,255,255));
        cumparLira = new JLabel("");
        cumparLira.setForeground(new Color(255,255,255));
        vandEuro = new JLabel("");
        vandEuro.setForeground(new Color(255,255,255));
        vandDolar = new JLabel("");
        vandDolar.setForeground(new Color(255,255,255));
        vandLira = new JLabel("");
        vandLira.setForeground(new Color(255,255,255));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(dolar)
                                        .addComponent(lira)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(euro)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(cumparare)
                                                .addComponent(cumparEuro, GroupLayout.Alignment.TRAILING))
                                        .addComponent(cumparDolar)
                                        .addComponent(cumparLira))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(vanzare)
                                        .addComponent(vandEuro)
                                        .addComponent(vandDolar)
                                        .addComponent(vandLira))
                                .addGap(91, 91, 91))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(cumparare)
                                        .addComponent(vanzare))
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(euro)
                                        .addComponent(cumparEuro)
                                        .addComponent(vandEuro))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(dolar)
                                        .addComponent(cumparDolar)
                                        .addComponent(vandDolar))
                                .addGap(70, 70, 70)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lira)
                                        .addComponent(cumparLira)
                                        .addComponent(vandLira))
                                .addGap(53, 53, 53))
        );
        pack();
    }

    private void setLabels(){
        cumparEuro.setText(Currency.EURO.getCumparare()+"");
        cumparDolar.setText(Currency.DOLAR.getCumparare()+"");
        cumparLira.setText(Currency.LIRA.getCumparare()+"");

        vandEuro.setText(Currency.EURO.getVanzare()+"");
        vandDolar.setText(Currency.DOLAR.getVanzare()+"");
        vandLira.setText(Currency.LIRA.getVanzare()+"");
    }
}
