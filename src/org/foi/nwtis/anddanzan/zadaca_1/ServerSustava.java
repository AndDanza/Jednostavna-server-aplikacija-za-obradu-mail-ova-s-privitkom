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

    /**
     * Objekt evidencije za pohranu pomoću serijalizatora, static kako bi sve dretve mogle pristupat podacima
     */
    public static Evidencija evidencija;

    /**
     * Broj dretvi u stustavu trenutno.
     */
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
            System.out.println(ex.getMessage());
        }
    }

    private void pokreniPosluzitelj(Konfiguracija konf) {
        int port = Integer.parseInt(konf.dajPostavku("port"));
        int maxBrZahtjevaCekanje = Integer.parseInt(konf.dajPostavku("max.broj.zahtjeva.cekanje"));
        int maxBrRadnihDretvi = Integer.parseInt(konf.dajPostavku("max.broj.radnih.dretvi"));
        String datotekaEvidencije = konf.dajPostavku("datoteka.evidencije.rada");

        boolean radiDok = true;

        try {
            Konfiguracija evidencijaRada = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datotekaEvidencije);
            ServerSustava.evidencija = new Evidencija(evidencijaRada);
            
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            System.out.println(ex.getMessage());
            ServerSustava.evidencija = new Evidencija();
        }

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
                    Thread.sleep(6000);    //radi testiranja
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println("Korisnik se spojio!");
                if (brojacDretvi >= 64) {
                    brojacDretvi = 0;
                }

                if (brojacDretvi == maxBrRadnihDretvi) {
                    //TODO Ukoliko nema raspoložive radne dretve korisniku se vraća odgovor ERROR 01
                    //TODO Kreirat metodu za slanje poruke outputStreamom u socket posaljiPovratnuPoruku(socket, idPoruke)
                    //TODO Ažuriraj evidenciju rada
                }
                else {
                    RadnaDretva radnaDretva = new RadnaDretva(socket, "anddanzan-" + brojacDretvi, konf);
                    ServerSustava.brojacDretvi++;
                    long brDretvi = ServerSustava.evidencija.getUkupanBrojZahtjeva();
                    ServerSustava.evidencija.setUkupanBrojZahtjeva(++brDretvi);
                    radnaDretva.start();
                    //TODO Ažuriraj evidenciju rada
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
