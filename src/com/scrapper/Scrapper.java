package com.scrapper;
import org.jsoup.*;

/**
 * Using this class I will be able to extract my data from a link(to a product) from Emag, PcGarage and Cel.ro eventually
 * I want to extract the price and the name of the product, and display it to my customer and retain it in my table who store
 * information about the last transactions.
 *
 * For this purpose, I will use org.jsoup library, which helps me to extract the wanted data;
 * I made it abstract because I think that this is the best fit, because I don't have here an actual store to target.
 *
 * I also made this class the parent of the others via Inheritance, because they all share common attributes and methods.
 */

public abstract class Scrapper {
    final protected String url;
    protected String price;
    protected String productName;
    protected String valueType;
    protected int prc;

    public Scrapper(String url){
        this.url=url;
        this.price=null;
        this.productName=null;
        this.valueType=null;
        this.prc=0;
    }

    public abstract void scrape();

    public String getProductName(){
        return productName;
    }

    public int getPret(){
        return prc;
    }

    public String getMoneda(){
        return valueType;
    }
}
