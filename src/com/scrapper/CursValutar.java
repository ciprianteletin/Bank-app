package com.scrapper;

import org.jsoup.*;
import org.jsoup.nodes.*;

import javax.swing.*;
import java.io.IOException;

public class CursValutar {
    private  String curs;
    public double getLira(){
        String text=curs.substring(curs.indexOf("Lira sterlin"));
        StringBuilder val=new StringBuilder("");
        int space=0;
        int i=0;
        while(true){
            char c=text.charAt(i++);
            if(c==' ') {
                ++space;
                if(space==3)
                    break;
            }
            val.append(c);
        }
        text=val.toString();
        double euro=Double.parseDouble(text.split(" ")[2]);
        System.out.println(euro);
        return euro;
    }

    public double getDolar(){
        String text=curs.substring(curs.indexOf("Dolar american"));
        StringBuilder val=new StringBuilder("");
        int space=0;
        int i=0;
        while(true){
            char c=text.charAt(i++);
            if(c==' ') {
                ++space;
                if(space==3)
                    break;
            }
            val.append(c);
        }
        text=val.toString();
        double dolar=Double.parseDouble(text.split(" ")[2]);
        System.out.println(dolar);
        return dolar;
    }

    public double getEuro(){
        String text=curs.substring(curs.indexOf("Euro"));
        StringBuilder val=new StringBuilder("");
        int space=0;
        int i=0;
        while(true){
            char c=text.charAt(i++);
            if(c==' ') {
                ++space;
                if(space==2)
                    break;
            }
            val.append(c);
        }
        text=val.toString();
        double euro=Double.parseDouble(text.split(" ")[1]);
        System.out.println(euro);
        return euro;
    }

    public CursValutar(){
        try {
            Document doc = Jsoup.connect("https://www.cursvalutar.ro/").get();
            curs=doc.text();
        }catch (IOException err){
            JOptionPane.showMessageDialog(null,"Can't connect to the online page. Try again later...",
                    "No connexion",JOptionPane.WARNING_MESSAGE);
        }
    }
}
