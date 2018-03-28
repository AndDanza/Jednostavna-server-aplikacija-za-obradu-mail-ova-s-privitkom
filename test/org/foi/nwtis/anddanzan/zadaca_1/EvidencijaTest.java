package org.foi.nwtis.anddanzan.zadaca_1;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Andrea
 */
public class EvidencijaTest {

    Evidencija evidencija;

    public EvidencijaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        evidencija = new Evidencija();
        evidencija.setBrojNedozvoljenihZahtjeva(0);
        evidencija.setBrojNeispravnihZahtjeva(0);
        evidencija.setBrojObavljenihSerijalizacija(0);
        evidencija.setBrojPrekinutihZahtjeva(0);
        evidencija.setBrojUspjesnihZahtjeva(0);
        evidencija.setUkupanBrojZahtjeva(0);
        evidencija.setUkupnoVrijemeRadaRadnihDretvi(0);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of vratiPropertiesEvidencije method, of class Evidencija.
     */
    @Test
    public void testVratiPropertiesEvidencije() {
        System.out.println("vratiPropertiesEvidencije");
        Evidencija instance = new Evidencija();
        
        assertNotNull(instance);
        
        instance.setBrojNedozvoljenihZahtjeva(0);
        instance.setBrojNeispravnihZahtjeva(0);
        instance.setBrojObavljenihSerijalizacija(0);
        instance.setBrojPrekinutihZahtjeva(0);
        instance.setBrojUspjesnihZahtjeva(0);
        instance.setUkupanBrojZahtjeva(0);
        instance.setUkupnoVrijemeRadaRadnihDretvi(0);
        
        Properties expResult = evidencija.vratiPropertiesEvidencije();
        Properties result = instance.vratiPropertiesEvidencije();
        
        assertEquals(expResult, result);
        assertNotNull(result);
    }

    /**
     * Test of getUkupanBrojZahtjeva method, of class Evidencija.
     */
    @Test
    public void testGetUkupanBrojZahtjeva() {
        System.out.println("getUkupanBrojZahtjeva");
        Evidencija instance = new Evidencija();
        
        assertNotNull(instance);
        
        instance.setBrojNedozvoljenihZahtjeva(3);
        instance.setBrojNeispravnihZahtjeva(1);
        instance.setBrojPrekinutihZahtjeva(5);
        instance.setBrojUspjesnihZahtjeva(6);
        
        evidencija.setBrojNedozvoljenihZahtjeva(3);
        evidencija.setBrojNeispravnihZahtjeva(1);
        evidencija.setBrojPrekinutihZahtjeva(5);
        evidencija.setBrojUspjesnihZahtjeva(6);
        
        long expResult = evidencija.getUkupanBrojZahtjeva();
        long result = instance.getUkupanBrojZahtjeva();
        
        assertEquals(expResult, result);
        assertNotNull(result);
    }

    /**
     * Test of setUkupanBrojZahtjeva method, of class Evidencija.
     */
    @Ignore
    @Test
    public void testSetUkupanBrojZahtjeva() {
        System.out.println("setUkupanBrojZahtjeva");
        long ukupanBrojZahtjeva = 0L;
        Evidencija instance = new Evidencija();
        instance.setUkupanBrojZahtjeva(ukupanBrojZahtjeva);
    }

    /**
     * Test of getBrojNeispravnihZahtjeva method, of class Evidencija.
     */
    @Test
    public void testGetBrojNeispravnihZahtjeva() {
        System.out.println("getBrojNeispravnihZahtjeva");
        Evidencija instance = new Evidencija();
        
        assertNotNull(instance);
        
        instance.setBrojNeispravnihZahtjeva(15);
        long expResult = evidencija.getBrojNeispravnihZahtjeva();
        long result = instance.getBrojNeispravnihZahtjeva();
        
        assertNotEquals(expResult, result);
        assertNotNull(result);
    }

