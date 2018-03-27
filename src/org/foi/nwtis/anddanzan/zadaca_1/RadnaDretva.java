package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;
import org.nwtis.anddanzan.konfiguracije.KonfiguracijaApstraktna;
import org.nwtis.anddanzan.konfiguracije.NeispravnaKonfiguracija;
import org.nwtis.anddanzan.konfiguracije.NemaKonfiguracije;

/**
 * Dretva koja zaprima iobrađuje zahtjeve korisnika
 *
 * @author Andrea
 */
class RadnaDretva extends Thread {

    private Socket socket;
    private String nazivDretve;
    private Konfiguracija konf;
    /**
     * Varijabla na razini servera prema kojoj je određeno nalazi li se sustav u
     * stanju pauze ili ne
     */
    public static Boolean pauza = false;

    private long start;

    /**
     * Konstruktor
     *
     * @param socket Socket preko kojeg komuniciraju server i korisnik
     * @param nazivDretve ime dretve "anddanzan-i", (i broj od 0 do 63)
     * @param konf podac učitani iz konfiguracije
     */
    public RadnaDretva(Socket socket, String nazivDretve, Konfiguracija konf) {
        super(nazivDretve);
        this.socket = socket;
        this.nazivDretve = nazivDretve;
        this.konf = konf;
    }

    /**
     * Thread metoda
     */
    @Override
    public void interrupt() {
        super.interrupt();
    }

    /**
     * Metoda za obradu korisničkih podataka i izvršavanje naredbi
     */
    @Override
    public void run() {
//        try {
//            Thread.sleep(30000);    //radi testiranja
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
//        }

        String zahtjev = ServerSustava.zaprimiKomandu(socket);
        System.out.println("Dretva " + this.nazivDretve + " Komanda: " + zahtjev);

        //provjeri ispravnost primljene komande
        //Provjeriti dozvoljene komande 
        String komandaValjana = provjeriKomandu(zahtjev);
        String odgovorServera = "";

        synchronized (RadnaDretva.pauza) {
            //ni admin ni klijent = ERROR ...
            if (!komandaValjana.contains("admin") && !komandaValjana.contains("klijent")) {
                odgovorServera = komandaValjana;
            }
            else if (komandaValjana.contains("admin")) {
                odgovorServera = izvrsiKomandu(komandaValjana.split(";")[1].trim(), true);
            }
            else if (komandaValjana.contains("klijent") && !RadnaDretva.pauza) {
                odgovorServera = izvrsiKomandu(komandaValjana.split(";")[1].trim(), false);
            }
            else {
                odgovorServera = "ERROR 11; Server JE u stanju pauze!";
            }
        }

        long stop = System.currentTimeMillis();
        long vrijemeIzvrsavanja = stop - this.start;
        synchronized (ServerSustava.evidencija) {
            long ukupnoVrijemeDretvi = ServerSustava.evidencija.getUkupnoVrijemeRadaRadnihDretvi();
            ServerSustava.evidencija.setUkupnoVrijemeRadaRadnihDretvi(ukupnoVrijemeDretvi + vrijemeIzvrsavanja);
        }

        //vrati odgovarajući odgovor korisniku
        ServerSustava.posaljiOdgovor(socket, odgovorServera);
    }

