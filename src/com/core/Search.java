package com.core;

import com.URL;
import org.jdatepicker.impl.*;
import com.card.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * Clasa cu acelasi rol ca si cea numita Istoric, cu mentiunea ca in cazul acestei interfete grafice, utilizatorul poate preciza
 * anumite criterii pentru afisarea tranzactiilor, precum data in care s-au efectuat, intervalul de pret sau numele produsului(
 * poate fi si o parte din acesta, se vor face potriviri pe text, fara a tine cont de dimensiunea literelor).
 *
 * Fiecare metoda de cautare(implementata pentru fiecare buton in parte) va verifica existenta tranzactiilor, caz in care
 * se va afisa un mesaj de eroare. La fiecare afisare, se vor afisa doar tranzactiile corecte(se va goli la fiecare reutilizare
 * zona de text).
 */
public class Search {
    private JFrame display;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private JPanel constant,change;
    private JTextField nume,pret;
    private JComboBox<String> selectare;
    private JButton date,value,name;
    private JLabel info;
    private ScrollTranzactii scroll=new ScrollTranzactii();
    private Card card;
    private Enable enable;
    private Connection conn;

    Search(Card c,Enable e){
        this.card=c;
        this.enable=e;
        try{
            conn= DriverManager.getConnection(URL.url,"cipri","linux_mint");
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't connect to database...closing the app",
                    "Fatal error",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
        scroll.getFrame().setVisible(false);
        display=new JFrame("Search");
        display.getContentPane().setBackground(new Color(56,56,56));
        display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        display.setLocationRelativeTo(null);
        display.setResizable(false);
        display.setSize(new Dimension(400,200));
        display.setLayout(new BoxLayout(display.getContentPane(),BoxLayout.PAGE_AXIS));
        addClose();
        initConstant();
        initDatePicker();
        initChange();
        addFocus();
        addTool();
        addComboListener();
        makeDate();
        display.setVisible(true);
    }

    private void addClose(){
        this.display.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                enable.enable();
                scroll.getFrame().dispose();
                try {
                    conn.close();
                }catch (SQLException sql){
                    //Nothing
                }
            }
        });
    }

    private void addTool(){
        nume.setToolTipText("Insert the name of the product or a part of it");
        pret.setToolTipText("Insert 2 integer numbers who represent the price where we can fit product price");
    }

    private void addKey(){
        pret.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char c=keyEvent.getKeyChar();
                if((c<'0' || c>'9') && c!='-' && c!=8)
                    keyEvent.consume();
                if(pret.getText().contains("-") && c=='-')
                    keyEvent.consume();
                if(pret.getText().isEmpty() && c=='-')
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //Nothing here
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //Nothing here
            }
        });
    }

    private void addFocus(){
        nume.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(nume.getForeground().equals(new Color(0,100,100))){
                    nume.setText("");
                    nume.setForeground(new Color(56,56,56));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(nume.getText().isEmpty()){
                    nume=new JTextField("ex. Intel Core I7...");
                    nume.setForeground(new Color(0,100,100));
                }
            }
        });

        pret.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if(pret.getForeground().equals(new Color(0,100,100))){
                    pret.setText("");
                    pret.setForeground(new Color(56,56,56));
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(pret.getText().isEmpty()){
                    pret=new JTextField("ex. 1000-3000");
                    pret.setForeground(new Color(0,100,100));
                }
            }
        });
    }

    private void initConstant(){
        constant=new JPanel(new GridLayout(2,1));
        constant.setPreferredSize(new Dimension(400,200));
        constant.setBackground(new Color(56,56,56));
        info=new JLabel("Selectati modul dupa care doriti sa filtrati datele: ");
        info.setForeground(new Color(255,255,255));
        selectare=new JComboBox<>();
        selectare.addItem("Date");
        selectare.addItem("Pret");
        selectare.addItem("Nume");
        constant.add(info);
        constant.add(selectare);
        display.add(constant);
    }

    /**
     * In aceasta metoda se poate observa modificarea(prin intermediul apelului prin alta metoda) dinamica a continutului si a
     * componentelor panoului din partea de jos a interfetei.
     * Aceasta modificare se face in functie de optiunea selectata, astfel incat utilizatorul poate introduce si cauta tranzactii
     * in functie de necesitate
     */
    private void addComboListener(){
        selectare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String select=(String)selectare.getSelectedItem();
                scroll.getFrame().setVisible(false);
                if(select.equals("Date")){
                    change.removeAll();
                    change.revalidate();
                    makeDate();
                    change.add(datePicker);
                    change.add(date);
                }else if(select.equals("Pret")){
                    change.removeAll();
                    change.revalidate();
                    //I found a strange behavior there, my focus is gone if I don't call this;
                    addFocus();
                    addTool();
                    addKey();
                    makePrice();
                    change.add(pret);
                    change.add(value);
                }else if(select.equals("Nume")){
                    change.removeAll();
                    change.revalidate();
                    addFocus();
                    addTool();
                    makeName();
                    change.add(nume);
                    change.add(name);
                }
            }
        });
    }

    private void initChange(){
        change=new JPanel(new GridLayout(2,1));
        change.setBackground(new Color(56,56,56));
        change.setPreferredSize(new Dimension(400,200));

        nume=new JTextField("ex. Intel Core I7...");
        nume.setForeground(new Color(0,100,100));

        pret=new JTextField("ex. 1000-3000");
        pret.setForeground(new Color(0,100,100));

        date=new JButton("Confirm");
        date.setBackground(new Color(56,56,56));
        date.setForeground(new Color(0,255,0));
        date.setFocusPainted(false);

        name=new JButton("Confirm");
        name.setBackground(new Color(56,56,56));
        name.setForeground(new Color(0,255,0));
        name.setFocusPainted(false);

        value=new JButton("Confirm");
        value.setBackground(new Color(56,56,56));
        value.setForeground(new Color(0,255,0));
        value.setFocusPainted(false);

        change.add(datePicker);
        change.add(date);
        display.add(change);
    }

    private void makePrice(){
        value.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scroll.getTextArea().setText("");
                if(checkNumbers()){
                    String[] prices=pret.getText().split("-");
                    int p1=Integer.parseInt(prices[0]);
                    int p2=Integer.parseInt(prices[1]);
                    try{
                        PreparedStatement pst=conn.prepareStatement("SELECT name,price,moneda,buy_date FROM tranzactii " +
                                "WHERE cardID=? AND price BETWEEN ? AND ?");
                        pst.setInt(1,card.getID());
                        pst.setInt(2,p1);
                        pst.setInt(3,p2);

                        boolean inf=false;
                        ResultSet rs=pst.executeQuery();
                        JTextArea text=scroll.getTextArea();
                        while(rs.next()){
                            inf=true;
                            String name=rs.getString(1);
                            double price=rs.getDouble(2);
                            String moneda=rs.getString(3);
                            java.sql.Date date=rs.getDate(4);
                            text.append(name+"\n-amount "+price+" "+moneda+" in date "+date+"\n\n");
                        }
                        if(!inf){
                            scroll.getFrame().setVisible(false);
                            JOptionPane.showMessageDialog(null,"There are no transaction with the price between inserted data!",
                                    "No transactions",JOptionPane.WARNING_MESSAGE);
                        }else{
                            scroll.getFrame().setVisible(true);
                        }

                    }catch (SQLException sql){
                        //Nothing to do
                    }
                }
            }
        });
    }

    private void makeDate(){
        date.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scroll.getTextArea().setText("");
                Object obj=datePicker.getModel().getValue();
                if(obj!=null) {
                    Date selectedDate = (Date) obj;
                    java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
                    try{
                        PreparedStatement pst=conn.prepareStatement("SELECT name,price,moneda,buy_date FROM tranzactii " +
                                "WHERE buy_date=? AND cardID=?");
                        pst.setDate(1,sqlDate);
                        pst.setInt(2,card.getID());
                        boolean inf=false;
                        ResultSet rs=pst.executeQuery();
                        JTextArea text=scroll.getTextArea();
                        while(rs.next()){
                            inf=true;
                            String name=rs.getString(1);
                            double price=rs.getDouble(2);
                            String moneda=rs.getString(3);
                            java.sql.Date date=rs.getDate(4);
                            text.append(name+"\n-amount "+price+" "+moneda+" in date "+date+"\n\n");
                        }

                        if(!inf){
                            scroll.getFrame().setVisible(false);
                            JOptionPane.showMessageDialog(null,"There are no transaction on the selected data!",
                                    "No transactions",JOptionPane.WARNING_MESSAGE);
                        }else{
                            scroll.getFrame().setVisible(true);
                        }

                    }catch (SQLException sql){
                        //Nothing to do
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Nothing selected!");
                }
            }
        });
    }

    private void makeName(){
        name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scroll.getTextArea().setText("");
                if(nume.getForeground().equals(new Color(56,56,56)) && !nume.getText().isEmpty()){
                    String selectedName=nume.getText().toLowerCase();
                    try{
                        PreparedStatement pst=conn.prepareStatement("SELECT name,price,moneda,buy_date FROM tranzactii WHERE cardID=?");
                        pst.setInt(1,card.getID());
                        ResultSet rs=pst.executeQuery();
                        boolean inf=false;
                        JTextArea text=scroll.getTextArea();
                        while(rs.next()){
                            String name=rs.getString(1).toLowerCase();
                            if(name.contains(selectedName)) {
                                inf=true;
                                name=rs.getString(1);
                                double price = rs.getDouble(2);
                                String moneda = rs.getString(3);
                                java.sql.Date date = rs.getDate(4);
                                text.append(name+"\n-amount "+price+" "+moneda+" in date "+date+"\n\n");
                            }
                        }
                        if(!inf){
                            JOptionPane.showMessageDialog(null,"No transaction found!");
                            scroll.getFrame().setVisible(false);
                        }else{
                            scroll.getFrame().setVisible(true);
                        }
                    }catch (SQLException sql){
                        //Nothing here
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"No name was inserted here!");
                }
            }
        });
    }

    private boolean checkNumbers(){
        if(pret.getText().isEmpty() || pret.getForeground().equals(new Color(0,100,100))) {
            JOptionPane.showMessageDialog(null,"No prices inserted!");
            return false;
        }
        if(!pret.getText().contains("-")){
            JOptionPane.showMessageDialog(null,"You must write an interval!");
            return false;
        }

        String[] prices=pret.getText().split("-");
        try{
            int p1=Integer.parseInt(prices[0]);
            int p2=Integer.parseInt(prices[1]);
            if(p1>p2){
                JOptionPane.showMessageDialog(null,"First number must be less than the second one!");
                return false;
            }
        }catch (NumberFormatException | ArrayIndexOutOfBoundsException nr){
            JOptionPane.showMessageDialog(null,"Numbers don't have a proper format!");
            return false;
        }
        return true;
    }

    private void initDatePicker(){
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }
}
//pentru formatul specific dat, astfel incat data extrasa si afisata sa fie in formatul necesar pentru obiecte de tip Date(java.sql.Date)
class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        return null;
    }
}
