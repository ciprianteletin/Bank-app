package com.selector;

import com.URL;
import com.core.Application;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TwoCards extends JFrame {
    private JButton card1, card2;
    private JLabel bancaCard1, numarCard1, bancaCard2;
    private JLabel numarCard2, tipCard1, tipCard2;
    private JLabel focusLabel;
    private int ID1,ID2;
    private String username;

    public TwoCards(String username) {
        this.username=username;
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        makeCardsF();
        updateData();
        getContentPane().setBackground(new Color(56,56,56));
        setSize(new Dimension(600, 360));
        setResizable(false);
        setVisible(true);
        configButtons();
    }

    private void makeID(int ID){
        try{
            Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
            PreparedStatement pst=conn.prepareStatement("UPDATE card_type SET current=? WHERE cardID=?");
            pst.setString(1,"T");
            pst.setInt(2,ID);
            pst.executeUpdate();
            TwoCards.this.dispose();
            SwingUtilities.invokeLater(()->new Application(username));
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't select the card from database.." +
                    "closing the app");
            System.exit(1);
        }
    }


    private void configButtons(){
        card1.addActionListener((ae)->makeID(ID1));
        card2.addActionListener((ae)->makeID(ID2));
    }

    private void makeCardsF(){
        try{
            Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
            PreparedStatement pst=conn.prepareStatement("SELECT cardID FROM card_data WHERE user=?");
            pst.setString(1,username);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                int cardID=rs.getInt(1);
                pst=conn.prepareStatement("UPDATE card_type SET current=? WHERE cardID=?");
                pst.setString(1,"F");
                pst.setInt(2,cardID);
                pst.executeUpdate();
            }
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't connect to database," +
                    "so we can't choose a card..closing the app","Error",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }

    private void updateData(){
        try{
            int contor=1;
            Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
            PreparedStatement pst=conn.prepareStatement("SELECT cardID,number FROM card_data WHERE user=?");
            pst.setString(1,username);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                int cardID=rs.getInt(1);
                String number=rs.getString(2);
                pst=conn.prepareStatement("SELECT banca,tip FROM card_type WHERE cardID=?");
                pst.setInt(1,cardID);
                ResultSet result=pst.executeQuery();
                result.next();
                String banca=result.getString(1);
                String tip=result.getString(2);
                if(contor==1){
                    ID1=cardID;
                    numarCard1.setText(number);
                    bancaCard1.setText(banca);
                    tipCard1.setText(tip);
                }
                else{
                    ID2=cardID;
                    numarCard2.setText(number);
                    bancaCard2.setText(banca);
                    tipCard2.setText(tip);
                }
                ++contor;
            }
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't update card data..exit the app","Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {
        card1 = new JButton();
        card1.setBackground(new Color(56, 56, 56));
        card1.setForeground(new Color(255, 255, 255));
        card1.setIcon(new ImageIcon("/home/cipri/Downloads/card1.png"));
        card1.setBorderPainted(false);
        card2 = new JButton();
        card2.setBackground(new Color(56, 56, 56));
        card2.setForeground(new Color(255, 255, 255));
        card2.setIcon(new ImageIcon("/home/cipri/Downloads/card2.png"));
        card2.setBorderPainted(false);
        bancaCard1 = new JLabel();
        bancaCard1.setForeground(new Color(255, 255, 255));
        numarCard1 = new JLabel();
        numarCard1.setForeground(new Color(255, 255, 255));
        bancaCard2 = new JLabel();
        bancaCard2.setForeground(new Color(255, 255, 255));
        numarCard2 = new JLabel();
        numarCard2.setForeground(new Color(255, 255, 255));
        tipCard1 = new JLabel();
        tipCard1.setForeground(new Color(255, 255, 255));
        tipCard2 = new JLabel();
        tipCard2.setForeground(new Color(255, 255, 255));
        focusLabel = new JLabel();
        SwingUtilities.invokeLater(focusLabel::requestFocus);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(card1, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(bancaCard1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(numarCard1)
                                                .addGap(50, 50, 50)
                                                .addComponent(tipCard1)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(card2, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(bancaCard2)
                                        .addComponent(focusLabel)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(numarCard2)
                                                .addGap(50, 50, 50)
                                                .addComponent(tipCard2)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(card1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(card2, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(bancaCard1)
                                        .addComponent(bancaCard2))
                                .addComponent(focusLabel)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(numarCard1)
                                                        .addComponent(numarCard2))
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(tipCard2)
                                                        .addComponent(tipCard1))
                                                .addContainerGap(23, Short.MAX_VALUE))))
        );
        pack();
    }
}
