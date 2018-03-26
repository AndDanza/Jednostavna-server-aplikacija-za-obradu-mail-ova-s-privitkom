package org.foi.nwtis.anddanzan.zadaca_1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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
 *
 * @author Andrea
 */
public abstract class IOT implements Serializable, InterfaceIOT {

    public static int brojac = 0;
    int id;
    String lokacija;
    long vrijemeMilisekunde;
    public static List<IOT> uredajiIOT = null;

    /**
     * Getter za dohvaćanje id-a
     *
     * @return vraća <code>int</code> varijablu s vrijednosti id-a
     */
    public int dohvatiId() {
        return id;
    }

    /**
     * Setter za postavljane id-a
     *
     * @param id dobiveni id zapisuje u arijablu klase
     */
    public void postaviId(int id) {
        this.id = id;
    }

    /**
     * Setter za postavljanje nove lokacije u varijablu klase
     *
     * @param lokacija lokacija za pohranu u varijablu klase
     */
    public void postaviLokaciju(String lokacija) {
        this.lokacija = lokacija;
    }

    /**
     * Getter za dohvaćanje lokacije
     *
     * @return vraća string vrijednost lokacije IOT uređaja
     */
    public String dohvatiLokaciju() {
        return lokacija;
    }

    /**
     * Setter za postavljanje novog vremena mjerenja u varijablu klase
     *
     * @param vrijeme vrijeme u milisekundama za zapisu u varijablu klase
     */
    public void postaviVrijemeMilisekunde(long vrijeme) {
        this.vrijemeMilisekunde = vrijeme;
    }

    /**
     * Getter za dohvaćanje vremena mjerenja u milisekundama
     *
     * @return vrijeme u milisekundama tipa <code>long</code>
     */
    public long dohvatiVrijemeMilisekunde() {
        return vrijemeMilisekunde;
    }

    /**
     * Getter za dohvaćanje vremena mjerenja u obliku datuma
     *
     * @return vrijeme u milisekundama tipa <code>string</code>
     */
    public String dohvatiVrijemeMjerenjaDatum() {
        String timeString = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS").format(this.vrijemeMilisekunde);
        return timeString;
    }

    //deserijalizacija IOT-a za slanje i učitavanje
    /**
     * Statična metoda za pohranu podataka iz liste IOTUređaja u datoteku
     *
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

    /**
     * Metoda za parsiranje stringa json-a i punjenje liste IOT uređaja
     * objektima
     *
     * @param result json objekt prikazan u varijabli tipa string
     */
    public static String popuniListuUredaja(String result) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(result).getAsJsonObject();

        String lokacija = json.get("lokacija").toString();
        int id = Integer.valueOf(json.get("id").toString());
        long vrijeme = Long.valueOf(json.get("vrijemeMilisekunde").toString());
        IOT iotUredaj = null;

        if (json.has("temperatura")) {
            int vrijednost = Integer.valueOf(json.get("temperatura").toString());

            iotUredaj = new IOTTemperatura(id, lokacija, vrijednost, vrijeme);
        }
        else if (json.has("vlaga")) {
            int vrijednost = Integer.valueOf(json.get("vlaga").toString());

            iotUredaj = new IOTVLaga(id, lokacija, vrijednost, vrijeme);
        }
        else if (json.has("brzinaVjetra")) {
            int vrijednost = Integer.valueOf(json.get("brzinaVjetra").toString());

            iotUredaj = new IOTVjetar(id, lokacija, vrijednost, vrijeme);
        }

        if (iotUredaj != null) {
            for (IOT iot : IOT.uredajiIOT) {
                if (iot.dohvatiId() == iotUredaj.dohvatiId()) {
                    IOT.uredajiIOT.remove(iot);
                    IOT.uredajiIOT.add(iotUredaj);
                    return "OK 21;";
                }

                else {
                    IOT.uredajiIOT.add(iotUredaj);
                    return "OK 20";
                }
            }
        }
        
        return "ERROR 21; Sadržaj IOT datoteke nije valjan";

    }

}
