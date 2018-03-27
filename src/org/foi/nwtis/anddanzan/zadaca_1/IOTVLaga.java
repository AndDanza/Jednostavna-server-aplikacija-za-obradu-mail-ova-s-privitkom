/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.anddanzan.zadaca_1;

import java.text.SimpleDateFormat;

/**
 * Klasa za pohranu podataka o IOT uređaju za mjerenje vlage zraka
 *
 * @author Andrea
 */
public class IOTVLaga implements InterfaceIOT {

    int vlaga;
    String lokacija;
    long vrijemeMilisekunde;

    /**
     * Konstruktor za instanciranje novog IOT uređaja
     *
     * @param lokacija lokacija uređaja
     * @param vlaga izmjerena vlaga zraka
     * @param vrijemeMjerenja vrijeme mjerenja zadano u milisekundama
     */
    public IOTVLaga(String lokacija, int vlaga, long vrijemeMjerenja) {
        this.lokacija = lokacija;
        this.vlaga = vlaga;
        this.vrijemeMilisekunde = vrijemeMjerenja;
    }

    /**
     * Setter za postavljanje nove lokacije u varijablu klase
     *
     * @param lokacija lokacija za pohranu u varijablu klase
     */
    @Override
    public void postaviLokaciju(String lokacija) {
        this.lokacija = lokacija;
    }

    /**
     * Getter za dohvaćanje lokacije
     *
     * @return vraća string vrijednost lokacije IOT uređaja
     */
    @Override
    public String dohvatiLokaciju() {
        return lokacija;
    }

    /**
     * Setter za postavljanje novog vremena mjerenja u varijablu klase
     *
     * @param vrijeme vrijeme u milisekundama za zapisu u varijablu klase
     */
    @Override
    public void postaviVrijemeMilisekunde(long vrijeme) {
        this.vrijemeMilisekunde = vrijeme;
    }

    /**
     * Getter za dohvaćanje vremena mjerenja u milisekundama
     *
     * @return vrijeme u milisekundama tipa <code>long</code>
     */
    @Override
    public long dohvatiVrijemeMilisekunde() {
        return vrijemeMilisekunde;
    }

    /**
     * Getter za dohvaćanje vremena mjerenja u obliku datuma
     *
     * @return vrijeme u milisekundama tipa <code>string</code>
     */
    @Override
    public String dohvatiVrijemeMjerenjaDatum() {
        String timeString = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS").format(this.vrijemeMilisekunde);
        return timeString;
    }

    /**
     * Getter za dohvaćanje vlage zraka
     *
     * @return vraća <code>int</code> varijablu s vrijednosti temeprature
     */
    @Override
    public int dohvatiVrijednostMjerenja() {
        return this.vlaga;
    }

    /**
     * Setter za postavljanje nove vrijednosti vlage zraka
     *
     * @param mjerenje nova mjerenja vlage u zraku za pohranu u klasu
     */
    @Override
    public void postaviVrijednostMjerenja(int mjerenje) {
        this.vlaga = mjerenje;
    }

}
