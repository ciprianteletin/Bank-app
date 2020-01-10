package com.scrapper;

import org.jsoup.*;
import org.jsoup.select.Elements;
import org.jsoup.nodes.*;

import javax.swing.*;

/**
 * The purpose of this class is to get the price of the product as well as the name of it from the online marketplace Cel.ro;
 * Addressed method to extract desired data: obtain the document(html code) from the specific web page, sending specific html paragraph
 * to methods with the purpose to target and extract the data that the user wants to see.
 */

public class Celro extends Scrapper {

    public Celro(String url){
        super(url);
    }

    private void makePrice(String text){
        String onlyNumbers="";
        String onlyLetters="";
        for(int i=0;i<text.length();++i){
            char c=text.charAt(i);
            if(c>='0' && c<='9')
                onlyNumbers+=c;
            else
                onlyLetters+=c;
        }
        this.price=text;
        this.valueType=onlyLetters;
        this.prc=Integer.parseInt(onlyNumbers);
    }

    @Override
    public void scrape() {
        try{
            Document doc=Jsoup.connect(url).get();
            Elements e=doc.select("div.row");
            this.productName=e.select("div.col-50:nth-of-type(2)").select("h1.productName").text();

            e=doc.select("div.pret_info");
            String split=e.text().split(" ")[0];
            makePrice(split);

            System.out.println(prc+" "+valueType);

        }catch (Exception err){
            JOptionPane.showMessageDialog(null,"Invalid link","Wrong link",JOptionPane.WARNING_MESSAGE);
        }
    }
}
