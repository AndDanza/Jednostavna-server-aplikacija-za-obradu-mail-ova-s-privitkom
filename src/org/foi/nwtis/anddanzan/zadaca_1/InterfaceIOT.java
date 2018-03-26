package org.foi.nwtis.anddanzan.zadaca_1;

/**
 * Propisuje ponašanje IOT uređaja za one parametre koji nisu zadani apstraktnom IOT klasom
 * @author Andrea
 */
interface InterfaceIOT {
    public String dohvatiVrijednostMjerenja();
    public void postaviVrijednostMjerenja(int mjerenje);
    public void postaviLokaciju(String lokacija);
    public String dohvatiLokaciju();
    public void postaviVrijemeMilisekunde(long vrijeme);
    public long dohvatiVrijemeMilisekunde();
    public String dohvatiVrijemeMjerenjaDatum();
}
