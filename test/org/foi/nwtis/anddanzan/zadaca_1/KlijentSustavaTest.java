package org.foi.nwtis.anddanzan.zadaca_1;

import static junit.framework.TestCase.assertNotNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrea
 */
public class KlijentSustavaTest {

    static String adresa = "127.0.0.1";
    static int port = 8000;
    static String naredba;
    static String datoteka;
    static int maxVeza = 5;

    public KlijentSustavaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        Thread thread = new Thread() {
            public void run() {
                String[] args = new String[1];
                args[0] = "NWTiS_anddanzan.txt";
                ServerSustava.main(args);
            }
        };

        thread.start();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of preuzmiKontrolu method, of class KlijentSustava.
     */
    @Test

    public void testPreuzmiKontrolu() {

        System.out.println("preuzmiKontrolu");

        this.naredba = "iot";
        this.datoteka = " danzante1.json";

        KlijentSustava instance = new KlijentSustava();
        instance.adresa = this.adresa;
        instance.port = this.port;
        instance.komanda = "-s " + this.adresa + " -p " + this.port + this.datoteka;
        instance.preuzmiKontrolu();

        assertNotNull(instance);

        assertFalse(ServerSustava.uredajiIOT.isEmpty());

        AdministratorSustava instanceGasi = new AdministratorSustava();
        instanceGasi.adresa = adresa;
        instanceGasi.port = port;
        instanceGasi.komanda = "-k korisnik -l lozinka -s " + adresa + " -p " + port + "--zaustavi";
        instanceGasi.preuzmiKontrolu();
    }

}