    /**
     * Test of setBrojNeispravnihZahtjeva method, of class Evidencija.
     */
    @Ignore
    @Test
    public void testSetBrojNeispravnihZahtjeva() {
        System.out.println("setBrojNeispravnihZahtjeva");
        long brojNeispravnihZahtjeva = 0L;
        Evidencija instance = new Evidencija();
        instance.setBrojNeispravnihZahtjeva(brojNeispravnihZahtjeva);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBrojNedozvoljenihZahtjeva method, of class Evidencija.
     */
    @Test
    public void testGetBrojNedozvoljenihZahtjeva() {
        System.out.println("getBrojNedozvoljenihZahtjeva");
        Evidencija instance = new Evidencija();
        instance.setBrojNedozvoljenihZahtjeva(0);
        
        assertNotNull(instance);
        
        long expResult = evidencija.getBrojNedozvoljenihZahtjeva();
        long result = instance.getBrojNedozvoljenihZahtjeva();
        
        assertEquals(expResult, result);
        assertNotNull(evidencija);
    }

    /**
     * Test of setBrojNedozvoljenihZahtjeva method, of class Evidencija.
     */
    @Ignore
    @Test
    public void testSetBrojNedozvoljenihZahtjeva() {
        System.out.println("setBrojNedozvoljenihZahtjeva");
        long brojNedozvoljenihZahtjeva = 0L;
        Evidencija instance = new Evidencija();
        instance.setBrojNedozvoljenihZahtjeva(brojNedozvoljenihZahtjeva);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBrojUspjesnihZahtjeva method, of class Evidencija.
     */
    @Test
    public void testGetBrojUspjesnihZahtjeva() {
        System.out.println("getBrojUspjesnihZahtjeva");
        Evidencija instance = new Evidencija();
        instance.setBrojUspjesnihZahtjeva(10);
        
        assertNotNull(instance);
        
        long expResult = evidencija.getBrojUspjesnihZahtjeva();
        long result = instance.getBrojUspjesnihZahtjeva();
        
        assertNotEquals(expResult, result);
        assertNotNull(evidencija);
    }

    /**
     * Test of setBrojUspjesnihZahtjeva method, of class Evidencija.
     */
    @Ignore
    @Test
    public void testSetBrojUspjesnihZahtjeva() {
        System.out.println("setBrojUspjesnihZahtjeva");
        long brojUpsjesnihZahtjeva = 0L;
        Evidencija instance = new Evidencija();
        instance.setBrojUspjesnihZahtjeva(brojUpsjesnihZahtjeva);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBrojPrekinutihZahtjeva method, of class Evidencija.
     */
    @Test
    public void testGetBrojPrekinutihZahtjeva() {
        System.out.println("getBrojPrekinutihZahtjeva");
        Evidencija instance = new Evidencija();
        
        assertNotNull(instance);
        
        instance.setBrojPrekinutihZahtjeva(-10);
        
        long expResult = evidencija.getBrojPrekinutihZahtjeva();
        long result = instance.getBrojPrekinutihZahtjeva();
        
        assertNotEquals(expResult, result);
    }

    /**
     * Test of setBrojPrekinutihZahtjeva method, of class Evidencija.
     */
    @Ignore
    @Test
    public void testSetBrojPrekinutihZahtjeva() {
        System.out.println("setBrojPrekinutihZahtjeva");
        long brojPrekinutihZahtjeva = 0L;
        Evidencija instance = new Evidencija();
        instance.setBrojPrekinutihZahtjeva(brojPrekinutihZahtjeva);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUkupnoVrijemeRadaRadnihDretvi method, of class Evidencija.
     */
    @Test
    public void testGetUkupnoVrijemeRadaRadnihDretvi() {
        System.out.println("getUkupnoVrijemeRadaRadnihDretvi");
        Evidencija instance = new Evidencija();
        instance.setUkupnoVrijemeRadaRadnihDretvi(15616161);
        
        evidencija.setUkupnoVrijemeRadaRadnihDretvi(15616161);
        
        long expResult = evidencija.getUkupnoVrijemeRadaRadnihDretvi();
        long result = instance.getUkupnoVrijemeRadaRadnihDretvi();
        
        assertNotNull(evidencija);
        assertEquals(expResult, result);
    }

    /**
     * Test of setUkupnoVrijemeRadaRadnihDretvi method, of class Evidencija.
     */
    @Ignore
    @Test
    public void testSetUkupnoVrijemeRadaRadnihDretvi() {
        System.out.println("setUkupnoVrijemeRadaRadnihDretvi");
        long ukupnoVrijemeRadaRadnihDretvi = 0L;
        Evidencija instance = new Evidencija();
        instance.setUkupnoVrijemeRadaRadnihDretvi(ukupnoVrijemeRadaRadnihDretvi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBrojObavljenihSerijalizacija method, of class Evidencija.
     */
    @Test
    public void testGetBrojObavljenihSerijalizacija() {
        System.out.println("getBrojObavljenihSerijalizacija");
        Evidencija instance = new Evidencija();
        instance.setBrojObavljenihSerijalizacija(0);
        
        assertNotNull(instance);
        
        long expResult = evidencija.getBrojObavljenihSerijalizacija();
        long result = instance.getBrojObavljenihSerijalizacija();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of setBrojObavljenihSerijalizacija method, of class Evidencija.
     */
    @Ignore
    @Test
    public void testSetBrojObavljenihSerijalizacija() {
        System.out.println("setBrojObavljenihSerijalizacija");
        long brojObavljenihSerijalizacija = 0L;
        Evidencija instance = new Evidencija();
        instance.setBrojObavljenihSerijalizacija(brojObavljenihSerijalizacija);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
