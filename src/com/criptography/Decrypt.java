package com.criptography;

public class Decrypt {
    private String message;

    public String launch(String msg){
        this.message=msg;
        fibbo();
        permute();
        split();
        return this.message;
    }

    private void split(){
        StringBuilder split=new StringBuilder();
        for(int i=0;i<this.message.length();i=i+2)
            split.append(this.message.charAt(i));
        this.message=split.toString();
    }

    private void permute(){
        char[] perm=new char[message.length()];
        for(int i=0;i<perm.length;i++)
            perm[i]=this.message.charAt(i);

        for(int i=0;i<perm.length-2;++i){
            char first=perm[0];
            for(int j=0;j<perm.length-1;++j)
                perm[j]=perm[j+1];
            perm[perm.length-1]=first;
        }

        StringBuilder that=new StringBuilder();
        for(char c:perm)
            that.append(c);
        this.message=that.toString();
    }


    private void fibbo(){
        int a=0,b=1,c=1;
        String fin="";
        for(int i=0;i<message.length();++i){
            if(i%2==0)
                fin+=(char)(message.charAt(i)-c);
            else
                fin+=(char)(message.charAt(i)+c);
            a=b;
            b=c;
            c=b+c;
        }
        this.message=fin;
    }
}
