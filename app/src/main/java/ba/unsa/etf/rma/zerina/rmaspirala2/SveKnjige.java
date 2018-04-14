package ba.unsa.etf.rma.zerina.rmaspirala2;

import java.util.ArrayList;

/**
 * Created by zerin on 4/4/2018.
 */

public class SveKnjige {


    ArrayList<Knjiga> sveKnjige;

    public SveKnjige() {

        sveKnjige = new ArrayList<Knjiga>();

    }

    public ArrayList<Knjiga> getSveKnjige() {
        return sveKnjige;
    }

    public void setSveKnjige(ArrayList<Knjiga> sveKnjige) {
        this.sveKnjige = sveKnjige;
    }

    public void dodajKnjigu(Knjiga kk){
        sveKnjige.add(kk);
    }

    public int brojElemenata(){
        return sveKnjige.size();
    }

    public Knjiga vratiKnjigu(int i){
        return sveKnjige.get(i);
    }

}
