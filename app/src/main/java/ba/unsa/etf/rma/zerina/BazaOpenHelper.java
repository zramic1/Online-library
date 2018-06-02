package ba.unsa.etf.rma.zerina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by zerin on 5/25/2018.
 */

public class BazaOpenHelper extends SQLiteOpenHelper {

    int idAutora;
    String nazivKategorije = "";
    ArrayList<Integer>IDAutora = new ArrayList<Integer>();
    int idKnjige;
    String imeAutora = "";
    ArrayList<Integer> AutoroviID = new ArrayList<Integer>();
    ArrayList<String> imenaAutora = new ArrayList<String>();

    public static final String DATABASE_NAME = "mojaBaza.db";
    public static final String DATABASE_TABLE = "Kategorija";
    public static final int DATABASE_VERSION = 1;
    public static final String KATEGORIJA_ID = "_id";
    public static final String KATEGORIJA_NAZIV = "naziv";

    private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KATEGORIJA_ID + " integer primary key autoincrement, " +
            KATEGORIJA_NAZIV + " text not null);";

    public static final String DATABASE_TABLE1 = "Knjiga";
    public static final String KNJIGA_ID = "_id";
    public static final String KNJIGA_NAZIV = "naziv";
    public static final String KNJIGA_OPIS = "opis";
    public static final String KNJIGA_DATUM_OBJAVLJIVANJA = "datumObjavljivanja";
    public static final String KNJIGA_BROJ_STRANICA = "brojStranica";
    public static final String KNJIGA_ID_WEB_SERVIS = "idWebServis";
    public static final String KNJIGA_ID_KATEGORIJE = "idkategorije";
    public static final String KNJIGA_SLIKA = "slika";
    public static final String KNJIGA_PREGLEDANA = "pregledana";

    private static final String DATABASE_CREATE1 = "create table " + DATABASE_TABLE1 + " (" + KNJIGA_ID + " integer primary key autoincrement, " +
            KNJIGA_NAZIV + " text not null, " + KNJIGA_OPIS + " text not null, " + KNJIGA_DATUM_OBJAVLJIVANJA + " text not null, " +
            KNJIGA_BROJ_STRANICA + " integer, " + KNJIGA_ID_WEB_SERVIS + " text not null, " + KNJIGA_ID_KATEGORIJE + " integer, " +
            KNJIGA_SLIKA + " text not null, " + KNJIGA_PREGLEDANA + " integer);";

    public static final String DATABASE_TABLE2 = "Autor";
    public static final String AUTOR_ID = "_id";
    public static final String AUTOR_IME = "ime";

    private static final String DATABASE_CREATE2 = "create table " + DATABASE_TABLE2 + " (" + AUTOR_ID + " integer primary key autoincrement, " +
            AUTOR_IME + " text not null);";

    public static final String DATABASE_TABLE3 = "Autorstvo";
    public static final String AUTORSTVO_ID = "_id";
    public static final String AUTORSTVO_IDAUTORA = "idautora";
    public static final String AUTORSTVO_IDKNJIGE = "idknjige";

    private static final String DATABASE_CREATE3 = "create table " + DATABASE_TABLE3 + " (" + AUTORSTVO_ID + " integer primary key autoincrement, " +
            AUTORSTVO_IDAUTORA + " integer, " + AUTORSTVO_IDKNJIGE + " integer);";


