package org.foi.nwtis.anddanzan.zadaca_1;

import java.text.SimpleDateFormat;

/**
 * Klasa za pohranu podataka o IOT uređaju za mjerenje temperature
 *
 * @author Andrea
 */
public class IOTTemperatura implements InterfaceIOT {

    int temperatura;
    String lokacija;
    long vrijemeMilisekunde;

    /**
     * Konstruktor za instanciranje novog IOT uređaja
     *
     * @param lokacija lokacija uređaja
     * @param tempertatura izmjerena temeperatura
     * @param vrijemeMjerenja vrijeme mjerenja zadano u milisekundama
     */
    public IOTTemperatura(String lokacija, int tempertatura, long vrijemeMjerenja) {
        this.lokacija = lokacija;
        this.temperatura = tempertatura;
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
     * Getter za dohvaćanje vrijednosti mjerenja
     *
     * @return String vrijednost vrijednosti mjerenja
     */
    @Override
    public String dohvatiVrijednostMjerenja() {
        return String.valueOf(this.temperatura);
    }

    /**
     * Setter za postavljanje vrijednosti mjerenja
     *
     * @param mjerenje vrijednost mjerenja za zapisvanje
     */
    @Override
    public void postaviVrijednostMjerenja(int mjerenje) {
        this.temperatura = mjerenje;
    }
}
