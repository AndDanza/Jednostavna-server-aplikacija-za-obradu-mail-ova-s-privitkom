package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.nwtis.anddanzan.konfiguracije.Konfiguracija;
import org.nwtis.anddanzan.konfiguracije.KonfiguracijaApstraktna;
import org.nwtis.anddanzan.konfiguracije.NeispravnaKonfiguracija;
import org.nwtis.anddanzan.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author Andrea
 */
public class SerijalizatorEvidencijeTest {
    
    String datoteka = "NWTiS_anddanzan.txt";
    SerijalizatorEvidencije serijalizator;
    
    public SerijalizatorEvidencijeTest() {
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

    /**
     * Test of interrupt method, of class SerijalizatorEvidencije.
     */
    @Ignore
    @Test
    public void testInterrupt() {
        System.out.println("interrupt");
        SerijalizatorEvidencije instance = null;
        instance.interrupt();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of run method, of class SerijalizatorEvidencije.
     */
    @Test
    public void testRun() {
        System.out.println("start");
        Konfiguracija konf;
        try {
            konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            ServerSustava.evidencija = new Evidencija();
            serijalizator = new SerijalizatorEvidencije("Serijalizator", konf);
            serijalizator.start();
        } catch (NeispravnaKonfiguracija | NemaKonfiguracije ex) {
            Logger.getLogger(SerijalizatorEvidencijeTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        File datKonf = new File(datoteka);

        if (!datKonf.exists()) {
            fail("Datoteka ne postoji");
        }
        else if (datKonf.isDirectory()) {
            fail("Datoteka nije datoteka!");
        }

        assertNotNull(datKonf);
    }

    /**
     * Test of start method, of class SerijalizatorEvidencije.
     */
    @Ignore
    @Test
    public void testStart() {
        System.out.println("start");
        SerijalizatorEvidencije instance = null;
        instance.start();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
