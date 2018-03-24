package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.Serializable;
import java.util.Properties;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;

/**
 * Klasa koja opisuje ponašanje i podatke evidencije o serveru
 * @author Andrea
 */
public class Evidencija implements Serializable {

    private long ukupanBrojZahtjeva = 0;    //svi
    private long brojNeispravnihZahtjeva = 0;   //kriva sintaksa
    private long brojNedozvoljenihZahtjeva = 0; //nema pristup
    private long brojUspjesnihZahtjeva = 0; //izvršeni zahtjevi
    private long brojPrekinutihZahtjeva = 0;    //odbijeni jer nema dretvi
    private long ukupnoVrijemeRadaRadnihDretvi = 0; //rad svih dretvi
    private long brojObavljenihSerijalizacija = 0;

    /**
     * Konstruktor koji na temelju dobivene konfiguracije (učitane evidencije) inicijalizira objekt
     * @param evidencijaRada učitana evidencije u objekt konfiguracije
     */
    public Evidencija(Konfiguracija evidencijaRada) {
        this.ukupanBrojZahtjeva = Long.valueOf(evidencijaRada.dajPostavku("ukupan.broj.zahtjeva"));
        this.brojNeispravnihZahtjeva = Long.valueOf(evidencijaRada.dajPostavku("broj.neispravnih.zahtjeva"));
        this.brojNedozvoljenihZahtjeva = Long.valueOf(evidencijaRada.dajPostavku("broj.nedozvoljenih.zahtjeva"));
        this.brojUspjesnihZahtjeva = Long.valueOf(evidencijaRada.dajPostavku("broj.uspjesnih.zahtjeva"));
        this.brojPrekinutihZahtjeva = Long.valueOf(evidencijaRada.dajPostavku("broj.prekinutih.zahtjeva"));
        this.ukupnoVrijemeRadaRadnihDretvi = Long.valueOf(evidencijaRada.dajPostavku("ukupno.vrijeme.rada.radnih.dretvi"));
        this.brojObavljenihSerijalizacija = Long.valueOf(evidencijaRada.dajPostavku("broj.obavljenih.serijalizacija"));
    }

    /**
     * KOnstruktor bez argumenata
     */
    public Evidencija() {
    }
    
    /**
     * Metoda za kreiranje <code>Properties</code> objekta u kojem su pohranjeni podaci za naknadnu pohranu u konfiguraciju (datoteku evidencije)
     * @return objekt tipa <code>Properties</code> 
     */
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
    
    /**
     * Getter ukupnog broja zahtjeva
     * @return ukupan broj zahtjeva tipa <code>long</code>
     */
    public long getUkupanBrojZahtjeva() {
        return ukupanBrojZahtjeva;
    }

    /**
     * Setter ukupnog broja zahtjeva
     * @param ukupanBrojZahtjeva vrijednost koja se postavlja u varijablu <code>ukupanBrojZahtjeva</code>
     */
    public void setUkupanBrojZahtjeva(long ukupanBrojZahtjeva) {
        this.ukupanBrojZahtjeva = ukupanBrojZahtjeva;
    }

    /**
     * Getter broja neispravnih zahtjeva
     * @return broj neispravnih zahtjeva tipa <code>long</code>
     */
    public long getBrojNeispravnihZahtjeva() {
        return brojNeispravnihZahtjeva;
    }

    /**
     * Setter ukupnog broja neispravnih zahtjeva
     * @param brojNeispravnihZahtjeva vrijednost koja se postavlja u varijablu <code>brojNeispravnihZahtjeva</code>
     */
    public void setBrojNeispravnihZahtjeva(long brojNeispravnihZahtjeva) {
        this.brojNeispravnihZahtjeva = brojNeispravnihZahtjeva;
    }

    /**
     * Getter broja nedozvoljenih zahtjeva
     * @return broj nedozvoljenih zahtjeva tipa <code>long</code>
     */
    public long getBrojNedozvoljenihZahtjeva() {
        return brojNedozvoljenihZahtjeva;
    }

    /**
     * Setter ukupnog nedozvoljenih zahtjeva
     * @param brojNedozvoljenihZahtjeva vrijednost koja se postavlja u varijablu <code>brojNedozvoljenihZahtjeva</code>
     */
    public void setBrojNedozvoljenihZahtjeva(long brojNedozvoljenihZahtjeva) {
        this.brojNedozvoljenihZahtjeva = brojNedozvoljenihZahtjeva;
    }

    /**
     * Getter broja uspješnih zahtjeva
     * @return broj uspješnih zahtjeva tipa <code>long</code>
     */
    public long getBrojUspjesnihZahtjeva() {
        return brojUspjesnihZahtjeva;
    }

    /**
     * Setter ukupnog uspješnih zahtjeva
     * @param brojUpsjesnihZahtjeva vrijednost koja se postavlja u varijablu <code>brojUpsjesnihZahtjeva</code>
     */
    public void setBrojUspjesnihZahtjeva(long brojUpsjesnihZahtjeva) {
        this.brojUspjesnihZahtjeva = brojUpsjesnihZahtjeva;
    }

    /**
     * Getter broja prekinutih zahtjeva
     * @return broj prekinutih zahtjeva tipa <code>long</code>
     */
    public long getBrojPrekinutihZahtjeva() {
        return brojPrekinutihZahtjeva;
    }

    /**
     * Setter ukupnog prekinutih zahtjeva
     * @param brojPrekinutihZahtjeva vrijednost koja se postavlja u varijablu <code>brojPrekinutihZahtjeva</code>
     */
    public void setBrojPrekinutihZahtjeva(long brojPrekinutihZahtjeva) {
        this.brojPrekinutihZahtjeva = brojPrekinutihZahtjeva;
    }

    /**
     * Getter ukupno vrijeme rada radnih dretvi
     * @return ukupno vrijeme rada radnih dretvi tipa <code>long</code>
     */
    public long getUkupnoVrijemeRadaRadnihDretvi() {
        return ukupnoVrijemeRadaRadnihDretvi;
    }

    /**
     * Setter ukupnog vremena rada radnih dretvi
     * @param ukupnoVrijemeRadaRadnihDretvi vrijednost koja se postavlja u varijablu <code>ukupnoVrijemeRadaRadnihDretvi</code>
     */
    public void setUkupnoVrijemeRadaRadnihDretvi(long ukupnoVrijemeRadaRadnihDretvi) {
        this.ukupnoVrijemeRadaRadnihDretvi = ukupnoVrijemeRadaRadnihDretvi;
    }

    /**
     * Getter broja obavljenih serijalizacija
     * @return broj obavljenih serijalizacija tipa <code>long</code>
     */
    public long getBrojObavljenihSerijalizacija() {
        return brojObavljenihSerijalizacija;
    }

    /**
     * Setter ukupnog broja obavljenih serijalizacija
     * @param brojObavljenihSerijalizacija vrijednost koja se postavlja u varijablu <code>brojObavljenihSerijalizacija</code>
     */
    public void setBrojObavljenihSerijalizacija(long brojObavljenihSerijalizacija) {
        this.brojObavljenihSerijalizacija = brojObavljenihSerijalizacija;
    }

}
