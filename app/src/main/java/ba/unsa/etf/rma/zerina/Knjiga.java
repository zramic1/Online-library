package ba.unsa.etf.rma.zerina;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by zerin on 5/13/2018.
 */

public class Knjiga implements Parcelable{


    String id;
    String naziv;
    ArrayList<Autor> autori;
    String opis;
    String datumObjavljivanja;
    URL  slika;
    int brojStrinica;

    Bitmap slika1;
    boolean oznacena;
    String autor;
    String kategorija;
    boolean vrstaKnjige;

    public Knjiga(Bitmap slika, String naziv, String autor, String kategorija) {
        this.slika1 = slika;
        this.naziv = naziv;
        this.autor = autor;
        this.kategorija = kategorija;
        this.oznacena = false;
        this.vrstaKnjige = true;
    }

    public Knjiga(URL slika, String naziv, String autor, String kategorija) {
        this.slika = slika;
        this.naziv = naziv;
        this.autor = autor;
        this.kategorija = kategorija;
        this.oznacena = false;
        this.vrstaKnjige = true;
    }

    protected Knjiga(Parcel in) {
        id = in.readString();
        naziv = in.readString();
        opis = in.readString();
        datumObjavljivanja = in.readString();
        brojStrinica = in.readInt();
        slika1 = in.readParcelable(Bitmap.class.getClassLoader());
        oznacena = in.readByte() != 0;
        autor = in.readString();
        kategorija = in.readString();
        vrstaKnjige = in.readByte() != 0;
    }

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            return new Knjiga(in);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };

    public String getKategorija() {
        return kategorija;
    }

    public Bitmap getSlika() {
        return slika1;
    }

    public void setSlika(Bitmap slika) {
        this.slika1 = slika;
    }

    public String getAutor(){ return autor; }

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

    public boolean isVrstaKnjige() {
        return vrstaKnjige;
    }


    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStrinica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStrinica = brojStrinica;
        this.vrstaKnjige = false;
        this.oznacena = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlika1() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStrinica() {
        return brojStrinica;
    }

    public void setBrojStrinica(int brojStrinica) {
        this.brojStrinica = brojStrinica;
    }

    public String izdvojiGodinu(){
        return datumObjavljivanja.substring(0, 4);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(naziv);
        dest.writeString(opis);
        dest.writeString(datumObjavljivanja);
        dest.writeInt(brojStrinica);
        dest.writeParcelable(slika1, flags);
        dest.writeByte((byte) (oznacena ? 1 : 0));
        dest.writeString(autor);
        dest.writeString(kategorija);
        dest.writeByte((byte) (vrstaKnjige ? 1 : 0));
    }



}
