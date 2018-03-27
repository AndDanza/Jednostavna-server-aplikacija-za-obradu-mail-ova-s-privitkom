package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa klijenta koji ima ograničene komande prema serveru
 *
 * @author Andrea
 */
public class KlijentSustava extends KorisnikSustava {

    /**
     * Otvaranje soketa prema serveru te slanje komande i primanje odgovora
     */
    public void preuzmiKontrolu() {
        try {
            Socket socket = new Socket(this.adresa, this.port); //adresa i port dobiveni iz args[]

            String regexDat = "((([A-Za-z]:\\\\)?([A-Za-z0-9]+\\\\)*)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})";
            Pattern pattern = Pattern.compile(regexDat);
            Matcher m = pattern.matcher(this.komanda);

            if (m.find()) {
                String datoteka = m.group(0);
                this.komanda = this.komanda.replace(" " + datoteka, "");
                String iot = deserijalizirajZapisZaSlanje(datoteka);
                if (iot.contains("OK; ZN-KODOVI")) {
                    iot = iot.split("\n")[1];
                }
                this.komanda = this.komanda + ";" + iot + ";";
            }

            posaljiKomandu(socket, this.komanda);
            System.out.println("Komanda: "+this.komanda);

            String odgovor = zaprimiOdgovor(socket);
            System.out.println("Odgovor: " + odgovor);

        } 
        catch (IOException ex) {
            System.out.println("ERROR 02; Krivi port ili adresa u naredbi");
        }
    }

    /**
     * Učitavanje iot datoteke za slanje
     *
     * @param datoteka datoteka koju je potrebno učitati
     * @return string sadržaja datoteke
     */
    private String deserijalizirajZapisZaSlanje(String datoteka) {
        String datotekaIOT = "";
        try {
            File datIOT = new File(datoteka);
            InputStream is = Files.newInputStream(datIOT.toPath(), StandardOpenOption.READ);
            Scanner s = new Scanner(is).useDelimiter("\\A");
            datotekaIOT = s.hasNext() ? s.next() : "";
            is.close();
        } catch (IOException ex) {
            datotekaIOT = "ERROR 21; Došlo je do greške prilikom dohvaćanja IOT datoteke!";
        }

        return datotekaIOT;
    }
}
