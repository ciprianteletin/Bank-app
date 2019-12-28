package com.core;

import com.URL;
import com.automata.*;
import com.criptography.*;
import com.login.LoginInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePassword {
    private JFrame display;
    private JLabel pass,changePass;
    private JPanel btnPannel,passPanel;
    private JPasswordField password,confirmPassword;
    private JButton confirm,cancel;
    private String username;
    private int which;

    public ChangePassword(String username,int which){
        this.username=username;
        this.which=which;
        display=new JFrame("Change password");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setLocationRelativeTo(null);
        display.getContentPane().setBackground(new Color(56,56,56));
        display.setVisible(true);
        display.setLayout(new BorderLayout());
        display.setSize(new Dimension(500,180));
        createComponents();
        addKeyListener();
        cancelButton();
        confirmButton();
    }

    private void addKeyListener(){
        this.confirmPassword.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(confirmPassword.getText().length()>=15)
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
    }

    private void createComponents(){
        passPanel=new JPanel(new GridLayout(2,2));
        passPanel.setBackground(new Color(56,56,56));
        passPanel.setPreferredSize(new Dimension(400,80));
        pass=new JLabel("Password: ");
        pass.setForeground(new Color(255,255,255));
        changePass=new JLabel("Confirm password: ");
        changePass.setForeground(new Color(255,255,255));
        password=new JPasswordField("Password: ");
        SwingUtilities.invokeLater(pass::requestFocus);
        password.setForeground(new Color(255,0,0));
        password.setPreferredSize(new Dimension(50,20));
        confirmPassword=new JPasswordField("Confirm password: ");
        confirmPassword.setForeground(new Color(255,0,0));
        passPanel.add(pass);
        passPanel.add(password);
        passPanel.add(changePass);
        passPanel.add(confirmPassword);
        display.add(passPanel,BorderLayout.NORTH);
        addPlaceholders();
        buttonPanel();
    }

    private void buttonPanel(){
        btnPannel=new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPannel.setBackground(new Color(223,80,32));
        btnPannel.setPreferredSize(new Dimension(400,40));
        confirm=new JButton("Confirm: ");
        cancel=new JButton("Cancel: ");

        confirm.setFocusPainted(false);
        cancel.setFocusPainted(false);

        confirm.setBackground(new Color(56,56,56));
        cancel.setBackground(new Color(56,56,56));

        confirm.setForeground(new Color(0,255,0));
        cancel.setForeground(new Color(0,255,0));

        btnPannel.add(confirm);
        btnPannel.add(cancel);
        display.add(btnPannel,BorderLayout.SOUTH);
    }

    private void addPlaceholders(){
        password.addFocusListener(new FocusListener() {
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

        confirmPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(confirmPassword.getForeground().equals(new Color(255,0,0))){
                    confirmPassword.setText("");
                    confirmPassword.setForeground(new Color(0,100,100));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(confirmPassword.getText().isEmpty()){
                    confirmPassword.setForeground(new Color(255,0,0));
                    confirmPassword.setText("Confirm password: ");
                }
            }
        });
    }

    private void cancelButton(){
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int result=JOptionPane.showConfirmDialog(null,"Are you sure that you want to cancel?",
                        "Cancel operation",JOptionPane.YES_NO_OPTION);
                if(result==JOptionPane.YES_OPTION){
                    display.dispose();
                    SwingUtilities.invokeLater(()-> new Settings(which,username));
                }
            }
        });
    }

    private void confirmButton(){
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(password.getForeground().equals(new Color(255,0,0)) ||
                confirmPassword.getForeground().equals(new Color(255,0,0)))
                    JOptionPane.showMessageDialog(null,"You can't have empty fields!",
                            "Empty fields",JOptionPane.WARNING_MESSAGE);
                else if(!checkPassword() || !checkConfirmPassword()) {
                    JOptionPane.showMessageDialog(null, "Passwords must respect sign-up " +
                            "conditions and need to be equals", "Error", JOptionPane.WARNING_MESSAGE);
                    password.setForeground(new Color(255,0,0));
                    confirmPassword.setForeground(new Color(255,0,0));

                    password.setText("Password: ");
                    confirmPassword.setText("Confirm password: ");
                }
                else{
                    try{
                        Connection conn= DriverManager.getConnection(URL.url,"cipri","linux_mint");
                        PreparedStatement pst=conn.prepareStatement("SELECT password FROM passwords " +
                                "WHERE user=?");
                        pst.setString(1,username);
                        ResultSet rs=pst.executeQuery();
                        rs.next();
                        String pass=rs.getString(1);
                        pass= new Decrypt().launch(pass);
                        if(password.getText().equals(pass)) {
                            JOptionPane.showMessageDialog(null, "Your new password must be" +
                                    " different from the old one!", "Different", JOptionPane.WARNING_MESSAGE);
                            password.setForeground(new Color(255,0,0));
                            confirmPassword.setForeground(new Color(255,0,0));

                            password.setText("Password: ");
                            confirmPassword.setText("Confirm password: ");
                        }else{
                            pass=password.getText();
                            pass=new Encrypt().launch(pass);
                            pst=conn.prepareStatement("UPDATE passwords SET password=? WHERE user=?");
                            pst.setString(1,pass);
                            pst.setString(2,username);
                            pst.executeUpdate();

                            display.dispose();
                            //going back to login, because it's another password;
                            SwingUtilities.invokeLater(()->new LoginInterface().start());
                        }

                    }catch (SQLException sql){
                        JOptionPane.showMessageDialog(null,"Can't change password..Cancel this " +
                                "operation","Error database",JOptionPane.WARNING_MESSAGE);
                        SwingUtilities.invokeLater(()->new Settings(which,username));
                        display.dispose();
                    }
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

    private boolean checkConfirmPassword(){
        return password.getText().equals(confirmPassword.getText());
    }
}
