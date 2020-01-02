package com.scrapper;
import org.jsoup.*;

/**
 * Using this class I will be able to extract my data from a link(to a product) from Emag, Amazon and EBAY eventually
 * I want to extract the price and the name of the product, and display it to my customer and retain it in my table who store
 * information about the last transactions.
 *
 * For this purpose, I will use org.jsoup library, which helps me to extract the wanted data;
 */

//TODO
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
