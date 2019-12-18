package com.Utilizator;

import java.util.ArrayList;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//TODO REMINDER CA MAI AI DE LUCRU AICI
public class Client {
    private String username;
    private int age;
    private Card currentCard; //retains the current card;
    private int currentNumberID; //number of card in DB
    private int cardNumber=1; //the number of cards of this account;
    private String job;

    public Client(String username, String date, String job){
        this.username=username;
        this.age=convertDate(date);
        this.job=job;
    }

    public int getAge(){
        return this.age;
    }

    private int convertDate(String date){
        DateTimeFormatter format = DateTimeFormatter
                .ofPattern("dd/MM/yyyy");
        try{
            LocalDate lc=LocalDate.parse(date,format);
            LocalDate timeNow=LocalDate.now();

            return timeNow.getYear()-lc.getYear();
        }catch (DateTimeException exp) {
            //there won't be an exception because I already verify the format in the sign up class;
            return 0;
        }
    }

    public void setCurrentCard(Card current){
        currentCard=current;
    }

    public int getCardNumber(){
        return cardNumber;
    }

    public void setCardNumber(int number){
        cardNumber=number;
    }

    public void setCurrentNumberID(int numberID){currentNumberID=numberID;}

    public String getJob(){return this.job;}

    public String getUsername(){return this.username;}

}
