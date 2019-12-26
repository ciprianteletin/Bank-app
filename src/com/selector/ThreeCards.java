package com.selector;

import javax.swing.*;
import java.awt.*;
//TODO
public class ThreeCards extends JFrame {

    private JButton card1;
    private JButton card2;
    private JButton card3;
    private JLabel focusLabel;
    private JLabel bankCard1, numberCard1, typeCard1;
    private JLabel bankCard2, numberCard2, typeCard2;
    private JLabel bankCard3, numberCard3, typeCard3;
    private String username;

    public ThreeCards(String username) {
        this.username=username;
        this.getContentPane().setBackground(new Color(56,56,56));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(672, 300));
        initComponents();
        setResizable(false);
    }

    private void initComponents() {

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
        card1.setPreferredSize(new Dimension(200, 180));
        card2.setPreferredSize(new Dimension(200, 180));
        card3.setPreferredSize(new Dimension(200, 180));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(card1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(bankCard1)
                                        .addComponent(numberCard1))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(card2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(bankCard2)
                                        .addComponent(numberCard2))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(numberCard3)
                                        .addComponent(bankCard3)
                                        .addComponent(card3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addComponent(typeCard1)
                                .addGap(177, 177, 177)
                                .addComponent(typeCard2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(typeCard3)
                                .addGap(93, 93, 93))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(focusLabel))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(focusLabel)
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(card1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(card2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(card3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(bankCard1)
                                        .addComponent(bankCard2)
                                        .addComponent(bankCard3))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(typeCard1)
                                        .addComponent(typeCard2)
                                        .addComponent(typeCard3))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(numberCard1)
                                        .addComponent(numberCard2)
                                        .addComponent(numberCard3))
                                .addContainerGap())
        );

        pack();
    }
}
