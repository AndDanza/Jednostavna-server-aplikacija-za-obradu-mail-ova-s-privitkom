package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa za IOT uređaje za koje se pohranjuju podaci
 *
 * @author Andrea
 */
public class IOT implements Serializable{

    int id;
    List<InterfaceIOT> mjerenjaUredaja = null;

    public IOT(int id) {
        this.id = id;
        mjerenjaUredaja = new ArrayList<>();
    }

    /**
     * Getter za dohvaćanje liste objekata s podacima o mjerenju
     *
     * @return vraća listu tipa <code>InterfaceIOT</code> listu s objektima određenog iot uređaja
     */
    public List<InterfaceIOT> dohvatiMjerenjaUredaja() {
        return mjerenjaUredaja;
    }

    /**
     * Setter za punjenje liste objekata IOT uređaja
     *
     * @param mjerenjaUredaja lista objekata s podacima koje je potrebo pohraniti
     */
    public void postaviMjerenjaUredaja(List<InterfaceIOT> mjerenjaUredaja) {
        this.mjerenjaUredaja = mjerenjaUredaja;
    }
    
    /**
     * Metoda za dodavanje pojedinog objekta IOT mjerenja
     *
     * @param mjerenjeUredaja objekat s podacima koje je potrebo pohraniti
     */
    public void dodajMjerenjeUredaja(InterfaceIOT mjerenjeUredaja) {
        this.mjerenjaUredaja.add(mjerenjeUredaja);
    }
    
    /**
     * Metoda za azuriranje zapisa objekta s podacima
     *
     * @param mjerenjeUredaja objekat s podacima koji je potrebno pohraniti na mjesto postojećeg
     */
    public void azurirajMjerenjeUredaja(InterfaceIOT mjerenjeUredaja) {
        for(InterfaceIOT obj : this.mjerenjaUredaja)
            if(obj.dohvatiVrijemeMilisekunde() == mjerenjeUredaja.dohvatiVrijemeMilisekunde())
                this.mjerenjaUredaja.remove(obj);
        
        this.mjerenjaUredaja.add(mjerenjeUredaja);
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
     * Metoda za parsiranje stringa json-a i punjenje liste IOT uređaja
     * objektima
     *
     * @param result json objekt prikazan u varijabli tipa string
     */
    public String popuniListuUredaja(String result) {
        /*
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(result).getAsJsonObject();

        String lokacija = json.get("lokacija").toString();
        int id = Integer.valueOf(json.get("id").toString());
        long vrijeme = Long.valueOf(json.get("vrijemeMilisekunde").toString());
        InterfaceIOT iotUredaj = null;

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
            if (!IOT.uredajiIOT.isEmpty()) {
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
            else {
                IOT.uredajiIOT.add(iotUredaj);
                return "OK 20";
            }
        }

*/
        return "ERROR 21; Sadržaj IOT datoteke nije valjan";

    }

}
