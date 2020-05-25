package com.login;

import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import com.URL;
import com.automata.State;
import com.automata.FiniteAutomata;
import com.cardGenerator.GenerateCard;
import com.core.Settings;

/**
This class has the scope to let the client to choose what type of card they posses, at what bank, with what kind of costs in
 case of different operation(like paying taxes, transfer between accounts, online services)

 Also, the client will receive the sum of his credit card(I will generate a random value to simulate a real credit card);
 I will add a method to add money on the account from time to time(random).
 Also, I will generate random card type and from what bank is it, because I'm thinking that the card is recognised automated
 based on the provided information
 */
public class ChooseAccountInterface {
    private ImagePanel accountPanel;
    private JFrame account;
    private JTextField cardNumber,CVV;
    private JTextField expireDate;
    private JButton confirm;
    private String username;
    private int age;
    private String job;
    private boolean noCards;

    public ChooseAccountInterface(String username,int age,String job){
        this.username=username;
        this.noCards=false;
        this.age=age;
        this.job=job;
        account=new JFrame("Add card");
        account.setResizable(false);
        account.setPreferredSize(new Dimension(800,420));
        account.setLocationRelativeTo(null);
        closeApp();
        createBack();
        setTextFields();
        setButton();
        addComponents();
        placeHolders();
        fakeLabel();
        addMinus();
        restrictCvv();
        restrictExpireDate();
        buttonOnAction();
        account.pack();
        account.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        account.setVisible(true);
    }

