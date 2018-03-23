package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;
import org.nwtis.anddanzan.konfiguracije.KonfiguracijaApstraktna;
import org.nwtis.anddanzan.konfiguracije.NeispravnaKonfiguracija;
import org.nwtis.anddanzan.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author Andrea
 */
public class ServerSustava {

    public static Evidencija evidencija;
    public static int brojacDretvi = 0;

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
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            return; //server prekida s radmo u slučaju greške
        }
    }

    private void pokreniPosluzitelj(Konfiguracija konf) {
        int port = Integer.parseInt(konf.dajPostavku("port"));
        int maxBrZahtjevaCekanje = Integer.parseInt(konf.dajPostavku("max.broj.zahtjeva.cekanje"));
        int maxBrRadnihDretvi = Integer.parseInt(konf.dajPostavku("max.broj.radnih.dretvi"));
//        int intervalZaSerijalizaciju = Integer.parseInt(konf.dajPostavku("interval.za.serijalizaciju"));
//        String datEvidencije = konf.dajPostavku("datoteka.evidencije.rada");

        boolean radiDok = true;

        //TODO Provjeri i ako postoji učitaj evidenciju rada (koristeći KonfiguracijaApstraktna za učitavanje  provjeru)
        //TODO instancirat klasu Evidencija i učitat podatke iz evidencije u objekt
        //this.evidencija = pripremiEvidenciju(konf)
        //TODO instanciranje objekta za IOT uređaj - potrebno međusobno isključivanje za zapis iz RadneDretve u evidenciju
        
        SerijalizatorEvidencije serijalizatorEvid = new SerijalizatorEvidencije("anddanzan - Serijalizator", konf);
        serijalizatorEvid.start();
        try {
            ServerSocket serverSocket = new ServerSocket(port, maxBrZahtjevaCekanje);

            while (radiDok) {
                Socket socket = serverSocket.accept();

                try {
                    Thread.sleep(60000);    //radi testiranja
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println("Korisnik se spojio!");
                if (brojacDretvi >= 64) 
                    brojacDretvi = 0;

                if (brojacDretvi == maxBrRadnihDretvi) {
                    //TODO Ukoliko nema raspoložive radne dretve korisniku se vraća odgovor ERROR 01
                    //TODO Kreirat metodu za slanje poruke outputStreamom u socket posaljiPovratnuPoruku(socket, idPoruke)
                    //TODO Ažuriraj evidenciju rada
                }
                else {
                    RadnaDretva radnaDretva = new RadnaDretva(socket, "anddanzan-" + brojacDretvi, konf);
                    ServerSustava.brojacDretvi++;
                    radnaDretva.start();
                    //TODO Ažuriraj evidenciju rada
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
