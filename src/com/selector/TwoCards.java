package com.selector;

import javax.swing.*;
import java.awt.*;
public class TwoCards extends JFrame {
    private JButton card1, card2;
    private JLabel bancaCard1, numarCard1, bancaCard2;
    private JLabel numarCard2, tipCard1, tipCard2;
    private JLabel focusLabel;

    public TwoCards() {
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(56,56,56));
        setPreferredSize(new Dimension(600, 300));
        setVisible(true);
        setResizable(false);
    }

    private void initComponents() {

        card1 = new JButton();
        card1.setBackground(new Color(56,56,56));
        card1.setForeground(new Color(255,255,255));
        card1.setIcon(new ImageIcon("/home/cipri/Downloads/card1.png"));
        card1.setBorderPainted(false);
        card2 = new JButton();
        card2.setBackground(new Color(56,56,56));
        card2.setForeground(new Color(255,255,255));
        card2.setIcon(new ImageIcon("/home/cipri/Downloads/card2.png"));
        card2.setBorderPainted(false);
        bancaCard1 = new JLabel("Banca Transilvania");
        bancaCard1.setForeground(new Color(255,255,255));
        numarCard1 = new JLabel("1111-1111-1111-1111");
        numarCard1.setForeground(new Color(255,255,255));
        bancaCard2 = new JLabel("BCR");
        bancaCard2.setForeground(new Color(255,255,255));
        numarCard2 = new JLabel("2222-2222-2222-2222");
        numarCard2.setForeground(new Color(255,255,255));
        tipCard1 = new JLabel("Calator");
        tipCard1.setForeground(new Color(255,255,255));
        tipCard2 = new JLabel("Salarial");
        tipCard2.setForeground(new Color(255,255,255));
        focusLabel=new JLabel("");
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
                                .addGap(70, 70, 70)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(card1, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(card2, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
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
