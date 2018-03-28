package org.foi.nwtis.anddanzan.zadaca_1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;
import org.nwtis.anddanzan.konfiguracije.KonfiguracijaApstraktna;
import org.nwtis.anddanzan.konfiguracije.NeispravnaKonfiguracija;
import org.nwtis.anddanzan.konfiguracije.NemaKonfiguracije;

/**
 * Klasa servera
 *
 * @author Andrea
 */
public class ServerSustava {

    /**
     * Objekt evidencije za pohranu pomoću serijalizatora, static kako bi sve
     * dretve mogle pristupat podacima
     */
    public static Evidencija evidencija;
    /**
     * Statična lista objekata za zapis podataka s IOT uređaja
     */
    public static List<IOT> uredajiIOT = new ArrayList<>();
    /**
     * Zastavica koja se postavlja na false kad administrator pozove naredbu
     * zaustavi
     */
    public static Boolean radi = true;
    /**
     * Redni broj dretve
     */
    public int redniBrojDretvi = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Krivi broj argumenata");
            return;
        }

        try {
            Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
            ServerSustava serverSustava = new ServerSustava();
            serverSustava.pokreniPosluzitelj(konf);

        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za pokretanje servera
     *
     * @param konf konfiguracijska datoteka učitana u objekt
     * <code>Konfiguracija</code> (koristi Properties i ime datoteke)
     */
    private synchronized void pokreniPosluzitelj(Konfiguracija konf) throws IOException, InterruptedException {
        int port = Integer.parseInt(konf.dajPostavku("port"));
        int maxBrZahtjevaCekanje = Integer.parseInt(konf.dajPostavku("max.broj.zahtjeva.cekanje"));
        int maxBrRadnihDretvi = Integer.parseInt(konf.dajPostavku("max.broj.radnih.dretvi"));

        pokreniEvidentiranje(konf);

        ServerSocket serverSocket = new ServerSocket(port, maxBrZahtjevaCekanje);

        while (ServerSustava.radi) {
            Socket socket = serverSocket.accept();
            System.out.println("Korisnik se spojio!");

            //Sleep(n) - može i tu, ali onda server svakih  milisekundi prihvaća zahtjev
            //6bitni redni broj dretve
            
            if ((Thread.activeCount()-2) == maxBrRadnihDretvi) {
                //Kreirat metodu za slanje poruke outputStreamom u socket
                System.out.println("Korisnik odspojen - nema dretve");
                ServerSustava.posaljiOdgovor(socket, "ERROR 01; Nema raspolozive radne dretve!");

                //Ažuriraj evidenciju rada
                ServerSustava.azurirajEvidenciju("prekinuti");

            }
            else {
                RadnaDretva radnaDretva = new RadnaDretva(socket, "anddanzan-" + this.redniBrojDretvi, konf);
                this.redniBrojDretvi = this.redniBrojDretvi >= 64 ? this.redniBrojDretvi = 0 : this.redniBrojDretvi+1;
                radnaDretva.start();

                //ukupan broj dretvi
                ServerSustava.azurirajEvidenciju("brojDretvi");
            }

            //inače server ostane radit
            Thread.sleep(1000);
        }

        pokreniEvidentiranje(konf);

        this.notifyAll();

        System.exit(0);
    }

    /**
     * Metoda za slanje komande kroz socket pomoću <code>OutputStream-a</code>
     *
     * @param socket Kreirani socket za korisnike
     * @param poruka string varijabla s odgovorom servera
     */
    public static void posaljiOdgovor(Socket socket, String poruka) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(poruka.getBytes());
            outputStream.flush();
            socket.shutdownOutput();
        } catch (IOException ex) {
            Logger.getLogger(KorisnikSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za primanje zahtjeva klijenta (komande) kroz socket pomoću
     * <code>InputStream-a</code>
     *
     * @param socket Kreirani socket za korisnike
     * @return string zahtjeva korisnika
     */
    public static String zaprimiKomandu(Socket socket) {
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
            Logger.getLogger(KorisnikSustava.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stringBuffer.toString();
    }

    //deserijalizacija IOT-a za slanje i učitavanje
    /**
     * Statična metoda za pohranu podataka iz liste IOTUređaja u datoteku
     */
    public static String serijalizirajIOT(Konfiguracija konf) {
        Gson builder = new GsonBuilder().setPrettyPrinting().create();
        String json = "";
        synchronized (ServerSustava.uredajiIOT) {
            json = builder.toJson(ServerSustava.uredajiIOT);
        }
        //json = jsonData.replace("\\\"", "");
        String kodZnakova = konf.dajPostavku("skup.kodova.znakova");
        String header = "OK; ZN-KODOVI " + kodZnakova + "; DUZINA ";
        header += json.getBytes().length + "<CRLF>\n";
        return header + json + ";";
    }

    /**
     * Statična metoda za ažuriranje prekinutih zahtjeva i broja dretvi
     *
     * @param tip string za određivanje vrste ažuriranja evidencije
     */
    public static synchronized void azurirajEvidenciju(String tip) {
        if (tip.equals("prekinuti")) {
            long prekinutiZahtjevi = ServerSustava.evidencija.getBrojPrekinutihZahtjeva();
            ServerSustava.evidencija.setBrojPrekinutihZahtjeva(++prekinutiZahtjevi);
        }
        else if (tip.equals("brojDretvi")) {
            long brDretvi = ServerSustava.evidencija.getBrojUspjesnihZahtjeva();
            ServerSustava.evidencija.setBrojUspjesnihZahtjeva(++brDretvi);
        }
        else if (tip.equals("nedozvoljeni")) {
            long nedozvoljeniZahtjevi = ServerSustava.evidencija.getBrojNedozvoljenihZahtjeva();
            ServerSustava.evidencija.setBrojNedozvoljenihZahtjeva(++nedozvoljeniZahtjevi);
        }
        else if (tip.equals("neispravni")) {
            long neispravniZahtjevi = ServerSustava.evidencija.getBrojNeispravnihZahtjeva();
            ServerSustava.evidencija.setBrojNeispravnihZahtjeva(++neispravniZahtjevi);
        }
    }

    /**
     * Metoda za pokretanje evidentiranja na serveru (instanciranje objekta i
     * pokretanje serijalizatora)
     *
     * @param konf objekt tipa Konfigutacija s podacima iz konfiguracijske
     * datoteke
     */
    private synchronized void pokreniEvidentiranje(Konfiguracija konf) {
        String datotekaEvidencije = konf.dajPostavku("datoteka.evidencije.rada");

        //Provjeri i ako postoji učitaj evidenciju rada, ako ne inicijaliziraj (koristeći KonfiguracijaApstraktna za učitavanje  provjeru)
        try {
            Konfiguracija evidencijaRada = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datotekaEvidencije);
            ServerSustava.evidencija = new Evidencija(evidencijaRada);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            System.out.println(ex.getMessage());
            ServerSustava.evidencija = new Evidencija();
        }

        //instanciranje objekta za IOT uređaj
        //TODO potrebno međusobno isključivanje za zapis iz RadneDretve u evidenciju
        SerijalizatorEvidencije serijalizatorEvid = new SerijalizatorEvidencije("anddanzan - Serijalizator", konf);
        serijalizatorEvid.start();
    }
}
