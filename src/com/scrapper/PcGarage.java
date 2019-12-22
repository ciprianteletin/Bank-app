package com.scrapper;

import javax.swing.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

/**
 * The purpose of this class is to get the price of the product as well as the name of it from the online marketplace PCGarage.com;
 */

public class PcGarage extends Scrapper {
    private void makePrice(String e){
        String price=e;
        String[] splitter=price.split(" ");
        this.valueType=splitter[1];
        this.price=splitter[0];

        price=price.substring(0,price.indexOf(','));
        StringBuilder copy= new StringBuilder();
        for(int i=0;i<price.length();++i){
            char c=price.charAt(i);
            if(c>='0' && c<='9')
                copy.append(c);
        }
        this.prc=Integer.parseInt(copy.toString())+1;
    }

    private void makeByRateAndDiscount(Document doc){
        Elements html=doc.select("div.ps-top-taller.ps-top");
        Elements e=html.select("p.ps-sell-price").select("span:nth-of-type(2)");
        makePrice(e.text());

        e=doc.select("div.cc-nobg");
        e=e.select("h1.p-name");
        this.productName=e.text().split(",")[0];
    }

    public PcGarage(String url){
        super(url);
    }

    @Override
    public void scrape() {
        try{
            Document doc=Jsoup.connect(url).get();
            makeByRateAndDiscount(doc);
            System.out.println(productName+" "+price);

        }catch (Exception err){
            JOptionPane.showMessageDialog(null,"Incorrect link","Wrong link",JOptionPane.WARNING_MESSAGE);
        }
    }
}
