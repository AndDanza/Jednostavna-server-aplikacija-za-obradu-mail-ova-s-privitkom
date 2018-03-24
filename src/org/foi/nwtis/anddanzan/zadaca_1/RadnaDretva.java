package org.foi.nwtis.anddanzan.zadaca_1;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;

/**
 * Dretva koja zaprima iobrađuje zahtjeve korisnika
 *
 * @author Andrea
 */
class RadnaDretva extends Thread {

    private Socket socket;
    private String nazivDretve;
    private Konfiguracija konf;

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
     * Thread metoda
     */
    @Override
    public void run() {
        try {
            Thread.sleep(30000);    //radi testiranja
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }

        String zahtjev = ServerSustava.zaprimiKomandu(socket);
        System.out.println("Dretva " + this.nazivDretve + " Komanda: " + zahtjev);

        //provjeri ispravnost primljene komande
        String komandaValjana = provjeriKomandu(zahtjev);

        //vrati odgovarajući odgovor korisniku
        if (!komandaValjana.equals("OK")) {
            ServerSustava.posaljiOdgovor(socket, "ERROR 02; " + komandaValjana);
        }
        else {
            ServerSustava.posaljiOdgovor(socket, komandaValjana + ";");   //samo za potrebe testiranja
        }

        //TODO Provjeriti dozvoljene komande
//      String odgovorServera = "";
//      odgovorServera = izvrsiKomanduAdmina(komanda);
//      odgovorServera = izvrsiKomanduKlijenta(komanda);
        long stop = System.currentTimeMillis();
        long vrijemeIzvrsavanja = stop - this.start;
        long ukupnoVrijemeDretvi = ServerSustava.evidencija.getUkupnoVrijemeRadaRadnihDretvi();
        ServerSustava.evidencija.setUkupnoVrijemeRadaRadnihDretvi(ukupnoVrijemeDretvi + vrijemeIzvrsavanja);
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
        Boolean provjeriUsera = provjeriKorisnickoIme(komanda);
        Boolean provjeriAdresu = provjeriAdresu(komanda);
        Boolean provjeriPort = provjeriPort(komanda);
        String provjeriKomadnu = provjeriNaredbu(komanda);

        if (provjeriKomadnu != null) {
            if (provjeriUsera && provjeriAdresu && provjeriPort) {
                return "OK";
            }
            else if (provjeriAdresu && provjeriPort) {
                return "OK";
            }
            else {
                return "Sintaksa naredbe neispravna ili komanda nije dozvoljena!";
            }
        }
        else {
            return "Sintaksa naredbe neispravna ili komanda nije dozvoljena!";
        }
    }

    /**
     * Provjera unosa korisničkog imena i lozinke u komandi korištenjem regex-a
     *
     * @param komanda string zahtjev zaprimljena kroz socket
     * @return true (lozinka i korisničko ime pravilno uneseni) ili false
     * (nepravilno unesena lozinka i korisničko ime)
     */
    private Boolean provjeriKorisnickoIme(String komanda) {
        String usernamePass = "-k ([A-Za-z0-9_-]{3,10}) -l ([A-Za-z0-9_\\-#!]{3,10})";  //-k korisink -l lozinka, vrijednosti u polju na indexma: 1, 2
        Boolean admin = false;

        Pattern pattern = Pattern.compile(usernamePass);
        Matcher m = pattern.matcher(komanda);

        if (m.find()) {
            String korisnik = m.group(1).trim();
            String loznika = m.group(2).trim();

            for (int i = 0; i < 10; i++) {
                String konfLozinka = konf.dajPostavku("admin." + i + "." + korisnik);
                if (konfLozinka != null) {
                    if (konfLozinka.equals(loznika)) {
                        return true;
                    }
                }
            }
        }
        return admin;

    }

