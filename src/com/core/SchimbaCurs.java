package com.core;

import com.card.Card;
import com.cardGenerator.Currency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SchimbaCurs {
    private Enable enable;
    private Card card;
    private JFrame display;
    private JLabel moneda_crt;
    private JComboBox<String> currency;
    private JButton change;

    public SchimbaCurs(Card card,Enable e){
        this.card=card;
        this.enable=e;
        display=new JFrame("Schimbare curs!");
        display.getContentPane().setBackground(new Color(56,56,56));
        display.setSize(new Dimension(400,150));
        display.setLayout(new GridLayout(3,1));
        display.setResizable(false);
        display.setLocationRelativeTo(null);
        display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        display.setVisible(true);

        initComponents();
        closeOperation();
        configButton();
    }

    private void closeOperation(){
        display.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                enable.enable();
            }
        });
    }

    private void initComponents(){
        moneda_crt=new JLabel("Actual currency: "+card.getMoneda());
        moneda_crt.setForeground(new Color(255,255,255));

        currency=new JComboBox<String>();
        currency.setForeground(new Color(0,100,100));
        currency.addItem("Lei");
        currency.addItem("Euro");
        currency.addItem("Dolar");
        currency.addItem("Lira");

        change=new JButton("Change!");
        change.setFocusPainted(false);
        change.setForeground(new Color(0,255,0));
        change.setBackground(new Color(56,56,56));

        display.add(moneda_crt);
        display.add(currency);
        display.add(change);
    }

    private void configButton(){
        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //cast
                String choice=(String)currency.getSelectedItem();
                enable.enable();
                //not null bruh
                if(choice.equals(card.getMoneda()))
                    JOptionPane.showMessageDialog(null,"Your currency is already the one selected!");
                else{
                    int result=JOptionPane.showConfirmDialog(null,"Conversion taxes will be applied! Are you" +
                            " sure that you want to continue?","Taxes applicable",JOptionPane.YES_NO_OPTION);
                    if(result==JOptionPane.YES_OPTION){
                        if(card.getMoneda().equals("Lei")){
                            Currency curr=Currency.valueOf(choice.toUpperCase());
                            card.setSuma(curr.convertDinLei(card.getSuma()));
                            card.setMoneda(choice);
                            if(card.getLim_transfer()!=-1)
                                //limita nu va fi conditionata de taxe, la convertirea inapoi voi aplica between
                                card.setLim_transfer(curr.convertDinLei(card.getLim_transfer()));
                        }else{
                            if(choice.equals("Lei")){
                                Currency curr=Currency.valueOf(card.getMoneda().toUpperCase());
                                card.setMoneda("Lei");
                                if(card.getLim_transfer()!=-1)
                                    card.setLim_transfer(curr.convertBetween(card.getLim_transfer()));
                                card.setSuma(curr.convertInLei(card.getSuma()));
                            }else{
                              //convertim in lei, apoi in ce a fost ales;
                              Currency c1=Currency.valueOf(card.getMoneda().toUpperCase());
                              Currency c2=Currency.valueOf(choice.toUpperCase());
                              //folosesc convertBetween deoarece nu am o metoda directa de convertire din 2 metode straine, astfel ca
                              //utilizatorul nu pierde foarte mult(in functie de cursul zilei)
                              card.setMoneda(choice);
                              card.setSuma(c1.convertBetween(card.getSuma()));
                              card.setSuma(c2.convertDinLei(card.getSuma()));
                              if(card.getLim_transfer()!=-1){
                                  card.setLim_transfer(c1.convertBetween(card.getLim_transfer()));
                                  card.setLim_transfer(c2.convertDinLei(card.getLim_transfer()));
                              }
                            }
                        }
                        card.updateDB();
                        display.dispose();
                    }
                }
            }
        });
    }
}
