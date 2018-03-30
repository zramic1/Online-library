package ba.unsa.etf.rma.zerina.spirala1;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by zerin on 3/25/2018.
 */

public class Knjiga implements Serializable {


    String naziv, autor, kategorija;
    Bitmap slika;
    boolean oznacena;

    public Knjiga(Bitmap slika, String naziv, String autor, String kategorija) {
        this.slika = slika;
        this.naziv = naziv;
        this.autor = autor;
        this.kategorija = kategorija;
        this.oznacena = false;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getAutor() {
        return autor;
    }

    public String getKategorija() {
        return kategorija;
    }

    public Bitmap getSlika() {
        return slika;
    }

    public void setSlika(Bitmap slika) {
        this.slika = slika;
    }


    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public boolean isOznacena() {
        return oznacena;
    }

    public void setOznacena(boolean oznacena) {
        this.oznacena = oznacena;
    }

}
