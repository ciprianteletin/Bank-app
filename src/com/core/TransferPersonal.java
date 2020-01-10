package com.core;

import com.URL;
import com.card.Card;
import com.cardGenerator.Currency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
/**
 * Are acelasi scop si functionalitate precum clasa Transfer, diferenta fiind ca in cazul transferului personal, IBAN-ul fiecarui
 * card de pe cont(exceptandu-l pe cel curent) este afisat, iar comisioanele de transfer nu sunt aplicabile de aceasta data.
 */
class TransferPersonal {
    private JFrame display;
    private JPanel inserter,ibans,push;
    private JLabel info,iban1,iban2,desc,desc1;
    private JTextField ibanText,pay;
    private JButton transfer;
    private Card card;
    private Enable enable;
    private Connection conn;
    private String username;
    private int cards;

    TransferPersonal(Card c,Enable e,String user){
        this.cards=0;
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
        initIban();
        initPush();
        populateLabels();
        addClose();
        addFocus();
        addKey();
        configTransfer();
        display.setSize(new Dimension(400,400));
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

                double plata=Math.round(Double.parseDouble(pay.getText())*100)/100.0;

                if(!card.getMoneda().equals("Lei")){
                    Currency currency=Currency.valueOf(card.getMoneda().toUpperCase());
                    plata=currency.convertDinLei(plata);
                }

                if(plata>card.getSuma()){
                    JOptionPane.showMessageDialog(null,"The amount inserted is greater than the amount from card!",
                            "Amount too low",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //limita nu se modifica, fiindca e transfer intre conturi proprii

                String iban=ibanText.getText().toUpperCase();
                if(cards==1){
                    if(!iban1.getText().toLowerCase().equals(iban.toLowerCase())){
                        JOptionPane.showMessageDialog(null,"The inserted IBAN doesn't belong to one of your personal cards",
                                "No IBAN",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }else{
                    if(!iban1.getText().toLowerCase().equals(iban.toLowerCase()) && !iban2.getText().toLowerCase().equals(iban.toLowerCase())){
                        JOptionPane.showMessageDialog(null,"The inserted IBAN doesn't belong to one of your personal cards",
                                "No IBAN",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                    enable.enable();
                    try{
                        PreparedStatement pst=conn.prepareStatement("SELECT cardID FROM card_data WHERE IBAN=?");
                        pst.setString(1,iban);
                        ResultSet rs=pst.executeQuery();
                        rs.next(); //only one card with that IBAN;
                        int ID=rs.getInt(1); //unde transfer;
                        card.setSuma(card.getSuma()-plata);
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
                        pst.setString(2,"Transfer personal dat");
                        pst.setDouble(3,plataCard);
                        pst.setString(4,card.getMoneda());
                        pst.setDate(5,Date.valueOf(LocalDate.now()));
                        pst.executeUpdate();
                        //tranzactii celalalt card;
                        pst.setInt(1,ID);
                        pst.setString(2,"Transfer personal primit");
                        pst.setDouble(3,plata); //cat am primit
                        pst.setString(4,moneda);
                        //data e la fel;
                        pst.executeUpdate();
                        conn.close();
                        JOptionPane.showMessageDialog(null,"Transfer between cards realised with success!");
                        display.dispose();
                    }catch (SQLException sql){
                        JOptionPane.showMessageDialog(null,"Can't realize a transfer right now..Try again later");
                        display.dispose();
                    }
                }
        });
    }

    private void populateLabels(){
        try{
            PreparedStatement pst=conn.prepareStatement("SELECT cardID,IBAN FROM card_data WHERE user=?");
            pst.setString(1,username);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                int ID=rs.getInt(1);
                if(ID!=card.getID()){
                    ++cards;
                    if(cards==1)
                        iban1.setText(rs.getString(2));
                    else
                        iban2.setText(rs.getString(2));
                }
            }
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't realize a transfer right now..Try again later");
            display.dispose();
        }
    }

    private void initInserter(){
        inserter=new JPanel(new GridLayout(2,2));
        inserter.setBackground(new Color(0,100,100));
        inserter.setPreferredSize(new Dimension(400,95));
        desc=new JLabel("Iban: ");
        desc.setForeground(new Color(255,255,255));
        inserter.add(desc);
        desc1=new JLabel("Suma transferata: ");
        desc1.setForeground(new Color(255,255,255));
        ibanText=new JTextField();
        ibanText.setForeground(new Color(0,100,100));
        ibanText.setPreferredSize(new Dimension(300,35));
        inserter.add(ibanText);
        pay=new JTextField("ex. 1000.25");
        pay.setForeground(new Color(56,56,56));
        pay.setPreferredSize(new Dimension(300,35));
        inserter.add(desc1);
        inserter.add(pay);
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
    }

    private void initIban(){
        ibans=new JPanel(new GridLayout(3,1));
        ibans.setBackground(new Color(56,56,56));
        ibans.setPreferredSize(new Dimension(400,240));

        info=new JLabel("Below you have the specific ibans of your cards: ");
        info.setForeground(new Color(255,255,255));

        iban1=new JLabel();
        iban1.setForeground(new Color(255,255,255));
        iban1.setFont(new Font(Font.MONOSPACED,Font.BOLD,16));

        iban2=new JLabel("Card 3 not found!");
        iban2.setForeground(new Color(255,255,255));
        iban2.setFont(new Font(Font.MONOSPACED,Font.BOLD,16));

        ibans.add(info); ibans.add(iban1); ibans.add(iban2);
        display.add(ibans);
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
