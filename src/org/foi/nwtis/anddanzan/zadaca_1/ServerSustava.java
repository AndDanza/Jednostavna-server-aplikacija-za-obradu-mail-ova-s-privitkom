package org.foi.nwtis.anddanzan.zadaca_1;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
     * Broj dretvi u stustavu trenutno.
     */
    public static int brojacDretvi = 0;

    /**
     * Statična lista objekata za zapis podataka s IOT uređaja
     */
    public static List<IOT> uredajiIOT = null;

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

            //samo za testiranje
            Random rand = new Random();
            uredajiIOT = new ArrayList<>();
            IOT objektIOT = new IOT(1);
            IOTTemperatura temp = null;
            for (int i = 0; i < 5; i++) {
                int n = rand.nextInt(30) + 1;
                temp = new IOTTemperatura("Pula", n, System.currentTimeMillis() + n);
                objektIOT.dodajMjerenjeUredaja(temp);
            }
            uredajiIOT.add(objektIOT);

            objektIOT = new IOT(2);
            IOTVjetar vjetar = null;
            for (int i = 0; i < 5; i++) {
                int n = rand.nextInt(150) + 1;
                vjetar = new IOTVjetar("Varaždin", n, System.currentTimeMillis() + n);
                objektIOT.dodajMjerenjeUredaja(vjetar);
            }
            uredajiIOT.add(objektIOT);

            objektIOT = new IOT(3);
            IOTVLaga vlaga = null;
            for (int i = 0; i < 5; i++) {
                int n = rand.nextInt(20) + 1;
                vlaga = new IOTVLaga("Požega", n, System.currentTimeMillis() + n);
                objektIOT.dodajMjerenjeUredaja(vlaga);
            }
            uredajiIOT.add(objektIOT);
            //samo za testiranje
            serverSustava.pokreniPosluzitelj(konf);

        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Metoda za pokretanje servera
     *
     * @param konf konfiguracijska datoteka učitana u objekt
     * <code>Konfiguracija</code> (koristi Properties i ime datoteke)
     */
    private void pokreniPosluzitelj(Konfiguracija konf) {
        int port = Integer.parseInt(konf.dajPostavku("port"));
        int maxBrZahtjevaCekanje = Integer.parseInt(konf.dajPostavku("max.broj.zahtjeva.cekanje"));
        int maxBrRadnihDretvi = Integer.parseInt(konf.dajPostavku("max.broj.radnih.dretvi"));
        String datotekaEvidencije = konf.dajPostavku("datoteka.evidencije.rada");

        boolean radiDok = true;

        //instanciranje liste IOT uređaja
        //ServerSustava.uredajiIOT = new ArrayList<>();

        //Provjeri i ako postoji učitaj evidenciju rada, ako ne inicijaliziraj (koristeći KonfiguracijaApstraktna za učitavanje  provjeru)
        try {
            Konfiguracija evidencijaRada = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datotekaEvidencije);
            ServerSustava.evidencija = new Evidencija(evidencijaRada);

        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            System.out.println(ex.getMessage());
            ServerSustava.evidencija = new Evidencija();
        }

        //TODO instanciranje objekta za IOT uređaj - potrebno međusobno isključivanje za zapis iz RadneDretve u evidenciju
        SerijalizatorEvidencije serijalizatorEvid = new SerijalizatorEvidencije("anddanzan - Serijalizator", konf);
        serijalizatorEvid.start();
        try {
            ServerSocket serverSocket = new ServerSocket(port, maxBrZahtjevaCekanje);

            while (radiDok) {
                Socket socket = serverSocket.accept();
                System.out.println("Korisnik se spojio!");

                //Sleep(n) - može i tu, ali onda server svakih  milisekundi prihvaća zahtjev
                //Smanji broj aktivnih radnih dretvi kod servera sustava (-2 jer računa glavnu dretvu, a i brojanje kreće od 0)
                ServerSustava.brojacDretvi = Thread.activeCount() - 2;

                //6bitni redni broj dretve
                if (brojacDretvi >= 64) {
                    brojacDretvi = 0;
                }

                if (brojacDretvi == maxBrRadnihDretvi) {
                    //Kreirat metodu za slanje poruke outputStreamom u socket
                    System.out.println("Korisnik odspojen - nema dretve");
                    ServerSustava.posaljiOdgovor(socket, "ERROR 01; Nema raspolozive radne dretve!");

                    //Ažuriraj evidenciju rada
                    long prekinutiZahtjevi = ServerSustava.evidencija.getBrojPrekinutihZahtjeva();
                    ServerSustava.evidencija.setBrojPrekinutihZahtjeva(++prekinutiZahtjevi);

                }
                else {
                    RadnaDretva radnaDretva = new RadnaDretva(socket, "anddanzan-" + brojacDretvi, konf);
                    radnaDretva.start();

                    //ukupan broj dretvi
                    long brDretvi = ServerSustava.evidencija.getBrojUspjesnihZahtjeva();
                    ServerSustava.evidencija.setBrojUspjesnihZahtjeva(++brDretvi);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     *
     * @param datoteka
     */
    public static String serijalizirajIOT(Konfiguracija konf) {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(ServerSustava.uredajiIOT);
        json = json.replace("\\\"", "");
        String kodZnakova = konf.dajPostavku("skup.kodova.znakova");
        String header = "OK; ZN-KODOVI " + kodZnakova + "; DUZINA ";
        header += json.getBytes().length + "<CRLF>\n";
        return header + json + ";";
    }
}
