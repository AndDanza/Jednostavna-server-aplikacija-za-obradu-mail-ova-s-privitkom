package org.foi.nwtis.anddanzan.zadaca_1;

/**
 * Klasa za pohranu podataka o IOT uređaju za mjerenje temperature
 * @author Andrea
 */
public class IOTTemperatura extends IOT {

    int temperatura;

    /**
     * Konstruktor
     * @param id identifikator uređaja
     * @param lokacija lokacija uređaja
     * @param tempertatura izmjerena temeperatura
     * @param vrijemeMjerenja vrijeme mjerenja zadano u milisekundama
     */
    public IOTTemperatura(int id, String lokacija, int tempertatura, long vrijemeMjerenja) {
        this.id = id;
        this.lokacija = lokacija;
        this.temperatura = tempertatura;
        this.vrijemeMilisekunde = vrijemeMjerenja;
    }
    /**
     * Getter za dohvaćanje temeprature
     * @return vraća <code>int</code> varijablu s vrijednosti temeprature
     */
    public int dohvatiTemperaturu() {
        return temperatura;
    }

    /**
     * Setter za postavljanje nove vrijednosti temeprature
     * @param temperatura nova temepratura za pohranu u klasu
     */
    public void postaviTemperaturu(int temperatura) {
        this.temperatura = temperatura;
    }

    /**
     * Metoda propisana sučeljem <code>InterfaceIOT</code>
     * @return String vrijednost vrijednosti mjerenja
     */
    @Override
    public String vrijednostMjerenja() {
        return String.valueOf(this.temperatura);
    }
}
