package com.card;

import com.URL;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;

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

    public Card(String username){
        this.username=username;
        try{
            conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");
        }catch (SQLException sql){
            JOptionPane.showMessageDialog(null,"Can't create my card...closing the app",
                    "Critical error",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
        updateCardDetails();
        checkVirat();
        checkLimita();
    }

    private void checkVirat(){
        try{
            PreparedStatement pst=conn.prepareStatement("SELECT data_virament,venit_lunar FROM financiar " +
                    "WHERE cardID=?");
            pst.setInt(1,this.ID);
            ResultSet rs=pst.executeQuery();
            rs.next();
            int data=rs.getInt(1);
            double venit=Math.round(rs.getDouble(2)*100)/100.0;
            int zi= LocalDate.now().getDayOfMonth();

            pst=conn.prepareStatement("SELECT virat FROM card_type WHERE cardID=?");
            pst.setInt(1,ID);
            rs=pst.executeQuery();
            rs.next();
            String virat=rs.getString(1);
            if(data==zi && virat.equals("F")){
                setSuma(getSuma()+venit);
                pst=conn.prepareStatement("UPDATE card_type SET virat=? WHERE cardID=?");
                pst.setString(1,"T");
                pst.setInt(2,ID);
                pst.executeUpdate();
            }else if(data!=zi){
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

    private void checkLimita(){
        try{
            PreparedStatement pst=conn.prepareStatement("SELECT limita_transfer,zi FROM data_limit WHERE cardID=?");
            pst.setInt(1,ID);
            ResultSet rs=pst.executeQuery();
            rs.next();
            double limita=Math.round(rs.getDouble(1)*100)/100.0;
            int day=rs.getInt(2);
            int zi=LocalDate.now().getDayOfMonth();
            if(day!=zi){
                setLim_transfer(limita);
                pst=conn.prepareStatement("UPDATE data_limit SET zi=? WHERE cardID=?");
                pst.setInt(1,zi);
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

    public void updateDB(){
        try{
            PreparedStatement pst=conn.prepareStatement("UPDATE financiar SET suma=?,lim_transfer=?,moneda=?" +
                    " WHERE cardID=?");
            pst.setDouble(1,getSuma());
            pst.setDouble(2,getLim_transfer());
            pst.setString(3,getMoneda());
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

    public void closeConnection(){
        try{
            conn.close();
        }catch (SQLException sql){
            //Nothing
        }
    }

    @Override
    public String toString() {
        return "Card{" +
                "suma=" + suma +
                ", lim_transfer=" + lim_transfer +
                ", moneda='" + moneda + '\'' +
                ", com_online=" + com_online +
                ", com_factura=" + com_factura +
                ", com_transfer=" + com_transfer +
                ", dobanda=" + dobanda +
                ", block='" + block + '\'' +
                ", username='" + username + '\'' +
                ", ID=" + ID +
                '}';
    }
}
