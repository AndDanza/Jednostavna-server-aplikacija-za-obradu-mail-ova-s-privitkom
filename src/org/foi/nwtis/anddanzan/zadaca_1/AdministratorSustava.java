package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Administrator sustava koji može kontrolirati rad sustava (servera)
 * @author Andrea
 */
public class AdministratorSustava extends KorisnikSustava{
    
    /**
     * Konstruktor
     */
    public AdministratorSustava() {
        super();
    }
    
    /**
     * Otvaranje soketa prema serveru te slanje komande i primanje odgovora
     */
    public void preuzmiKontrolu(){
        try {
            Socket socket = new Socket(this.adresa, this.port);
            
            posaljiKomandu(socket, this.komanda);

            String odgovor = zaprimiOdgovor(socket);
            System.out.println("Odgovor: " + odgovor);
        } catch (IOException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
