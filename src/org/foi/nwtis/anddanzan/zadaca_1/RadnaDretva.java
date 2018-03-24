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
        String odgovorServera = "";

        //vrati odgovarajući odgovor korisniku
        if (!komandaValjana.contains("admin") && !komandaValjana.contains("klijent")) {
            odgovorServera = komandaValjana;
        }
        else if (komandaValjana.contains("admin")) {
            //Provjeriti dozvoljene komande 
            odgovorServera = izvrsiKomandu(komandaValjana.split(";")[1].trim(), true);
        }
        else if (komandaValjana.contains("klijent") && !RadnaDretva.pauza) {
            odgovorServera = izvrsiKomandu(komandaValjana.split(";")[1].trim(), false);
        }
        else {
            odgovorServera = "ERROR 11; Server JE u stanju pauze!";
        }

        long stop = System.currentTimeMillis();
        long vrijemeIzvrsavanja = stop - this.start;
        long ukupnoVrijemeDretvi = ServerSustava.evidencija.getUkupnoVrijemeRadaRadnihDretvi();
        ServerSustava.evidencija.setUkupnoVrijemeRadaRadnihDretvi(ukupnoVrijemeDretvi + vrijemeIzvrsavanja);

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

        //ispravna komanda
        if (provjeriKomadnu != null) {
            //admin ili ne
            if (provjeriUsera.equals("OK;")) {
                //je li zahtjev u komandi adminov ili ne
                if (!provjeriKomadnu.contains("admin")) {
                    long nedozvoljeniZahtjevi = ServerSustava.evidencija.getBrojNedozvoljenihZahtjeva();
                    ServerSustava.evidencija.setBrojNedozvoljenihZahtjeva(nedozvoljeniZahtjevi);
                    poruka = "ERROR 10; Klijentska komanda - nemate ovlasti!!";
                }
                else {
                    poruka = provjeriKomadnu;
                }
            }
            else {
                //je li zahtjev u komandi klijentov ili ne
                if (!provjeriKomadnu.contains("klijent")) {
                    long nedozvoljeniZahtjevi = ServerSustava.evidencija.getBrojNedozvoljenihZahtjeva();
                    ServerSustava.evidencija.setBrojNedozvoljenihZahtjeva(nedozvoljeniZahtjevi);
                    poruka = "ERROR 10; Administratorska komanda - nemate ovlasti!";
                }
                else {
                    poruka = provjeriKomadnu;
                }
            }
        }
        else {
            long nedozvoljeniZahtjevi = ServerSustava.evidencija.getBrojNeispravnihZahtjeva();
            ServerSustava.evidencija.setBrojNeispravnihZahtjeva(nedozvoljeniZahtjevi);
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
        String naredbaAdmin = "\\-\\-(kreni)|\\-\\-(zaustavi)|\\-\\-(pauza)|\\-\\-(stanje)|\\-\\-(evidencija) ((([A-Za-z]:)?(\\\\)?([A-Za-z0-9]+\\\\)?)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})|\\-\\-(iot) ((([A-Za-z]:)?(\\\\)?([A-Za-z0-9]+\\\\)?)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})";
        String naredbaKlijent = "((--spavanje) (600|[1-5]?[0-9]?[0-9]{1}) )?((([A-Za-z]:)?(\\\\)?([A-Za-z0-9]+\\\\)?)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})";

        Pattern pattern = Pattern.compile(naredbaAdmin);
        Matcher m = pattern.matcher(komanda);
        if (m.find()) {
            String replacedString = m.group(0).replace("--", "");
            return "admin;" + replacedString;
        }

        pattern = Pattern.compile(naredbaKlijent);
        m = pattern.matcher(komanda);
        if (m.find()) {
            String replacedString = m.group(0).replace("--", "");
            return "klijent;" + replacedString;
        }

        return null;
    }

    private String izvrsiKomandu(String komanda, Boolean admin) {
        if(admin)
            if(komanda.contains("kreni")){
                if(!RadnaDretva.pauza)
                    return "ERROR 12; Server NIJE u stanju pauze!";
                else{
                    RadnaDretva.pauza = false;
                    return "OK;";
                }
            }
            else if(komanda.contains("zaustavi"))
                return "zaustavi";
            else if(komanda.contains("pauza")){
                if(RadnaDretva.pauza)
                    return "ERROR 11; Server JE u stanju pauze!";
                else{
                    RadnaDretva.pauza = true;
                    return "OK;";
                }
            }  
            else if(komanda.contains("evidencija"))
                return "evidencija";
            else if(komanda.contains("stanje"))
                return "stanje";
            else
                return "iot";
        else{
            if(komanda.contains("spavanje"))
                return "spavanje";
        }
        return "";
    }

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
}
