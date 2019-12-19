import com.criptography.Decrypt;
import com.criptography.Encrypt;
import com.login.LoginInterface;
import javax.swing.*;
/**
 *  Punctul de start al aplicatiei bancare.
 *  De aici vor porni toate procesele relatate de aplicatia principala
 *  //TODO operatiuni pe care le poate implementa;
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
               new LoginInterface().start();
            }
        });
    }
}
