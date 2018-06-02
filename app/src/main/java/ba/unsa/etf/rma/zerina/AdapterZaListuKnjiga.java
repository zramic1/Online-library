package ba.unsa.etf.rma.zerina;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static ba.unsa.etf.rma.zerina.BazaOpenHelper.AUTOR_IME;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KNJIGA_BROJ_STRANICA;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KNJIGA_DATUM_OBJAVLJIVANJA;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KNJIGA_ID;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KNJIGA_ID_KATEGORIJE;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KNJIGA_ID_WEB_SERVIS;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KNJIGA_NAZIV;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KNJIGA_OPIS;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KNJIGA_PREGLEDANA;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KNJIGA_SLIKA;


/**
 * Created by zerin on 5/14/2018.
 */

public class AdapterZaListuKnjiga extends BaseAdapter {

    public static String nazivKnjige = "";
    public static String imeAutora = "";
    public static Knjiga knjiga;


    Context c;
    ArrayList<Knjiga> knjigeZaIspis;

    public AdapterZaListuKnjiga(Context c, ArrayList<Knjiga> knjigeZaIspis) {
        this.c = c;
        this.knjigeZaIspis = knjigeZaIspis;
    }

    public Context getC() {
        return c;
    }

    public ArrayList<Knjiga> getKnjige() {
        return knjigeZaIspis;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public void setKnjige(ArrayList<Knjiga> knjige) {
        this.knjigeZaIspis = knjige;
    }






    @Override
    public int getCount() {
        return knjigeZaIspis.size();
    }

    @Override
    public Object getItem(int position) {
        return knjigeZaIspis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.element_liste_knjiga, null);

        ImageView eNaslovna = (ImageView) v.findViewById(R.id.eNaslovna);
        TextView eNaziv = (TextView) v.findViewById(R.id.eNaziv);
        TextView eAutor = (TextView) v.findViewById(R.id.eAutor);
        TextView eDatumObjavljivanja = (TextView) v.findViewById(R.id.eDatumObjavljivanja);
        TextView eBrojStranica = (TextView) v.findViewById(R.id.eBrojStranica);
        TextView eOpis = (TextView) v.findViewById(R.id.eOpis);
        Button dPreporuci = (Button) v.findViewById(R.id.dPreporuci);

        dPreporuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nazivKnjige = knjigeZaIspis.get(position).getNaziv();
                if(!knjigeZaIspis.get(position).isVrstaKnjige()){
                    ArrayList<Autor> autors = new ArrayList<Autor>();
                    autors = knjigeZaIspis.get(position).getAutori();
                    if(autors.size() > 0) imeAutora = autors.get(0).getImeiPrezime();
                }
                else imeAutora = knjigeZaIspis.get(position).getAutor();
                knjiga = knjigeZaIspis.get(position);

                UcitajKontakte();
            }
        });

        eNaziv.setText(knjigeZaIspis.get(position).getNaziv());
        if(knjigeZaIspis.get(position).isVrstaKnjige()) {
            eAutor.setText(knjigeZaIspis.get(position).getAutor());
        }
        else {
            ArrayList<Autor> autors = knjigeZaIspis.get(position).getAutori();

            StringBuilder builder = new StringBuilder();
            for (int i=0; i<autors.size(); i++) {
                builder.append(autors.get(i).getImeiPrezime() + "\n");
            }

            eAutor.setText(builder.toString());
        }
        if(knjigeZaIspis.get(position).isVrstaKnjige()) {
            eNaslovna.setImageBitmap(knjigeZaIspis.get(position).getSlika());
        }
        else{
            Picasso.with(c).load(knjigeZaIspis.get(position).getSlika1().toString()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .into(eNaslovna, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        if(!knjigeZaIspis.get(position).isVrstaKnjige()){
            eDatumObjavljivanja.setText(knjigeZaIspis.get(position).getDatumObjavljivanja());
        }
        else {
            eDatumObjavljivanja.setText("");
        }
        if(!knjigeZaIspis.get(position).isVrstaKnjige()){
            String s = String.valueOf(knjigeZaIspis.get(position).getBrojStrinica());
            eBrojStranica.setText(s);
        }
        else {
            eBrojStranica.setText("");
        }
        if(!knjigeZaIspis.get(position).isVrstaKnjige()){
            eOpis.setText(knjigeZaIspis.get(position).getOpis());
        }
        else {
            eOpis.setText("");
        }

        if(knjigeZaIspis.get(position).isOznacena())
            v.setBackgroundResource(R.color.bojaZaElementListe);

        return v;
    }

    public void UcitajKontakte(){

        ArrayList<Kontakt> kontakti = new ArrayList<Kontakt>();
        Cursor kursor = null;
        String ime = "";
        String email = "";
        String brojTelefona = "";
        String id = "";

        try{
            kursor = KategorijeAkt.contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }

        if(kursor.getCount() > 0) {

            while (kursor.moveToNext()) {
                id = kursor.getString(kursor.getColumnIndex(ContactsContract.Contacts._ID));
                ime = kursor.getString(kursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Cursor kursorTelefon = KategorijeAkt.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                        , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                while (kursorTelefon.moveToNext()) {
                    brojTelefona = kursorTelefon.getString(kursorTelefon.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                kursorTelefon.close();

                Cursor emailKursor = KategorijeAkt.contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);

                while (emailKursor.moveToNext()) {
                    email = emailKursor.getString(emailKursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emailKursor.close();

                Kontakt k = new Kontakt(ime, email, brojTelefona);
                kontakti.add(k);

                id = "";
                ime = "";
                email = "";
                brojTelefona = "";

            }


            kursor.close();
        }


            FragmentPreporuci fp = new FragmentPreporuci();
            Bundle b = new Bundle();
            b.putParcelableArrayList("kontakti", kontakti);
            fp.setArguments(b);

            if (KategorijeAkt.siri == false)
                KategorijeAkt.fm.beginTransaction().replace(R.id.mjesto1, fp).commit();
            else {
                KategorijeAkt.m1.setVisibility(View.GONE);
                KategorijeAkt.m2.setVisibility(View.GONE);
                KategorijeAkt.m3.setVisibility(View.VISIBLE);
                KategorijeAkt.fm.beginTransaction().replace(R.id.mjesto3, fp).commit();
            }
        }

}




