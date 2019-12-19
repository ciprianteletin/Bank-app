package com.core;

import com.login.LoginInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JFrame {
    private JButton changeCard, addCard;
    private JButton changePassword, removeCard;
    private JButton blockCard, deleteAccount;
    private JButton back,logout;
    private JLabel image, hello;
    private int which;
    private String username;

    public Settings(int which,String username) {
        this.username=username;
        this.which=which;
        this.setTitle("Settings");
        createImg();
        initialize();
        JLabel label=new JLabel("");
        setDimension();
        this.getContentPane().setBackground(new Color(92, 219, 149));
        this.setColors();
        this.makeOrder();
        this.backButton();
        this.mainButtons();
        this.add(label);
        SwingUtilities.invokeLater(label::requestFocus);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void backButton(){
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Settings.this.dispose();
                if(which==0)
                    SwingUtilities.invokeLater(()->new Application(username));
                else
                    SwingUtilities.invokeLater(()->new SecondApplication(username));
            }
        });
    }

    private void mainButtons(){
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Settings.this.dispose();
                SwingUtilities.invokeLater(()->new LoginInterface().start());
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

        addCard.setBackground(new Color(5,56,107));
        addCard.setForeground(new Color(237, 245, 225));

        changePassword.setBackground(new Color(5,56,107));
        changePassword.setForeground(new Color(237, 245, 225));

        removeCard.setBackground(new Color(5,56,107));
        removeCard.setForeground(new Color(237, 245, 225));

        blockCard.setBackground(new Color(5,56,107));
        blockCard.setForeground(new Color(237, 245, 225));

        removeCard.setBackground(new Color(5,56,107));
        removeCard.setForeground(new Color(237, 245, 225));

        deleteAccount.setBackground(new Color(5,56,107));
        deleteAccount.setForeground(new Color(237, 245, 225));

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