    public BazaOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase baza = getWritableDatabase();
        SQLiteDatabase baza1 = getReadableDatabase();
    }

    public BazaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public long dodajKategoriju(String naziv) {

        boolean postoji = false;

        ContentValues noveVrijednosti = new ContentValues();
        noveVrijednosti.put(KATEGORIJA_NAZIV, naziv);
        SQLiteDatabase db = KategorijeAkt.baza.getWritableDatabase();

        SQLiteDatabase db1 = KategorijeAkt.baza.getReadableDatabase();
        Cursor kursor = db1.rawQuery("select * from " + DATABASE_TABLE, null);
        int indexKoloneNaziv = kursor.getColumnIndexOrThrow(BazaOpenHelper.KATEGORIJA_NAZIV);
        while (kursor.moveToNext()){
            if(kursor.getString(indexKoloneNaziv).equals(naziv)) postoji = true;
        }

        kursor.close();
        if (postoji == false){
            return db.insert(DATABASE_TABLE, null, noveVrijednosti);
        }
        return -1;
    }

    public long dodajKnjigu(Knjiga knjiga){
        ContentValues noveVrijednosti = new ContentValues();
        if(knjiga.isVrstaKnjige()){
            noveVrijednosti.put(KNJIGA_NAZIV, knjiga.getNaziv());
            noveVrijednosti.put(KNJIGA_OPIS, "");
            noveVrijednosti.put(KNJIGA_DATUM_OBJAVLJIVANJA, "");
            noveVrijednosti.put(KNJIGA_BROJ_STRANICA, 0);
            noveVrijednosti.put(KNJIGA_ID_WEB_SERVIS, "");
            SQLiteDatabase db = KategorijeAkt.baza.getReadableDatabase();
            String[] koloneRezultat1 = {BazaOpenHelper.KATEGORIJA_ID, BazaOpenHelper.KATEGORIJA_NAZIV};
            String where1 = BazaOpenHelper.KATEGORIJA_NAZIV + "=\'" + knjiga.getKategorija() + "\'";
            Cursor kursor = db.query(DATABASE_TABLE, koloneRezultat1, where1, null, null, null, null);
            int indexKoloneID = kursor.getColumnIndexOrThrow(BazaOpenHelper.KATEGORIJA_ID);
            while (kursor.moveToNext()){
                noveVrijednosti.put(KNJIGA_ID_KATEGORIJE, kursor.getString(indexKoloneID));
            }
            boolean prazno = false;
            if(knjiga.getSlika().equals(null)){
                prazno = true;
            }
            if(!prazno) {
                String slika = BitmapToBase64(knjiga.getSlika());
                noveVrijednosti.put(KNJIGA_SLIKA, slika);
            }
            int pregledana = 0;
            if(knjiga.isOznacena() == true) pregledana = 1;
            noveVrijednosti.put(KNJIGA_PREGLEDANA, pregledana);


            boolean ima = false;

            for(int i=0; i<ListeFragment.autori.size(); i++){
                if(ListeFragment.autori.get(i).getImeiPrezime().equalsIgnoreCase(knjiga.getAutor())){
                    ima = true;
                    String rezultat[] = {AUTOR_ID, AUTOR_IME};
                    String where = AUTOR_IME + "=\'" + ListeFragment.autori.get(i).getImeiPrezime() + "\'";
                    Cursor kurs = db.query(DATABASE_TABLE2, rezultat, where, null, null, null, null);
                    int indexKolone= kurs.getColumnIndexOrThrow(BazaOpenHelper.AUTOR_ID);
                    while (kurs.moveToNext()){
                        idAutora = Integer.valueOf(kurs.getString(indexKolone));
                    }

                }
            }
            if(ima == false){
                Autor a = new Autor(knjiga.getAutor());
                ContentValues vrijednost = new ContentValues();
                vrijednost.put(AUTOR_IME, a.getImeiPrezime());
                SQLiteDatabase db1 = KategorijeAkt.baza.getWritableDatabase();
                idAutora = Integer.valueOf(String.valueOf(db1.insert(DATABASE_TABLE2, null, vrijednost)));
                ListeFragment.autori.add(a);
            }

            int idKnjige = Integer.valueOf(String.valueOf(db.insert(DATABASE_TABLE1, null, noveVrijednosti)));
            ContentValues vrijednost = new ContentValues();
            vrijednost.put(AUTORSTVO_IDKNJIGE, idKnjige);
            vrijednost.put(AUTORSTVO_IDAUTORA, idAutora);
            SQLiteDatabase db2 = KategorijeAkt.baza.getWritableDatabase();
            db2.insert(DATABASE_TABLE3, null, vrijednost);

            return  Long.valueOf(String.valueOf(idKnjige));
        }
        else{
            IDAutora.clear();
            noveVrijednosti.put(KNJIGA_NAZIV, knjiga.getNaziv());
            noveVrijednosti.put(KNJIGA_OPIS, knjiga.getOpis());
            noveVrijednosti.put(KNJIGA_DATUM_OBJAVLJIVANJA, knjiga.getDatumObjavljivanja());
            noveVrijednosti.put(KNJIGA_BROJ_STRANICA, knjiga.getBrojStrinica());
            noveVrijednosti.put(KNJIGA_ID_WEB_SERVIS, knjiga.getId());
            SQLiteDatabase db1 = KategorijeAkt.baza.getReadableDatabase();
            String[] koloneRezultat11 = {BazaOpenHelper.KATEGORIJA_ID, BazaOpenHelper.KATEGORIJA_NAZIV};
            String where11 = BazaOpenHelper.KATEGORIJA_NAZIV + "=\'" + knjiga.getKategorija() + "\'";
            Cursor kursor1 = db1.query(DATABASE_TABLE, koloneRezultat11, where11, null, null, null, null);
            int indexKoloneID1 = kursor1.getColumnIndexOrThrow(BazaOpenHelper.KATEGORIJA_ID);
            while (kursor1.moveToNext()){
                noveVrijednosti.put(KNJIGA_ID_KATEGORIJE, kursor1.getString(indexKoloneID1));
            }
            boolean prazno = false;
            if(knjiga.getSlika1().equals(null)){
                prazno = true;
            }
            if(!prazno) {
                noveVrijednosti.put(KNJIGA_SLIKA, knjiga.getSlika1().toString());
            }
            int pregledana = 0;
            if(knjiga.isOznacena() == true) pregledana = 1;
            noveVrijednosti.put(KNJIGA_PREGLEDANA, pregledana);

            ArrayList<Autor> autors = knjiga.getAutori();
            for(int j=0; j<autors.size();j++){
                boolean autorPostoji2 = false;
                for (int k=0; k<ListeFragment.autori.size(); k++){
                    if(autors.get(j).getImeiPrezime().toUpperCase().equals(ListeFragment.autori.get(k).getImeiPrezime().toUpperCase())){
                        ListeFragment.autori.get(k).setImaAutora(true);

                        autorPostoji2 = true;
                        String rezultat[] = {AUTOR_ID, AUTOR_IME};
                        SQLiteDatabase db5 = KategorijeAkt.baza.getWritableDatabase();
                        String where = AUTOR_IME + "=\'" + ListeFragment.autori.get(k).getImeiPrezime() + "\'";
                        Cursor kurs = db5.query(DATABASE_TABLE2, rezultat, where, null, null, null, null);
                        int indexKolone= kurs.getColumnIndexOrThrow(BazaOpenHelper.AUTOR_ID);
                        while (kurs.moveToNext()){
                            idAutora = Integer.valueOf(kurs.getString(indexKolone));
                            IDAutora.add(idAutora);
                        }
                        kurs.close();
                    }
                }
                if(autorPostoji2 == false) {
                    Autor aa = new Autor(autors.get(j).getImeiPrezime());
                    ContentValues vrijednost = new ContentValues();
                    vrijednost.put(AUTOR_IME, aa.getImeiPrezime());
                    SQLiteDatabase db = KategorijeAkt.baza.getWritableDatabase();
                    idAutora = Integer.valueOf(String.valueOf(db.insert(DATABASE_TABLE2, null, vrijednost)));
                    IDAutora.add(idAutora);
                    ListeFragment.autori.add(aa);
                }
            }


            int idKnjige = Integer.valueOf(String.valueOf(db1.insert(DATABASE_TABLE1, null, noveVrijednosti)));

            ContentValues vrijednost = new ContentValues();
            vrijednost.put(AUTORSTVO_IDKNJIGE, idKnjige);
            for (int m=0; m<IDAutora.size(); m++) {
                vrijednost.put(AUTORSTVO_IDAUTORA, IDAutora.get(m));
                SQLiteDatabase db2 = KategorijeAkt.baza.getWritableDatabase();
                db2.insert(DATABASE_TABLE3, null, vrijednost);
            }
            return Long.valueOf(String.valueOf(idKnjige));
        }

    }




    public ArrayList<Knjiga> knjigeKategorije (long idKategorije) throws MalformedURLException {
        SQLiteDatabase db = KategorijeAkt.baza.getReadableDatabase();
        ArrayList<Knjiga> knjigeZaVratiti = new ArrayList<Knjiga>();

        Cursor zaKnjige = db.rawQuery("select * from " + DATABASE_TABLE1, null);
        int indexZaKnjigu = zaKnjige.getColumnIndexOrThrow(KNJIGA_ID_KATEGORIJE);
        ArrayList<String> naziviKnjiga = new ArrayList<String>();
        while (zaKnjige.moveToNext()){
            if(Integer.valueOf(String.valueOf(idKategorije)) == zaKnjige.getInt(indexZaKnjigu)){
                int indexZaNazivKnjige = zaKnjige.getColumnIndexOrThrow(KNJIGA_NAZIV);
                naziviKnjiga.add(zaKnjige.getString(indexZaNazivKnjige));
            }
        }

        String rezultat [] = {KNJIGA_ID, KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_DATUM_OBJAVLJIVANJA, KNJIGA_BROJ_STRANICA, KNJIGA_ID_WEB_SERVIS, KNJIGA_ID_KATEGORIJE, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};
        String where = KNJIGA_ID_KATEGORIJE+ "=\'" + idKategorije + "\'";
        Cursor kursorKnjige1 = db.query(DATABASE_TABLE1, rezultat, where, null, null, null, null);
        ArrayList<String>opisi = new ArrayList<String>();
        int indexKolonaOpis = kursorKnjige1.getColumnIndexOrThrow(KNJIGA_OPIS);
        while(kursorKnjige1.moveToNext()){
            opisi.add(kursorKnjige1.getString(indexKolonaOpis));
        }




        Cursor kursorKnjige2 = db.query(DATABASE_TABLE1, rezultat, where, null, null, null, null);
        ArrayList<String>datumiObjavljivanja = new ArrayList<String>();
        int indexKolonaDatumObjavljivanja = kursorKnjige2.getColumnIndexOrThrow(KNJIGA_DATUM_OBJAVLJIVANJA);
        while(kursorKnjige2.moveToNext()){
            if(kursorKnjige2.getString(indexKolonaDatumObjavljivanja).isEmpty()){
                datumiObjavljivanja.add("");
            }
            else datumiObjavljivanja.add(kursorKnjige2.getString(indexKolonaDatumObjavljivanja));
        }


        Cursor kursorKnjige3 = db.query(DATABASE_TABLE1, rezultat, where, null, null, null, null);
        ArrayList<Integer>brojStranica = new ArrayList<Integer>();
        int indexKolonaBrojStranica = kursorKnjige3.getColumnIndexOrThrow(KNJIGA_BROJ_STRANICA);
        while(kursorKnjige3.moveToNext()){
            brojStranica.add(Integer.valueOf(kursorKnjige3.getString(indexKolonaBrojStranica)));
        }

        Cursor kursorKnjige4 = db.query(DATABASE_TABLE1, rezultat, where, null, null, null, null);
        ArrayList<Integer> idKategorije1 = new ArrayList<Integer>();
        int indexKolonaIDKategorije = kursorKnjige4.getColumnIndexOrThrow(KNJIGA_ID_KATEGORIJE);
        while(kursorKnjige4.moveToNext()){
            idKategorije1.add(Integer.valueOf(kursorKnjige4.getString(indexKolonaIDKategorije)));
        }


        Cursor kursorKnjige5 = db.query(DATABASE_TABLE1, rezultat, where, null, null, null, null);
        ArrayList<String>slike = new ArrayList<String>();
        int indexKolonaSlika = kursorKnjige5.getColumnIndexOrThrow(KNJIGA_SLIKA);
        while(kursorKnjige5.moveToNext()){
            slike.add(kursorKnjige5.getString(indexKolonaSlika));
        }


        Cursor kursorKnjige6 = db.query(DATABASE_TABLE1, rezultat, where, null, null, null, null);
        ArrayList<Integer>pogledane = new ArrayList<Integer>();
        int indexKolonaPogledana = kursorKnjige6.getColumnIndexOrThrow(KNJIGA_PREGLEDANA);
        while(kursorKnjige6.moveToNext()){
            pogledane.add(Integer.valueOf(kursorKnjige6.getString(indexKolonaPogledana)));
        }


        Cursor kursorKnjige7 = db.query(DATABASE_TABLE1, rezultat, where, null, null, null, null);
        ArrayList<String>webServisi = new ArrayList<String>();
        int indexKolonaWebServis = kursorKnjige7.getColumnIndexOrThrow(KNJIGA_ID_WEB_SERVIS);
        while(kursorKnjige7.moveToNext()){
            webServisi.add(kursorKnjige7.getString(indexKolonaWebServis));
        }

        Cursor kursorKnjige8 = db.query(DATABASE_TABLE1, rezultat, where, null, null, null, null);
        ArrayList<Integer>IDKnjiga = new ArrayList<Integer>();
        int indexKolonaIDKnjiga = kursorKnjige8.getColumnIndexOrThrow(KNJIGA_ID);
        while(kursorKnjige8.moveToNext()){
            IDKnjiga.add(Integer.valueOf(kursorKnjige8.getString(indexKolonaIDKnjiga)));
        }


        for(int i=0; i<naziviKnjiga.size(); i++){

                if (webServisi.get(i).equals("")) {
                    continue;
                } else {

                    Cursor kursor = db.rawQuery("select * from " + DATABASE_TABLE3, null);
                    int index = kursor.getColumnIndexOrThrow(AUTORSTVO_IDKNJIGE);

                    AutoroviID.clear();

                    while (kursor.moveToNext()) {
                        Cursor kk = db.rawQuery("select * from " + DATABASE_TABLE1, null);
                        int ii = kk.getColumnIndexOrThrow(KNJIGA_NAZIV);
                        while (kk.moveToNext()) {
                            if (naziviKnjiga.get(i).equals(kk.getString(ii))) {
                                int iii = kk.getColumnIndexOrThrow(KNJIGA_ID);
                                idKnjige = kk.getInt(iii);
                            }
                        }

                        if (idKnjige == kursor.getInt(index)) {
                            int indexAutor = kursor.getColumnIndexOrThrow(AUTORSTVO_IDAUTORA);
                            idAutora = kursor.getInt(indexAutor);
                            AutoroviID.add(idAutora);
                        }
                    }

                    imenaAutora.clear();

                    Cursor kursorZaAutora = db.rawQuery("select * from " + DATABASE_TABLE2, null);
                    int indexZaAutora = kursorZaAutora.getColumnIndexOrThrow(AUTOR_ID);
                    while (kursorZaAutora.moveToNext()) {
                        for (int l = 0; l < AutoroviID.size(); l++) {
                            if (kursorZaAutora.getInt(indexZaAutora) == AutoroviID.get(l)) {
                                int indexAutor1 = kursorZaAutora.getColumnIndexOrThrow(AUTOR_IME);
                                imeAutora = kursorZaAutora.getString(indexAutor1);
                                imenaAutora.add(imeAutora);
                            }
                        }
                    }

                    Cursor ku = db.rawQuery("select * from " + DATABASE_TABLE, null);
                    int indexKolona = ku.getColumnIndexOrThrow(KATEGORIJA_ID);
                    while (ku.moveToNext()) {
                        if (idKategorije1.get(i).equals(ku.getInt(indexKolona))) {
                            int indexNazivKat = ku.getColumnIndexOrThrow(KATEGORIJA_NAZIV);
                            nazivKategorije = ku.getString(indexNazivKat);
                        }
                    }

                }

                ArrayList<Autor> autoriZaSlanje = new ArrayList<Autor>();
                for (int m = 0; m < imenaAutora.size(); m++) {
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
                knjigeZaVratiti.add(kk);
            }



        return knjigeZaVratiti;

    }


    ArrayList<Knjiga> knjigeAutora(long idAutora){

        SQLiteDatabase db = KategorijeAkt.baza.getReadableDatabase();
        String rezultat [] = {AUTORSTVO_ID, AUTORSTVO_IDAUTORA, AUTORSTVO_IDKNJIGE};
        String where = AUTORSTVO_IDAUTORA + "=\'" + idAutora + "\'";
        Cursor kursor = db.query(DATABASE_TABLE3, rezultat, where, null, null, null, null);
        int indexKnjige = kursor.getColumnIndexOrThrow(AUTORSTVO_IDKNJIGE);

        ArrayList<Integer> idKnjiga = new ArrayList<Integer>();
        ArrayList<Knjiga> knjigeZaVratiti = new ArrayList<>();
        while(kursor.moveToNext()){
            idKnjiga.add(kursor.getInt(indexKnjige));
        }


        String rezultat1 [] = {KNJIGA_ID, KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_DATUM_OBJAVLJIVANJA, KNJIGA_BROJ_STRANICA, KNJIGA_ID_WEB_SERVIS, KNJIGA_ID_KATEGORIJE, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};

        for(int k=0;k<idKnjiga.size();k++) {
            String where1 = KNJIGA_ID + "=\'" + idKnjiga.get(k) + "\'";
            Cursor kurs = db.query(DATABASE_TABLE1, rezultat1, where1, null, null, null, null);

            String rezultat5 [] = {AUTORSTVO_ID, AUTORSTVO_IDAUTORA, AUTORSTVO_IDKNJIGE};
            String where5 = AUTORSTVO_IDKNJIGE + "=\'" + idKnjiga.get(k) + "\'";
            Cursor kursor5 = db.query(DATABASE_TABLE3, rezultat5, where5, null, null, null, null);

            ArrayList<Integer> idAUtora = new ArrayList<Integer>();
            while (kursor5.moveToNext()){
                idAUtora.add(kursor5.getInt(kursor5.getColumnIndexOrThrow(AUTORSTVO_IDAUTORA)));
            }

            ArrayList<Autor> autors = new ArrayList<Autor>();
            String rez[] = {AUTOR_ID, AUTOR_IME};
            for(int p=0; p<idAUtora.size(); p++){
                String whereUslov = AUTOR_ID + "=\'" + idAUtora.get(p) + "\'";
                Cursor kkk = db.query(DATABASE_TABLE2, rez, whereUslov, null, null, null, null);
                while (kkk.moveToNext()){
                    Autor a = new Autor(kkk.getString(kkk.getColumnIndexOrThrow(AUTOR_IME)));
                    autors.add(a);
                }
            }



            while (kurs.moveToNext()){
                String s = kurs.getString(kurs.getColumnIndexOrThrow(KNJIGA_ID_WEB_SERVIS));
                if(!s.isEmpty()){
                    String id = kurs.getString(kurs.getColumnIndexOrThrow(KNJIGA_ID_WEB_SERVIS));
                    String naziv = kurs.getString(kurs.getColumnIndexOrThrow(KNJIGA_NAZIV));
                    String opis = kurs.getString(kurs.getColumnIndexOrThrow(KNJIGA_OPIS));
                    String datumObjavljivanja = kurs.getString(kurs.getColumnIndexOrThrow(KNJIGA_DATUM_OBJAVLJIVANJA));
                    String slika = kurs.getString(kurs.getColumnIndexOrThrow(KNJIGA_SLIKA));
                    URL myURL = null;
                    try {
                        myURL = new URL(slika);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    int brojStranica = kurs.getInt(kurs.getColumnIndexOrThrow(KNJIGA_BROJ_STRANICA));
                    int pregledana = kurs.getInt(kurs.getColumnIndexOrThrow(KNJIGA_PREGLEDANA));
                    int idKategorije = kurs.getInt(kurs.getColumnIndexOrThrow(KNJIGA_ID_KATEGORIJE));

                    String rez1[] = {KATEGORIJA_ID, KATEGORIJA_NAZIV};
                    String whereU = KATEGORIJA_ID + "=\'" + idKategorije + "\'";
                    Cursor ku = db.query(DATABASE_TABLE, rez1, whereU, null, null, null, null);
                    String nazivKat = "";
                    while (ku.moveToNext()){
                        nazivKat = ku.getString(ku.getColumnIndexOrThrow(KATEGORIJA_NAZIV));
                    }

                    Knjiga knjiga = new Knjiga(id, naziv, autors, opis, datumObjavljivanja, myURL, brojStranica);
                    if(pregledana == 0) knjiga.setOznacena(false);
                    else knjiga.setOznacena(true);
                    knjiga.setKategorija(nazivKat);

                    knjigeZaVratiti.add(knjiga);


                }
            }
        }
            return knjigeZaVratiti;
    }


    public String BitmapToBase64(Bitmap b){
        ByteArrayOutputStream niz = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, niz);
        byte [] bytes = niz.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public Bitmap Base64ToBitmap(String b64){
        byte [] bytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE1);
        db.execSQL(DATABASE_CREATE2);
        db.execSQL(DATABASE_CREATE3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE1);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE2);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE3);


        onCreate(db);

    }

}

