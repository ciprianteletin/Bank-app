package com.core;

import com.URL;
import com.automata.*;
import com.criptography.Decrypt;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;

public class BlockUnblock{
    private JFrame display;
    private JButton confirm;
    private JLabel focus;
    private  JPasswordField password;
    private String username;

    public BlockUnblock(String username){
        this.username=username;
        display=new JFrame("CheckPassword");
        display.getContentPane().setBackground(new Color(56,56,56));
        display.setResizable(false);
        display.setSize(new Dimension(350,200));
        display.setLocationRelativeTo(null);
        display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        display.setVisible(true);
        initComponents();
        addListener();
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(password.getForeground().equals(new Color(255,0,0))) {
                    JOptionPane.showMessageDialog(null, "Password field can't be empty!",
                            "Empty field", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(!checkPassword()){
                    JOptionPane.showMessageDialog(null,"Password must respect sign-up " +
                            "convention!","Error password",JOptionPane.WARNING_MESSAGE);
                    password.setForeground(new Color(255,0,0));
                    password.setText("Password: ");
                    return;
                }

                String pass=password.getText();
                try{
                    Connection conn= DriverManager.getConnection(URL.url,"cipri","linux_mint");
                    PreparedStatement pst=conn.prepareStatement("SELECT password FROM passwords WHERE user=?");
                    pst.setString(1,username);
                    ResultSet rs=pst.executeQuery();
                    rs.next();
                    String encrypt=new Decrypt().launch(rs.getString(1));
                    if(!pass.equals(encrypt)){
                        JOptionPane.showMessageDialog(null,"Wrong password!","Wrong pass",
                                JOptionPane.WARNING_MESSAGE);
                        conn.close();
                        return;
                    }
                    pst=conn.prepareStatement("SELECT cd.cardID,blocked FROM card_data cd INNER JOIN card_type ct ON cd.cardID = ct.cardID " +
                            "WHERE user=? AND current=?");
                    pst.setString(1,username);
                    pst.setString(2,"T");
                    rs=pst.executeQuery();
                    if(rs.next()){ // only one card active
                        int ID = rs.getInt(1);
                        String blocked = rs.getString(2);
                        pst = conn.prepareStatement("UPDATE card_type SET blocked=? WHERE cardID=?");
                        if (blocked.equals("A"))
                            pst.setString(1, "B");
                        else
                            pst.setString(1, "A");
                        pst.setInt(2, ID);
                        pst.executeUpdate();
                    }else{
                        JOptionPane.showMessageDialog(null,"No card registered!","No card!",JOptionPane.WARNING_MESSAGE);
                    }
                    conn.close();
                    display.dispose();
                }catch (SQLException sql){
                    sql.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Can't connect to database...closing " +
                            "the operation","Error",JOptionPane.WARNING_MESSAGE);
                    display.dispose();
                }

            }
        });
    }

    private void initComponents(){
        password=new JPasswordField("Password: ");
        password.setForeground(new Color(255,0,0));
        password.setPreferredSize(new Dimension(400,50));
        focus=new JLabel("");

        display.add(focus);
        SwingUtilities.invokeLater(focus::requestFocus);
        focus.setForeground(new Color(255,255,255));
        focus.setText("Insert your password to confirm this operation");

        confirm=new JButton("Confirm: ");
        confirm.setBackground(new Color(223,80,32));
        confirm.setForeground(new Color(56,56,56));
        confirm.setFocusPainted(false);
        confirm.setPreferredSize(new Dimension(50,50));

        display.add(password,BorderLayout.NORTH);
        display.add(confirm,BorderLayout.SOUTH);
    }

    private void addListener(){
        this.password.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(password.getText().length()>=15)
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //DO NOTHING
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //DO NOTHING
            }
        });

        this.password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(password.getForeground().equals(new Color(255,0,0))){
                    password.setText("");
                    password.setForeground(new Color(0,100,100));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(password.getText().isEmpty()){
                    password.setForeground(new Color(255,0,0));
                    password.setText("Password: ");
                }
            }
        });
    }

    private boolean checkPassword(){
        State s0=new State(".","[0-9]");
        State s1=new State(".","[0-9]");
        State s2=new State(".","",true);

        FiniteAutomata automate=new FiniteAutomata(password.getText(),s0,s1,s2);
        return automate.validateData();
    }
}
