package org.foi.nwtis.anddanzan.zadaca_1;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Administrator sustava koji može kontrolirati rad sustava (servera)
 *
 * @author Andrea
 */
public class AdministratorSustava extends KorisnikSustava {

    /**
     * Konstruktor
     */
    public AdministratorSustava() {
        super();
    }

    /**
     * Otvaranje soketa prema serveru te slanje komande i primanje odgovora
     */
    public void preuzmiKontrolu() {
        try {
            Socket socket = new Socket(this.adresa, this.port);
            
            posaljiKomandu(socket, this.komanda);

            String odgovor = zaprimiOdgovor(socket);

            if (odgovor.contains("OK; ZN-KODOVI")) {
                String regexDat = "((([A-Za-z]:\\\\)?([A-Za-z0-9]+\\\\)*)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})";
                Pattern pattern = Pattern.compile(regexDat);
                Matcher m = pattern.matcher(this.komanda);

                if (m.find()) {
                    String datoteka = m.group(0);
                    FileWriter file = new FileWriter(datoteka);
                    file.write(odgovor);
                    file.flush();
                    file.close();
                }
            }
            
            System.out.println("Odgovor: " + odgovor);

        } catch (IOException ex) {
            System.out.println("ERROR 02; Krivi port ili adresa u naredbi");
        }
    }

}
