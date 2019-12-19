package com.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecondApplication extends JFrame {
    /**
     * Buttons from here down are made for the second page of the menu app;
     */
    private JButton istoricTranzactii,soldCurent;
    private JButton IBAN,searchTransaction;
    private JPanel page2;
    private JButton switchPage,settings;
    private JLabel hello,image,currentFocus;
    private String username;

    public SecondApplication(String username){
        this.username=username;
        this.setTitle("Main");
        initSecondPage();
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.DARK_GRAY);
        this.makeSwitch();
        this.settingsAction();
        this.setResizable(false);
        this.focusOn();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void focusOn(){
        SwingUtilities.invokeLater(currentFocus::requestFocus);
    }

    private void settingsAction(){
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SecondApplication.this.dispose();
                SwingUtilities.invokeLater(()->new Settings(1,username));
            }
        });
    }

    private void makeSwitch(){
        switchPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SecondApplication.super.dispose();
                SwingUtilities.invokeLater(()->new Application(username));
            }
        });
    }

    private void setImage(){
        ImageIcon img=new ImageIcon("/home/cipri/Downloads/online-shop.png");
        image=new JLabel("",img,JLabel.CENTER);
    }

    private void initSecondPage(){
        this.page2=new JPanel();
        this.hello=new JLabel();
        this.currentFocus=new JLabel();
        setImage();
        this.istoricTranzactii=new JButton("Istoric");
        this.soldCurent=new JButton("SoldCurent");
        this.IBAN=new JButton("IBAN");
        this.searchTransaction=new JButton("SearchTranz");
        this.switchPage = new JButton();
        this.settings = new JButton();

        hello.setText("Hello, "+this.username);
        page2.setBackground(Color.gray);
        currentFocus.setText("");

        istoricTranzactii.setPreferredSize(new Dimension(125,30));
        istoricTranzactii.setBackground(new Color(56,56,56));
        istoricTranzactii.setForeground(new Color(211,211,211));

        soldCurent.setPreferredSize(new Dimension(125,30));
        soldCurent.setBackground(new Color(56,56,56));
        soldCurent.setForeground(new Color(211,211,211));

        IBAN.setPreferredSize(new Dimension(125,30));
        IBAN.setBackground(new Color(56,56,56));
        IBAN.setForeground(new Color(211,211,211));

        searchTransaction.setPreferredSize(new Dimension(125,30));
        searchTransaction.setBackground(new Color(56,56,56));
        searchTransaction.setForeground(new Color(211,211,211));

        GroupLayout page2Layout = new GroupLayout(page2);
        page2.setLayout(page2Layout);
        page2Layout.setHorizontalGroup(
                page2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(page2Layout.createSequentialGroup()
                                .addGroup(page2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(page2Layout.createSequentialGroup()
                                                .addGap(54, 54, 54)
                                                .addGroup(page2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(istoricTranzactii, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(soldCurent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(page2Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(image)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                                .addGroup(page2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(IBAN, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(hello, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchTransaction,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39))
        );
        page2Layout.setVerticalGroup(
                page2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(page2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(page2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(hello)
                                        .addComponent(image))
                                .addGap(133, 133, 133)
                                .addGroup(page2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(istoricTranzactii, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(IBAN, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 180, Short.MAX_VALUE)
                                .addGroup(page2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(soldCurent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchTransaction,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
                                .addGap(100, 100, 100))
        );
        page2.add(currentFocus);

        switchPage.setText("SwitchPage");
        settings.setIcon(new ImageIcon("/home/cipri/Downloads/settings.png"));
        settings.setBorderPainted(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(page2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(161, 161, 161)
                                .addComponent(switchPage)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(settings))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(page2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(switchPage)
                                                .addContainerGap())
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 12, Short.MAX_VALUE)
                                                .addComponent(settings))))
        );

        pack();
    }
}