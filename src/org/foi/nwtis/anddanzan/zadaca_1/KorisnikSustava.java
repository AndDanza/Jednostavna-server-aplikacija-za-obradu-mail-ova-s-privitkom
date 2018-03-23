package org.foi.nwtis.anddanzan.zadaca_1;

/**
 *
 * @author grupa_2
 */
public class KorisnikSustava {
    String korisnik;
    String lozinka;
    String adresa;
    int port;
    
    String[] args;
    boolean administrator = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO provjeri upisane argumente
        KorisnikSustava korisnik = new KorisnikSustava();
        korisnik.preuzmiPostavke(args); //TODO kroz arg prosljeđuje se komanda, na temelju komande odredi koji je korisnik (admin ili klijent)
        korisnik.args = args;
        
        
        if(true){
            AdministratorSustava admin = new AdministratorSustava();
            admin.preuzmiKontrolu();    //stvaranje socketa i povezivanje sa serverom
        }
        else{
            //TODO kreiraj objekt korisnik sustava i predaj mu kontrolu
            KlijentSustava klijent = new KlijentSustava();
            klijent.preuzmiKontrolu();
        }
    }

    public KorisnikSustava() {
        preuzmiPostavke(args);
    }

    
    private void preuzmiPostavke(String[] args) {
        //TODO hardkodiran admin, potrebno doradit za korisnika
//        this.korisnik = "matnovak";
//        this.lozinka = "123456";
        this.port = 8000;
        this.adresa = "127.0.0.1";
        
        if(korisnik != null){
            if(!korisnik.isEmpty()){
                administrator = true;
            }
        }
        
        if(this.lozinka != null){
            this.lozinka = this.lozinka.trim();
            if(!lozinka.isEmpty()){
                administrator = true;
            }
            else{
                administrator = false;
            }
        }
        else{
                administrator = false;
            }
        
        //TODO provjera ako je korisnik administrator u postavkama
        //TODO Ažuriraj evidenciju
    }
    
}
