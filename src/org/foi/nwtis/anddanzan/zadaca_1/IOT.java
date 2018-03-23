package org.foi.nwtis.anddanzan.zadaca_1;

/**
 *
 * @author Andrea
 */
public abstract class IOT {
    int id;
    String lokacija;    
    
    //TODO kreirat klasu IOTTemperatura
    //TODO kreirat klasu IOTVlaga
    //TODO kreirat klasu IOTVjetar

    public int dohvatiId() {
        return id;
    }

    public void postaviId(int id) {
        this.id = id;
    }

    public String dohvatiLokacija() {
        return lokacija;
    }

    public void postaviLokacija(String lokacija) {
        this.lokacija = lokacija;
    }
}
