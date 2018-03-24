package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.net.Socket;

/**
 * Klasa klijenta koji ima ograničene komande prema serveru
 * @author Andrea
 */
public class KlijentSustava extends KorisnikSustava{

    /**
     * Otvaranje soketa prema serveru te slanje komande i primanje odgovora
     */
    public void preuzmiKontrolu(){
        try {
            Socket socket = new Socket(this.adresa, this.port); //adresa i port dobiveni iz args[]
                        
            posaljiKomandu(socket, this.komanda);
            
            String odgovor = zaprimiOdgovor(socket);
            System.out.println("Odgovor: " + odgovor);
            
        } catch (IOException ex) {
            System.out.println("ERROR 02; Krivi port ili adresa u naredbi");
        }
    }
}
