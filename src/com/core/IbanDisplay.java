package com.core;

import com.URL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class IbanDisplay {
    private JFrame display;
    private JLabel iban,nume,banca,tip;
    private JLabel ibans,numes,bancas,tips;
    private JLabel titleLb;
    private String username;
    private JPanel title;
    private JButton print;

    public IbanDisplay(String user) {
        this.username = user;
        display = new JFrame("Personal account details");
        display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        display.getContentPane().setBackground(new Color(56, 56, 56));
        display.setSize(new Dimension(400, 375));
        display.setLocationRelativeTo(null);
        display.setVisible(true);
        display.setResizable(false);
        makeNorthPane();
        makeWestPane();
        makeCenterPane();
        makeEastPane();
        makePrint();
    }

    private void makeNorthPane(){
        JLabel user=new JLabel("IBAN for the account "+username);
        user.setForeground(new Color(200,90,0));
        title=new JPanel();
        title.setBackground(new Color(20,50,50));
        titleLb=new JLabel();
        titleLb.setIcon(new ImageIcon("/home/cipri/Downloads/iban.png"));
        title.setLayout(new FlowLayout(FlowLayout.LEFT));
        title.add(titleLb);
        title.add(user);
        title.setPreferredSize(new Dimension(400,80));
        display.add(title,BorderLayout.NORTH);
    }

    private void makeWestPane(){
        JPanel west=new JPanel();
        west.setBackground(new Color(56,56,56));
        west.setPreferredSize(new Dimension(100,300));
        iban=new JLabel("IBAN:");
        iban.setForeground(new Color(255,255,255));
        nume=new JLabel("Full name:");
        nume.setForeground(new Color(255,255,255));
        banca=new JLabel("Bank:");
        banca.setForeground(new Color(255,255,255));
        tip=new JLabel("Card type:");
        tip.setForeground(new Color(255,255,255));

        west.setLayout(new GridLayout(4,1));
        west.add(iban);
        west.add(nume);
        west.add(banca);
        west.add(tip);

        display.add(west,BorderLayout.WEST);
    }

    private void takeData(){
        try {
            Connection c = DriverManager.getConnection(URL.url, "cipri", "linux_mint");
            PreparedStatement pst=c.prepareStatement("SELECT first_name,last_name FROM users WHERE user=?");
            pst.setString(1,username);
            ResultSet rs=pst.executeQuery();
            rs.next();  //to advance to my fist line of data
            String first_name=rs.getString(1);
            String last_name=rs.getString(2);
            numes.setText(last_name+" "+first_name);

            pst=c.prepareStatement("SELECT IBAN,banca,tip FROM (SELECT IBAN,banca,tip FROM card_data cd " +
                    "INNER JOIN card_type ct ON cd.cardID=ct.cardID AND current=? WHERE user=?) tbt");
            pst.setString(1,"T");
            pst.setString(2,username);

            rs=pst.executeQuery();
            rs.next(); //same reason, to go to the first line of data
            ibans.setText(rs.getString(1));
            String bank=rs.getString(2);
            if(bank.equals("BT"))
                bank="Banca Transilvania";
            else if(bank.equals("BCR"))
                bank="Banca Comerciala Romana";
            bancas.setText(bank);
            tips.setText(rs.getString(3));

            c.close();
        }catch (SQLException err){
            JOptionPane.showMessageDialog(null,"Can't connect to database...closing the frame"
            ,"Connection failed",JOptionPane.WARNING_MESSAGE);
            display.dispose();
        }
    }

    private void makeCenterPane(){
        JPanel center=new JPanel();
        center.setBackground(new Color(56,56,56));
        center.setPreferredSize(new Dimension(100,300));
        ibans=new JLabel();
        bancas=new JLabel();
        numes=new JLabel();
        tips=new JLabel();
        ibans.setForeground(new Color(255,255,255));
        numes.setForeground(new Color(255,255,255));
        bancas.setForeground(new Color(255,255,255));
        tips.setForeground(new Color(255,255,255));
        takeData();
        center.setLayout(new GridLayout(4,1));
        center.add(ibans);
        center.add(numes);
        center.add(bancas);
        center.add(tips);

        display.add(center,BorderLayout.CENTER);
    }

    private void makeEastPane(){
        JPanel east=new JPanel();
        east.setLayout(new GridLayout(3,1));
        JLabel inv1=new JLabel("Invisible");
        JLabel inv2=new JLabel("Invisible");
        inv1.setForeground(new Color(20,50,50));
        inv2.setForeground(new Color(20,50,50));
        east.setPreferredSize(new Dimension(90,300));
        east.setBackground(new Color(20,50,50));
        print=new JButton();
        print.setBorderPainted(false);
        print.setBackground(new Color(20,50,50));
        print.setIcon(new ImageIcon("/home/cipri/Downloads/printer.png"));
        SwingUtilities.invokeLater(inv1::requestFocus);
        east.add(inv1);
        east.add(print);
        east.add(inv2);
        display.add(east,BorderLayout.EAST);
    }

    private void makePrint(){
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                display.dispose();
                SwingUtilities.invokeLater(()->new FileChooser(ibans.getText(),numes.getText(),
                        bancas.getText(),tips.getText()));
            }
        });

    }
}
