package com.card;

import com.URL;
import com.cardGenerator.Currency;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
/**
 * Clasa prin intermediul careia se creeaza un card bancar prin preluarea datelor din baza de date. Clasa card este folosita in cadrul
 * interfetelor de gestionare a soldului curent(obiect prin care se realizeaza operatii bancare),
 * dar si in cea in ceea ce priveste setarile cardului curent activ.
 * Prin intermediul ei, de asemenea, se gestioneaza virarea salariului si limita de efectuare a operatiilor zilnice.
 */
public class Card {
    private double suma;
    private double lim_transfer;
    private String moneda;
    private double com_online;
    private double com_factura;
    private double com_transfer;
    private double dobanda;
    private String block;
    private String username;
    private int ID;
    private Connection conn;

    /**
     * Constructorul clasei, prin care se realizeaza conexiunea unica pentru toate metodele clasei in sine;
     * Punctul de apel al restul metodelor
     */
    public Card(String username){
        this.username=username;
        try{
            conn=DriverManager.getConnection(URL.url,"root","linux_mint");
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't create my card...closing the app",
                    "Critical error",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
        updateCardDetails();
        checkVirat();
        checkLimita();
    }

    /**
     * Metoda prin intermediul caruia se verifica daca este necesar sa se vireze salariul clientului sau nu. In cazul in care salariul
     * trebuie virat(am depasit data de virament iar acesta nu a fost transmis catre client), se va actualiza tabelul card_type unde
     * retinem daca banii au fost virati sau nu, iar tranzactia se depunde in tabela tranzactii;
     */
    private void checkVirat(){
        try{
            PreparedStatement pst=conn.prepareStatement("SELECT data_virament,venit_lunar FROM financiar " +
                    "WHERE cardID=?");
            pst.setInt(1,this.ID);
            ResultSet rs=pst.executeQuery();
            rs.next();
            int data=rs.getInt(1);
            double venit=Math.round(rs.getDouble(2)*100)/100.0;
            if(!moneda.equals("Lei")){
                Currency currency=Currency.valueOf(moneda.toUpperCase());
                venit=currency.convertDinLei(venit); //adun salariul la valuta curenta;
            }
            int zi= LocalDate.now().getDayOfMonth();

            pst=conn.prepareStatement("SELECT virat FROM card_type WHERE cardID=?");
            pst.setInt(1,ID);
            rs=pst.executeQuery();
            rs.next();
            String virat=rs.getString(1);
            if(data<=zi && virat.equals("F") && data!=-1){
                double dob=Math.round(((dobanda*venit)/100)*100)/100.0;
                setSuma(getSuma()+venit+dob);
                updateDB();
                pst=conn.prepareStatement("UPDATE card_type SET virat=? WHERE cardID=?");
                pst.setString(1,"T");
                pst.setInt(2,ID);
                pst.executeUpdate();

                pst=conn.prepareStatement("INSERT INTO tranzactii VALUES (?,?,?,?,?)");
                pst.setInt(1,ID);
                pst.setString(2,"Salar");
                pst.setDouble(3,venit);
                pst.setString(4,moneda);
                pst.setDate(5,Date.valueOf(LocalDate.now()));

                pst.executeUpdate();
            }else if(data>zi && virat.equals("T") && data!=-1){
                pst=conn.prepareStatement("UPDATE card_type SET virat=? WHERE cardID=?");
                pst.setString(1,"F");
                pst.setInt(2,ID);
                pst.executeUpdate();
            }
        }catch (SQLException sql){
            sql.printStackTrace();
            JOptionPane.showMessageDialog(null,"Can't create my card...closing the app",
                    "Critical error",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }
    /**
     *Metoda prin intermediul careia se reseteaza zilnic limita de transfer din cazul cardului nostru. Fiecare card are asociata
     *o limita de bani pentru efectuarea operatiei, ce trebuie zilnic resetata pentru a permite utilizatorului sa realizeze ce
     *isi doreste
     */
    private void checkLimita(){
        try{
            PreparedStatement pst=conn.prepareStatement("SELECT limita_transfer,zi FROM data_limit WHERE cardID=?");
            pst.setInt(1,ID);
            ResultSet rs=pst.executeQuery();
            rs.next();
            double limita;
            if(rs.getDouble(1)!=-1) {
                limita = Math.round(rs.getDouble(1) * 100) / 100.0;
                if (!moneda.equals("Lei")) {
                    Currency currency = Currency.valueOf(moneda.toUpperCase());
                    limita = currency.convertDinLei(limita);
                    //convert the current limit to the actual card currency;
                }
            }else
                limita=-1;
                int day = rs.getInt(2);
                int zi = LocalDate.now().getDayOfMonth();
                if (day != zi) {
                    setLim_transfer(limita);
                    pst = conn.prepareStatement("UPDATE data_limit SET zi=? WHERE cardID=?");
                    pst.setInt(1, zi);
                    pst.setInt(2, ID);
                    pst.executeUpdate();
                    updateDB();
                }
        }catch (SQLException sql){
            sql.printStackTrace();
            JOptionPane.showMessageDialog(null,"Can't create my card...closing the app",
                    "Critical error",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }
    /**
     *Metoda prin intermediul careia se extrag din baza de date si din tabelele corespunzatoare datele referitoare
     * la cardul activ cu scopul de a fi stocate in cadrul obiectului nostru.
     */
    public void updateCardDetails(){
        try{
            PreparedStatement pst=conn.prepareStatement("SELECT cd.cardID FROM card_data cd " +
                    "INNER JOIN card_type ct ON cd.cardID = ct.cardID WHERE user=? AND current=?");
            pst.setString(1,username);
            pst.setString(2,"T");
            ResultSet rs=pst.executeQuery();
            rs.next(); //ID of the current card;
            int ID=rs.getInt(1);
            this.ID=ID;
            updateBlock(ID);
            pst=conn.prepareStatement("SELECT suma,lim_transfer,moneda," +
                    "com_online,com_factura,com_transfer,dobanda FROM financiar WHERE cardID=?");
            pst.setInt(1,ID);
            rs=pst.executeQuery();
            rs.next();
            setSuma(rs.getDouble(1));
            setLim_transfer(rs.getDouble(2));
            setMoneda(rs.getString(3));
            setCom_online(rs.getDouble(4));
            setCom_factura(rs.getDouble(5));
            setCom_transfer(rs.getDouble(6));
            setDobanda(rs.getDouble(7));
        }catch (SQLException sql){
            sql.printStackTrace();
            JOptionPane.showMessageDialog(null,"Can't create my card...closing the app",
                    "Critical error",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Metoda ce are ca si scop actualizarea campului "blocked" in cazul in care cardul a fost blocat din setari.
     * @param ID
     * @throws SQLException
     */
    private void updateBlock(int ID) throws SQLException{
        PreparedStatement pst=conn.prepareStatement("SELECT blocked FROM card_type WHERE cardID=?");
        pst.setInt(1,ID);
        ResultSet rs=pst.executeQuery();
        rs.next();
        block=rs.getString(1);
    }

    public boolean isBlocked(){
        return !block.equals("A");
    }
    /**
     * Prin intermediul acestei metode actualizez informatiile fincanicare referitoare la card in urma efectuarii unei operatii
     * de acest tip. Dupa fiecare apel in care se efectueaza o modificare, aceasta metoda va fi apelata
     */
    public void updateDB(){
        try{
            PreparedStatement pst=conn.prepareStatement("UPDATE financiar SET suma=?,lim_transfer=?,moneda=?" +
                    " WHERE cardID=?");
            pst.setDouble(1,getSuma());
            pst.setDouble(2,getLim_transfer());
            pst.setString(3,getMoneda());
            pst.setInt(4,ID);
            pst.executeUpdate();
        }catch (SQLException err){
            //
        }
    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {

        this.suma = Math.round(suma*100)/100.0;
    }

    public double getLim_transfer() {
        return lim_transfer;
    }

    public void setLim_transfer(double lim_transfer) {
        this.lim_transfer=lim_transfer;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public double getCom_online() {
        return com_online;
    }

    public void setCom_online(double com_online) {

        this.com_online = Math.round(com_online*100)/100.0;
    }

    public double getCom_factura() {
        return com_factura;
    }

    public void setCom_factura(double com_factura) {

        this.com_factura = Math.round(com_factura*100)/100.0;
    }

    public double getCom_transfer() {
        return com_transfer;
    }

    public void setCom_transfer(double com_transfer) {

        this.com_transfer = Math.round(com_transfer*100)/100.0;
    }

    public void setDobanda(double dobanda){
        this.dobanda=dobanda;
    }

    public double getDobanda(){return this.dobanda;}

    public void setBlock(String block){
        this.block=block;
    }

    public int getID(){return this.ID;}

    public void closeConnection(){
        try{
            conn.close();
        }catch (SQLException sql){
            //Nothing
        }
    }

    /**
     * Verific daca data de expirare a fost atinsa sau depasita, metoda returnand o valoare booleana ce indica acest lucru
     * @param expire
     * @return
     */
    public static boolean verifyExpireDate(String expire){
        String[] date=expire.split("/");
        int month=Integer.parseInt(date[0]);
        int year=Integer.parseInt(date[1]);

        int currentMonth=LocalDate.now().getMonthValue();
        int currentYear=LocalDate.now().getYear();

        if(currentYear>year)
            return false;

        if(currentYear==year && currentMonth>=month)
            return false;

        return true;
    }

    /**
     * In momentul in care cardul a expirat si clientul doreste prelungirea acestuia, se apeleaza aceasta metoda ce are ca si scop
     * prelungirea si actualizarea datelor din cadrul bazei de date.
     * @param expire
     * @param cardID
     */
    public static void expandContract(String expire,int cardID){
        String[] date=expire.split("/");
        int month=Integer.parseInt(date[0]);
        int year=Integer.parseInt(date[1]);
        ++year;
        String newDate=month+"/"+year;
        try{
            Connection connection=DriverManager.getConnection(URL.url,"root","linux_mint");
            PreparedStatement pst=connection.prepareStatement("UPDATE card_data SET expire_date=? WHERE cardID=?");
            pst.setString(1,newDate);
            pst.setInt(2,cardID);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,"Prelungire realizata cu success!");
            connection.close();
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't launch the app..Closing everything");
            System.exit(1);
        }
    }
}
