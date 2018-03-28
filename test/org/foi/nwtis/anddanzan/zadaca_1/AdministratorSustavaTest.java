package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.File;
import junit.extensions.TestSetup;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Andrea
 */
public class AdministratorSustavaTest extends TestCase {

    static String adresa = "127.0.0.1";
    static int port = 8000;
    static String naredba;
    static String datoteka;
    static int maxVeza = 5;

    public AdministratorSustavaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    public static TestSetup suite() {
        TestSetup setup = new TestSetup(new TestSuite(AdministratorSustavaTest.class)) {
            //pozivanje setUp-a samo jedno
            protected void setUp() throws Exception {
                Thread thread = new Thread() {
                    public void run() {
                        String[] args = new String[1];
                        args[0] = "NWTiS_anddanzan.txt";
                        ServerSustava.main(args);
                    }
                };

                thread.start();
            }

            protected void tearDown() throws Exception {
                //pozivanje tearDown-a samo jedno
                AdministratorSustava instanceGasi = new AdministratorSustava();
                instanceGasi.adresa = adresa;
                instanceGasi.port = port;
                instanceGasi.komanda = "-k korisnik -l lozinka -s " + adresa + " -p " + port + "--zaustavi";
                instanceGasi.preuzmiKontrolu();
            }
        };

        return setup;
    }

    /**
     * Test of preuzmiKontrolu method, of class AdministratorSustava.
     */
    @Test
    public void testPreuzmiKontroluEvidencijaDat() {

        System.out.println("preuzmiKontrolu");

        this.naredba = "evidencija";
        this.datoteka = "evid.json";

        AdministratorSustava instance = new AdministratorSustava();
        instance.adresa = this.adresa;
        instance.port = this.port;
        instance.komanda = "-k korisnik -l lozinka -s " + this.adresa + " -p " + this.port + " --" + this.naredba + " " + this.datoteka;
        instance.preuzmiKontrolu();

        assertNotNull(instance);

        File datKonf = new File(this.datoteka);

        assertTrue(datKonf.exists());

        assertFalse(datKonf.isDirectory());
    }

    /**
     * Test of preuzmiKontrolu method, of class AdministratorSustava.
     */
    @Test
    public void testPreuzmiKontroluIotDat() {

        System.out.println("preuzmiKontrolu");

        this.naredba = "iot";
        this.datoteka = "iot.json";

        AdministratorSustava instance = new AdministratorSustava();
        instance.adresa = this.adresa;
        instance.port = this.port;
        instance.komanda = "-k korisnik -l lozinka -s " + this.adresa + " -p " + this.port + " --" + this.naredba + " " + this.datoteka;
        instance.preuzmiKontrolu();

        assertNotNull(instance);

        File datKonf = new File(this.datoteka);

        assertTrue(datKonf.exists());

        assertFalse(datKonf.isDirectory());
    }
}
