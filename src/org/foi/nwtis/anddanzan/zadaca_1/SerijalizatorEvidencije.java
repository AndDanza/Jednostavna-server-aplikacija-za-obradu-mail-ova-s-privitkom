package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;

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
            
            System.out.println("Dretva " + this.nazivDretve + " krenula u: " + pocetak);
            
            File datEvidencije = new File(nazivDatEvidencije);
            ObjectOutputStream objOutputStream = null;
            try {
                objOutputStream = new ObjectOutputStream(new FileOutputStream(datEvidencije));
                //TODO pozvati objekt evidencija rada iz ServerSustava Evidencija zapisEvidencije = ServerSustava.evidencija;
                //TODO serijalizirat evidenciju u datoteku
                //TODO ažuriraj evidenciju brojObavljenihSerijalizacija
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            } finally{
                try {
                    objOutputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
                }
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
