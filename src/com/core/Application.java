package com.core;

import com.URL;
import com.card.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Application extends JFrame{
    /**
     * Buttons used for the first page, although some of them are common for both of them, like the image or currentFocus;
     */
    private JButton switchPage, plataFactura;
    private JButton plataOnline, transfer;
    private JButton transferPersonal, conversie;
    private JButton cursValutar, settings;
    private JLabel hello, currentFocus, image;
    private JPanel page1;
    private String username;
    private Card card;

    public Application(String username) {
            this.username=username;
            createCard();
            this.setTitle("Main");
            initFirstPage();
            this.setLocationRelativeTo(null);
            this.getContentPane().setBackground(Color.DARK_GRAY);
            this.makeSwitch();
            this.settingsAction();
            this.focusOn();
            this.setResizable(false);
            setVisible(true);
            makeButtons();
            disableButtons();
        }

        private void disableButtons(){
            if(card == null || card.isBlocked()){
                plataFactura.setEnabled(false);
                plataOnline.setEnabled(false);
                transfer.setEnabled(false);
                transferPersonal.setEnabled(false);
                conversie.setEnabled(false);
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

        private void settingsAction(){
            settings.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Application.super.dispose();
                    if(card!=null)
                        card.closeConnection();
                    SwingUtilities.invokeLater(()->new Settings(0,username));
                }
            });
        }

        private void makeSwitch(){
            switchPage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                        Application.super.dispose();
                        if(card!=null)
                            card.closeConnection();
                        SwingUtilities.invokeLater(()->new SecondApplication(username));
                    }
            });
        }

    /**
     * La fel ca in cazul SecondApplication, se vor apela interfetele specifice fiecarui buton in parte
     */
    private void makeButtons(){
            cursValutar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    SwingUtilities.invokeLater(CursValutarButton::new);
                }
            });

            conversie.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Application.this.setEnabled(false);
                    SwingUtilities.invokeLater(()->new SchimbaCurs(card,()->Application.this.setEnabled(true)));
                }
            });

            plataFactura.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JOptionPane.showMessageDialog(null,"Introduceti suma in lei, indiferent de tipul monedei de pe card\n" +
                            "Nu se vor percepe comisioane referitoare la convertirea sumei de plata");
                    Application.this.setEnabled(false);
                    SwingUtilities.invokeLater(()->new Facturi(card,()->Application.this.setEnabled(true)));
                }
            });

            plataOnline.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JOptionPane.showMessageDialog(null,"Suma introdusa trebuie sa fie in lei.\n" +
                            "Sunt acceptate doar urmatoarele site-uri:\n" +
                            "Emag,PCGarage si Cel.ro");
                    Application.this.setEnabled(false);
                    SwingUtilities.invokeLater(()->new Online(card,()->Application.this.setEnabled(true)));
                }
            });

            transferPersonal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try{
                        Connection conn=DriverManager.getConnection(URL.url,"root","linux_mint");
                        PreparedStatement pst=conn.prepareStatement("SELECT cardID FROM card_data WHERE user=?");
                        pst.setString(1,username);
                        ResultSet rs=pst.executeQuery();
                        int nr=0;
                        while (rs.next())
                            ++nr;
                        if(nr==1)
                            JOptionPane.showMessageDialog(null,"You have only one card!");
                        else {
                            JOptionPane.showMessageDialog(null,"Insert the amount you want to transfer in Lei");
                            Application.this.setEnabled(false);
                            SwingUtilities.invokeLater(() -> new TransferPersonal(card, () -> Application.this.setEnabled(true),username));
                        }
                    }catch (SQLException sql){
                        JOptionPane.showMessageDialog(null,"Can't transfer data..","Error",JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            transfer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Application.this.setEnabled(false);
                    SwingUtilities.invokeLater(()->new Transfer(card,()->Application.this.setEnabled(true),username));
                }
            });
        }

        private void setImage(){
            ImageIcon img=new ImageIcon("D:\\Aplicatie Bancara\\src\\imagini\\online-shop.png");
            image=new JLabel("",img,JLabel.CENTER);
        }

        private void initFirstPage() {

            page1 = new JPanel();
            hello = new JLabel();
            currentFocus = new JLabel();
            plataFactura = new JButton();
            plataOnline = new JButton();
            transfer = new JButton();
            transferPersonal = new JButton();
            conversie = new JButton();
            cursValutar = new JButton();
            setImage();
            switchPage = new JButton();
            settings = new JButton();

            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            page1.setBackground(Color.gray);

            hello.setText("Hello, "+this.username);

            currentFocus.setText("");

            plataFactura.setText("PlataFactura");
            plataFactura.setPreferredSize(new Dimension(125, 30));
            plataFactura.setBackground(new Color(56,56,56));
            plataFactura.setForeground(new Color(211,211,211));
            plataFactura.setFocusPainted(false);

            plataOnline.setText("PlataOnline");
            plataOnline.setPreferredSize(new Dimension(125, 30));
            plataOnline.setBackground(new Color(56,56,56));
            plataOnline.setForeground(new Color(211,211,211));
            plataOnline.setFocusPainted(false);

            transfer.setText("Transfer");
            transfer.setPreferredSize(new Dimension(125, 30));
            transfer.setBackground(new Color(56,56,56));
            transfer.setForeground(new Color(211,211,211));
            transfer.setFocusPainted(false);

            transferPersonal.setText("TransPers");
            transferPersonal.setPreferredSize(new Dimension(125,30));
            transferPersonal.setBackground(new Color(56,56,56));
            transferPersonal.setForeground(new Color(211,211,211));
            transferPersonal.setFocusPainted(false);

            conversie.setText("Conversie");
            conversie.setPreferredSize(new Dimension(125, 30));
            conversie.setBackground(new Color(56,56,56));
            conversie.setForeground(new Color(211,211,211));
            conversie.setFocusPainted(false);

            cursValutar.setText("CursValutar");
            cursValutar.setPreferredSize(new Dimension(125, 30));
            cursValutar.setBackground(new Color(56,56,56));
            cursValutar.setForeground(new Color(211,211,211));
            cursValutar.setFocusPainted(false);

            GroupLayout page1Layout = new GroupLayout(page1);
            page1.setLayout(page1Layout);
            page1Layout.setHorizontalGroup(
                    page1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(page1Layout.createSequentialGroup()
                                    .addGroup(page1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(page1Layout.createSequentialGroup()
                                                    .addGap(54, 54, 54)
                                                    .addGroup(page1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                            .addComponent(plataFactura, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(transfer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(conversie, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(page1Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addComponent(image)))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                                    .addGroup(page1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(plataOnline, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(hello, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(currentFocus, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(transferPersonal, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cursValutar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addGap(39, 39, 39))
            );
            page1Layout.setVerticalGroup(
                    page1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(page1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(page1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(hello)
                                            .addComponent(image))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(currentFocus)
                                    .addGap(111, 111, 111)
                                    .addGroup(page1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(plataFactura, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(plataOnline, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addGap(65, 65, 65)
                                    .addGroup(page1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(transfer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(transferPersonal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
                                    .addGap(64, 64, 64)
                                    .addGroup(page1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(conversie, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cursValutar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(143, Short.MAX_VALUE))
            );

            switchPage.setText("SwitchPage");
            settings.setIcon(new ImageIcon("D:\\Aplicatie Bancara\\src\\imagini\\settings.png"));
            settings.setBorderPainted(false);

            GroupLayout layout = new GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(page1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                    .addComponent(page1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
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