    /**
     * Thread metoda
     */
    @Override
    public synchronized void start() {
        start = System.currentTimeMillis();
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Metoda za validaciju ulazne komande korisnika
     *
     * @param komanda string zahtjev zaprimljena kroz socket
     * @return true (zahtjev je valjana) ili false (zahtjev nije dobro zadana)
     */
    private String provjeriKomandu(String komanda) {
        String provjeriUsera = provjeriKorisnickoIme(komanda);
        String provjeriKomadnu = provjeriNaredbu(komanda);
        String poruka = null;

        //ispravna komanda?
        if (provjeriKomadnu != null) {
            //admin ili ne?
            if (provjeriUsera.equals("OK;")) {
                //je li zahtjev u komandi adminov ili ne?   admin;spavanje - ne može proći
                if (!provjeriKomadnu.contains("admin")) {
                    ServerSustava.azurirajEvidenciju("nedozvoljeni");
                    poruka = "ERROR 10; Klijentska komanda - nemate ovlasti!!";
                }
                else {
                    poruka = provjeriKomadnu;
                }
            }
            else {
                //zahtjev ne odgovara adminu, ali ni klijentu (--spavanje korišteno kod adminove naredbe)
                if (!provjeriKomadnu.contains("klijent")) {
                    ServerSustava.azurirajEvidenciju("nedozvoljeni");
                    poruka = "ERROR 10; Administratorska komanda - nemate ovlasti!";
                }
                else {
                    poruka = provjeriKomadnu;
                }
            }
        }
        else {
            ServerSustava.azurirajEvidenciju("neispravni");
            poruka = "ERROR 02; Sintaksa naredbe neispravna!";
        }

        return poruka;
    }

    /**
     * Provjera unosa korisničkog imena i lozinke u komandi korištenjem regex-a
     *
     * @param komanda string zahtjev zaprimljena kroz socket
     * @return true (lozinka i korisničko ime pravilno uneseni) ili false
     * (nepravilno unesena lozinka i korisničko ime)
     */
    private String provjeriKorisnickoIme(String komanda) {
        String usernamePass = "-k ([A-Za-z0-9_-]{3,10}) -l ([A-Za-z0-9_\\-#!]{3,10})";  //-k korisink -l lozinka, vrijednosti u polju na indexma: 1, 2
        Pattern pattern = Pattern.compile(usernamePass);
        Matcher m = pattern.matcher(komanda);

        if (m.find()) {
            String korisnik = m.group(1).trim();
            String loznika = m.group(2).trim();

            for (int i = 0; i < 10; i++) {
                String konfLozinka = konf.dajPostavku("admin." + i + "." + korisnik);
                if (konfLozinka != null) {
                    if (konfLozinka.equals(loznika)) {
                        return "OK;";
                    }
                }
            }
            return "ERROR 10; Korisnik nije administrator ili lozinka ne odgovara";
        }
        return "ERROR 02; Sintaksa nije ispravna ili komanda nije dozvoljena";

    }

    /**
     * Provjera unosa naredbe serveru u komandi korištenjem regex-a
     *
     * @param komanda string zahtjev zaprimljena kroz socket
     * @return true (naredba pravilno uneseni) ili false (nepravilno unesena
     * naredba)
     */
    private String provjeriNaredbu(String komanda) {
        String naredbaAdmin = "\\-\\-(kreni)|\\-\\-(zaustavi)|\\-\\-(pauza)|\\-\\-(stanje)|\\-\\-(evidencija) ((([A-Za-z]:\\\\)?([A-Za-z0-9]+\\\\)*)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})|\\-\\-(iot) ((([A-Za-z]:\\\\)?([A-Za-z0-9]+\\\\)*)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})";
        String naredbaKlijent = "(--spavanje) (600|[1-5]?[0-9]?[0-9]{1})|\\{";

        Pattern pattern = Pattern.compile(naredbaAdmin);
        Matcher m = pattern.matcher(komanda);
        if (m.find()) {
            String replacedString = m.group(0).replace("--", "");
            return "admin;" + replacedString;   //admin;pauza - izgled povratne poruke
        }

        pattern = Pattern.compile(naredbaKlijent);
        m = pattern.matcher(komanda);
        if (m.find()) {
            String replacedString = m.group(0).replace("--", "");
            if (replacedString.contains("spavanje")) {
                return "klijent;" + replacedString; //klijent;spavanje
            }
            else {
                return "klijent;" + komanda.substring(komanda.indexOf(";") + 1, komanda.length() - 1);    //dan je sadržaj iot dat (nema naredbe)
            }
        }

        return null;    //nije pravilno zadana
    }

    /**
     * Metoda za kontrolu i pozivanje izvođenja naredbi
     *
     * @param komanda komanda admina ili klijenta
     * @param admin <code>Boolean</code> varijabla koja definira je li komanda
     * adminova ili klijentova
     * @return
     */
    private String izvrsiKomandu(String komanda, Boolean admin) {
        if (admin) {
            if (komanda.contains("kreni")) {
                return promjenaStanjaServera("kreni");
            }
            else if (komanda.contains("pauza")) {
                return promjenaStanjaServera("pauza");
            }
            else if (komanda.contains("stanje")) {
                return promjenaStanjaServera("stanje");
            }
            //TODO OK;2 - dobio zaustavi zahtjev, ali još nije ugašen u potpunosti
            else if (komanda.contains("zaustavi")) {
                return promjenaStanjaServera("zaustavi");
            }
            //TODO Pročitat misli da saznamo šta radi - forum
            else if (komanda.contains("evidencija")) {
                return deserijalizirajZapisZaSlanje("datoteka.evidencije.rada");
            }
            else {
                return ServerSustava.serijalizirajIOT(this.konf);
            }
        }
        else {
            if (komanda.contains("spavanje")) {
                return spavaj(komanda);
            }
            else {
                return ServerSustava.popuniListuUredaja(komanda);
            }
        }
    }

    /**
     * Metoda za učitavanje datoteke, dodavanje propisanog headera (zaglavlja)
     * ili poruke pogreške
     *
     * @param datoteka datoteka koju je potrebno učitati (iot/evidencija)
     * @return string vrijednost datoteke + zaglavlje
     */
    private String deserijalizirajZapisZaSlanje(String datoteka) {
        String deserijaliziranaEvidencija = "";
        String kodZnakova = konf.dajPostavku("skup.kodova.znakova");
        String header = "OK; ZN-KODOVI " + kodZnakova + "; DUZINA ";

        Konfiguracija evidencija;
        try {
            evidencija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(konf.dajPostavku(datoteka));
            Properties prop = evidencija.dajSvePostavke();

            Set<Entry<Object, Object>> entries = prop.entrySet();
            for (Entry<Object, Object> entry : entries) {
                deserijaliziranaEvidencija += entry.getKey() + " = " + entry.getValue() + "\n";
            }
            header += deserijaliziranaEvidencija.getBytes().length + "<CRLF>\n";
            deserijaliziranaEvidencija = header + deserijaliziranaEvidencija.trim() + ";";
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deserijaliziranaEvidencija;
    }

    /**
     * Metoda za pokretanje spavanja zadanog kod klijenta
     *
     * @param komanda komanda s naredbom za spavanje
     * @return poruka (greška ili OK)
     */
    private String spavaj(String komanda) {
        String regex = "(600|[1-5]?[0-9]?[0-9]{1})";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(komanda);
        if (m.find()) {
            int spavaj = Integer.valueOf(m.group(0));
            try {
                Thread.sleep(spavaj * 1000);  //secu milisek
            } catch (InterruptedException ex) {
                return "ERROR 22; Spavanje dretve nije uspjelo";
            }
        }
        return "OK;";
    }

    /**
     * Metoda za postavljanje zastavice pauza i njenu kontrolu
     *
     * @param stanje string naziv naredbe (kreni, stanje, pauza)
     * @return poruka greške ili OK
     */
    private synchronized String promjenaStanjaServera(String stanje) {
        String odgovor = "";

        if (stanje.equals("kreni")) {
            odgovor = RadnaDretva.pauza == false ? "ERROR 12; Server NIJE u stanju pauze!" : "OK;";
            RadnaDretva.pauza = false;
        }
        else if (stanje.equals("pauza")) {
            odgovor = RadnaDretva.pauza == false ? "OK;" : "ERROR 11; Server JE u stanju pauze!";
            RadnaDretva.pauza = true;
        }
        else if (stanje.equals("stanje")) {
            if (!ServerSustava.radi) {
                return "OK;2";
            }
            if (RadnaDretva.pauza) {
                odgovor = "OK;0";
            }
            else {
                odgovor = "OK;1";
            }
        }
        else {
            ServerSustava.radi = false;
            odgovor = "OK;";
        }

        return odgovor;
    }
}
