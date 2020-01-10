package com.scrapper;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.*;

/**
 * The purpose of this class is to get the price of the product as well as the name of it from the online marketplace Emag.com;
 */

public class Emag extends Scrapper {

    public Emag(String url){
        super(url);
    }

    /**
     * Aceasta metoda imi salveaza pretul in int, rotunjit, deoarece pretul salvat din cadrul paginii este unul specific paginilor web
     * ce se ocupa de marketing(ex. 489.99)
     */
    private void makePrice(){
        StringBuilder copy= new StringBuilder();
        for(int i=0; i<this.price.length();++i) {
            char c=this.price.charAt(i);
            if (c>='0' && c<='9')
                copy.append(c);
        }
        this.prc=Integer.parseInt(copy.toString())+1;
    }

    /**
     * Metoda ce imi extrage din codul html ce incapsuleaza informatia pretul curent al produsului
     * @param value
     */
    private void getPrice(Elements value){
        value=value.select("p.product-new-price");
        String[] splitter=value.text().split(" ");
        int index=splitter[0].length()-2;
        this.valueType=splitter[1];
        this.price=splitter[0].substring(0,index);
        makePrice();
    }

    /**
     * In cadrul acestei metode extrag documentul html din cadrul conexiunii cu pagina emag, dupa care extrag gruparile de date(
     * in functie de div) pentru a extrage informatia precisa din fiecare grup in parte
     */
    @Override
    public void scrape() {
        try {
           final Document document = Jsoup.connect(url).get();
            Elements value=document.select("div.product-page-pricing.product-highlight");
            value=value.select("p.product-new-price");
            getPrice(value);
            value=document.select("div.has-subtitle-info.page-header");
            value=value.select("h1.page-title");

            this.productName=value.text().split(",")[0];
            System.out.println(productName);
            System.out.println(prc+" "+valueType);

        }catch (Exception err){
            JOptionPane.showMessageDialog(null,"Website not available!","Wrong site",JOptionPane.WARNING_MESSAGE);
        }
    }
}
