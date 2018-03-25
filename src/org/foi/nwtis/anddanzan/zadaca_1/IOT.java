package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa za IOT uređaje za koje se pohranjuju podaci
 * @author Andrea
 */
public abstract class IOT implements Serializable, InterfaceIOT{
    int id;
    String lokacija;
    long vrijemeMjerenjaMilisekunde;
    public static List<IOT> uredajiIOT = new ArrayList<IOT>();
    
    
    //TODO kreirat klasu IOTTemperatura
    //TODO kreirat klasu IOTVlaga
    //TODO kreirat klasu IOTVjetar

    /**
     * Getter za dohvaćanje id-a
     * @return vraća <code>int</code> varijablu s vrijednosti id-a
     */
    public int dohvatiId() {
        return id;
    }

    /**
     * Getter za dohvaćanje lokacije
     * @return vraća string vrijednost lokacije IOT uređaja
     */
    public String dohvatiLokacija() {
        return lokacija;
    }
    
    /**
     * Getter za dohvaćanje vremena mjerenja u milisekundama
     * @return vrijeme u milisekundama tipa <code>long</code>
     */
    public long dohvatiVrijemeMjerenjaMilisekunde() {
        return vrijemeMjerenjaMilisekunde;
    }
    
    /**
     * Getter za dohvaćanje vremena mjerenja u obliku datuma
     * @return vrijeme u milisekundama tipa <code>string</code>
     */
    public String dohvatiVrijemeMjerenjaDatum() {
        String timeString = new SimpleDateFormat("HH:mm:ss:SSS").format(this.vrijemeMjerenjaMilisekunde);
        return timeString;
    }
    
    //TODO serijalizacija IOT-a za pohranu
    //public void serijalizirajIOT(){}
    
    //TODO deserijalizacija IOT-a za slanje i učitavanje
    //public IOT deserijalizirajIOT(){}

    
}
