package com.core;

import com.URL;
import com.card.Card;
import com.card.SoldCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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
    private Card card;

    public SecondApplication(String username){
        this.username=username;
        createCard();
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
        this.makeButtons();
        this.disableButtons();
    }

    private void disableButtons(){
        if(card==null || card.isBlocked()){
            IBAN.setEnabled(false);
            istoricTranzactii.setEnabled(false);
            searchTransaction.setEnabled(false);
            soldCurent.setEnabled(false);
        }
    }

    private void createCard(){
        try{
            Connection c= DriverManager.getConnection(URL.url,"root","linux_mint");
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

    private void focusOn(){
        SwingUtilities.invokeLater(currentFocus::requestFocus);
    }

    /**
     * Face transferul catre interfata grafica destinata setarilor. Se apeleaza constructorul pentru interfata de Setari
     */
    private void settingsAction(){
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SecondApplication.this.dispose();
                if(card!=null)
                    card.closeConnection();
                SwingUtilities.invokeLater(()->new Settings(1,username));
            }
        });
    }

    /**
     * Face legatura dintre cele 2 interfete pentru gestiunea operatiilor cardului.
     */
    private void makeSwitch(){
        switchPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SecondApplication.super.dispose();
                if(card!=null)
                    card.closeConnection();
                SwingUtilities.invokeLater(()->new Application(username));
            }
        });
    }

    private void setImage(){
        ImageIcon img=new ImageIcon("D:\\Aplicatie Bancara\\src\\imagini\\online-shop.png");
        image=new JLabel("",img,JLabel.CENTER);
    }

    /**
     * Se apeleaza interfata grafica specifica fiecarui buton apasat.
     */
    private void makeButtons(){
        IBAN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null,"Press print button if you want" +
                        " your info into a PDF","IBAN display",JOptionPane.DEFAULT_OPTION);
                SwingUtilities.invokeLater(()->new IbanDisplay(username));
            }
        });

        soldCurent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SwingUtilities.invokeLater(()->new SoldCard(card));
            }
        });

        istoricTranzactii.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SecondApplication.this.setEnabled(false);
                SwingUtilities.invokeLater(()->new IstoricTranzactii(card,()->SecondApplication.this.setEnabled(true)));
            }
        });

        searchTransaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SecondApplication.this.setEnabled(false);
                SwingUtilities.invokeLater(()->new Search(card,()->SecondApplication.this.setEnabled(true)));
            }
        });
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
        istoricTranzactii.setFocusPainted(false);

        soldCurent.setPreferredSize(new Dimension(125,30));
        soldCurent.setBackground(new Color(56,56,56));
        soldCurent.setForeground(new Color(211,211,211));
        soldCurent.setFocusPainted(false);

        IBAN.setPreferredSize(new Dimension(125,30));
        IBAN.setBackground(new Color(56,56,56));
        IBAN.setForeground(new Color(211,211,211));
        IBAN.setFocusPainted(false);

        searchTransaction.setPreferredSize(new Dimension(125,30));
        searchTransaction.setBackground(new Color(56,56,56));
        searchTransaction.setForeground(new Color(211,211,211));
        searchTransaction.setFocusPainted(false);

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
        settings.setIcon(new ImageIcon("D:\\Aplicatie Bancara\\src\\imagini\\settings.png"));
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
