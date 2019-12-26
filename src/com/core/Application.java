package com.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public Application(String username) {
            this.username=username;
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
        }
        private void focusOn(){
            SwingUtilities.invokeLater(currentFocus::requestFocus);
        }

        private void settingsAction(){
            settings.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Application.super.dispose();
                    SwingUtilities.invokeLater(()->new Settings(0,username));
                }
            });
        }

        private void makeSwitch(){
            switchPage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                        Application.super.dispose();
                        SwingUtilities.invokeLater(()->new SecondApplication(username));
                    }
            });
        }

        private void makeButtons(){
            cursValutar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    SwingUtilities.invokeLater(CursValutarButton::new);
                }
            });
        }

        private void setImage(){
            ImageIcon img=new ImageIcon("/home/cipri/Downloads/online-shop.png");
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
            settings.setIcon(new ImageIcon("/home/cipri/Downloads/settings.png"));
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
