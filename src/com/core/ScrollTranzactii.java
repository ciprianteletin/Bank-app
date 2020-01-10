package com.core;

import javax.swing.*;
import java.awt.*;

/**
 * Interfata grafica ce contine un scroll pane si un text-area, in cadrul carora se vor afisa tranzactiile cerute din cadrul interfetelor
 * Istoric, cat si SearchTranz. S-a folosit un scroll pane pentru cazul in care tranzactiile sunt multe din punct de vedere numeric/lungi,
 * si nu mai incap pe ecranul utilizator.
 */
public class ScrollTranzactii {
    private JFrame display;
    private JTextArea textArea;
    private JScrollPane scrollPane;

    ScrollTranzactii() {
        this.display=new JFrame("Tranzactii");
        this.display.setLayout(new BorderLayout());
        this.display.setLocationRelativeTo(null);
        this.display.getContentPane().setBackground(new Color(56,56,56));
        this.display.setSize(new Dimension(700,700));
        this.display.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        textArea = new JTextArea(20, 20);
        textArea.setForeground(new Color(255,255,255));
        textArea.setBackground(new Color(56,56,56));
        textArea.setFont(new Font(Font.SERIF,Font.ITALIC,17));
        textArea.setEnabled(false);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        display.add(scrollPane,BorderLayout.CENTER);
    }
    //Returnare frame si zona de text, pentru actualizare si inchidere.
    JTextArea getTextArea(){return textArea;}
    JFrame getFrame(){return display;}
}
