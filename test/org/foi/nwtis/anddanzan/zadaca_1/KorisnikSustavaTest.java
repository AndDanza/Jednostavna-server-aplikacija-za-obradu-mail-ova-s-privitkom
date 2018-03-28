package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class KorisnikSustavaTest {

    int port = 8000;
    String adresa = "127.0.0.1";
    int maxCekanje = 5;
    ServerSocket serverSocket;

    public KorisnikSustavaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            serverSocket = new ServerSocket(port, maxCekanje);
        } catch (IOException ex) {
            Logger.getLogger(KorisnikSustavaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(KorisnikSustavaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of main method, of class KorisnikSustava.
     */
    @Ignore
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        KorisnikSustava.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of posaljiKomandu method, of class KorisnikSustava.
     */
    @Test
    public void testPosaljiKomandu() {
        try {
            System.out.println("posaljiKomandu");
            Socket socket = new Socket(adresa, port);
            KorisnikSustava instance = new KorisnikSustava();
            instance.posaljiKomandu(socket, "Šaljem ti pismo prijatelju");

            Socket zaprimi = serverSocket.accept();
            InputStream is = zaprimi.getInputStream();

            assertNotNull(is);

            OutputStream os = zaprimi.getOutputStream();
            os.write("Stiglo je pismo prijatelju".getBytes());
            zaprimi.shutdownOutput();

        } catch (IOException ex) {
            Logger.getLogger(KorisnikSustavaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of zaprimiOdgovor method, of class KorisnikSustava.
     */
    @Test
    public void testZaprimiOdgovor() {
        try {
            System.out.println("posaljiKomandu");
            Socket socket = new Socket(adresa, port);
            KorisnikSustava instance = new KorisnikSustava();
            OutputStream os = socket.getOutputStream();
            os.write("Pismo je stiglo, šaljem dalje".getBytes());
            socket.shutdownOutput();
            
            Socket zaprimi = serverSocket.accept();
            InputStream is = zaprimi.getInputStream();

            assertNotNull(is);

            OutputStream oss = zaprimi.getOutputStream();
            oss.write("Odlično vidimo se".getBytes());
            zaprimi.shutdownOutput();
            
            String odgovor = instance.zaprimiOdgovor(socket);
            
            assertNotNull(odgovor);

        } catch (IOException ex) {
            Logger.getLogger(KorisnikSustavaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