    public ChooseAccountInterface(String username,int age,String job,boolean noCards){
        this(username,age,job);
        this.noCards=noCards;
    }
    /**
    If user presses exit on this interface, I will delete all the new records data that has no correspondent;
     Correspondent means that it has no card registered; This was a problem although, because when I removed all my cards
     from the Settings interface, there was a possibility that my client could lost his account(solved)
     **/
    private void closeApp(){
        account.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    Connection connection = DriverManager.getConnection(URL.url, "root", "linux_mint");
                    PreparedStatement pst=connection.prepareStatement("SELECT cardID FROM card_data WHERE user=?");
                    pst.setString(1,username);
                    ResultSet rs=pst.executeQuery();
                    //in case of having already an account and want to add another card on it;
                    if(!rs.next() && !noCards) {
                        pst = connection.prepareStatement("DELETE FROM adress WHERE user=?");
                        pst.setString(1, username);
                        pst.executeUpdate();

                        pst = connection.prepareStatement("DELETE FROM info WHERE user=?");
                        pst.setString(1, username);
                        pst.executeUpdate();

                        pst = connection.prepareStatement("DELETE FROM passwords WHERE user=?");
                        pst.setString(1, username);
                        pst.executeUpdate();

                        pst = connection.prepareStatement("DELETE FROM users WHERE user=?");
                        pst.setString(1, username);
                        pst.executeUpdate();
                    }
                    if(noCards)
                        SwingUtilities.invokeLater(()->new Settings(1,username));
                    connection.close();
                }catch (SQLException err){
                    err.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Connexion failed...Closing the app");
                    System.exit(1);
                }
            }
        });
    }

    private void placeHolders(){
        cardNumber.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(cardNumber.getForeground().equals(new Color(150,150,200))) {
                    cardNumber.setText("");
                    cardNumber.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(cardNumber.getText().isEmpty()){
                    cardNumber.setText("Card number: ");
                    cardNumber.setForeground(new Color(150,150,200));
                }
            }
        });

        CVV.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(CVV.getForeground().equals(new Color(150,150,200))) {
                    CVV.setText("");
                    CVV.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(CVV.getText().isEmpty()) {
                    CVV.setText("CVV: ");
                    CVV.setForeground(new Color(150,150,200));
                }
            }
        });

        expireDate.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(expireDate.getForeground().equals(new Color(150,150,200))) {
                    expireDate.setText("");
                    expireDate.setForeground(new Color(50, 70, 90));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(expireDate.getText().isEmpty()){
                    expireDate.setText("Expire date mm/yyyy: ");
                    expireDate.setForeground(new Color(150,150,200));
                }
            }
        });
    }

    private void fakeLabel(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy=1;
        gbc.gridx=0;
        JLabel fake=new JLabel();
        fake.setPreferredSize(new Dimension(1,1));
        SwingUtilities.invokeLater(fake::requestFocus);
        accountPanel.add(fake,gbc);
    }

    private void createBack(){
        accountPanel =new ImagePanel();
        account.add(accountPanel);
    }

    private void setButton(){
        confirm=new JButton("Confirm");
        confirm.setPreferredSize(new Dimension(120,40));
        confirm.setBackground(Color.DARK_GRAY);
        confirm.setForeground(Color.YELLOW);
    }

    private void addComponents(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor=GridBagConstraints.FIRST_LINE_START;
        gbc.weighty = 0;
        gbc.weightx = 0;
        gbc.gridx=0; gbc.gridy=0;
        accountPanel.setLayout(new GridBagLayout());
        accountPanel.add(cardNumber,gbc);
        gbc.gridx=1; gbc.gridy=1;
        gbc.anchor=GridBagConstraints.CENTER;
        accountPanel.add(CVV,gbc);
        gbc.gridx=2; gbc.gridy=2;
        gbc.anchor=GridBagConstraints.LAST_LINE_END;
        accountPanel.add(expireDate,gbc);
        gbc.gridx=2; gbc.gridy=3;
        gbc.weighty = 0;
        accountPanel.add(confirm,gbc);
    }

    private void setTextFields(){
        cardNumber=new JTextField("Card number: ");
        cardNumber.setHorizontalAlignment(JTextField.CENTER);
        cardNumber.setPreferredSize(new Dimension(200,35));
        cardNumber.setForeground(new Color(150,150,200));
        CVV=new JTextField("CVV: ");
        CVV.setHorizontalAlignment(JTextField.CENTER);
        CVV.setPreferredSize(new Dimension(200,35));
        CVV.setForeground(new Color(150,150,200));
        expireDate=new JTextField("Expire date mm/yyyy: ");
        expireDate.setHorizontalAlignment(JTextField.CENTER);
        expireDate.setPreferredSize(new Dimension(200,35));
        expireDate.setForeground(new Color(150,150,200));
    }

    private void restrictExpireDate(){
        expireDate.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(expireDate.getText().length()>6)
                    keyEvent.consume();
                char number=keyEvent.getKeyChar();
                if(number > '9' || number < '0')
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //Nothing to do here
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                String onlyNumbers="";
                for(int i=0;i<expireDate.getText().length();++i) {
                    char c=expireDate.getText().charAt(i);
                    if(c>='0' && c<='9'){
                       onlyNumbers+=c;
                    }
                }

                StringBuilder date=new StringBuilder("");
                for(int i=0;i<onlyNumbers.length();++i){
                    if(i==2)
                        date.append('/');
                    date.append(onlyNumbers.charAt(i));
                }

                expireDate.setText(date.toString());
            }
        });
    }

    private void restrictCvv(){
        CVV.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char keyChar=keyEvent.getKeyChar();
                if(CVV.getText().length()==3)
                    keyEvent.consume();
                if(keyChar>'9' || keyChar<'0')
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //Nothing to do
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //Nothing to do
            }
        });
    }

    private void addMinus(){
        cardNumber.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(cardNumber.getText().length()>=19 || (keyEvent.getKeyChar()<'0' || keyEvent.getKeyChar()>'9'))
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //Nothing here to do
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                String number=cardNumber.getText();
                char charPressed=keyEvent.getKeyChar();
                if(number.length()<19 && (charPressed<='9' && charPressed>='0' || charPressed==8)){
                    String onlyNumbers="";
                    for(int i=0;i<number.length();i++){
                        if(number.charAt(i)>='0' && number.charAt(i)<='9')
                            onlyNumbers+=number.charAt(i);
                    }
                    number="";
                    for(int i=0;i<onlyNumbers.length();i++){
                        if(i%4==0 && i!=0)
                            number+='-';
                        number+=onlyNumbers.charAt(i);
                    }
                cardNumber.setText(number);
                }
            }
        });
    }

    private boolean checkNotEmpty(){
        return cardNumber.getText().isEmpty() || CVV.getText().isEmpty() || expireDate.getText().isEmpty();
    }

    private boolean checkData(){
        if(!checkNumber()){
            JOptionPane.showMessageDialog(null,"Text number must have 16 digits and the next format:" +
                    "xxxx-xxxx-xxxx-xxxx","Invalid number",JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if(!checkExpireDate()){
            JOptionPane.showMessageDialog(null,"Expire date must be valid and to follow the" +
                    "next format: xx/xxxx or the card is already expired","Expire date wrong",JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if(CVV.getText().length()!=3){
            JOptionPane.showMessageDialog(null,"CVV must respect the following format: xxx","Wrong CVV"
                    ,JOptionPane.WARNING_MESSAGE);
            return false;
        }
            return true;
    }

    private boolean checkNumber(){
        if(cardNumber.getText().length()!=19)
            return false;
        State s0=new State("","[0-9]");
        State s1=new State("","-");
        State s2=new State("","",true);

        FiniteAutomata automate=new FiniteAutomata(cardNumber.getText(),s0,s0,s0,s0,s1,s0,s0,s0,s0,s1
        ,s0,s0,s0,s0,s1,s0,s0,s0,s0,s2);
        return automate.validateData();
    }

    private boolean checkMonth(String month){
        int mon=Integer.parseInt(month); //doesn't throw an exception because I restricted my input to be only formed with digits
        return mon>=1 && mon<=12;
    }

    private boolean checkYear(String year){
        int yr=Integer.parseInt(year);
        int crtYr= LocalDate.now().getYear();
        return yr > crtYr && yr - crtYr < 10;
    }

    private boolean checkBoth(String month,String year){
        int yr=Integer.parseInt(year);
        int crtYr= LocalDate.now().getYear();
        if(yr!=crtYr)
            return false;
        int m=Integer.parseInt(month);
        int crtMonth=LocalDate.now().getMonthValue();
        return crtMonth>m;
    }

    private boolean checkExpireDate(){
        if(expireDate.getText().length()!=7)
            return false;
        String[] splitted=expireDate.getText().split("/");
        String month=splitted[0];
        String year=splitted[1];
        return (checkMonth(month) && checkYear(year)) || checkBoth(month,year);
    }

    private void clear(){
        CVV.setText("CVV: ");
        CVV.setForeground(new Color(150,150,200));
        cardNumber.setText("Card number: ");
        cardNumber.setForeground(new Color(150,150,200));
        expireDate.setText("Expire date mm/yyyy: ");
        expireDate.setForeground(new Color(150,150,200));
    }

    /**
     * Event for checking the provided information and if I can create a card(if the card number is less or equal to a specific
     * card limit).
     * If it's the second or my third card that I want to create, it will be set to "false" because I have a active card already
     * (the one that is used to access the button from Settings interface). Also, via this, I insert card info in my database.
     */
    private void buttonOnAction(){
        confirm.addActionListener((ae)->{
            if(checkNotEmpty())
                JOptionPane.showMessageDialog(null,"You can't have empty fields!","Error",
                        JOptionPane.WARNING_MESSAGE);
            else if(checkData()) {
                try {
                    Connection connection = DriverManager.getConnection(URL.url, "root", "linux_mint");
                    PreparedStatement pt=connection.prepareStatement("SELECT user FROM card_data WHERE user=?");
                    pt.setString(1,username);
                    ResultSet result=pt.executeQuery();
                    int cardNr=0;
                    while(result.next()){
                        ++cardNr;
                    }

                    if((job.toLowerCase().equals("student") || age<=26) && cardNr==1){
                        JOptionPane.showMessageDialog(null,"A student can have only one card recorded",
                                "Too many cards!",JOptionPane.WARNING_MESSAGE);
                        SwingUtilities.invokeLater(new LoginInterface()::start);
                        account.dispose();
                    }

                    else if(cardNr==3){
                        JOptionPane.showMessageDialog(null,"You can have a maximum of three cards recorded!",
                                "Too many cards!",JOptionPane.WARNING_MESSAGE);
                        SwingUtilities.invokeLater(new LoginInterface()::start);
                        account.dispose();
                    }

                    else{
                        pt=connection.prepareStatement("SELECT user FROM card_data WHERE number=? AND cvv=?");
                        pt.setString(1,cardNumber.getText());
                        pt.setString(2,CVV.getText());

                        result=pt.executeQuery();

                        if(result.next()){
                            JOptionPane.showMessageDialog(null,"Card already registered!" +
                                            "Insert another card;",
                                    "Card registered!",JOptionPane.WARNING_MESSAGE);
                            clear();
                        }

                        else{
                            char current='F';
                            //if it's the first card, then current will be T, else will be F because we already
                            //have a current card;
                            if(cardNr==0)
                                current='T';
                            GenerateCard.generate(age,job,cardNr+1,username,cardNumber.getText(),
                                    CVV.getText(),expireDate.getText(),current);
                            JOptionPane.showConfirmDialog(null, "Your card was inserted with success!",
                                    "Success!",JOptionPane.DEFAULT_OPTION);
                            if(cardNr==0)
                                SwingUtilities.invokeLater(new LoginInterface()::start);
                            else
                                SwingUtilities.invokeLater(()->new Settings(1,username));
                            account.dispose();
                        }
                    }

                    connection.close();
                }catch (SQLException err){
                    err.printStackTrace();
                    System.out.println("Error in accessing local databass...Closing the app");
                    System.exit(1);
                }
            }
        });
    }
}

class ImagePanel extends JPanel{

    private BufferedImage image;

    ImagePanel() {
        try {
            image = ImageIO.read(new File("D:\\Aplicatie Bancara\\src\\imagini\\bank.jpg"));
        } catch (IOException ex) {
            System.out.println("Image not found...closing the program");
            System.exit(-1);
        }
    }
    /**
     * This is some good stuff that I found on StackOverflow, it adds a background to my JPanel(Image Panel
     * by extends) and it let me do my job without the override of components;
     * Until this project will be finish, I will be learning about this, but anyway, it's quite straightforward
     * Method it's called on it's own;
     * @param g it contains a graphic component which allows me to draw my background;
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
