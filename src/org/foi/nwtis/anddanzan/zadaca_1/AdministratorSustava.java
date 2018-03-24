package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.net.Socket;

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
            System.out.println("ERROR 02; Krivi port ili adresa u naredbi");
        }
    }
    
}
