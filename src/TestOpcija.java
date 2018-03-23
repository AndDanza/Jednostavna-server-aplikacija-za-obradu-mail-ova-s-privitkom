/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author dkermek
 */
public class TestOpcija {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        while (true) {
            // -server -konf datoteka(.txt | .xml) [-load]
            String usernamePass = "-k ([A-Za-z0-9_-]{3,10}) -l ([A-Za-z0-9_\\-#!]{3,10})";  //-k korisink -l lozinka, vrijednosti u polju na indexma: 1, 2
            String ipAdresa = "-s (\\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\\b)"; //127.0.0.1, vrijednosti u polju na indexma: 1
            String adresa = "-s ((?:[A-Za-z0-9]+(?:\\.[A-Za-z0-9])?)+)"; //anddanzan.foi.hr, , vrijednosti u polju na indexma: 1
            String port = "-p ((?:8|9)?[0-9]{1}[0-9]{1}[0-9]{1}){1}"; //-p 8999, , vrijednosti u polju na indexma: 1
            String naredba = "\\-\\-(kreni)|\\-\\-(zaustavi)|\\-\\-(pauza)|\\-\\-(stanje)|\\-\\-(evidencija) ([A-Za-z0-9_-]+\\.{1}[A-Za-z0-9]{1,10})|\\-\\-(iot) ([A-Za-z0-9_-]+\\.{1}[A-Za-z0-9]{1,10})";
            //--stanje, --evidencija dat_dat-dat.txt, vrijednost != null, ako se radi o evidenciji ili iot vrijednosti na: != null i sljedeće polje +1
            
            String regex = usernamePass;

            Scanner sc = new Scanner(System.in);
            System.out.print("Rijec: ");
            String p = sc.nextLine();
            System.out.println("Uneseno: " + p);

            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(p);
            boolean status = m.find();
            if (status) {
                int poc = 0;
                int kraj = m.groupCount();
                for (int i = poc; i <= kraj; i++) {
                    if (m.group(i) != null) {
                        System.out.println(i + ". " + m.group(i));
                        if(m.group(i).equals("stop")){
                            return;
                        }
                    }
                    
                }
            }
            else {
                System.out.println("Ne odgovara!");
            }
        }
    }
}
