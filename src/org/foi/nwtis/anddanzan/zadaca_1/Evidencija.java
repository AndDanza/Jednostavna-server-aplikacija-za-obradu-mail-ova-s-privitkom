package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.Serializable;
import java.util.Properties;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;

/**
 *
 * @author grupa_2
 */
public class Evidencija implements Serializable {

    private long ukupanBrojZahtjeva = 0;    //svi
    private long brojNeispravnihZahtjeva = 0;   //kriva sintaksa
    private long brojNedozvoljenihZahtjeva = 0; //nema pristup
    private long brojUspjesnihZahtjeva = 0; //izvršeni zahtjevi
    private long brojPrekinutihZahtjeva = 0;    //odbijeni jer nema dretvi
    private long ukupnoVrijemeRadaRadnihDretvi = 0; //rad svih dretvi
    private long brojObavljenihSerijalizacija = 0;

    public Evidencija(Konfiguracija evidencijaRada) {
        this.ukupanBrojZahtjeva = Long.valueOf(evidencijaRada.dajPostavku("ukupan.broj.zahtjeva"));
        this.brojNeispravnihZahtjeva = Long.valueOf(evidencijaRada.dajPostavku("broj.neispravnih.zahtjeva"));
        this.brojNedozvoljenihZahtjeva = Long.valueOf(evidencijaRada.dajPostavku("broj.nedozvoljenih.zahtjeva"));
        this.brojUspjesnihZahtjeva = Long.valueOf(evidencijaRada.dajPostavku("broj.uspjesnih.zahtjeva"));
        this.brojPrekinutihZahtjeva = Long.valueOf(evidencijaRada.dajPostavku("broj.prekinutih.zahtjeva"));
        this.ukupnoVrijemeRadaRadnihDretvi = Long.valueOf(evidencijaRada.dajPostavku("ukupno.vrijeme.rada.radnih.dretvi"));
        this.brojObavljenihSerijalizacija = Long.valueOf(evidencijaRada.dajPostavku("broj.obavljenih.serijalizacija"));
    }

    public Evidencija() {
    }
    
    public Properties vratiPropertiesEvidencije(){
        setUkupanBrojZahtjeva(brojNedozvoljenihZahtjeva+brojNeispravnihZahtjeva+brojPrekinutihZahtjeva+brojUspjesnihZahtjeva);
        
        Properties prop = new Properties();
        prop.setProperty("ukupan.broj.zahtjeva", String.valueOf(this.ukupanBrojZahtjeva));
        prop.setProperty("broj.neispravnih.zahtjeva", String.valueOf(this.brojNeispravnihZahtjeva));
        prop.setProperty("broj.nedozvoljenih.zahtjeva", String.valueOf(this.brojNedozvoljenihZahtjeva));
        prop.setProperty("broj.uspjesnih.zahtjeva", String.valueOf(this.brojUspjesnihZahtjeva));
        prop.setProperty("broj.prekinutih.zahtjeva", String.valueOf(this.brojPrekinutihZahtjeva));
        prop.setProperty("ukupno.vrijeme.rada.radnih.dretvi", String.valueOf(this.ukupnoVrijemeRadaRadnihDretvi));
        prop.setProperty("broj.obavljenih.serijalizacija", String.valueOf(this.brojObavljenihSerijalizacija));
        return prop;
    }
    
    public long getUkupanBrojZahtjeva() {
        return ukupanBrojZahtjeva;
    }

    public void setUkupanBrojZahtjeva(long ukupanBrojZahtjeva) {
        this.ukupanBrojZahtjeva = ukupanBrojZahtjeva;
    }

    public long getBrojNeispravnihZahtjeva() {
        return brojNeispravnihZahtjeva;
    }

    public void setBrojNeispravnihZahtjeva(long brojNeispravnihZahtjeva) {
        this.brojNeispravnihZahtjeva = brojNeispravnihZahtjeva;
    }

    public long getBrojNedozvoljenihZahtjeva() {
        return brojNedozvoljenihZahtjeva;
    }

    public void setBrojNedozvoljenihZahtjeva(long brojNedozvoljenihZahtjeva) {
        this.brojNedozvoljenihZahtjeva = brojNedozvoljenihZahtjeva;
    }

    public long getBrojUspjesnihZahtjeva() {
        return brojUspjesnihZahtjeva;
    }

    public void setBrojUpsjesnihZahtjeva(long brojUpsjesnihZahtjeva) {
        this.brojUspjesnihZahtjeva = brojUpsjesnihZahtjeva;
    }

    public long getBrojPrekinutihZahtjeva() {
        return brojPrekinutihZahtjeva;
    }

    public void setBrojPrekinutihZahtjeva(long brojPrekinutihZahtjeva) {
        this.brojPrekinutihZahtjeva = brojPrekinutihZahtjeva;
    }

    public long getUkupnoVrijemeRadaRadnihDretvi() {
        return ukupnoVrijemeRadaRadnihDretvi;
    }

    public void setUkupnoVrijemeRadaRadnihDretvi(long ukupnoVrijemeRadaRadnihDretvi) {
        this.ukupnoVrijemeRadaRadnihDretvi = ukupnoVrijemeRadaRadnihDretvi;
    }

    public long getBrojObavljenihSerijalizacija() {
        return brojObavljenihSerijalizacija;
    }

    public void setBrojObavljenihSerijalizacija(long brojObavljenihSerijalizacija) {
        this.brojObavljenihSerijalizacija = brojObavljenihSerijalizacija;
    }

}
