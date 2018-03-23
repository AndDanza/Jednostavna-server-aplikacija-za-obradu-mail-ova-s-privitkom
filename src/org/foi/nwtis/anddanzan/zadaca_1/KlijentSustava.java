package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrea
 */
public class KlijentSustava extends KorisnikSustava{
    public void preuzmiKontrolu(){
        try {
            Socket socket = new Socket(this.adresa, this.port); //adresa i port dobiveni iz args[]
                        
            posaljiKomandu(socket, this.komanda);
            
            String odgovor = zaprimiOdgovor(socket);
            System.out.println("Odgovor: " + odgovor);
            
        } catch (IOException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
