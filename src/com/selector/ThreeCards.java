package com.selector;

import com.URL;
import com.core.Application;
import com.core.Settings;
import com.login.LoginInterface;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

//TODO
public class ThreeCards{

    private JFrame display;
    private JButton card1;
    private JButton card2;
    private JButton card3;
    private JLabel focusLabel;
    private JLabel bankCard1, numberCard1, typeCard1;
    private JLabel bankCard2, numberCard2, typeCard2;
    private JLabel bankCard3, numberCard3, typeCard3;
    private int ID1,ID2,ID3;
    private String username;
    private JPanel panelCard1,panelCard2,panelCard3;
    private int selectOrRemove;

    public ThreeCards(String username,int selectOrRemove) {
        this.username=username;
        this.selectOrRemove=selectOrRemove;
        display=new JFrame("Three cards");
        display.setLayout(new GridLayout(1,3));
        display.getContentPane().setBackground(new Color(56,56,56));
        display.setLocationRelativeTo(null);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setSize(new Dimension(450, 330));
        initComponents();
        makeCardsF();
        updateData();
        configButtons();
        display.setResizable(false);
        display.setVisible(true);
    }

    private void removeCard(int ID){
        try{
            Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
            PreparedStatement pst=conn.prepareStatement("DELETE FROM tranzactii WHERE cardID=?");
            pst.setInt(1,ID);
            pst.executeUpdate();

            pst=conn.prepareStatement("DELETE FROM financiar WHERE cardID=?");
            pst.setInt(1,ID);
            pst.executeUpdate();

            pst=conn.prepareStatement("DELETE FROM card_type WHERE cardID=?");
            pst.setInt(1,ID);
            pst.executeUpdate();

            pst=conn.prepareStatement("DELETE FROM card_data WHERE cardID=?");
            pst.setInt(1,ID);
            pst.executeUpdate();

            conn.close();
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't remove the card..going " +
                    "back to settings","Remove card",JOptionPane.WARNING_MESSAGE);
            display.dispose();
            SwingUtilities.invokeLater(()->new Settings(1,username));
        }
    }

    private void configButtons() {
        card1.addActionListener((e) -> {
            if (selectOrRemove == 0) {
                makeID(ID1);
                return;
            }
            removeCard(ID1);
            display.dispose();
            SwingUtilities.invokeLater(()->new LoginInterface().start());
        });

        card2.addActionListener((e) -> {
            if(selectOrRemove == 0) {
                makeID(ID2);
                return;
            }
            removeCard(ID2);
            display.dispose();
            SwingUtilities.invokeLater(()->new LoginInterface().start());
        });

        card3.addActionListener((e)->{
            if(selectOrRemove == 0) {
                makeID(ID3);
                return;
            }
            removeCard(ID3);
            display.dispose();
            SwingUtilities.invokeLater(()->new LoginInterface().start());
        });
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
                    numberCard1.setText(number);
                    bankCard1.setText(banca);
                    typeCard1.setText(tip);
                }
                else if(contor==2){
                    ID2=cardID;
                    numberCard2.setText(number);
                    bankCard2.setText(banca);
                    typeCard2.setText(tip);
                }else{
                    ID3=cardID;
                    numberCard3.setText(number);
                    bankCard3.setText(banca);
                    typeCard3.setText(tip);
                }
                ++contor;
            }
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't update card data..exit the app","Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void makeID(int ID){
        try{
            Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
            PreparedStatement pst=conn.prepareStatement("UPDATE card_type SET current=? WHERE cardID=?");
            pst.setString(1,"T");
            pst.setInt(2,ID);
            pst.executeUpdate();
            display.dispose();
            SwingUtilities.invokeLater(()->new Application(username));
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't select the card from database.." +
                    "closing the app");
            System.exit(1);
        }
    }


    private void makeCardsF(){
        try{
            Connection conn= DriverManager.getConnection(URL.url,"cipri","linux_mint");
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

    private void initComponents() {
        panelCard1=new JPanel(new GridLayout(4,1));
        panelCard1.setBackground(new Color(56,56,56));
        panelCard2=new JPanel(new GridLayout(4,1));
        panelCard2.setBackground(new Color(56,56,56));
        panelCard3=new JPanel(new GridLayout(4,1));
        panelCard3.setBackground(new Color(56,56,56));
        card1 = new JButton();
        card1.setBackground(new Color(56,56,56));
        card1.setIcon(new ImageIcon("/home/cipri/Downloads/card1s.png"));
        card1.setBorderPainted(false);
        card2 = new JButton();
        card2.setBackground(new Color(56,56,56));
        card2.setIcon(new ImageIcon("/home/cipri/Downloads/card2s.png"));
        card2.setBorderPainted(false);
        card3 = new JButton();
        card3.setBackground(new Color(56,56,56));
        card3.setIcon(new ImageIcon("/home/cipri/Downloads/card3s.png"));
        card3.setBorderPainted(false);
        bankCard1 = new JLabel();
        bankCard1.setForeground(new Color(255,255,255));
        numberCard1 = new JLabel();
        numberCard1.setForeground(new Color(255,255,255));
        typeCard1 = new JLabel();
        typeCard1.setForeground(new Color(255,255,255));
        bankCard2 = new JLabel();
        bankCard2.setForeground(new Color(255,255,255));
        numberCard2 = new JLabel();
        numberCard2.setForeground(new Color(255,255,255));
        typeCard2 = new JLabel();
        typeCard2.setForeground(new Color(255,255,255));
        bankCard3 = new JLabel();
        bankCard3.setForeground(new Color(255,255,255));
        numberCard3 = new JLabel();
        numberCard3.setForeground(new Color(255,255,255));
        typeCard3 = new JLabel();
        typeCard3.setForeground(new Color(255,255,255));
        focusLabel = new JLabel();
        SwingUtilities.invokeLater(focusLabel::requestFocus);
        card1.setSize(new Dimension(70, 150));
        card2.setSize(new Dimension(70, 150));
        card3.setSize(new Dimension(70, 150));

        panelCard1.add(card1);
        panelCard1.add(bankCard1);
        panelCard1.add(typeCard1);
        panelCard1.add(numberCard1);

        panelCard2.add(card2);
        panelCard2.add(bankCard2);
        panelCard2.add(typeCard2);
        panelCard2.add(numberCard2);

        panelCard3.add(card3);
        panelCard3.add(bankCard3);
        panelCard3.add(typeCard3);
        panelCard3.add(numberCard3);

        display.add(panelCard1);
        display.add(panelCard2);
        display.add(panelCard3);
    }
}
