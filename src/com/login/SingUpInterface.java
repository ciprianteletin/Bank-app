package com.login;

import com.URL;
import com.automata.FiniteAutomata;
import com.automata.State;
import com.criptography.Encrypt;

import java.sql.*;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/**
 * The purpose of this class is to let the user to create an personal account, with unique ID,email,and other personal data;
 * @author cipri
 */
//TODO PUNE PAROLA IN BAZA DE DATE IN MOD CRYPTAT!!!(ENCRYPT SI DECRYPT, apel launch)
//TODO VERIFICARE MAIL,cnp,telefon diferit
class SingUpInterface{
    private JFrame singUpFrame;
    private JPanel insertBoxes,btnPanel;
    private JButton reset,confirm;
    private JTextField username,CNP,address,firstName,lastName,birthday,job,email,phoneNumber;
    private JPasswordField password,confirmPassword;

    private void initializeFrame(){
        singUpFrame=new JFrame("Sign Up");
        singUpFrame.setPreferredSize(new Dimension(500,1005)); //larger enough to fit all the component in an elegant manner;
        singUpFrame.setLocationRelativeTo(null);
        singUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        singUpFrame.pack();
        singUpFrame.setVisible(true);
    }

    private void initializePanel(){
        insertBoxes=new JPanel();
        insertBoxes.setPreferredSize(new Dimension(500,1000));
        insertBoxes.setBackground(new Color(0,128,128));
        insertBoxes.setLayout(new FlowLayout(FlowLayout.CENTER,5,60));
        ImageIcon img=new ImageIcon("/home/cipri/Downloads/rename.png");
        Image resize=img.getImage().getScaledInstance(220,250,Image.SCALE_DEFAULT);
        img=new ImageIcon(resize);
        JLabel label=new JLabel("",img,JLabel.CENTER);
        label.setMaximumSize(new Dimension(200,200));
        insertBoxes.add(label);
        JLabel star;
        img=new ImageIcon("/home/cipri/Downloads/star.png");
        resize=img.getImage().getScaledInstance(35,35,Image.SCALE_SMOOTH);
        img=new ImageIcon(resize);
        star=new JLabel("",img,JLabel.CENTER);
        star.setPreferredSize(new Dimension(35,35));
        insertBoxes.add(star);
    }

    private void initializeTextFields(){
        username=new JTextField("Username: ");
        username.setForeground(new Color(150,150,200));
        username.setPreferredSize(new Dimension(190,35));
        username.setHorizontalAlignment(JTextField.CENTER);

        password=new JPasswordField("Password: ");
        password.setForeground(new Color(150,150,200));
        password.setPreferredSize(new Dimension(190,35));
        password.setHorizontalAlignment(JPasswordField.CENTER);

        confirmPassword=new JPasswordField("Confirm: ");
        confirmPassword.setPreferredSize(new Dimension(190,35));
        confirmPassword.setForeground(new Color(150,150,200));
        confirmPassword.setHorizontalAlignment(JPasswordField.CENTER);

        firstName=new JTextField("First name: ");
        lastName=new JTextField("Last name: ");
        firstName.setPreferredSize(new Dimension(190,35));
        lastName.setPreferredSize(new Dimension(190,35));
        firstName.setForeground(new Color(150,150,200));
        lastName.setForeground(new Color(150,150,200));
        firstName.setHorizontalAlignment(JTextField.CENTER);
        lastName.setHorizontalAlignment(JTextField.CENTER);

        CNP=new JTextField("CNP: ");
        CNP.setPreferredSize(new Dimension(190,35));
        CNP.setForeground(new Color(150,150,200));
        CNP.setHorizontalAlignment(JTextField.CENTER);

        birthday=new JTextField("Birthday(DD/MM/YYYY): ");
        birthday.setPreferredSize(new Dimension(190,35));
        birthday.setForeground(new Color(150,150,200));
        birthday.setHorizontalAlignment(JTextField.CENTER);

        address=new JTextField("Address: ");
        address.setPreferredSize(new Dimension(190,35));
        address.setForeground(new Color(150,150,200));
        address.setHorizontalAlignment(JTextField.CENTER);

        email=new JTextField("E-mail: ");
        email.setPreferredSize(new Dimension(190,35));
        email.setForeground(new Color(150,150,200));
        email.setHorizontalAlignment(JTextField.CENTER);

        job=new JTextField("Job: ");
        job.setPreferredSize(new Dimension(190,35));
        job.setForeground(new Color(150,150,200));
        job.setHorizontalAlignment(JTextField.CENTER);

        phoneNumber=new JTextField("Phone: ");
        phoneNumber.setForeground(new Color(150,150,200));
        phoneNumber.setPreferredSize(new Dimension(190,35));
        phoneNumber.setHorizontalAlignment(JTextField.CENTER);
    }

