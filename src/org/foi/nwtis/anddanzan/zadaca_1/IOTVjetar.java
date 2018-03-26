package org.foi.nwtis.anddanzan.zadaca_1;

import java.text.SimpleDateFormat;

/**
 * Klasa za pohranu podataka o IOT uređaju za mjerenje jačine vjetra
 *
 * @author Andrea
 */
public class IOTVjetar implements InterfaceIOT {

    int brzinaVjetra;
    String lokacija;
    long vrijemeMilisekunde;

    /**
     * Konstruktor
     *
     * @param lokacija lokacija uređaja
     * @param brzinaVjetra izmjerena brzina vjetra
     * @param vrijemeMjerenja vrijeme mjerenja zadano u milisekundama
     */
    public IOTVjetar(String lokacija, int brzinaVjetra, long vrijemeMjerenja) {
        this.lokacija = lokacija;
        this.brzinaVjetra = brzinaVjetra;
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
        return Integer.toString(this.brzinaVjetra);
    }

    /**
     * Setter za postavljanje nove vrijednosti brzine vjetra
     *
     * @param mjerenje nova mjerenja brzine vjetra za pohranu u klasu
     */
    @Override
    public void postaviVrijednostMjerenja(int mjerenje) {
        this.brzinaVjetra = mjerenje;
    }

}
