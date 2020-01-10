package com.core;

import com.URL;
import com.card.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
/**
 * Prin intermediul clasei IstoricTranzactii, se afiseaza(folosind interfata grafica creata prin clasa ScrollTranzactii).
 * Afisarea tranzactiilor se face in functie de optiunea aleasa de utilizator, care poate opta sa afiseze 5,10,15,20 sau
 * toate tranzactiile disponibile in tabela tranzactii pentru ID-ul corespunzator cardului.
 */
public class IstoricTranzactii {
    private Enable enable;
    private Card card;
    private JFrame display;
    private JLabel select;
    private JComboBox<String> tranzactii;
    private JButton show;
    private ScrollTranzactii scroll=new ScrollTranzactii();

    public IstoricTranzactii(Card card,Enable e){
        scroll.getFrame().setVisible(false);
        this.card=card;
        this.enable=e;
        display=new JFrame("Istoric tranzactii!");
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
                scroll.getFrame().dispose();
            }
        });
    }

    /**
     * In cadrul acestei metode se initializeaza obiectul de tip JComboBox si totodata se extrage din tabela tranzactii numarul
     * acestora asociate cardului;
     * Daca nu exista nici o tranzactie, interfata de istoric se inchide afisandu-se un mesaj
     */
    private void initComponents(){
        select =new JLabel("Selectati numarul de tranzactii pe care doriti sa le vizualizati");
        select.setForeground(new Color(255,255,255));

        tranzactii =new JComboBox<String>();
        tranzactii.setForeground(new Color(0,100,100));
        tranzactii.addItem("5");
        tranzactii.addItem("10");
        tranzactii.addItem("15");
        tranzactii.addItem("20");
        String total="All"; int nr=0;
        try{
            Connection conn= DriverManager.getConnection(URL.url,"cipri","linux_mint");
            PreparedStatement pst=conn.prepareStatement("SELECT cardID FROM tranzactii WHERE cardID=?");
            pst.setInt(1,card.getID());
            ResultSet rs=pst.executeQuery();
            while(rs.next())
                ++nr;
            conn.close();
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't display last operations!","Error",JOptionPane.WARNING_MESSAGE);
            IstoricTranzactii.this.display.dispose();
        }

        if(nr==0){
            JOptionPane.showMessageDialog(null,"Nu exista tranzactii!");
            display.dispose();
            enable.enable();
            return;
        }
        total+="("+nr+")";
        tranzactii.addItem(total);

        show =new JButton("Display");
        show.setFocusPainted(false);
        show.setForeground(new Color(0,255,0));
        show.setBackground(new Color(56,56,56));

        display.add(select);
        display.add(tranzactii);
        display.add(show);
    }

    /**
     * Aceasta metoda afiseaza, in functie de numarul ales de utilizator, ultimele tranzactii efectuate. In cazul in care numarul
     * selectat depaseste numarul de tranzactii ale cardului, se vor afisa toate tranzactiile actuale.
     */
    private void configButton(){
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String number=(String)tranzactii.getSelectedItem();
                scroll.getFrame().setVisible(true);
                scroll.getTextArea().setText("");
                //not null
                int nr=0;
                if(!number.contains("All")){
                    nr = Integer.parseInt(number);
                   try{
                        Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
                        PreparedStatement pst=conn.prepareStatement("SELECT name,price,moneda,buy_date FROM tranzactii " +
                                "WHERE cardID=? ORDER BY buy_date LIMIT ?");
                        pst.setInt(1,card.getID());
                        pst.setInt(2,nr);
                        ResultSet rs=pst.executeQuery();
                        JTextArea text=scroll.getTextArea();
                        while(rs.next()){
                            String name=rs.getString(1);
                            double price=rs.getDouble(2);
                            String moneda=rs.getString(3);
                            Date date=rs.getDate(4);
                            text.append(name+"\n-amount "+price+" "+moneda+" in date "+date+"\n\n");
                        }
                        conn.close();
                    }catch (SQLException sql){
                        JOptionPane.showMessageDialog(null,"Can't display last operations!","Error",JOptionPane.WARNING_MESSAGE);
                        IstoricTranzactii.this.display.dispose();
                    }
                }else{
                    try{
                        Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
                        PreparedStatement pst=conn.prepareStatement("SELECT name,price,moneda,buy_date FROM tranzactii " +
                                "WHERE cardID=? ORDER BY buy_date");
                        pst.setInt(1,card.getID());
                        ResultSet rs=pst.executeQuery();
                        JTextArea text=scroll.getTextArea();
                        while(rs.next()){
                            String name=rs.getString(1);
                            double price=rs.getDouble(2);
                            String moneda=rs.getString(3);
                            Date date=rs.getDate(4);
                            text.append(name+"\n-amount "+price+" "+moneda+" in date "+date+"\n\n");
                        }
                        conn.close();
                    }catch (SQLException sql){
                        JOptionPane.showMessageDialog(null,"Can't display last operations!","Error",JOptionPane.WARNING_MESSAGE);
                        IstoricTranzactii.this.display.dispose();
                    }
                }
            }
        });
    }
}

