package org.foi.nwtis.anddanzan.zadaca_1;

/**
 * Propisuje ponašanje IOT uređaja za one parametre koji nisu zadani apstraktnom
 * IOT klasom
 *
 * @author Andrea
 */
interface InterfaceIOT {

    /**
     * Vrijednost mjerenja zadana je u intu, a ova metoda pretvara u string
     *
     * @return string vrijednost integera koji predstavlja mjerenje određene
     * vrijednosti
     */
    public int dohvatiVrijednostMjerenja();

    /**
     * Postavljanje vrijednosti u varijablu
     * @param mjerenje integer vrijednost
     */
    public void postaviVrijednostMjerenja(int mjerenje);

    /**
     * Lokacija mjerenja vrijednosti u objektu
     * @param lokacija String vrijednost lokacije mjerenja
     */
    public void postaviLokaciju(String lokacija);

    /**
     * Dohvaćanje lokacije mjerenja
     * @return String vrijednost lokacije mjerenja
     */
    public String dohvatiLokaciju();

    /**
     * Postavljanje vremena mjerenja zadano u milisekundama
     * @param vrijeme milisekunde vremena mjerenja
     */
    public void postaviVrijemeMilisekunde(long vrijeme);

    /**
     * Dohvaćanje long vrijednosti vremena mjerenja u izvornom tipu, long
     * @return longvrijednost vremena mjerenja
     */
    public long dohvatiVrijemeMilisekunde();

    /**
     * Konverzija vremena mjerenja u string
     * @return string u obliku dd.MM.yy HH:mm:ss
     */
    public String dohvatiVrijemeMjerenjaDatum();
}
