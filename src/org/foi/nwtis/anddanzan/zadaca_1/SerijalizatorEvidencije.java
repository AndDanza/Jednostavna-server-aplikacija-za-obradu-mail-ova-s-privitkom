package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
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
class SerijalizatorEvidencije extends Thread{
    
    private String nazivDretve;
    private Konfiguracija konf;
    private boolean radiDok = true;

    SerijalizatorEvidencije(String nazivDretve, Konfiguracija konf) {
        super(nazivDretve);
        this.nazivDretve = nazivDretve;
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        super.interrupt(); 
    }

    @Override
    public void run() {
        String nazivDatEvidencije = konf.dajPostavku("datoteka.evidencije.rada");
        int intervalZaSerijalizaciju = Integer.parseInt(konf.dajPostavku("interval.za.serijalizaciju"));
        
        while (radiDok) {            
            long pocetak = System.currentTimeMillis();
            String timeString = new SimpleDateFormat("HH:mm:ss:SSS").format(pocetak);
            System.out.println("Dretva " + this.nazivDretve + " krenula u: " + timeString);
            
            try {
                //pozvati objekt evidencijaProp rada iz ServerSustava 
                Evidencija evidencijaZaPohranu = ServerSustava.evidencija;
                Properties evidencijaProp = evidencijaZaPohranu.vratiPropertiesEvidencije();
                //serijalizirat evidenciju u datoteku
                Konfiguracija pohranaEvidencije = KonfiguracijaApstraktna.kreirajKonfiguraciju(nazivDatEvidencije);
                pohranaEvidencije.kopirajKonfiguraciju(evidencijaProp);
                pohranaEvidencije.spremiKonfiguraciju();
                
                //ažuriraj evidenciju brojObavljenihSerijalizacija
                long brSerijalizacija = ServerSustava.evidencija.getBrojObavljenihSerijalizacija();
                ServerSustava.evidencija.setBrojObavljenihSerijalizacija(++brSerijalizacija);
            } 
            catch (/*IOException | */NemaKonfiguracije | NeispravnaKonfiguracija ex) {
                System.out.println(ex.getMessage());
            }
            
            long kraj = System.currentTimeMillis();
            long odradeno = kraj - pocetak;
            long cekaj = intervalZaSerijalizaciju*1000 - odradeno;
            
            try {
                Thread.sleep(cekaj);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }
    
}
