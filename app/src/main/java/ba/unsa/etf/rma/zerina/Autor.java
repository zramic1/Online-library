package ba.unsa.etf.rma.zerina;

import java.util.ArrayList;

/**
 * Created by zerin on 5/13/2018.
 */

public class Autor {

    String imeiPrezime;
    ArrayList<String> knjige = new ArrayList<String>();

    int brojKnjiga;
    boolean imaAutora;


    public Autor(String ime) {
        this.imeiPrezime = ime;
        this.brojKnjiga = 1;
        this.imaAutora = false;

    }


    public Autor(String imeiPrezime, String id) {
        this.imeiPrezime = imeiPrezime;
        dodajKnjigu(id);
    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public void dodajKnjigu(String id){
        boolean ima = false;
        for(int i=0; i<knjige.size();i++){
            if(knjige.get(i).equals(id)) ima = true;
        }
        if(ima == false) knjige.add(id);
    }


    public int getBrojKnjiga() {
        return brojKnjiga;
    }

    public void setBrojKnjiga() {
        this.brojKnjiga++;
    }


    public boolean isImaAutora() {
        return imaAutora;
    }

    public void setImaAutora(boolean imaAutora) {
        this.imaAutora = imaAutora;
    }



}
