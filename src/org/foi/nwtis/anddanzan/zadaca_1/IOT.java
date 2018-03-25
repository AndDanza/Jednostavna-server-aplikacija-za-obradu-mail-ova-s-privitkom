package org.foi.nwtis.anddanzan.zadaca_1;

import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nwtis.anddanzan.konfiguracije.KonfiguracijaJSON;

/**
 * Klasa za IOT uređaje za koje se pohranjuju podaci
 * @author Andrea
 */
public abstract class IOT implements Serializable, InterfaceIOT {

    int id;
    String lokacija;
    long vrijemeMilisekunde;
    public static List<IOT> uredajiIOT = new ArrayList<IOT>();

    /**
     * Getter za dohvaćanje id-a
     * @return vraća <code>int</code> varijablu s vrijednosti id-a
     */
    public int dohvatiId() {
        return id;
    }

    /**
     * Setter za postavljane id-a
     * @param id dobiveni id zapisuje u arijablu klase
     */
    public void postaviId(int id) {
        this.id = id;
    }
    
    /**
     * Setter za postavljanje nove lokacije u varijablu klase
     * @param lokacija lokacija za pohranu u varijablu klase
     */
    public void postaviLokaciju(String lokacija) {
        this.lokacija = lokacija;
    }

    /**
     * Getter za dohvaćanje lokacije
     * @return vraća string vrijednost lokacije IOT uređaja
     */
    public String dohvatiLokaciju() {
        return lokacija;
    }
    
    /**
     * Setter za postavljanje novog vremena mjerenja u varijablu klase
     * @param vrijeme vrijeme u milisekundama za zapisu u varijablu klase
     */
    public void postaviVrijemeMilisekunde(long vrijeme) {
        this.vrijemeMilisekunde = vrijeme;
    }

    /**
     * Getter za dohvaćanje vremena mjerenja u milisekundama
     * @return vrijeme u milisekundama tipa <code>long</code>
     */
    public long dohvatiVrijemeMilisekunde() {
        return vrijemeMilisekunde;
    }

    /**
     * Getter za dohvaćanje vremena mjerenja u obliku datuma
     * @return vrijeme u milisekundama tipa <code>string</code>
     */
    public String dohvatiVrijemeMjerenjaDatum() {
        String timeString = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS").format(this.vrijemeMilisekunde);
        return timeString;
    }

    //deserijalizacija IOT-a za slanje i učitavanje
    /**
     * Statična metoda za pohranu podataka iz liste IOTUređaja u datoteku
     * @param datoteka 
     */
    public static void pohraniPodatke(String datoteka) {
        try (FileWriter file = new FileWriter(datoteka)) {
            String json = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(IOT.uredajiIOT);
            file.write(json);
        } catch (IOException ex) {
            Logger.getLogger(KonfiguracijaJSON.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //deserijalizacija IOT-a za slanje i učitavanje
    /**
     * Metoda za učitavanje podataka o IOT uređajima te punjenje lise onjekata IOT uređaja
     * @param datoteka 
     */
    public static void ucitajPodatke(String datoteka) {
        try (FileReader file = new FileReader(datoteka)) {
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                int znak = file.read();

                if (znak == -1) {
                    break;
                }

                stringBuffer.append((char) znak);
            }
            String result = stringBuffer.toString();
                        
            //TODO naputni listu IOT objektima
            //TODO kako odredit koji objekt mora bit instanciran
            System.out.println(result);
        } catch (IOException ex) {
            Logger.getLogger(KonfiguracijaJSON.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
