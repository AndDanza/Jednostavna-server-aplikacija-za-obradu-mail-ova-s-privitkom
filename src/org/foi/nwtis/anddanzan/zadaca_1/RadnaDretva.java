package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;

/**
 *
 * @author Andrea
 */
class RadnaDretva extends Thread {

    private Socket socket;
    private String nazivDretve;
    private Konfiguracija konf;

    /**
     *
     * @param socket
     * @param nazivDretve
     * @param konf
     */
    public RadnaDretva(Socket socket, String nazivDretve, Konfiguracija konf) {
        super(nazivDretve);
        this.socket = socket;
        this.nazivDretve = nazivDretve;
        this.konf = konf;
    }

    /**
     *
     */
    @Override
    public void interrupt() {
        super.interrupt();
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            InputStream inputStream = this.socket.getInputStream();
            OutputStream outputStream = this.socket.getOutputStream();

            StringBuffer stringBuffer = new StringBuffer();

            while (true) {
                int znak = inputStream.read();

                if (znak == -1) {
                    break;
                }

                stringBuffer.append((char) znak);
            }

            String komanda = stringBuffer.toString();
            System.out.println("Dretva " + this.nazivDretve + " Komanda: " + komanda);

            Boolean komandaValjana = provjeriKomandu(komanda);   //TODO provjeri ispravnost primljene komande
            //TODO vrati odgovarajući odgovor korisniku
            //TODO Provjeriti dozvoljene komande

            String odgovorServera = "";
            if (komandaValjana) {
                odgovorServera = izvrsiKomanduAdmina(komanda);
                odgovorServera = izvrsiKomanduKlijenta(komanda);
            }
            else {
                odgovorServera = porukaPogreske("01");
            }

            outputStream.write(odgovorServera.getBytes());
            outputStream.flush();   //čisti output stream
            socket.shutdownOutput();

        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }

        //TODO smanji broj aktivnih radnih dretvi kod servera sustava
        ServerSustava.brojacDretvi--;   //TODO zašto ne radi prvi zahtjev   
    }

    /**
     *
     */
    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Metoda za validaciju ulazne komande korisnika
     *
     * @param komanda string komanda zaprimljena kroz socket
     * @return true (komanda je valjana) ili false (komanda nije dobro zadana)
     */
    private Boolean provjeriKomandu(String komanda) {
        Boolean provjeriKomadnu = provjeriKorisnickoIme(komanda);
        if (provjeriKomadnu) {
            provjeriKomadnu = provjeriAdresu(komanda);

            if (provjeriKomadnu) {
                provjeriKomadnu = provjeriPort(komanda);

                if (provjeriKomadnu) {
                    return provjeriNaredbu(komanda);
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Provjera unosa korisničkog imena i lozinke u komandi korištenjem regex-a
     *
     * @param komanda string komanda zaprimljena kroz socket
     * @return true (lozinka i korisničko ime pravilno uneseni) ili false
     * (nepravilno unesena lozinka i korisničko ime)
     */
    private Boolean provjeriKorisnickoIme(String komanda) {
        String usernamePass = "-k ([A-Za-z0-9_-]{3,10}) -l ([A-Za-z0-9_\\-#!]{3,10})";  //-k korisink -l lozinka, vrijednosti u polju na indexma: 1, 2

        Pattern pattern = Pattern.compile(usernamePass);
        Matcher m = pattern.matcher(komanda);

        return m.find();
    }

    /**
     * Provjera unosa ip adrese ili imenom zadane adrese u komandi korištenjem
     * regex-a
     *
     * @param komanda string komanda zaprimljena kroz socket
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
     * @param komanda string komanda zaprimljena kroz socket
     * @return true (port pravilno uneseni) ili false (nepravilno unesen port)
     */
    private Boolean provjeriPort(String komanda) {
        String port = "-p ((?:8|9)?[0-9]{1}[0-9]{1}[0-9]{1}){1}"; //-komanda 8999, , vrijednosti u polju na indexma: 1

        Pattern pattern = Pattern.compile(port);
        Matcher m = pattern.matcher(komanda);

        return m.find();
    }

    /**
     * Provjera unosa naredbe serveru u komandi korištenjem regex-a
     *
     * @param komanda string komanda zaprimljena kroz socket
     * @return true (naredba pravilno uneseni) ili false (nepravilno unesena
     * naredba)
     */
    private Boolean provjeriNaredbu(String komanda) {
        String naredba = "\\-\\-(kreni)|\\-\\-(zaustavi)|\\-\\-(pauza)|\\-\\-(stanje)|\\-\\-(evidencija) ([A-Za-z0-9_-]+\\.{1}[A-Za-z0-9]{1,10})|\\-\\-(iot) ([A-Za-z0-9_-]+\\.{1}[A-Za-z0-9]{1,10})";

        Pattern pattern = Pattern.compile(naredba);
        Matcher m = pattern.matcher(komanda);

        return m.find();
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
