package com.core;

import com.URL;
import com.login.ChooseAccountInterface;
import com.login.LoginInterface;
import com.selector.ThreeCards;
import com.selector.TwoCards;
import com.card.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Settings extends JFrame {
    private JButton changeCard, addCard;
    private JButton changePassword, removeCard;
    private JButton blockCard, deleteAccount;
    private JButton back,logout;
    private JLabel image, hello;
    private int which;
    private String username;
    private Card card;

    public Settings(int which,String username) {
        this.username=username;
        this.which=which;
        this.setTitle("Settings");
        createImg();
        createCard();
        initialize();
        JLabel label=new JLabel("");
        setDimension();
        this.getContentPane().setBackground(new Color(92, 219, 149));
        this.setColors();
        this.makeOrder();
        this.backButton();
        this.logoutButton();
        this.add(label);
        this.deleteAccountButton();
        this.addCardButton();
        this.changeButton();
        this.changePasswordButton();
        this.removeButton();
        this.blockButton();
        this.setBlockName();
        SwingUtilities.invokeLater(label::requestFocus);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setBlockName(){
        if(card!=null && card.isBlocked())
            blockCard.setText("UnblockCard");
        else
            blockCard.setText("BlockCard");
    }

    private void createCard(){
        try{
            Connection c= DriverManager.getConnection(URL.url,"cipri","linux_mint");
            PreparedStatement pst=c.prepareStatement("SELECT cardID FROM card_data WHERE user=?");
            pst.setString(1,username);
            ResultSet rs=pst.executeQuery();
            if(rs.next())
                card=new Card(username);
            else
                card=null;
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Card failed to be created","Card failed",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }

    private void blockButton(){
        blockCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //TODO event for changing the name of the button
                Settings.this.setEnabled(false);
                SwingUtilities.invokeLater(()->new BlockUnblock(username,()->Settings.this.setEnabled(true),()->{
                    if(blockCard.getText().equals("BlockCard") && card!=null) {
                        blockCard.setText("UnblockCard");
                        card.setBlock("B");
                    }
                    else {
                        if(card!=null)
                            card.setBlock("A");
                        blockCard.setText("BlockCard");
                    }
                }));
            }
        });
    }

    private void removeButton(){
        removeCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
                    PreparedStatement pst=conn.prepareStatement("SELECT cardID FROM card_data WHERE user=?");
                    pst.setString(1,username);
                    ResultSet rs=pst.executeQuery();
                    int cardNr=0,ID=0;
                    while(rs.next()){
                        ++cardNr;
                        ID=rs.getInt(1);
                    }
                    if(cardNr==0){
                        JOptionPane.showMessageDialog(null,"You don't have a card!","No card",JOptionPane.WARNING_MESSAGE);
                    }
                    else if(cardNr==1){
                        pst=conn.prepareStatement("DELETE FROM tranzactii WHERE cardID=?");
                        pst.setInt(1,ID);
                        pst.executeUpdate();

                        pst=conn.prepareStatement("DELETE FROM financiar WHERE cardID=?");
                        pst.setInt(1,ID);
                        pst.executeUpdate();

                        pst=conn.prepareStatement("DELETE FROM card_type WHERE cardID=?");
                        pst.setInt(1,ID);
                        pst.executeUpdate();

                        pst=conn.prepareStatement("DELETE FROM data_limit WHERE cardID=?");
                        pst.setInt(1,ID);
                        pst.executeUpdate();

                        pst=conn.prepareStatement("DELETE FROM card_data WHERE cardID=?");
                        pst.setInt(1,ID);
                        pst.executeUpdate();

                        int result=JOptionPane.showConfirmDialog(null,"You have no card left! Do you want to delete your account?",
                                "No cards remaining",JOptionPane.YES_NO_OPTION);
                        if(result==JOptionPane.YES_OPTION){
                            pst=conn.prepareStatement("DELETE FROM adress WHERE user=?");
                            pst.setString(1,username);
                            pst.executeUpdate();

                            pst=conn.prepareStatement("DELETE FROM info WHERE user=?");
                            pst.setString(1,username);
                            pst.executeUpdate();

                            pst=conn.prepareStatement("DELETE FROM passwords WHERE user=?");
                            pst.setString(1,username);
                            pst.executeUpdate();

                            pst=conn.prepareStatement("DELETE FROM users WHERE user=?");
                            pst.setString(1,username);
                            pst.executeUpdate();
                            card.closeConnection();
                            Settings.this.dispose();
                            //account deleted
                            SwingUtilities.invokeLater(()->new LoginInterface().start());
                        }else {
                            //to make card null and to set buttons as needed
                            Settings.this.dispose();
                            SwingUtilities.invokeLater(() -> new Settings(which, username));
                        }
                    }else if(cardNr==2){
                        JOptionPane.showMessageDialog(null,"Select the card that you want to remove!",
                                "Remove card",JOptionPane.INFORMATION_MESSAGE);
                        Settings.this.dispose();
                        SwingUtilities.invokeLater(()->new TwoCards(username,1));
                    }else if(cardNr==3){
                        JOptionPane.showMessageDialog(null,"Select the card that you want to remove!",
                                "Remove card",JOptionPane.INFORMATION_MESSAGE);
                        Settings.this.dispose();
                        SwingUtilities.invokeLater(()->new ThreeCards(username,1));
                    }
                    pst=conn.prepareStatement("ALTER TABLE card_data AUTO_INCREMENT=1");
                    pst.executeUpdate();
                    conn.close();
                }catch (SQLException sql){
                    sql.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Can't remove the card!",
                            "Error",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void backButton(){
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Settings.this.dispose();
                if(card!=null)
                    card.closeConnection();
                if(which==0)
                    SwingUtilities.invokeLater(()->new Application(username));
                else
                    SwingUtilities.invokeLater(()->new SecondApplication(username));
            }
        });
    }

    private void logoutButton(){
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Settings.this.dispose();
                if(card!=null)
                    card.closeConnection();
                SwingUtilities.invokeLater(()->new LoginInterface().start());
            }
        });
    }

    private void changePasswordButton(){
        changePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Settings.this.dispose();
                if(card!=null)
                    card.closeConnection();
                SwingUtilities.invokeLater(()->new ChangePassword(username,which));
            }
        });
    }

    private void changeButton(){
        changeCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
                    PreparedStatement pst=conn.prepareStatement("SELECT cardID FROM card_data WHERE user=?");
                    pst.setString(1,username);
                    ResultSet rs=pst.executeQuery();
                    int cardNr=0;
                    while(rs.next()){
                        ++cardNr;
                    }
                    if (cardNr == 0) {
                        JOptionPane.showMessageDialog(null,"No card left to change!","No cards",JOptionPane.WARNING_MESSAGE);
                    } else if (cardNr == 1)
                        JOptionPane.showMessageDialog(null, "You only have one card!",
                                "One card", JOptionPane.WARNING_MESSAGE);
                    else if (cardNr == 2) {
                        SwingUtilities.invokeLater(() -> new TwoCards(username, 0));
                        card.closeConnection();
                        Settings.this.dispose();
                    } else {
                        SwingUtilities.invokeLater(() -> new ThreeCards(username, 0));
                        card.closeConnection();
                        Settings.this.dispose();
                    }
                }catch (SQLException sql){
                    JOptionPane.showMessageDialog(null,"Can't change the card...Error" +
                            " from database","Error",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void deleteAccountButton(){
        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int del=JOptionPane.showConfirmDialog(null,"All your data will be lost! " +
                        "Are you sure about this operation?","Delete account",JOptionPane.YES_NO_OPTION);
                if(del==JOptionPane.YES_OPTION){
                    try {
                        Connection conn = DriverManager.getConnection(URL.url, "cipri", "linux_mint");
                        //passwords table
                        PreparedStatement pst=conn.prepareStatement("DELETE FROM passwords WHERE user=?");
                        pst.setString(1,username);
                        pst.executeUpdate();
                        //address table
                        pst=conn.prepareStatement("DELETE FROM adress WHERE user=?");
                        pst.setString(1,username);
                        pst.executeUpdate();
                        //info table
                        pst=conn.prepareStatement("DELETE FROM info WHERE user=?");
                        pst.setString(1,username);
                        pst.executeUpdate();
                        //get cardID
                        pst=conn.prepareStatement("SELECT cardID FROM card_data WHERE user=?");
                        pst.setString(1,username);
                        ResultSet rs=pst.executeQuery();
                        while(rs.next()){
                            int ID=rs.getInt(1);
                            //card_type for all the cards
                            pst=conn.prepareStatement("DELETE FROM card_type WHERE cardID=?");
                            pst.setInt(1,ID);
                            pst.executeUpdate();
                            //financiar
                            pst=conn.prepareStatement("DELETE FROM financiar WHERE cardID=?");
                            pst.setInt(1,ID);
                            pst.executeUpdate();
                            //tranzactii
                            pst=conn.prepareStatement("DELETE FROM tranzactii WHERE cardID=?");
                            pst.setInt(1,ID);
                            pst.executeUpdate();
                            //data_limit
                            pst=conn.prepareStatement("DELETE FROM data_limit WHERE cardID=?");
                            pst.setInt(1,ID);
                            pst.executeUpdate();
                            //finally,card_data
                            pst=conn.prepareStatement("DELETE FROM card_data WHERE cardID=?");
                            pst.setInt(1,ID);
                            pst.executeUpdate();
                        }
                        //finally,delete the account
                        pst=conn.prepareStatement("DELETE FROM users WHERE user=?");
                        pst.setString(1,username);
                        pst.executeUpdate();

                        //if I delete the last user, I want the cardID to stay at his position;
                        pst=conn.prepareStatement("ALTER TABLE card_data AUTO_INCREMENT=1");
                        pst.executeUpdate();
                        conn.close();
                        if(card!=null)
                            card.closeConnection();
                        Settings.this.dispose(); //hmm, interesting, this is static
                        SwingUtilities.invokeLater(()->new LoginInterface().start());
                    }catch (SQLException err){
                        JOptionPane.showConfirmDialog(null,"Can't connect to database.." +
                                "abort this operation");
                    }
                }
            }
        });
    }

    private void addCardButton(){
        addCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
                    PreparedStatement pst=conn.prepareStatement("SELECT birthday,job FROM info WHERE user=?");
                    pst.setString(1,username);
                    ResultSet rs=pst.executeQuery();
                    rs.next(); //account 100% existent! And only one!
                    Date dt=rs.getDate(1);
                    LocalDate lc=dt.toLocalDate();
                    LocalDate current=LocalDate.now();
                    int age=(int)ChronoUnit.YEARS.between(lc,current);
                    String job=rs.getString(2);
                    if(card!=null)
                        SwingUtilities.invokeLater(()->new ChooseAccountInterface(username,age,job));
                    else
                        SwingUtilities.invokeLater(()->new ChooseAccountInterface(username,age,job,true));
                    conn.close();
                    if(card!=null)
                        card.closeConnection();
                    Settings.this.dispose();
                }catch (SQLException sql){
                    JOptionPane.showMessageDialog(null,"Can't connect to database.." +
                            "abort this operation..","Connection error",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void createImg(){
        ImageIcon img=new ImageIcon("/home/cipri/Downloads/technical-support.png");
        image=new JLabel("",img,JLabel.CENTER);
    }

    private void setColors(){
        changeCard.setBackground(new Color(5,56,107));
        changeCard.setForeground(new Color(237, 245, 225));
        changeCard.setFocusPainted(false);

        addCard.setBackground(new Color(5,56,107));
        addCard.setForeground(new Color(237, 245, 225));
        addCard.setFocusPainted(false);

        changePassword.setBackground(new Color(5,56,107));
        changePassword.setForeground(new Color(237, 245, 225));
        changePassword.setFocusPainted(false);

        removeCard.setBackground(new Color(5,56,107));
        removeCard.setForeground(new Color(237, 245, 225));
        removeCard.setFocusPainted(false);

        blockCard.setBackground(new Color(5,56,107));
        blockCard.setForeground(new Color(237, 245, 225));
        blockCard.setFocusPainted(false);

        removeCard.setBackground(new Color(5,56,107));
        removeCard.setForeground(new Color(237, 245, 225));
        removeCard.setFocusPainted(false);

        deleteAccount.setBackground(new Color(5,56,107));
        deleteAccount.setForeground(new Color(237, 245, 225));
        deleteAccount.setFocusPainted(false);

        logout.setBackground(new Color(5,56,107));
        logout.setForeground(new Color(237, 245, 225));

    }

    private void setDimension(){
        blockCard.setPreferredSize(new Dimension(130, 30));
        removeCard.setPreferredSize(new Dimension(130, 30));
        changeCard.setPreferredSize(new Dimension(130,30));
        addCard.setPreferredSize(new Dimension(130,30));
        changePassword.setPreferredSize(new Dimension(130,30));
        deleteAccount.setPreferredSize(new Dimension(130,30));
        logout.setPreferredSize(new Dimension(130,30));
    }

    private void initialize(){
        changeCard = new JButton();
        addCard = new JButton();
        changePassword = new JButton();
        removeCard = new JButton();
        blockCard = new JButton();
        deleteAccount = new JButton();
        back = new JButton();
        logout=new JButton();
        hello = new JLabel();

        changeCard.setText("ChangeCard");
        addCard.setText("AddCard");
        changePassword.setText("ChangePass");
        removeCard.setText("RemoveCard");
        blockCard.setText("BlockCard");
        deleteAccount.setText("DeleteAccount");
        logout.setText("Logout");
        back.setIcon(new ImageIcon("/home/cipri/Downloads/left-arrow.png"));
        back.setBorderPainted(false);
        hello.setText("Hello, "+this.username);
    }

    private void makeOrder() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(155, 155, 155)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                        .addComponent(addCard,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(changeCard,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(changePassword,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(removeCard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(blockCard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(deleteAccount,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(logout,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(back))
                                .addContainerGap(127, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(image)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(hello)
                                .addGap(57, 57, 57))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(image)
                                        .addComponent(hello))
                                .addGap(75, 75, 75)
                                .addComponent(changeCard,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(addCard,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(changePassword, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(removeCard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(blockCard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(deleteAccount,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                .addGap(37,37,37)
                                .addComponent(logout,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                .addGap(106, 106, 106)
                                .addComponent(back))
        );
        pack();
    }
}
