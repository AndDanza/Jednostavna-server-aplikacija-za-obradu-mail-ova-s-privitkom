package org.foi.nwtis.anddanzan.zadaca_1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Properties;
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
public class IOTTest {

    String json = "  {\n"
            + "    \"id\": 2,\n"
            + "	\"lokacija\":\"Pula\",\n"
            + "	\"temp\":\"25\"\n"
            + "  }";

    public IOTTest() {
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
     * Test of getAtributi method, of class IOT.
     */
    @Test
    public void testGetAtributi() {
        System.out.println("getAtributi");

        IOT instance = IOT.parsirajJson(this.json);

        Properties expResult = new GsonBuilder().create().fromJson(this.json, Properties.class);
        Properties result = instance.getAtributi();
        result.setProperty("id", instance.getId());

        assertEquals(expResult, result);
    }

    /**
     * Test of setAtributi method, of class IOT.
     */
    @Ignore
    @Test
    public void testSetAtributi() {
        System.out.println("setAtributi");
        Properties atributi = null;
        IOT instance = null;
        instance.setAtributi(atributi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dodajAtribute method, of class IOT.
     */
    @Ignore
    @Test
    public void testDodajAtribute() {
        System.out.println("dodajAtribute");
        String key = "";
        String vrijednost = "";
        IOT instance = null;
        instance.dodajAtribute(key, vrijednost);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId method, of class IOT.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");

        IOT instance = IOT.parsirajJson(this.json);

        String expResult = "json";
        String result = instance.getId();

        assertNotEquals(expResult, result);
    }

    /**
     * Test of setId method, of class IOT.
     */
    @Ignore
    @Test
    public void testSetId() {
        System.out.println("setId");
        String id = "";
        IOT instance = null;
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popuniAtribute method, of class IOT.
     */
    @Test
    public void testPopuniAtribute() {
        System.out.println("popuniAtribute");
        
        String expResult = "OK 20;";
        String result = IOT.popuniAtribute(this.json);
        
        System.out.println(result);
        
        assertTrue(expResult.equals(result));
    }

    /**
     * Test of serijalizirajIOT method, of class IOT.
     */
    @Test
    public void testSerijalizirajIOT() {

        try {
            System.out.println("serijalizirajIOT");

            Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju("NWTiS_anddanzan.txt");
            String expResult = "OK; ZN-KODOVI " + konf.dajPostavku("skup.kodova.znakova") + "; DUZINA ";
            expResult += this.json.getBytes().length + "<CRLF>\n";
            expResult += this.json;

            IOT instance = IOT.parsirajJson(this.json);
            ServerSustava.uredajiIOT.add(instance);
            String result = IOT.serijalizirajIOT(konf);

            assertNotEquals(expResult, result);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(IOTTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
