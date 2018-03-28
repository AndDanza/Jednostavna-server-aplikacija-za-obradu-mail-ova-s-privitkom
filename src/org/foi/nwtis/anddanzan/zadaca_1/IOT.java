package org.foi.nwtis.anddanzan.zadaca_1;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.Serializable;
import java.util.Properties;
import java.util.Set;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;

/**
 * Klasa za IOT uređaje za koje se pohranjuju podaci
 *
 * @author Andrea
 */
public class IOT implements Serializable {

    String id;
    /**
     * Lista instanci objekata koji implementiraju InterfaceIOT s podacima o
     * mjerenjima uređaja
     */
    Properties atributi;

    /**
     * Konstruktor
     *
     * @param id Identifikator uređaja
     * @param atributi Properties objekat s podacima (atributima) iot uređaja
     */
    public IOT(Properties atributi) {
        this.atributi = atributi;
    }

    /**
     * Getter za dohvaćanje liste objekata s podacima o mjerenju
     *
     * @return vraća listu tipa <code>Properties</code> s atributima uređaja
     * određenog iot uređaja
     */
    public Properties getAtributi() {
        return atributi;
    }

    /**
     * Setter za punjenje liste atributa IOT uređaja
     *
     * @param atributi lista objekata s podacima koje je potrebo pohraniti
     */
    public void setAtributi(Properties atributi) {
        this.atributi = atributi;
    }

    /**
     * Metoda za dodavanje pojedinog objekta IOT mjerenja
     *
     * @param key
     * @param vrijednost
     */
    public void dodajAtribute(String key, String vrijednost) {
        this.atributi.setProperty(key, vrijednost);
    }

    /**
     * Getter za dohvaćanje id-a
     *
     * @return vraća <code>int</code> varijablu s vrijednosti id-a
     */
    public String getId() {
        return this.id;
    }

    /**
     * Setter za postavljane id-a
     *
     * @param id dobiveni id zapisuje u varijablu klase
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Metoda za ažuriranje atributa klase
     *
     * @param iotKlijenta atributi uređaja
     */
    private void azurirajAtribute(IOT iotKlijenta) {
        Properties klijentProp = iotKlijenta.getAtributi();

        Set<String> keysKlijent = klijentProp.stringPropertyNames();
        for (String keyK : keysKlijent) {
            this.atributi.setProperty(keyK, klijentProp.getProperty(keyK));
        }
    }

    /**
     * Metoda za parsiranje stringa jsonData-a i punjenje liste IOT uređaja
     * objektima
     *
     * @param result jsonData objekt prikazan u varijabli tipa string
     */
    public synchronized static String popuniAtribute(String result) {
        IOT iotKlijenta = iotKlijenta = parsirajJson(result);

        if (iotKlijenta != null) {
            if (!ServerSustava.uredajiIOT.isEmpty()) {
                for (IOT iotServera : ServerSustava.uredajiIOT) {
                    if (iotKlijenta.getId().equals(iotServera.getId())) {
                        iotServera.azurirajAtribute(iotKlijenta);
                        return "OK 21;";
                    }
                }
                ServerSustava.uredajiIOT.add(iotKlijenta);
                return "OK 20;";
            }
            else {
                ServerSustava.uredajiIOT.add(iotKlijenta);
                return "OK 20;";
            }
        }
        return "ERROR 21; Sadržaj IOT datoteke nije valjan";

    }

    /**
     * Dobiveni json string (JsonObject) parsira se u obliku objekta IOT uređaja
     * koji sadržava Properties u kojem je id obavezan
     *
     * @param result string jsona
     * @return IOT objekt popunjen podacima iz stringa jsona
     */
    public static IOT parsirajJson(String result) throws JsonSyntaxException {
        try {
            Properties prop = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().fromJson(result, Properties.class);

            if (!prop.containsKey("id")) {
                return null;
            }
            String id = prop.getProperty("id");
            prop.remove("id");

            IOT zapisKlijenta = new IOT(prop);
            zapisKlijenta.setId(id);

            return zapisKlijenta;
        } catch (JsonSyntaxException ex) {
            return null;
        }
    }

    /**
     * Statična metoda za pohranu podataka iz liste IOTUređaja u string
     *
     * @param konf
     */
    public static String serijalizirajIOT(Konfiguracija konf) {
        JsonArray lista = new JsonArray();
        JsonObject objekt;

        synchronized (ServerSustava.uredajiIOT) {
            for (IOT uredaj : ServerSustava.uredajiIOT) {
                objekt = new JsonObject();
                objekt.addProperty("id", uredaj.getId());

                Properties props = uredaj.getAtributi();
                Set<String> keys = props.stringPropertyNames();
                for (String key : keys) {
                    objekt.addProperty(key, props.getProperty(key));
                }

                lista.add(objekt);
            }
        }

        String json = lista.toString();
        String kodZnakova = konf.dajPostavku("skup.kodova.znakova");
        String header = "OK; ZN-KODOVI " + kodZnakova + "; DUZINA ";
        header += json.getBytes().length + "<CRLF>\n";
        return header + json + ";";
    }
}
