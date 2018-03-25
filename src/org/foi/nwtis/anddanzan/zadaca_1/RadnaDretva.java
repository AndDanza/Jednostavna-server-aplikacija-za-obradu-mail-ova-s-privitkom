package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        String naredbaAdmin = "\\-\\-(kreni)|\\-\\-(zaustavi)|\\-\\-(pauza)|\\-\\-(stanje)|\\-\\-(evidencija) ((([A-Za-z]:\\\\)?([A-Za-z0-9]+\\\\)?)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})|\\-\\-(iot) ((([A-Za-z]:\\\\)?([A-Za-z0-9]+\\\\)?)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})";
        String naredbaKlijent = "((--spavanje) (600|[1-5]?[0-9]?[0-9]{1}) )?((([A-Za-z]:\\\\)?([A-Za-z0-9]+\\\\)?)?([A-Za-z0-9]+\\.(txt|xml|json|bin|TXT|XML|JSON|BIN)){1})";

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
        if (admin) {
            if (komanda.contains("kreni")) {
                RadnaDretva.pauza = false;
                return RadnaDretva.pauza == false ? "ERROR 12; Server NIJE u stanju pauze!" : "OK;";
            }
            else if (komanda.contains("pauza")) {
                RadnaDretva.pauza = true;
                return RadnaDretva.pauza == false ? "ERROR 11; Server JE u stanju pauze!" : "OK;";
            }
            else if (komanda.contains("stanje")) {
                if (RadnaDretva.pauza) {
                    return "OK;0";
                }
                else {
                    return "OK;1";
                }
            }
            //TODO OK;2 - dobio zaustavi zahtjev, ali još nije ugašen u potpunosti
            else if (komanda.contains("zaustavi")) {
                return "zaustavi";
            }
            //TODO Pročitat misli da saznamo šta radi - forum
            else if (komanda.contains("evidencija")) {
                return deserijalizirajZapisZaSlanje("datoteka.evidencije.rada");
            }
            else {
                return deserijalizirajZapisZaSlanje("datoteka.iot.zapisa");
            }
        }
        else {
            if (komanda.contains("spavanje")) {
                return "spavanje";
            }
        }
        return "";
    }

    private String deserijalizirajZapisZaSlanje(String datoteka) {
        //TODO kod bin datoteke bi moglo sve past, nakoniot kreiranja probat preko objekta ovoodradit
        String deserijaliziranaEvidencija = "";
        String kodZnakova = konf.dajPostavku("skup.kodova.znakova");
        String header = "OK; ZN-KODOVI " + kodZnakova + "; DUZINA ";

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(konf.dajPostavku(datoteka)));
            header += encoded.length + "<CRLF>\n";
            deserijaliziranaEvidencija = new String(encoded, kodZnakova);
            deserijaliziranaEvidencija = header + deserijaliziranaEvidencija.trim();
        } catch (IOException ex) {
            if(datoteka.contains("evidencija"))
                    deserijaliziranaEvidencija = "ERROR 15; Doslo je do greske pri dohvacanju evidencijem rada!";
            else
                deserijaliziranaEvidencija = "ERROR 16; Doslo je do greske pri dohvacanju datoteke IOT uređaja!";
        }

        return deserijaliziranaEvidencija;
    }
}
