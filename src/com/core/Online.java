package com.core;

import com.URL;
import com.card.Card;
import com.cardGenerator.Currency;
import com.scrapper.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class Online {
    private JFrame display;
    private JLabel ln;
    private JTextField link;
    private JPanel info,press,iconPanel;
    private JButton confirm;
    private Card card;
    private Enable enable;

    Online(Card c,Enable e){
        this.card=c;
        this.enable=e;
        this.display=new JFrame("Produse online");
        this.display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.display.getContentPane().setBackground(new Color(56,56,56));
        this.display.setSize(new Dimension(400,300));
        GridLayout layout=new GridLayout(3,1);
        layout.setVgap(20);
        initIconPanel();
        initInfo();
        initConfirm();
        closeOpp();
        confirmAction();
        this.display.setLayout(layout);
        this.display.setLocationRelativeTo(null);
        this.display.setResizable(false);
        this.display.setVisible(true);
    }

    private void closeOpp(){
        display.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                enable.enable();
            }
        });
    }

    private void confirmAction(){
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!link.getText().isEmpty() && link.getForeground().equals(new Color(56,56,56))){
                    String link=Online.this.link.getText();
                    Scrapper scrapper;
                    if(link.contains("www.emag.ro"))
                        scrapper=new Emag(link);
                    else if(link.contains("www.cel.ro"))
                        scrapper=new Celro(link);
                    else
                        scrapper=new PcGarage(link);
                    scrapper.scrape();
                    double price=scrapper.getPret();
                    String moneda=scrapper.getMoneda();
                    String nume=scrapper.getProductName();

                    if(!card.getMoneda().equals("Lei")){
                        Currency curr=Currency.valueOf(card.getMoneda().toUpperCase());
                        price=curr.forTransfer(price);
                    }

                    int result=JOptionPane.showConfirmDialog(null,"We identify the next product:\n" +
                            nume+"\n"+price+" "+card.getMoneda()+"\nIs the correct one?","Select product",JOptionPane.YES_NO_OPTION);
                    if(result==JOptionPane.YES_OPTION){
                        double comm=Math.round(price*card.getCom_online())/100.0;
                        if(price+comm>card.getSuma()){
                            JOptionPane.showMessageDialog(null,"Insufficient money");
                            return;
                        }
                        if(price+comm>card.getLim_transfer() && card.getLim_transfer()!=-1){
                            JOptionPane.showMessageDialog(null,"Your limit is exceeded");
                            return;
                        }
                        enable.enable();

                        try {
                            Connection conn = DriverManager.getConnection(URL.url, "cipri", "linux_mint");
                            PreparedStatement pst = conn.prepareStatement("INSERT INTO tranzactii VALUES (?,?,?,?,?)");
                            pst.setInt(1, card.getID());
                            pst.setString(2, nume);
                            pst.setDouble(3, price);
                            pst.setString(4, card.getMoneda());
                            Date date = Date.valueOf(LocalDate.now());
                            pst.setDate(5, date);
                            pst.executeUpdate();
                        } catch (SQLException sql) {
                            JOptionPane.showMessageDialog(null, "Esuare tranzactie!", "Error", JOptionPane.WARNING_MESSAGE);
                        }

                        card.setSuma(card.getSuma() - price - comm);
                        if (card.getLim_transfer() != -1)
                            card.setLim_transfer(card.getLim_transfer() - price);
                        card.updateDB();
                        display.dispose();
                    }

                }
            }
        });
    }

    private void initIconPanel(){
        iconPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        iconPanel.setBackground(new Color(56,56,56));
        iconPanel.setPreferredSize(new Dimension(400,150));
        JLabel icon=new JLabel();
        icon.setIcon(new ImageIcon("/home/cipri/Downloads/new-product.png"));
        iconPanel.add(icon);
        display.add(iconPanel);
    }

    private void initInfo(){
        info=new JPanel(new FlowLayout());
        info.setBackground(new Color(56,56,56));
        info.setPreferredSize(new Dimension(400,100));
        ln=new JLabel("Link: ");
        ln.setForeground(new Color(255,255,255));
        info.add(ln);
        SwingUtilities.invokeLater(info::requestFocus);
        link=new JTextField("www.example.com");
        link.setBackground(new Color(255,255,255));
        link.setForeground(new Color(10,145,80));
        link.setPreferredSize(new Dimension(300,35));
        addFocus();
        info.add(link);
        display.add(info);
    }

    private void addFocus(){
        link.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(link.getForeground().equals(new Color(10,145,80))){
                    link.setForeground(new Color(56,56,56));
                    link.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(link.getText().isEmpty()){
                    link=new JTextField("www.example.com");
                    link.setForeground(new Color(10,145,80));
                }
            }
        });
    }

    private void initConfirm(){
        FlowLayout layout=new FlowLayout(FlowLayout.CENTER);
        layout.setVgap(20);
        press=new JPanel(layout);
        press.setBackground(new Color(223,80,32));
        press.setPreferredSize(new Dimension(400,50));
        confirm=new JButton("Confirm!");
        confirm.setPreferredSize(new Dimension(150,35));
        confirm.setBorderPainted(false);
        confirm.setFocusPainted(false);
        confirm.setBackground(new Color(56,56,56));
        confirm.setForeground(new Color(0,255,0));
        press.add(confirm);
        display.add(press);
    }
}
