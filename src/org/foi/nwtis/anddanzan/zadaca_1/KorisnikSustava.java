package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa koja definira stanja i ponašanje korisnika - Administratora i Klijenta
 *
 * @author Andrea
 */
public class KorisnikSustava {

    String korisnik;
    String lozinka;
    String adresa;
    int port;
    String komanda;

    String[] args;
    boolean administrator = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //provjeri upisane argumente
        KorisnikSustava korisnik = new KorisnikSustava();
        //kroz arg prosljeđuje se komanda, na temelju komande odredi koji je korisnik (admin ili klijent)
        korisnik.preuzmiPostavke(args);
        korisnik.args = args;

        if (korisnik.administrator) {
            AdministratorSustava admin = new AdministratorSustava();
            admin.adresa = korisnik.adresa;
            admin.port = korisnik.port;
            admin.komanda = korisnik.komanda;
            admin.preuzmiKontrolu();    //stvaranje socketa i povezivanje sa serverom
        }
        else {
            KlijentSustava klijent = new KlijentSustava();
            klijent.adresa = korisnik.adresa;
            klijent.port = korisnik.port;
            klijent.komanda = korisnik.komanda;
            klijent.preuzmiKontrolu();
        }
    }

    /**
     * Određivanje vrste korisnika te učitavanje ulaznih parametara (komande)
     *
     * @param args the command line arguments
     */
    private void preuzmiPostavke(String[] args) {
        String usernamePass = "-k ([A-Za-z0-9_-]{3,10}) -l ([A-Za-z0-9_\\-#!]{3,10})";  //-k korisink -l lozinka, vrijednosti u polju na indexma: 1, 2

        this.komanda = "";
        for (String arg : args) {
            this.komanda += arg + " ";
        }

        Pattern pattern = Pattern.compile(usernamePass);
        Matcher m = pattern.matcher(this.komanda);

        if (m.find()) {
            this.korisnik = args[1].trim();
            this.lozinka = args[3].trim();
            this.port = Integer.valueOf(args[7]);
            this.adresa = args[5].trim();

            this.administrator = true;
        }
        else {
            this.port = Integer.valueOf(args[3]);
            this.adresa = args[1].trim();
        }
    }

    /**
     * Metoda za slanje komande kroz socket pomoću <code>OutputStream-a</code>
     *
     * @param socket Kreirani socket korisnika prema serveru
     * @param komanda string varijabla s komandom za server
     */
    protected void posaljiKomandu(Socket socket, String komanda) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(this.komanda.getBytes());
            outputStream.flush();
            socket.shutdownOutput();
        } catch (IOException ex) {
            System.out.println("ERROR; Server ne odgovara");
        }
    }

    /**
     * Metoda za primanje odgovora servera kroz socket pomoću
     * <code>InputStream-a</code>
     *
     * @param socket Kreirani socket korisnika prema serveru
     * @return string odgovora servera
     */
    protected String zaprimiOdgovor(Socket socket){
        InputStream inputStream = null;
        StringBuffer stringBuffer = null;
        try {
            inputStream = socket.getInputStream();
            stringBuffer = new StringBuffer();
            while (true) {
                int znak = inputStream.read();

                if (znak == -1) {
                    break;
                }

                stringBuffer.append((char) znak);
            }
            socket.shutdownInput();
        } catch (IOException ex) {
            System.out.println("ERROR; Server ne odgovara");
        }

        return stringBuffer.toString();
    }
}