    /**
     * Provjera unosa ip adrese ili imenom zadane adrese u komandi korištenjem
     * regex-a
     *
     * @param komanda string zahtjev zaprimljena kroz socket
     * @return true (ip adresa ili adresa pravilno uneseni) ili false
     * (nepravilno unesena ip adresa ili adresa)
     */
    private Boolean provjeriAdresu(String komanda) {
        String ipAdresa = "-s (\\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\\b)"; //127.0.0.1, vrijednosti u polju na indexma: 1
        String adresa = "-s ((?:[A-Za-z0-9]+(?:\\.[A-Za-z0-9])?)+)"; //anddanzan.foi.hr, , vrijednosti u polju na indexma: 1

        Pattern pattern = Pattern.compile(ipAdresa);
        Matcher m = pattern.matcher(komanda);
        Boolean ip = m.find();

        pattern = Pattern.compile(adresa);
        m = pattern.matcher(komanda);
        Boolean adr = m.find();

        if (ip || adr) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Provjera unosa porta zadanog u komandi korištenjem regex-a
     *
     * @param komanda string zahtjev zaprimljena kroz socket
     * @return true (port pravilno uneseni) ili false (nepravilno unesen port)
     */
    private Boolean provjeriPort(String komanda) {
        String port = "-p ((?:8|9){1}[0-9]{1}[0-9]{1}[0-9]{1}){1}"; //-zahtjev 8999, , vrijednosti u polju na indexma: 1

        Pattern pattern = Pattern.compile(port);
        Matcher m = pattern.matcher(komanda);

        return m.find();
    }

    /**
     * Provjera unosa naredbe serveru u komandi korištenjem regex-a
     *
     * @param komanda string zahtjev zaprimljena kroz socket
     * @return true (naredba pravilno uneseni) ili false (nepravilno unesena
     * naredba)
     */
    private String provjeriNaredbu(String komanda) {
        String naredbaAdmin = "\\-\\-(kreni)|\\-\\-(zaustavi)|\\-\\-(pauza)|\\-\\-(stanje)|\\-\\-(evidencija) ([A-Za-z0-9_-]+\\.{1}[A-Za-z0-9]{1,10})|\\-\\-(iot) ([A-Za-z0-9_-]+\\.{1}[A-Za-z0-9]{1,10})";
        String naredbaKlijent = "(?:(--spavanje) (600|[1-5]?[0-9]?[0-9]{1}) )?([A-Za-z0-9_-]+\\.{1}(i?)(txt|xml|json|bin))";

        Pattern pattern = Pattern.compile(naredbaAdmin);
        Matcher m = pattern.matcher(komanda);
        Boolean admin = m.find();

        pattern = Pattern.compile(naredbaKlijent);
        m = pattern.matcher(komanda);
        Boolean klijent = m.find();

        if (admin) {
            return "admin";
        }
        else if (klijent) {
            return "klijent";
        }
        else {
            return null;
        }
    }

    private String izvrsiKomanduAdmina(String komanda) {
        //TODO case-evi za komande i poziv metoda koje odrađuju

        /*
        potvrdan odgovor
        ----------------
        OK
        Ako je u redu i ako server nije u stanju pauze, korisniku se vraća odgovor OK; 0.
        Ako je u redu i ako server je u stanju pauze, korisniku se vraća odgovor OK; 1.
        Ako je u redu i ako je server ranije dobio komandu za zaustavljenje a još nije zatvorio prijem zahtjeva, korisniku se vraća odgovor OK; 2.
        evidencija i iot - Ako je u redu korisniku se vraća odgovor OK; ZN-KODOVI kod; DUZINA n<CRLF> i zatim vraća deserijalizirane podatke o evidenciji rada u formatiranom obliku u zadanom skupu kodova znakova iz postavki.
        
        
        pauza - primi naredbe samo admina
        kreni - prijem svih komandi
        zaustavi - ugasi server
        stanje - vrati poruku stanja servera
        evidencija - vrati evidenciju
        iot - vrati iot datoteku
        
        bool varijabla za stanje pauza = false;
         */
        return "";
    }

    private String porukaPogreske(String kodGreske) {
        //TODO case-evi za poruke greške dobivene u pozivu
        /*
        negativan odgovor
        -------------------
        Ako je u stanju pauze vraća se odgovor ERROR 11
        korisnik nije administrator ili lozinka ne odgovara, vraća se odgovor ERROR 10
        Ako nije u stanju pauze vraća se odgovor ERROR 12
        Ako nešto nije u redu s prekidom rada ili serijalizacijom vraća se odgovor ERROR 13
        Ako nešto nije u redu s evidencijom rada vraća se odgovor ERROR 15 - evidencija
        ko nešto nije u redu s evidencijom rada vraća se odgovor ERROR 16 - iot
        došlo do problema tijekom rada vraća mu se odgovor ERROR 21 - klijent
        Ako je neispravan json format vraća odgovor ERROR 20 - iot klijent
        Ako nije uspjela odraditi čekanje vraća mu se odgovor ERROR 22 - klijent
         */
        return "";
    }

    private String izvrsiKomanduKlijenta(String komanda) {
        //TODO case-evi za komande i poziv metoda koje odrađuju

        /*
        potvrdan odgovor
        ----------------
        OK
        Ako je sve u redu i dodan je novi IOT, vraća mu se odgovor OK 20; 
        Ako je sve u redu i ažuriran je postojeći IOT, vraća mu se odgovor OK 21;
        
        datoteka - upload iot datoteke
        spavanje - spavanje dretve n milisekundi
         */
        return "";
    }
}
