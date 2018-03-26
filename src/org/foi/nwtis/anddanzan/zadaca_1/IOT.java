package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa za IOT uređaje za koje se pohranjuju podaci
 *
 * @author Andrea
 */
public class IOT implements Serializable {

    int id;
    List<InterfaceIOT> mjerenjaUredaja = null;

    public IOT(int id) {
        this.id = id;
        mjerenjaUredaja = new ArrayList<>();
    }

    /**
     * Getter za dohvaćanje liste objekata s podacima o mjerenju
     *
     * @return vraća listu tipa <code>InterfaceIOT</code> listu s objektima
     * određenog iot uređaja
     */
    public List<InterfaceIOT> dohvatiMjerenjaUredaja() {
        return mjerenjaUredaja;
    }

    /**
     * Setter za punjenje liste objekata IOT uređaja
     *
     * @param mjerenjaUredaja lista objekata s podacima koje je potrebo
     * pohraniti
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
     * @param novaMjerenjaUredaja lista novih mjerenja koje je potrebno dodati
     */
    public void azurirajMjerenjeUredaja(List<InterfaceIOT> novaMjerenjaUredaja) {
        for (InterfaceIOT obj : novaMjerenjaUredaja) {
            this.mjerenjaUredaja.add(obj);
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
}
