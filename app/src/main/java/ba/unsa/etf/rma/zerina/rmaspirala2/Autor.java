package ba.unsa.etf.rma.zerina.rmaspirala2;

import java.util.ArrayList;

/**
 * Created by zerin on 4/5/2018.
 */

public class Autor {

    String ime;
    int brojKnjiga;
    boolean imaAutora;



    public Autor(String ime) {
        this.ime = ime;
        this.brojKnjiga = 1;
        this.imaAutora = false;

    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
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
