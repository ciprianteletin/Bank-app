package com.core;

import com.URL;
import com.card.Card;
import com.cardGenerator.Currency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

class Facturi {
    private JFrame display;
    private JPanel date,press;
    private JLabel furn,price;
    private JTextField furnizor,pret;
    private JButton pay;
    private Card card;
    private Enable enable;

    Facturi(Card c, Enable e){
        this.card=c;
        this.enable=e;
        this.display=new JFrame("Plata facturi");
        this.display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.display.getContentPane().setBackground(new Color(56,56,56));
        this.display.setSize(new Dimension(300,500));
        this.display.setLayout(new BoxLayout(display.getContentPane(),BoxLayout.PAGE_AXIS));
        this.display.setLocationRelativeTo(null);
        this.display.setResizable(false);
        this.display.add(Box.createVerticalStrut(50));
        initDate();
        this.display.add(Box.createVerticalStrut(70));
        initPress();
        closeOpp();
        pressConfig();
        SwingUtilities.invokeLater(furn::requestFocus);
        this.display.setVisible(true);
    }

    private void closeOpp(){
        this.display.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                enable.enable();
            }
        });
    }

    private void pressConfig(){
        pay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double pre=Math.round(Double.parseDouble(pret.getText())*100)/100.0;
                if(!card.getMoneda().equals("Lei")){
                    Currency currency=Currency.valueOf(card.getMoneda().toUpperCase());
                    pre=currency.convertDinLei(pre);
                }

                if(pre>card.getSuma()){
                    JOptionPane.showMessageDialog(null,"Nu exista suficienti bani pe card!\nSuma curenta: "+card.getSuma(),
                            "Suma insuficienta",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(pre>card.getLim_transfer() && card.getLim_transfer()!=-1){
                    JOptionPane.showMessageDialog(null,"Ati depasit limita admisa de plati!\nLimita curenta: "+card.getLim_transfer(),
                            "Limita atinsa",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                enable.enable();
                try{
                    Connection conn= DriverManager.getConnection(URL.url,"cipri","linux_mint");
                    PreparedStatement pst=conn.prepareStatement("INSERT INTO tranzactii VALUES (?,?,?,?,?,?)");
                    pst.setInt(1,card.getID());
                    pst.setString(2,furnizor.getText());
                    pst.setDouble(3,pre);
                    pst.setString(4,card.getMoneda());
                    pst.setInt(5,1);
                    Date date=Date.valueOf(LocalDate.now());
                    pst.setDate(6,date);
                    pst.executeUpdate();
                }catch (SQLException sql){
                    JOptionPane.showMessageDialog(null,"Esuare tranzactie!","Error",JOptionPane.WARNING_MESSAGE);
                }
                double comm=Math.round(((pre*card.getCom_factura())/100)*100)/100.0;
                //aplicare comision
                card.setSuma(card.getSuma()-pre-comm);
                if(card.getLim_transfer()!=-1)
                    card.setLim_transfer(card.getLim_transfer()-pre);
                card.updateDB();
                display.dispose();
            }
        });
    }

    private void initPress(){
        FlowLayout layout=new FlowLayout(FlowLayout.CENTER);
        layout.setVgap(10);
        press=new JPanel(layout);
        press.setBackground(new Color(223,80,32));
        press.setPreferredSize(new Dimension(400,80));
        pay=new JButton("Pay!");
        pay.setForeground(new Color(0,255,0));
        pay.setBackground(new Color(56,56,56));
        pay.setFocusPainted(false);
        pay.setPreferredSize(new Dimension(100,50));
        pay.setBorderPainted(false);
        press.add(pay);
        display.add(press);
    }

    private void initDate(){
        GridLayout layout=new GridLayout(2,2);
        layout.setVgap(200);
        date=new JPanel(layout);
        date.setBackground(new Color(56,56,56));
        date.setPreferredSize(new Dimension(400,300));
        furn=new JLabel("Furnizor: ");
        furn.setForeground(new Color(255,255,255));
        price=new JLabel("Pret: ");
        price.setForeground(new Color(255,255,255));

        furnizor=new JTextField();
        furnizor.setForeground(new Color(56,56,56));
        pret=new JTextField("0.0");
        pret.setSize(new Dimension(200,70));
        pret.setForeground(new Color(50,50,200));
        date.add(furn); date.add(furnizor);
        date.add(price); date.add(pret);
        this.display.add(date);
        addFocus();
        addKey();
    }

    private void addFocus(){
        pret.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(pret.getForeground().equals(new Color(50,50,200))) {
                    pret.setText("");
                    pret.setForeground(new Color(56,56,56));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(pret.getText().isEmpty()){
                    pret.setText("0.0");
                    pret.setForeground(new Color(50,50,200));
                }
            }
        });
    }

    private void addKey(){
        pret.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char c=keyEvent.getKeyChar();
                if((c<'0' || c>'9') && c!='.' && c!=8)
                    keyEvent.consume();
                else if(c=='.'){
                    if(pret.getText().isEmpty() || pret.getText().contains("."))
                        keyEvent.consume();
                }else if(c>='0' && c<='9'){
                    if(pret.getText().length()==1 && pret.getText().charAt(0)=='0') {
                        pret.setText(c + "");
                        keyEvent.consume();
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        furnizor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char c=keyEvent.getKeyChar();
                if(!Character.isAlphabetic(c) && !Character.isSpaceChar(c))
                    keyEvent.consume();
                else if(!furnizor.getText().isEmpty() && Character.isSpaceChar(c) &&
                        (Character.isSpaceChar(furnizor.getText().charAt(furnizor.getText().length()-1))))
                    keyEvent.consume();
                else if(furnizor.getText().isEmpty() && Character.isSpaceChar(c))
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
    }
}
