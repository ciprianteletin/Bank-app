package com.login;
import com.automata.FiniteAutomata;
import com.automata.State;
import com.core.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Interfata de logare, in care utilizatorul isi va introduce datele contului personal;
 * Iar tot prin intermediul acestei clase, vor fi preluate datele din baza de date si alocate clientului
 * @author cipri
 **/
public class LoginInterface {
    private JFrame frame;
    private JButton logIn,singUp;
    private JTextField user;
    private JPasswordField password;
    private JPanel info;
    private JPanel register;
    /**
     * This function is the main point of the application, from here we will open/close other windows, register data in the database
     * and so on.
     */
    public void start(){
        frame=new JFrame("Login");
        info=new JPanel(new GridBagLayout());
        register=new JPanel(new FlowLayout());
        register.setBackground(new Color(223,80,32));
        createImage();
        frame.add(info,BorderLayout.CENTER);
        frame.add(register,BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1300,700));
        frame.getContentPane().setBackground(new Color(18,119,130));
        info.setBackground(new Color(18,119,130));
        info.setPreferredSize(new Dimension(200,200));
        info.setLayout(new FlowLayout());
        info.setMaximumSize(info.getPreferredSize());
        logIn=new JButton("Login");
        singUp=new JButton("SignUp");
        user=new JTextField();
        password=new JPasswordField();
        this.invisibleFocus();
        info.add(user);
        info.add(password);
        register.add(logIn);
        register.add(singUp);
        this.placeholder();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.buttonAction();
    }

    /**
     * With this method I want to have in every input box a placeholder text until I put the focus on them
     * and to set a size for it;
     */
    private void placeholder(){
        this.user.setPreferredSize(new Dimension(300,40));
        this.password.setPreferredSize(new Dimension(300,40));

        this.user.setHorizontalAlignment(JTextField.CENTER);
        this.password.setHorizontalAlignment(JTextField.CENTER);

        this.user.setMaximumSize(this.user.getPreferredSize());
        this.password.setMaximumSize(this.password.getPreferredSize());

        this.keyLimiter();

        user.setForeground(new Color(150,150,200));
        password.setForeground(new Color(150,150,200));
        this.user.setText("Username: ");
        this.password.setText("Password: ");

        this.user.addFocusListener(new FocusListener(){
            /**
             * With this one I will make my text in a gray-color;
             * @param focus
             */
            @Override
            public void focusGained(FocusEvent focus){
                user.setForeground(new Color(50,70,90));
                user.setText("");
                user.setFont (new Font ("TimesRoman", Font.BOLD , 12));
            }

            /**
             * And with this one I will put back the text once the focus is gone;
             * @param focus
             */
            @Override
            public void focusLost(FocusEvent focus){
                if(user.getText().isEmpty()){
                    user.setForeground(new Color(150,150,200));
                    user.setText("Username: ");
                }
            }
        });


        this.password.addFocusListener(new FocusListener() {
            /**
             * Same as the method for the user box;
             * @param focus
             */
            @Override
            public void focusGained(FocusEvent focus) {
                password.setForeground(new Color(50,70,90));
                password.setText("");
            }

            /**
             * Same as the method for the user box;
             * @param focus
             */
            @Override
            public void focusLost(FocusEvent focus) {
                if(String.valueOf(password.getPassword()).isEmpty()) {
                    password.setForeground(new Color(150, 150, 200));
                    password.setText("Password: ");
                }
            }
        });

    }

    /**
     * Creating an invisible label to move down a little my text-fields;
     */
    private void invisibleFocus(){
        JLabel label=new JLabel("Masked one");
        SwingUtilities.invokeLater(label::requestFocus); //method reference, object::method;
        label.setForeground(new Color(18,119,130));
        label.setMaximumSize(new Dimension(100,100));
        label.setVisible(true);
        info.add(label);
    }

    private void keyLimiter(){
        this.user.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(user.getText().length()>=12)
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //nothing, but needed override
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //nothing, but needed override
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

    private void createImage(){
        ImageIcon image=new ImageIcon("/home/cipri/Downloads/shopping-store.png");
        JLabel label=new JLabel("",image,JLabel.CENTER);
        info.add(label);
    }

    private void buttonAction() {
        logIn.addActionListener((ae) -> {
            if(!checkInputs()){
                JOptionPane.showMessageDialog(frame,"You must respect the format from sign-up","Wrong format",JOptionPane.WARNING_MESSAGE);
            }
            else if(!checkIfEmpty()) {
                JOptionPane.showMessageDialog(frame,"You can't have empty fields!","Empty fields",JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showConfirmDialog(frame, "You logged in successfully!", "You are now logged in", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
                frame.dispose(); //inchide frame-ul si toate procesele asociate acestuia
                SwingUtilities.invokeLater(Application::new); //pornim aplicatia in sine, cea din care putem sa gestionam activitatile contului
            }

        });

        singUp.addActionListener((ae) -> {
            JOptionPane.showConfirmDialog(frame, "TODO", "TODO", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            final SingUpInterface singUp = new SingUpInterface();
            frame.dispose();
            SwingUtilities.invokeLater(singUp::launchSingIn);
        });
    }

    private boolean checkInputs(){
        return checkUser() && checkPassowrd();
    }

    private boolean checkUser(){
        State s0=new State("","[A-Z]");
        State s1=new State("[A-Z]","[a-z]");
        State s2=new State("[a-z]","[0-9]");
        State s3=new State("[0-9]","",true);
        FiniteAutomata automate=new FiniteAutomata(user.getText(),s0,s1,s2,s3);
        return automate.validateData();
    }

    private boolean checkPassowrd(){
        State s0=new State(".","[0-9]");
        State s1=new State(".","[0-9]");
        State s2=new State(".","",true);

        FiniteAutomata automate=new FiniteAutomata(password.getText(),s0,s1,s2);
        return automate.validateData();

    }

    private boolean checkIfEmpty(){
        Color c=new Color(150,150,200);
        if(this.user.getForeground().equals(c) || this.user.getText().isEmpty())
            return false;
        if(this.password.getForeground().equals(c) || this.password.getText().isEmpty())
            return false;
        return true;
    }
    //TODO accesare date client din baza de date in cadrul logarii
}
