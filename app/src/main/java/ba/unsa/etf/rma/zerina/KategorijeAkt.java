package ba.unsa.etf.rma.zerina;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.stetho.Stetho;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static ba.unsa.etf.rma.zerina.BazaOpenHelper.AUTORSTVO_ID;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.AUTORSTVO_IDAUTORA;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.AUTORSTVO_IDKNJIGE;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.AUTOR_ID;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.AUTOR_IME;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.DATABASE_TABLE;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.DATABASE_TABLE1;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.DATABASE_TABLE2;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.DATABASE_TABLE3;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KATEGORIJA_ID;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.KATEGORIJA_NAZIV;
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

public class KategorijeAkt extends AppCompatActivity {

    public static boolean siri = false;
    public static FragmentManager fm;
    public static ContentResolver contentResolver;
    public static FrameLayout m1, m2, m3;
    public static BazaOpenHelper baza;
    int idAutora;
    String imeAutora = "";
    String nazivKategorije = "";
    int idKnjige;
    ArrayList<Integer> AutoroviID = new ArrayList<Integer>();
    ArrayList<String> imenaAutora = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategorije_akt);

        baza = new BazaOpenHelper(this);
        Stetho.initializeWithDefaults(this);

        SQLiteDatabase database = baza.getWritableDatabase();
        //baza.onUpgrade(database, 0, 0);

        ListeFragment.lista.clear();

        SQLiteDatabase db = KategorijeAkt.baza.getReadableDatabase();
        Cursor kurs = db.rawQuery("select * from " + DATABASE_TABLE, null);
        int indexKoloneNaziv = kurs.getColumnIndexOrThrow(BazaOpenHelper.KATEGORIJA_NAZIV);
        while(kurs.moveToNext()) {
            ListeFragment.lista.add(kurs.getString(indexKoloneNaziv));
        }

        ListeFragment.listaKnjiga.ocisti();

        ArrayList<String>naziviKnjiga = new ArrayList<String>();

        Cursor kursorKnjige = db.rawQuery("select * from " + DATABASE_TABLE1, null);
        int indexKolonaNaziv = kursorKnjige.getColumnIndexOrThrow(KNJIGA_NAZIV);
        while(kursorKnjige.moveToNext()){
            naziviKnjiga.add(kursorKnjige.getString(indexKolonaNaziv));
        }



        Cursor kursorKnjige1 = db.rawQuery("select * from " + DATABASE_TABLE1, null);
        ArrayList<String>opisi = new ArrayList<String>();
        int indexKolonaOpis = kursorKnjige1.getColumnIndexOrThrow(KNJIGA_OPIS);
        while(kursorKnjige1.moveToNext()){
            opisi.add(kursorKnjige1.getString(indexKolonaOpis));
        }



        Cursor kursorKnjige2 = db.rawQuery("select * from " + DATABASE_TABLE1, null);
        ArrayList<String>datumiObjavljivanja = new ArrayList<String>();
        int indexKolonaDatumObjavljivanja = kursorKnjige2.getColumnIndexOrThrow(KNJIGA_DATUM_OBJAVLJIVANJA);
        while(kursorKnjige2.moveToNext()){
            if(kursorKnjige2.getString(indexKolonaDatumObjavljivanja).isEmpty()){
                datumiObjavljivanja.add("");
            }
            else datumiObjavljivanja.add(kursorKnjige2.getString(indexKolonaDatumObjavljivanja));
        }


        Cursor kursorKnjige3 = db.rawQuery("select * from " + DATABASE_TABLE1, null);
        ArrayList<Integer>brojStranica = new ArrayList<Integer>();
        int indexKolonaBrojStranica = kursorKnjige3.getColumnIndexOrThrow(KNJIGA_BROJ_STRANICA);
        while(kursorKnjige3.moveToNext()){
            brojStranica.add(Integer.valueOf(kursorKnjige3.getString(indexKolonaBrojStranica)));
        }

        Cursor kursorKnjige4 = db.rawQuery("select * from " + DATABASE_TABLE1, null);
        ArrayList<Integer>idKategorije = new ArrayList<Integer>();
        int indexKolonaIDKategorije = kursorKnjige4.getColumnIndexOrThrow(KNJIGA_ID_KATEGORIJE);
        while(kursorKnjige4.moveToNext()){
            idKategorije.add(Integer.valueOf(kursorKnjige4.getString(indexKolonaIDKategorije)));
        }


        Cursor kursorKnjige5 = db.rawQuery("select * from " + DATABASE_TABLE1, null);
        ArrayList<String>slike = new ArrayList<String>();
        int indexKolonaSlika = kursorKnjige5.getColumnIndexOrThrow(KNJIGA_SLIKA);
        while(kursorKnjige5.moveToNext()){
            slike.add(kursorKnjige5.getString(indexKolonaSlika));
        }


        Cursor kursorKnjige6 = db.rawQuery("select * from " + DATABASE_TABLE1, null);
        ArrayList<Integer>pogledane = new ArrayList<Integer>();
        int indexKolonaPogledana = kursorKnjige6.getColumnIndexOrThrow(KNJIGA_PREGLEDANA);
        while(kursorKnjige6.moveToNext()){
            pogledane.add(Integer.valueOf(kursorKnjige6.getString(indexKolonaPogledana)));
        }


        Cursor kursorKnjige7 = db.rawQuery("select * from " + DATABASE_TABLE1, null);
        ArrayList<String>webServisi = new ArrayList<String>();
        int indexKolonaWebServis = kursorKnjige7.getColumnIndexOrThrow(KNJIGA_ID_WEB_SERVIS);
        while(kursorKnjige7.moveToNext()){
            webServisi.add(kursorKnjige7.getString(indexKolonaWebServis));
        }

        Cursor kursorKnjige8 = db.rawQuery("select * from " + DATABASE_TABLE1, null);
        ArrayList<Integer>IDKnjiga = new ArrayList<Integer>();
        int indexKolonaIDKnjiga = kursorKnjige8.getColumnIndexOrThrow(KNJIGA_ID);
        while(kursorKnjige8.moveToNext()){
            IDKnjiga.add(Integer.valueOf(kursorKnjige8.getString(indexKolonaIDKnjiga)));
        }


       for(int i=0; i<naziviKnjiga.size(); i++){
            if(webServisi.get(i).equals("")){
                Cursor kursor = db.rawQuery("select * from " + DATABASE_TABLE3, null);
                int index = kursor.getColumnIndexOrThrow(AUTORSTVO_IDKNJIGE);
                while (kursor.moveToNext()){


                   Cursor kk = db.rawQuery("select * from " + DATABASE_TABLE1, null);
                   int ii = kk.getColumnIndexOrThrow(KNJIGA_NAZIV);
                   while (kk.moveToNext()){
                       if(naziviKnjiga.get(i).equals(kk.getString(ii))){
                           int iii = kk.getColumnIndexOrThrow(KNJIGA_ID);
                           idKnjige = kk.getInt(iii);
                       }
                   }

                    if(idKnjige == kursor.getInt(index)){
                        int indexAutor = kursor.getColumnIndexOrThrow(AUTORSTVO_IDAUTORA);
                        idAutora = kursor.getInt(indexAutor);
                    }



                }

                Cursor kursorZaAutora = db.rawQuery("select * from " + DATABASE_TABLE2, null);
                int indexZaAutora = kursorZaAutora.getColumnIndexOrThrow(AUTOR_ID);
                while (kursorZaAutora.moveToNext()){
                    if(kursorZaAutora.getInt(indexZaAutora) == idAutora){
                        int indexAutor1 = kursorZaAutora.getColumnIndexOrThrow(AUTOR_IME);
                        imeAutora = kursorZaAutora.getString(indexAutor1);
                    }
                }

                Cursor ku = db.rawQuery("select * from " + DATABASE_TABLE, null);
                int indexKolona = ku.getColumnIndexOrThrow(KATEGORIJA_ID);
                while (ku.moveToNext()){
                    if(idKategorije.get(i).equals(ku.getInt(indexKolona))){
                        int indexNazivKat = ku.getColumnIndexOrThrow(KATEGORIJA_NAZIV);
                        nazivKategorije = ku.getString(indexNazivKat);
                    }
                }

                Bitmap bitmap = baza.Base64ToBitmap(slike.get(i));

                Knjiga k = new Knjiga(bitmap, naziviKnjiga.get(i), imeAutora, nazivKategorije);
                if(pogledane.get(i) == 1){
                    k.setOznacena(true);
                }
                else k.setOznacena(false);
                ListeFragment.listaKnjiga.dodajKnjigu(k);
            }
            else{

                Cursor kursor = db.rawQuery("select * from " + DATABASE_TABLE3, null);
                int index = kursor.getColumnIndexOrThrow(AUTORSTVO_IDKNJIGE);

                AutoroviID.clear();

                while (kursor.moveToNext()){
                    Cursor kk = db.rawQuery("select * from " + DATABASE_TABLE1, null);
                    int ii = kk.getColumnIndexOrThrow(KNJIGA_NAZIV);
                    while (kk.moveToNext()){
                        if(naziviKnjiga.get(i).equals(kk.getString(ii))){
                            int iii = kk.getColumnIndexOrThrow(KNJIGA_ID);
                            idKnjige = kk.getInt(iii);
                        }
                    }

                    if(idKnjige == kursor.getInt(index)){
                        int indexAutor = kursor.getColumnIndexOrThrow(AUTORSTVO_IDAUTORA);
                        idAutora = kursor.getInt(indexAutor);
                        AutoroviID.add(idAutora);
                    }
                }

                imenaAutora.clear();

                Cursor kursorZaAutora = db.rawQuery("select * from " + DATABASE_TABLE2, null);
                int indexZaAutora = kursorZaAutora.getColumnIndexOrThrow(AUTOR_ID);
                while (kursorZaAutora.moveToNext()){
                    for(int l=0; l<AutoroviID.size(); l++) {
                        if (kursorZaAutora.getInt(indexZaAutora) == AutoroviID.get(l)) {
                            int indexAutor1 = kursorZaAutora.getColumnIndexOrThrow(AUTOR_IME);
                            imeAutora = kursorZaAutora.getString(indexAutor1);
                            imenaAutora.add(imeAutora);
                        }
                    }
                }

                Cursor ku = db.rawQuery("select * from " + DATABASE_TABLE, null);
                int indexKolona = ku.getColumnIndexOrThrow(KATEGORIJA_ID);
                while (ku.moveToNext()){
                    if(idKategorije.get(i).equals(ku.getInt(indexKolona))){
                        int indexNazivKat = ku.getColumnIndexOrThrow(KATEGORIJA_NAZIV);
                        nazivKategorije = ku.getString(indexNazivKat);
                    }
                }

            }

            ArrayList<Autor> autoriZaSlanje = new ArrayList<Autor>();
            for(int m=0; m<imenaAutora.size(); m++){
                Autor aa = new Autor(imenaAutora.get(m));
                autoriZaSlanje.add(aa);
            }

           URL myURL = null;
           try {
               myURL = new URL(slike.get(i));
           } catch (MalformedURLException e) {
               e.printStackTrace();
           }

           Knjiga kk = new Knjiga(webServisi.get(i), naziviKnjiga.get(i), autoriZaSlanje, opisi.get(i), datumiObjavljivanja.get(i), myURL, brojStranica.get(i));
           kk.setKategorija(nazivKategorije);
           if(pogledane.get(i) == 1){
               kk.setOznacena(true);
           }
           else kk.setOznacena(false);
           ListeFragment.listaKnjiga.dodajKnjigu(kk);
        }





        ListeFragment.autori.clear();

        Cursor kursor = db.rawQuery("select * from " + DATABASE_TABLE2, null);
        int index = kursor.getColumnIndexOrThrow(AUTOR_IME);
        if(kursor.getCount() > 0) {
            while (kursor.moveToNext()) {
                String ime = kursor.getString(index);
                Autor a = new Autor(ime);
                a.vratiNaNulu();
                ListeFragment.autori.add(a);
            }
        }


        fm = getFragmentManager();
        contentResolver = getContentResolver();
        FrameLayout fragmentKnjige = (FrameLayout) findViewById(R.id.mjesto2);
        FrameLayout fragmentDodavanjeKnjige = (FrameLayout) findViewById(R.id.mjesto3);
        m1 = (FrameLayout) findViewById(R.id.mjesto1);
        m2 = fragmentKnjige;
        m3 = fragmentDodavanjeKnjige;

        if(fragmentKnjige != null){
            siri = true;
            FragmentTransaction ft = fm.beginTransaction();
            KnjigeFragment fk = new KnjigeFragment();
            fragmentDodavanjeKnjige.setVisibility(View.INVISIBLE);

            ft.replace(R.id.mjesto2, fk);

            ListeFragment fl = new ListeFragment();
            ft.replace(R.id.mjesto1, fl, "lista");
            ft.commit();
        }

        else{
            siri = false;
            ListeFragment fragmentLista = (ListeFragment) fm.findFragmentByTag("lista");
            if(fragmentLista == null){
                fragmentLista = new ListeFragment();
                fm.beginTransaction().replace(R.id.mjesto1, fragmentLista, "lista").commit();
            }
            else {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }

    }
}
