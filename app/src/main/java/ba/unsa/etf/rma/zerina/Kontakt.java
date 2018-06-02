package ba.unsa.etf.rma.zerina;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zerin on 5/23/2018.
 */

public class Kontakt implements Parcelable {

    String ime;
    String email;
    String broj;


    public Kontakt(String ime, String email, String broj) {
        this.ime = ime;
        this.email = email;
        this.broj = broj;
    }

    protected Kontakt(Parcel in) {
        ime = in.readString();
        email = in.readString();
        broj = in.readString();
    }

    public static final Creator<Kontakt> CREATOR = new Creator<Kontakt>() {
        @Override
        public Kontakt createFromParcel(Parcel in) {
            return new Kontakt(in);
        }

        @Override
        public Kontakt[] newArray(int size) {
            return new Kontakt[size];
        }
    };

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ime);
        dest.writeString(email);
        dest.writeString(broj);
    }
}
