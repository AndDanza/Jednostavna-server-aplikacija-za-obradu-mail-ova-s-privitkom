package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.Serializable;

/**
 *
 * @author grupa_2
 */
public class Evidencija implements Serializable{
    private long ukupanBrojZahtjeva = 0;
    private long brojNeispravnihZahtjeva = 0;
    private long brojNedozvoljenihZahtjeva = 0;
    private long brojUpsjesnihZahtjeva = 0;
    private long brojPrekinutihZahtjeva = 0;
    private long ukupnoVrijemeRadaRadnihDretvi = 0;
    private long brojObavljenihSerijalizacija = 0;

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

    public long getBrojUpsjesnihZahtjeva() {
        return brojUpsjesnihZahtjeva;
    }

    public void setBrojUpsjesnihZahtjeva(long brojUpsjesnihZahtjeva) {
        this.brojUpsjesnihZahtjeva = brojUpsjesnihZahtjeva;
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
