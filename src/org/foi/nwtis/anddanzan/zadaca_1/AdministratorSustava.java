package org.foi.nwtis.anddanzan.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grupa_2
 */
public class AdministratorSustava extends KorisnikSustava{
    
    public AdministratorSustava() {
        super();
    }
    
    public void preuzmiKontrolu(){
        try {
            Socket socket = new Socket(this.adresa, this.port);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            
            String komanda = "KORISNIK "+korisnik+"; LOZINKA "+lozinka+"; PAUZA;";
            outputStream.write(komanda.getBytes());
            outputStream.flush();
            //outputStream.close(); ne može jer pada program nakon
            socket.shutdownOutput();    //zatvara output stream za socket
            
            StringBuffer stringBuffer = new StringBuffer();
            
            while (true) {  
                int znak = inputStream.read();
                
                if(znak == -1)
                    break;
                
                stringBuffer.append((char) znak);
            }
            
            System.out.println("Odgovor :" + stringBuffer.toString());
            
        } catch (IOException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
