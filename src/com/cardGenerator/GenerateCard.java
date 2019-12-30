package com.cardGenerator;

import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

import com.URL;

//TODO, dar ne trebuie mai intai clasa card aici, ca sa pot adauga ce e nevoie
//LA SIGN UP si ADD CARD, generam card de aici;
public abstract class GenerateCard {
    //method to generate cards; the first one will keep the salary in it, and of course here will be the main wealth of the user;

    public static void generate(int age,String job,int cardNumber,String user,
                                String number,String cvv,String expireDate,char current){
        Random r=new Random();
        boolean underStudent=false;
        int ban=r.nextInt(5); //bank type random;

        Banci banca=Banci.values()[ban]; //one of the banks defined in enum Banci;
        //IBAN generator
        String IBAN=generateIban(banca);
        //IBAN generator
        TipCard tipCard;
        int tip;
        boolean unemployed=false;
        if(age<=26 || job.toLowerCase().equals("student")) {
            underStudent=true; //if it's under 26 or a student, I assume that he's not working and that it has a student
            //card, and only one
            tipCard=TipCard.Student;
        }
        else if(job.toLowerCase().equals("unemployed")){
            //an unemployed person can have multiple cards(3), but with a lower amount of money on it based on age
            // this time;
            unemployed=true;
            tip = r.nextInt(4); //card type random;
            tipCard = TipCard.values()[tip];
        }
        else {
            tip = r.nextInt(4);
            tipCard = TipCard.values()[tip];
        }

        double limita;
        String moneda="Lei";
        limita=Math.round((banca.getLimita()+tipCard.getLimita())*100)/100.0;
        if(tipCard==TipCard.Calator) {
            limita=Math.round(Currency.EURO.convertInLei(limita)*100)/100.0;
            moneda = "Euro";
        }else if(tipCard==TipCard.Depozit){
            limita=-1;
        }

        //for the salary and sum I will consider the age of the person; first, I will check if he is student or unemployed or under 26
        double salary=0.0,sum;
        int salaryDate=-1;
        if(cardNumber==1) {
            salaryDate = r.nextInt(20) + 1;
            if (underStudent || unemployed) {
                sum = (Math.round(r.nextDouble()*100) / 100.0 + 0.4) * 1500.0; //Student or unemployed, so he has a hard life with money :))
                salary = (Math.round(r.nextDouble()*100) / 100.0 + 0.5) * 1000.0;
            } else if (age <= 32) {
                sum = (Math.round(r.nextDouble()*100) / 100.0 + 0.5) * 2600.0;
                salary = (Math.round(r.nextDouble()*100) / 100.0 + 0.5) * 2300.0;
            } else if (age <= 45) {
                sum = (Math.round(r.nextDouble()*100) / 100.0 + 0.7) * 4500.0;
                salary = (Math.round(r.nextDouble()*100) / 100.0 + 0.5) * 3000.0;
            } else {
                sum = (Math.round(r.nextDouble()*100) / 100.0 + 1.0) * 3850.0;
                salary = (Math.round(r.nextDouble()*100) / 100.0 + 0.5) * 4000.0;
            }

        }else{
            if (unemployed) {
                sum = (Math.round(r.nextDouble() * 100) / 100.0 + 0.1) * 1000.0;
            }else if(age <= 32){
                sum = (Math.round(r.nextDouble() * 100) / 100.0 + 0.2) * 1250.0;
            }else if(age<=45){
                sum = (Math.round(r.nextDouble() * 100) / 100.0 + 0.4) * 1550.0;
            }else{
                sum = (Math.round(r.nextDouble() * 100) / 100.0 + 0.6) * 1950.0;
            }
        }
        if(moneda.equals("Euro")){
            sum=Currency.EURO.convertInLei(sum);
        }
        /*I didn't change the comm based on monetary type, because the sum is greater in Lei that in other monetary types,
        so when I apply a comm, I will apply a lower one(because the payed products or taxes will be converted too;
        */

        try{
            Connection conn=DriverManager.getConnection(URL.url,"cipri","linux_mint");

            PreparedStatement pt=conn.prepareStatement("INSERT INTO card_data (user, number, cvv, expire_date, IBAN) " +
                    "VALUES (?,?,?,?,?)");

            pt.setString(1,user);
            pt.setString(2,number);
            pt.setString(3,cvv);
            pt.setString(4,expireDate);
            pt.setString(5,IBAN);
            pt.executeUpdate();

            pt=conn.prepareStatement("SELECT cardID FROM card_data ORDER BY cardID DESC LIMIT 1");
            ResultSet set=pt.executeQuery();
            set.next();
            int cardID=set.getInt(1);
            //only one value, last inserted, so no need to call next multiple times;

            pt=conn.prepareStatement("INSERT INTO card_type VALUES (?,?,?,?,?,?,?)");

            pt.setInt(1,cardID);
            pt.setString(2,banca.toString());
            pt.setString(3,tipCard.toString());
            pt.setString(4,banca.getCifru());
            pt.setString(5,current+"");
            pt.setString(6,"A");
            pt.setString(7,"F");
            pt.executeUpdate();

            //Preparing the data;
            double com_online=banca.getCom_online()+tipCard.getCom_online();
            com_online=Math.round(com_online*100)/100.0;
            double com_transfer=banca.getCom_transf()+tipCard.getCom_transf();
            com_transfer=Math.round(com_transfer*100)/100.0;
            double com_facturi=banca.getCom_factura()+tipCard.getCom_factura();
            com_facturi=Math.round(com_facturi*100)/100.0;

            double dobanda=banca.getDobanda()+tipCard.getDobanda();
            dobanda=Math.round(dobanda*100)/100.0;

            pt=conn.prepareStatement("INSERT INTO financiar VALUES (?,?,?,?,?,?,?,?,?,?)");
            pt.setInt(1,cardID);
            pt.setDouble(2,sum);
            pt.setString(3,moneda);
            pt.setInt(4,salaryDate);
            pt.setDouble(5,limita);
            pt.setDouble(6,salary);
            pt.setDouble(7,com_online);
            pt.setDouble(8,com_facturi);
            pt.setDouble(9,com_transfer);
            pt.setDouble(10,dobanda);
            pt.executeUpdate();

            int zi= LocalDate.now().getDayOfMonth();
            pt=conn.prepareStatement("INSERT INTO data_limit VALUES (?,?,?)");
            pt.setInt(1,cardID);
            pt.setDouble(2,limita);
            pt.setInt(3,zi);
            pt.executeUpdate();
            conn.close();

        }catch (SQLException err){
            err.printStackTrace();
            System.out.println("Cannot connect to database...Exit the app");
            System.exit(1);
        }
    }


    private static String generateIban(Banci banca){
        StringBuilder IBAN=new StringBuilder("RO");
        Random r=new Random();
        int code=r.nextInt(10);
        IBAN.append(code);
        code=r.nextInt(10);
        IBAN.append(code);

        IBAN.append(banca.getCifru());
        for(int i=0;i<16;i++){
            code=r.nextInt(10);
            IBAN.append(code);
        }
        return IBAN.toString();
    }
}
