import com.cardGenerator.Currency;
import com.login.LoginInterface;
import com.scrapper.CursValutar;

import javax.swing.*;

/**
 *  Punctul de start al aplicatiei bancare.
 *  De aici vor porni toate procesele relatate de aplicatia principala
 *
 * @author cipri
 * @version 1.0;
**/
public class Launch {
    public static void main(String[] args){
       SwingUtilities.invokeLater(new Runnable() {
            /**
             * Start a new thread for the swing app;
             */
       @Override
            public void run() {
           CursValutar curs=new CursValutar();
           Currency.EURO.setCurrency(curs.getEuro());
           Currency.DOLAR.setCurrency(curs.getDolar());
           Currency.LIRA.setCurrency(curs.getLira());
           new LoginInterface().start();
       }
        });
    }
}
