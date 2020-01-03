package com.core;

import com.URL;
import com.automata.FiniteAutomata;
import com.automata.State;
import com.card.Card;
import com.cardGenerator.Currency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

class Transfer {
    private JFrame display;
    private JPanel inserter,push;
    private JLabel desc,desc1,desc2;
    private JTextField ibanText,pay,user;
    private JButton transfer;
    private Card card;
    private Enable enable;
    private Connection conn;
    private String username;

    Transfer(Card c,Enable e,String user){
        this.username=user;
        this.card=c;
        this.enable=e;
        try{
            conn= DriverManager.getConnection(URL.url,"cipri","linux_mint");
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't realize a transfer right now..Try again later");
            return;
        }
        display=new JFrame("Transfer personal");
        display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        display.setLocationRelativeTo(null);
        display.setResizable(false);
        display.setLayout(new BoxLayout(display.getContentPane(),BoxLayout.PAGE_AXIS));
        display.getContentPane().setBackground(new Color(56,56,56));
        initInserter();
        initPush();
        configTransfer();
        addClose();
        addFocus();
        addKey();
        display.setSize(new Dimension(400,240));
        display.setVisible(true);
    }

    private void addClose(){
        display.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                enable.enable();
                try {
                    conn.close();
                }catch (SQLException sql){
                    //NOTHING
                }
            }
        });
    }

    private void initInserter(){
        inserter=new JPanel(new GridLayout(3,2));
        inserter.setBackground(new Color(0,100,100));
        inserter.setPreferredSize(new Dimension(400,145));
        desc=new JLabel("Iban: ");
        desc.setForeground(new Color(255,255,255));
        inserter.add(desc);
        desc1=new JLabel("Suma transferata:");
        desc1.setForeground(new Color(255,255,255));
        desc2=new JLabel("Username: ");
        desc2.setForeground(new Color(255,255,255));
        ibanText=new JTextField();
        ibanText.setForeground(new Color(0,100,100));
        ibanText.setPreferredSize(new Dimension(300,35));
        inserter.add(ibanText);
        pay=new JTextField("ex. 1000.25");
        pay.setForeground(new Color(56,56,56));
        pay.setPreferredSize(new Dimension(300,35));
        inserter.add(desc1);
        inserter.add(pay);
        user=new JTextField("Username: ");
        user.setForeground(new Color(56,56,56));
        user.setPreferredSize(new Dimension(300,3));
        inserter.add(desc2);
        inserter.add(user);
        display.add(inserter);
    }

    private void addFocus(){
        pay.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(pay.getForeground().equals(new Color(56,56,56))) {
                    pay.setText("");
                    pay.setForeground(new Color(0,100,100));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(pay.getText().isEmpty()){
                    pay.setText("ex. 1000.25");
                    pay.setForeground(new Color(56,56,56));
                }
            }
        });

        user.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(user.getForeground().equals(new Color(56,56,56))){
                    user.setText("");
                    user.setForeground(new Color(0,100,100));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(user.getText().isEmpty()){
                    user.setText("Username: ");
                    user.setForeground(new Color(56,56,56));
                }
            }
        });
    }

    private void configTransfer(){
        transfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(ibanText.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"No IBAN inserted!","Error",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(pay.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"No amount to transfer!","Error",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(user.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"No user inserted!","Error",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(!checkUsername()){
                    JOptionPane.showMessageDialog(null,"Username must respect sign up convention!",
                            "Error",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(user.getText().toLowerCase().equals(username)){
                    JOptionPane.showMessageDialog(null,"For personal transfer,use TransPers button");
                    return;
                }

                double plata=Double.parseDouble(pay.getText());
                if(!card.getMoneda().equals("Lei")){
                    Currency currency=Currency.valueOf(card.getMoneda().toUpperCase());
                    plata=currency.convertDinLei(plata);
                }

                double comm=Math.round(plata*card.getCom_transfer())/100.0;
                if(plata+comm>card.getSuma()){
                    JOptionPane.showMessageDialog(null,"The amount inserted is greater than the amount from card!",
                            "Amount too low",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(plata+comm>card.getLim_transfer() && card.getLim_transfer()!=-1){
                    JOptionPane.showMessageDialog(null,"Your limit is exceeded");
                    return;
                }

                enable.enable();
                String iban=ibanText.getText().toUpperCase();
                try{
                    PreparedStatement pst=conn.prepareStatement("SELECT cardID FROM card_data WHERE IBAN=? AND user=?");
                    pst.setString(1,iban);
                    pst.setString(2,user.getText());
                    ResultSet rs=pst.executeQuery();
                    //maybe the user inserted the iban of another person
                    if(!rs.next()){
                        JOptionPane.showMessageDialog(null,"The combination IBAN-user does not exist","User not found",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int ID=rs.getInt(1); //unde transfer;
                    card.setSuma(card.getSuma()-plata-comm);
                    if (card.getLim_transfer() != -1)
                        card.setLim_transfer(card.getLim_transfer() - plata);
                    double plataCard=plata; //in case we modify it
                    card.updateDB();
                    //substract the sum from our card

                    pst=conn.prepareStatement("SELECT moneda FROM financiar WHERE cardID=?");
                    pst.setInt(1,ID);
                    rs=pst.executeQuery();
                    rs.next();
                    String moneda=rs.getString(1);
                    //currency of the other card;
                    if(!card.getMoneda().equals("Lei")){
                        Currency currency=Currency.valueOf(card.getMoneda().toUpperCase());
                        plata=currency.convertBetween(plata);
                    }

                    if(!moneda.equals("Lei")){
                        Currency currency=Currency.valueOf(moneda.toUpperCase());
                        plata=currency.forTransfer(plata); //sa cumparam la valoarea actuala, nu cu taxa de vanzare a bancii;
                    }

                    //update financiar card cu iban dat
                    pst=conn.prepareStatement("UPDATE financiar SET suma=suma+? WHERE cardID=?");
                    pst.setDouble(1,plata);
                    pst.setInt(2,ID);
                    pst.executeUpdate();
                    //tranzactii
                    //tranzactie card curent
                    pst=conn.prepareStatement("INSERT INTO tranzactii VALUES (?,?,?,?,?)");
                    pst.setInt(1,card.getID());
                    pst.setString(2,"Transfer catre "+user.getText());
                    pst.setDouble(3,plataCard);
                    pst.setString(4,card.getMoneda());
                    pst.setDate(5,Date.valueOf(LocalDate.now()));
                    pst.executeUpdate();
                    //tranzactii celalalt card;
                    pst.setInt(1,ID);
                    pst.setString(2,"Transfer de la "+username);
                    pst.setDouble(3,plata); //cat am primit
                    pst.setString(4,moneda);
                    //data e la fel;
                    pst.executeUpdate();
                    conn.close();
                    JOptionPane.showMessageDialog(null,"Transfer between accounts realised with success!");
                    display.dispose();
                }catch (SQLException sql){
                    JOptionPane.showMessageDialog(null,"Can't realize a transfer right now..Try again later");
                    display.dispose();
                }
            }
        });
    }

    private boolean checkUsername(){
        State s0=new State("","[A-Z]");
        State s1=new State("[A-Z]","[a-z]");
        State s2=new State("[a-z]","[0-9]");
        State s3=new State("[0-9]","",true);
        FiniteAutomata automate=new FiniteAutomata(user.getText(),s0,s1,s2,s3);
        return automate.validateData();
    }

    private void addKey() {
        pay.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char c = keyEvent.getKeyChar();
                if ((c < '0' || c > '9') && c != '.' && c != 8)
                    keyEvent.consume();
                else if (c == '.') {
                    if (pay.getText().isEmpty() || pay.getText().contains("."))
                        keyEvent.consume();
                } else if (c >= '0' && c <= '9') {
                    if (pay.getText().length() == 1 && pay.getText().charAt(0) == '0') {
                        pay.setText(c + "");
                        keyEvent.consume();
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //NOTHING
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //NOTHING
            }
        });

        this.user.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(user.getText().length()>=12)
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //nothing, but needed override
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //nothing, but needed override
            }
        });
    }

    private void initPush(){
        push=new JPanel(new FlowLayout(FlowLayout.CENTER));
        push.setPreferredSize(new Dimension(400,65));
        push.setBackground(new Color(223,80,32));
        transfer=new JButton("Transfer!");
        transfer.setFocusPainted(false);
        transfer.setForeground(new Color(0,255,0));
        transfer.setBackground(new Color(56,56,56));
        transfer.setPreferredSize(new Dimension(125,50));
        push.add(transfer);
        display.add(push);
    }
}