    private void setToolTips(){
        username.setToolTipText("Format: XX..Xxx..xcc..c");
        password.setToolTipText("Format: Any character, but it must contain 2 digits");
        confirmPassword.setToolTipText("Format: Any character, but it must contain 2 digits");
        firstName.setToolTipText("Format: Xxx..x or Xxx..x-Xxx..x");
        lastName.setToolTipText("Format: Xxx..x");
        CNP.setToolTipText("Format: 13 digits");
        address.setToolTipText("Format: Country,City,Street");
        email.setToolTipText("Format: xxx..x@xxx.xxx, where x can be letter,digit, - or underscore\n" +
                "Underscore and digits can't appear after @");
        birthday.setToolTipText("Format: xx/xx/xxxx");
        job.setToolTipText("Format: Xxxx..xxx, where x is uppercase/lowercase letter or space");
        phoneNumber.setToolTipText("Format: 10 digits");
    }

    private void addComponents(){
        this.singUpFrame.add(insertBoxes);
        this.insertBoxes.add(username);
        makeStar();
        this.insertBoxes.add(password);
        makeStar();
        this.insertBoxes.add(firstName);
        makeStar();
        this.insertBoxes.add(confirmPassword);
        makeStar();
        this.insertBoxes.add(lastName);
        makeStar();
        this.insertBoxes.add(CNP);
        makeStar();
        this.insertBoxes.add(birthday);
        makeStar();
        this.insertBoxes.add(address);
        makeStar();
        this.insertBoxes.add(email);
        makeStar();
        this.insertBoxes.add(job);
        makeStar();
        this.insertBoxes.add(phoneNumber);
    }

    private void fakeLabel(){
        JLabel fake=new JLabel("Invisible");
        fake.setPreferredSize(new Dimension(1000,20));
        fake.setBackground(new Color(0,128,128));
        insertBoxes.add(fake);
    }

    private void buttonPanel(){
        btnPanel=new JPanel(new FlowLayout());
        btnPanel.setPreferredSize(new Dimension(1500,120));
        btnPanel.setBackground(new Color(175,0,42));
        insertBoxes.add(btnPanel);
    }

    private void makeStar(){
        ImageIcon img=new ImageIcon("/home/cipri/Downloads/star.png");
        Image resize=img.getImage().getScaledInstance(35,35,Image.SCALE_SMOOTH);
        img=new ImageIcon(resize);
        JLabel star=new JLabel("",img,JLabel.CENTER);
        star.setPreferredSize(new Dimension(35,35));
        this.insertBoxes.add(star);
    }

    private void setUpButtons(){
        this.reset=new JButton("Reset");
        this.confirm=new JButton("Confirm");

        this.reset.setPreferredSize(new Dimension(100,40));
        this.confirm.setPreferredSize(new Dimension(100,40));

        JLabel label=new JLabel("Hide");
        label.setForeground(new Color(175,0,42));
        SwingUtilities.invokeLater(label::requestFocus); //reference method for focus, used also onto the login interface;

        this.btnPanel.add(confirm);
        this.btnPanel.add(label);
        this.btnPanel.add(reset);
    }

