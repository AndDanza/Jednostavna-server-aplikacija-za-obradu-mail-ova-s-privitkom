package org.foi.nwtis.anddanzan.zadaca_1;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.Serializable;

/**
 * Klasa za IOT uređaje za koje se pohranjuju podaci
 *
 * @author Andrea
 */
public class IOT implements Serializable {

    int id;
    /**
     * Lista instanci objekata koji implementiraju InterfaceIOT s podacima o
     * mjerenjima uređaja
     */
    JsonArray skupAtributa = null;

    /**
     * Konstruktor
     *
     * @param id Identifikator uređaja
     * @param atributi lista json objekata s podacima (atributima) iot uređaja
     */
    public IOT(int id, JsonArray atributi) {
        this.id = id;
        skupAtributa = atributi;
    }

    /**
     * Getter za dohvaćanje liste objekata s podacima o mjerenju
     *
     * @return vraća listu tipa <code>JsonArray</code> s atributima uređaja
     * određenog iot uređaja
     */
    public JsonArray dohvatiAtribute() {
        return skupAtributa;
    }

    /**
     * Setter za punjenje liste atributa IOT uređaja
     *
     * @param atributi lista objekata s podacima koje je potrebo pohraniti
     */
    public void postaviAtribute(JsonArray atributi) {
        this.skupAtributa = atributi;
    }

    /**
     * Metoda za dodavanje pojedinog objekta IOT mjerenja
     *
     * @param atribut objekat s podacima koje je potrebo pohraniti
     */
    public void dodajAtribute(JsonArray atribut) {
        for (JsonElement jsonElement : atribut) {
            this.skupAtributa.add(jsonElement);
        }

    }

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
     * Metoda za parsiranje stringa jsonData-a i punjenje liste IOT uređaja
     * objektima
     *
     * @param result jsonData objekt prikazan u varijabli tipa string
     */
    public synchronized static String popuniListuUredaja(String result) {
        IOT iotKlijenta = null;
        try {
            iotKlijenta = parsirajJson(result);
        } catch (JsonSyntaxException ex) {
            return "ERROR 21; Sadržaj IOT datoteke nije valjan";
        }

        if (iotKlijenta != null) {
            if (!ServerSustava.uredajiIOT.isEmpty()) {
                for (IOT iotServera : ServerSustava.uredajiIOT) {
                    if (iotServera.dohvatiId() == iotKlijenta.dohvatiId()) { //pronađi iotuređaj prema id-u
                        iotServera.dodajAtribute(iotKlijenta.dohvatiAtribute());
                        return "OK 21;";
                    }
                }
                ServerSustava.uredajiIOT.add(iotKlijenta);
                return "OK 20";
            }
            else {
                ServerSustava.uredajiIOT.add(iotKlijenta);
                return "OK 20";
            }
        }
        return "ERROR 21; Sadržaj IOT datoteke nije valjan";

    }
    
    /**
     * Dobiveni json string (JsonObject) parsira se u obliku objekta IOT uređaja
     * s listom json objekata koji predstavljaju atribute
     *
     * @param result string jsona
     * @return IOT objekt popunjen podacima iz stringa jsona
     */
    private static IOT parsirajJson(String result) throws JsonSyntaxException {
        JsonParser parser = new JsonParser();
        JsonObject jsonData = parser.parse(result).getAsJsonObject();

        int id = Integer.valueOf(jsonData.get("id").toString());
        JsonArray atributi = jsonData.get("atributi").getAsJsonArray();
        IOT zapisKlijenta = new IOT(id, atributi);

        return zapisKlijenta;
    }
}
