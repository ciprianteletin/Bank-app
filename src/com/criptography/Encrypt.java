package com.criptography;

/**
 * Clasa in cadrul careia se cripteaza parola utilizatorului inainte sa fie stocata in cadrul tabelului passwords din baza de
 * date a aplicatiei. Algoritmul ales este unul unic, venit din inspiratie proprie si combinarea mai multor algoritmi simpli
 * cunoscuti.
 */
public class Encrypt {
    private String message;

    public String launch(String message){
        this.message=message;
        String reverse=this.reverse();
        String concatenate=this.concat(reverse);
        concatenate=this.permute(concatenate);
        return fibbo(concatenate);
    }

    private String reverse(){
        String reverse="";
        for(int i=0;i<this.message.length();i++)
            reverse=this.message.charAt(i)+reverse;
        return reverse;
    }

    private String concat(String reverse){
        String concat="";
        for(int i=0;i<this.message.length();i++)
            concat+=(this.message.charAt(i)+""+reverse.charAt(i));
        return concat;
    }

    private String permute(String concatenate){
        char[] perm=new char[concatenate.length()];
        for(int i=0;i<concatenate.length();i++)
            perm[i]=concatenate.charAt(i);

        for(int i=0;i<perm.length-2;i++){
            char last=perm[perm.length-1];
            for(int j=perm.length-1;j>0;j--)
                perm[j]=perm[j-1];
            perm[0]=last;
        }

        StringBuilder concat = new StringBuilder();
        for(char c:perm)
            concat.append(c);
        concatenate = concat.toString();
        return concatenate;
    }

    private String fibbo(String concat){
        int a=0,b=1,c=1;
        String fin="";
        for(int i=0;i<concat.length();++i){
            if(i%2==0)
                fin+=(char)(concat.charAt(i)+c);
            else
                fin+=(char)(concat.charAt(i)-c);
            a=b;
            b=c;
            c=b+c;
        }
        return fin;
    }
}