    private void placeholders(){
        this.username.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(!username.getForeground().equals(new Color(50,70,90))) {
                    username.setText("");
                    username.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(username.getText().isEmpty()){
                    username.setText("Username: ");
                    username.setForeground(new Color(150,150,200));}
            }
        });

        this.password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(!password.getForeground().equals(new Color(50,70,90))) {
                    password.setText("");
                    password.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(String.valueOf(password.getPassword()).isEmpty()){
                    password.setText("Password: ");
                    password.setForeground(new Color(150,150,200));
                }
            }
        });

        confirmPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(!confirmPassword.getForeground().equals(new Color(50,70,90))) {
                    confirmPassword.setText("");
                    confirmPassword.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(String.valueOf(confirmPassword.getPassword()).isEmpty()) {
                    confirmPassword.setText("Confirmation: ");
                    confirmPassword.setForeground(new Color(150, 150, 200));
                }
            }
        });

        firstName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(!firstName.getForeground().equals(new Color(50,70,90))) {
                    firstName.setText("");
                    firstName.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(firstName.getText().isEmpty()){
                    firstName.setText("First name: ");
                    firstName.setForeground(new Color(150,150,200));
                }
            }
        });

        lastName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(!lastName.getForeground().equals(new Color(50,70,90))) {
                    lastName.setText("");
                    lastName.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(lastName.getText().isEmpty()){
                    lastName.setText("Last name: ");
                    lastName.setForeground(new Color(150,150,200));
                }
            }
        });

        CNP.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(!CNP.getForeground().equals(new Color(50,70,90))) {
                    CNP.setText("");
                    CNP.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(CNP.getText().isEmpty()){
                    CNP.setText("CNP: ");
                    CNP.setForeground(new Color(150,150,200));
                }
            }
        });

        address.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(!address.getForeground().equals(new Color(50,70,90))) {
                    address.setText("");
                    address.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(address.getText().isEmpty()){
                    address.setText("Address: ");
                    address.setForeground(new Color(150,150,200));
                }
            }
        });

        job.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(!job.getForeground().equals(new Color(50,70,90))) {
                    job.setText("");
                    job.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(job.getText().isEmpty()) {
                    job.setText("Job: ");
                    job.setForeground(new Color(150, 150, 200));
                }
            }
        });

        phoneNumber.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                Color c=new Color(50,70,90);
                if(!c.equals(phoneNumber.getForeground())) {
                    phoneNumber.setText("");
                    phoneNumber.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(phoneNumber.getText().isEmpty()) {
                    phoneNumber.setText("Phone: ");
                    phoneNumber.setForeground(new Color(150, 150, 200));
                }
            }
        });

        birthday.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                Color c=new Color(50,70,90);
                if(!birthday.getForeground().equals(c)) {
                    birthday.setText("");
                    birthday.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(birthday.getText().isEmpty()) {
                    birthday.setText("Birthday(DD/MM/YYYY): ");
                    birthday.setForeground(new Color(150, 150, 200));
                }
            }
        });

        email.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                Color c=new Color(50,70,90);
                if(!email.getForeground().equals(c))
                {
                    email.setForeground(new Color(50,70,90));
                    email.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(email.getText().isEmpty()) {
                    email.setText("E-mail: ");
                    email.setForeground(new Color(150, 150, 200));
                }
            }
        });
    }

    private void KeySetter(){
        this.username.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(username.getText().length()>=12)
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

        this.CNP.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(CNP.getText().length()>12)
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //Nothing to do here
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //Nothing to do here
            }
        });

        this.phoneNumber.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char c=keyEvent.getKeyChar();
                if(phoneNumber.getText().length()>=10 || (c<'0' || c>'9'))
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //Nothing to do here
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //Nothing to do here
            }
        });

        this.birthday.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(birthday.getText().length()>=10 || ((keyEvent.getKeyChar()<'0' || keyEvent.getKeyChar()>'9')))
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //DoNothing
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyChar() >= '0' && keyEvent.getKeyChar() <= '9' || keyEvent.getKeyChar()==8) {
                    StringBuilder onlyNumber = new StringBuilder();
                    for (int i = 0; i < birthday.getText().length(); ++i) {
                        char c = birthday.getText().charAt(i);
                        if (c >= '0' && c <= '9')
                            onlyNumber.append(c);
                    }
                    int contor = 0;
                    StringBuilder text = new StringBuilder();
                    for (int i = 0; i < onlyNumber.length(); i++) {
                        ++contor;
                        text.append(onlyNumber.charAt(i));
                        if (contor == 2 || contor == 4)
                            text.append('/');
                    }
                    birthday.setText(text.toString());
                }
            }
        });
    }

    private LocalDate convertDate(String data){
        String[] dateSplit=data.split("/");
        data=dateSplit[2]+"-"+dateSplit[1]+"-"+dateSplit[0];
        return LocalDate.parse(data);
    }

    private int convertToAge(){
            DateTimeFormatter format = DateTimeFormatter
                    .ofPattern("dd/MM/yyyy");
            try{
                LocalDate lc=LocalDate.parse(birthday.getText(),format);
                LocalDate timeNow=LocalDate.now();
                int year=timeNow.getYear()-lc.getYear();
                return year;
            }catch (DateTimeException exp) {
                return -1;
            }
    }

    private void clearComponents(){
        username.setText("");
        password.setText("");
        confirmPassword.setText("");
        firstName.setText("");
        lastName.setText("");
        address.setText("");
        birthday.setText("");
        job.setText("");
        phoneNumber.setText("");
        CNP.setText("");
        email.setText("");
    }

    private void activateButtons(){
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Press ok for deleting all data, cancel otherwise.","Reset",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    clearComponents();
                    setForegorund();
                    setText();
                }
            }
        });

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CheckData ch=new CheckData();
                Connection connection=null;
                if(ch.errorWindow()) {
                    try{
                        connection=DriverManager.getConnection(URL.url,"cipri","linux_mint");
                        Statement stm=connection.createStatement();
                        ResultSet rs=stm.executeQuery("SELECT user FROM users WHERE user='"+username.getText()+"'");
                        if(!rs.next()){
                            PreparedStatement pre=connection.prepareStatement("SELECT user FROM info WHERE " +
                                    "cnp=? OR mail=? OR phone=?");
                            pre.setString(1,CNP.getText());
                            pre.setString(2,email.getText());
                            pre.setString(3,phoneNumber.getText());
                            rs=pre.executeQuery();
                            if(rs.next()){
                                pre=connection.prepareStatement("SELECT user FROM info WHERE cnp=?");
                                pre.setString(1,CNP.getText());
                                if(pre.executeQuery().next()){
                                    JOptionPane.showMessageDialog(null,"CNP already registered!"
                                    ,"Wrong cnp",JOptionPane.WARNING_MESSAGE);
                                    return; //finally here, so the connection will be closed
                                }

                                pre=connection.prepareStatement("SELECT user FROM info WHERE mail=?");
                                pre.setString(1,email.getText());
                                if(pre.executeQuery().next()){
                                    JOptionPane.showMessageDialog(null,"Mail already existent!",
                                            "Existing mail",JOptionPane.WARNING_MESSAGE);
                                    return;
                                }

                                pre=connection.prepareStatement("SELECT user FROM info WHERE phone=?");
                                pre.setString(1,phoneNumber.getText());
                                if(pre.executeQuery().next()){
                                    JOptionPane.showMessageDialog(null,"Phone already registered!",
                                            "Phone registered",JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }

                            Encrypt encrypt=new Encrypt();
                            String encryptPass=encrypt.launch(password.getText());
                            String first=firstName.getText();
                            String last=lastName.getText();
                            pre=connection.prepareStatement("INSERT INTO users VALUES (?,?,?)");
                            pre.setString(1,username.getText());
                            pre.setString(2,first);
                            pre.setString(3,last);
                            pre.executeUpdate();

                            pre=connection.prepareStatement("INSERT INTO passwords VALUES(?,?)");
                            pre.setString(1,username.getText());
                            pre.setString(2,encryptPass);
                            pre.executeUpdate();

                            pre=connection.prepareStatement("INSERT INTO info VALUES(?,?,?,?,?,?)");
                            pre.setString(1,username.getText());
                            pre.setString(2,job.getText());
                            pre.setString(3,CNP.getText());
                            pre.setString(4,email.getText());
                            pre.setObject(5,convertDate(birthday.getText()));
                            pre.setString(6,phoneNumber.getText());
                            pre.executeUpdate();

                            String[] adr=address.getText().split(",");
                            pre=connection.prepareStatement("INSERT INTO adress VALUES(?,?,?,?)");
                            pre.setString(1,username.getText());
                            pre.setString(2,adr[0]);
                            pre.setString(3,adr[1]);
                            pre.setString(4,adr[2]);
                            pre.executeUpdate();

                            int year=convertToAge(); //age of the user;
                            JOptionPane.showConfirmDialog(null,"You will be redirect to the next step!",
                                    "Success!",JOptionPane.DEFAULT_OPTION);
                            JOptionPane.showConfirmDialog(null,"TODO","TODO",JOptionPane.DEFAULT_OPTION);
                            singUpFrame.dispose();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    new ChooseAccountInterface(username.getText(),year,job.getText());
                                }
                            }); //This is new to me, I invoke new method
                            //as a reference,but good to know
                        }else{
                            JOptionPane.showMessageDialog(null,"Choose another username, the current one is taken",
                                    "Username taken",JOptionPane.WARNING_MESSAGE);
                        }
                    }catch (SQLException err){
                        JOptionPane.showMessageDialog(null,"An error has occurred..closing the app",
                                "Error",JOptionPane.WARNING_MESSAGE);
                        err.printStackTrace();
                        System.exit(1);
                    }finally {
                        try {
                            assert connection != null;
                            connection.close();
                        }catch (SQLException err){
                            err.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void setForegorund(){
        this.username.setForeground(new Color(150,150,200));
        this.password.setForeground(new Color(150,150,200));
        this.confirmPassword.setForeground(new Color(150,150,200));
        this.email.setForeground(new Color(150,150,200));
        this.CNP.setForeground(new Color(150,150,200));
        this.phoneNumber.setForeground(new Color(150,150,200));
        this.birthday.setForeground(new Color(150,150,200));
        this.address.setForeground(new Color(150,150,200));
        this.lastName.setForeground(new Color(150,150,200));
        this.firstName.setForeground(new Color(150,150,200));
        this.job.setForeground(new Color(150,150,200));
    }

    private void setText(){
        this.username.setText("Username: ");
        this.firstName.setText("First name: ");
        this.lastName.setText("Last name: ");
        this.address.setText("Address: ");
        this.birthday.setText("Birthday(DD/MM/YYYY): ");
        this.CNP.setText("CNP: ");
        this.phoneNumber.setText("Phone: ");
        this.email.setText("E-mail: ");
        this.confirmPassword.setText("Confirm: ");
        this.password.setText("Password: ");
        this.job.setText("Job: ");
    }


    void launchSingIn(){
        this.initializeFrame();
        this.initializePanel();
        this.initializeTextFields();
        this.setToolTips();
        this.addComponents();
        this.fakeLabel();
        this.buttonPanel();
        this.setUpButtons();
        this.placeholders();
        this.KeySetter();
        this.activateButtons();
    }

    /**
    This inner private class has the purpose to validate my input and to show different output windows in case of
     incorrectness
     @author cipri
     @version 1.0
     **/
    private class CheckData{
        /**
         * This will be the method that will show any error messages in case of empty fields, or wrong format in one of the fields
         * (for example, wrong e-mail, like __78@.);
         * From here, I will call every specific method to check all the possibilities
         * The process of validation is done with the help of a DFA(Determined Finite Automate)
         */
        boolean errorWindow(){
            if(!emptyCase())
                return false;
            if(!checkUsername()){
                JOptionPane.showMessageDialog(null,"Username must respect the following format: " +
                                "Capital letter(s),Small letter(s),Number(s)","Wrong username"
                        ,JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if(!checkFirstName()){
                JOptionPane.showMessageDialog(null,"FirstName must respect the following format: " +
                                "Capital letter,Small letters(optional a dash followed by another name)","Wrong FirstName"
                        ,JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(!checkLastName()){
                JOptionPane.showMessageDialog(null,"LastName must respect the following format: " +
                                "Capital letter,Small letters","Wrong LastName"
                        ,JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(!checkPassword()){
                JOptionPane.showMessageDialog(null,"Password must respect the following format: "+
                        "Any character and a minimum of 2 numbers","Password wrong",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(!checkConfirmPassword()){
                JOptionPane.showMessageDialog(null,"Passwords are not identical","Wrong password",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(!checkAddress()){
                JOptionPane.showMessageDialog(null,"Address must respect the following format: "+
                        "Country, City, Street","Wrong address",JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(!checkCNP()){
                JOptionPane.showMessageDialog(null,"CNP must contain 13 number character!","Wrong CNP",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(!checkBirthday()){
                JOptionPane.showMessageDialog(null,"Birthday must be valid and follow the next format: "+
                        "DD/MM/YYYY; Or you don't have 18 years old;","Wrong birthday",JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(!checkEmail()){
                JOptionPane.showMessageDialog(null,"Email must follows the next format: "+
                        "text[.-_]text@text.domain","Wrong email",JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(!checkJob()){
                JOptionPane.showMessageDialog(null,"Job description must use letter(s) only "+
                        "and start with a capital letter","Wrong job",JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(!checkPhone()){
                JOptionPane.showMessageDialog(null,"Phone number must have 10 digits",
                        "Wrong phone number",JOptionPane.WARNING_MESSAGE);
                return false;
            }

            return true;
        }

        //I will let the default constructor to do the job,because I have nothing to worry about;
        boolean emptyCase() {
            if (!checkIfEmpty())
                JOptionPane.showMessageDialog(null,"You can't have empty fields","Empty Fields"
                        ,JOptionPane.WARNING_MESSAGE);
            return checkIfEmpty();
        }

        boolean checkIfEmpty(){
            Color c=new Color(150,150,200);
            if(username.getForeground().equals(c) || username.getText().isEmpty())
                return false;
            if(password.getForeground().equals(c) || password.getText().isEmpty())
                return false;
            if(confirmPassword.getForeground().equals(c) || confirmPassword.getText().isEmpty())
                return false;
            if(firstName.getForeground().equals(c) || firstName.getText().isEmpty())
                return false;
            if(lastName.getForeground().equals(c) || lastName.getText().isEmpty())
                return false;
            if(CNP.getForeground().equals(c) || CNP.getText().isEmpty())
                return false;
            if(address.getForeground().equals(c) || address.getText().isEmpty())
                return false;
            if(birthday.getForeground().equals(c) || birthday.getText().isEmpty())
                return false;
            if(email.getForeground().equals(c) || email.getText().isEmpty())
                return false;
            if(job.getForeground().equals(c) || job.getText().isEmpty())
                return false;
            return !(phoneNumber.getForeground().equals(c) || phoneNumber.getText().isEmpty());
        }

        boolean checkUsername(){
            State s0=new State("","[A-Z]");
            State s1=new State("[A-Z]","[a-z]");
            State s2=new State("[a-z]","[0-9]");
            State s3=new State("[0-9]","",true);
            FiniteAutomata automate=new FiniteAutomata(username.getText(),s0,s1,s2,s3);
            return automate.validateData();
        }

        boolean checkLastName(){
            State s0=new State("","[A-Z]");
            State s1=new State("","[a-z]");
            State s2=new State("","[a-z]");
            State s3=new State("[a-z]","",true);

            FiniteAutomata automate=new FiniteAutomata(lastName.getText(),s0,s1,s2,s3);
            return automate.validateData();
        }

        boolean checkFirstName(){
            State s0=new State("","[A-Z]");
            State s1=new State("","[a-z]");
            State s2=new State("","[a-z]");
            State s3=new State("[a-z]","[\\-]",true);
            State s4=new State("[a-z]","",true);

            FiniteAutomata automate=new FiniteAutomata(firstName.getText(),s0,s1,s2,s3,s0,s1,s2,s4);
            return automate.validateData();
        }

        boolean checkPassword(){
            State s0=new State(".","[0-9]");
            State s1=new State(".","[0-9]");
            State s2=new State(".","",true);

            FiniteAutomata automate=new FiniteAutomata(password.getText(),s0,s1,s2);
            return automate.validateData();
        }

        boolean checkConfirmPassword(){
            return password.getText().equals(confirmPassword.getText());
        }

        boolean checkAddress(){
            State s0=new State("[A-Z,a-z ]","[,]");
            State s1=new State("[A-Z,a-z ]","[,]");
            State s2=new State("[A-Za-z0-9 ]","",true);

            FiniteAutomata automate=new FiniteAutomata(address.getText(),s0,s1,s2);
            return automate.validateData();
        }

        boolean checkCNP(){
            State s0=new State("","[0-9]");
            State s1=new State("","",true);
            FiniteAutomata automate=new FiniteAutomata(CNP.getText(),s0,s0,s0,s0,s0,s0,s0,s0,s0,s0,s0,s0,s0,s1);

            return automate.validateData();
        }

        boolean checkBirthday() {
            State s0 = new State("", "[0-9]");
            State s1 = new State("", "[/]");
            State s2 = new State("", "", true);
            FiniteAutomata automate = new FiniteAutomata(birthday.getText(), s0, s0, s1, s0, s0, s1, s0, s0, s0, s0, s2);
            if (automate.validateData()) {
                DateTimeFormatter format = DateTimeFormatter
                        .ofPattern("dd/MM/yyyy");
                try{
                    LocalDate lc=LocalDate.parse(birthday.getText(),format);
                    LocalDate timeNow=LocalDate.now();
                    int year=timeNow.getYear()-lc.getYear();
                    return year>=18 && year<=70;
                }catch (DateTimeException exp) {
                    return false;
                }
            }
            return false;
        }

        boolean checkEmail(){
            State s=new State("","[a-zA-Z]");
            State s0=new State("[a-zA-Z0-9\\.\\-_]","[@]");
            State s1=new State("[a-zA-Z\\-]","[\\.]");
            State s2=new State("","[a-z]");
            State s3=new State("[a-z]","",true);

            FiniteAutomata automate=new FiniteAutomata(email.getText(),s,s0,s1,s2,s3);
            return automate.validateData();
        }

        boolean checkJob(){
            State s0=new State("","[A-Z]");
            State s1=new State("[A-Za-z ]","",true);

            FiniteAutomata automate=new FiniteAutomata(job.getText(),s0,s1);
            return automate.validateData();
        }

        boolean checkPhone(){
            State s0=new State("","[0-9]");
            State s1=new State("","",true);

            FiniteAutomata automate=new FiniteAutomata(phoneNumber.getText(),s0,s0,s0,s0,s0,s0,s0,s0,s0,s0,s1);
            return automate.validateData();
        }
    }

    //TODO database entry and verify if data exists already
}