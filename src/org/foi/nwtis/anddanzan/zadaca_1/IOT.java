package org.foi.nwtis.anddanzan.zadaca_1;

/**
 * Klasa za IOT uređaje za koje se pohranjuju podaci
 * @author Andrea
 */
public abstract class IOT {
    int id;
    String lokacija;    
    
    //TODO kreirat klasu IOTTemperatura
    //TODO kreirat klasu IOTVlaga
    //TODO kreirat klasu IOTVjetar

    /**
     *
     * @return
     */

    public int dohvatiId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void postaviId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String dohvatiLokacija() {
        return lokacija;
    }

    /**
     *
     * @param lokacija
     */
    public void postaviLokacija(String lokacija) {
        this.lokacija = lokacija;
    }
}
