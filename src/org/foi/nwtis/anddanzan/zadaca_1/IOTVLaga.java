/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.anddanzan.zadaca_1;

/**
 * Klasa za pohranu podataka o IOT uređaju za mjerenje vlage zraka
 * @author Andrea
 */
public class IOTVLaga extends IOT {

    int vlaga;

    /**
     * Konstruktor
     * @param id identifikator uređaja
     * @param lokacija lokacija uređaja
     * @param vlaga izmjerena vlaga zraka
     * @param vrijemeMjerenja vrijeme mjerenja zadano u milisekundama
     */
    public IOTVLaga(int id, String lokacija, int vlaga, long vrijemeMjerenja) {
        this.id = id;
        this.lokacija = lokacija;
        this.vlaga = vlaga;
        this.vrijemeMilisekunde = vrijemeMjerenja;
    }

    /**
     * Getter za dohvaćanje vlage zraka
     * @return vraća <code>int</code> varijablu s vrijednosti temeprature
     */
    public int dohvatiVlagu() {
        return vlaga;
    }

    /**
     * Setter za postavljanje nove vrijednosti vlage zraka
     * @param vlaga nova mjerenja vlage u zraku za pohranu u klasu
     */
    public void postaviVlagu(int vlaga) {
        this.vlaga = vlaga;
    }

    /**
     * Metoda InterfeceIOT koja definira vraćanje vrijednosti mjerenja svakog IOT uređaja
     * @return string vrijednost mjerenja
     */
    @Override
    public String vrijednostMjerenja() {
        return String.valueOf(this.vlaga);
    }

}
