package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;
import org.nwtis.anddanzan.konfiguracije.KonfiguracijaApstraktna;
import org.nwtis.anddanzan.konfiguracije.NeispravnaKonfiguracija;
import org.nwtis.anddanzan.konfiguracije.NemaKonfiguracije;

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
            System.out.println("Komanda: "+this.komanda);

            String odgovor = zaprimiOdgovor(socket);
            System.out.println("Odgovor: " + odgovor);

            if (odgovor.contains("OK; ZN-KODOVI")) {
                String regexDat = "((([A-Za-z]:\\\\)?([A-Za-z0-9]+\\\\)*)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})";
                Pattern pattern = Pattern.compile(regexDat);
                Matcher m = pattern.matcher(this.komanda);

                if (m.find()) {
                    String datoteka = m.group(0);

                    regexDat = "ZN-KODOVI ([A-Za-z0-9_-]+)";
                    pattern = Pattern.compile(regexDat);
                    m = pattern.matcher(odgovor);
                    //dohvaćanje i postavljanje charseta
                    if (m.find()) {
                        String kod = m.group(0).split(" ")[1]; //ZN-KODOVI UTF-8 podjeli po razmku, uzmi desni dio i ukloni ;
                        Charset charset = Charset.forName(kod);
                        odgovor = new String(odgovor.getBytes(), charset).replace(";", "");
                        odgovor = odgovor.substring(odgovor.indexOf("\n") + 1);
                    }

                    if (this.komanda.contains("evidencija")) {
                        try {
                            Konfiguracija evidencija = KonfiguracijaApstraktna.kreirajKonfiguraciju(datoteka);
                            Properties prop = new Properties();
                            prop.load(new StringReader(odgovor));
                            evidencija.dodajKonfiguraciju(prop);
                            evidencija.spremiKonfiguraciju();
                        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
                            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }

        }
        catch (IOException ex) {
            System.out.println("ERROR 02; Krivi port ili adresa u naredbi");
        }
    }

}
