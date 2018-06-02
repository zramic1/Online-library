package ba.unsa.etf.rma.zerina;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static ba.unsa.etf.rma.zerina.BazaOpenHelper.AUTOR_ID;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.AUTOR_IME;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.DATABASE_TABLE;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.DATABASE_TABLE1;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.DATABASE_TABLE2;
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
 * Created by zerin on 5/15/2018.
 */

public class KnjigeFragment extends Fragment {

    ArrayList<Knjiga> knjige1;
    ArrayList<Knjiga>knjige2;
    String oznaceno = "";
    String poslati = "";
    AdapterZaListuKnjiga mojAdapter1;
    AdapterZaListuKnjiga mojAdapter;
    int IDKAtegorije;
    int IDAutora;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.knjige_fragment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ListView listaKnjiga = (ListView) getView().findViewById(R.id.listaKnjiga);
        Button dPovratak = (Button) getView().findViewById(R.id.dPovratak);

        Bundle b = getArguments();
        if(b != null){
            oznaceno = b.getString("oznaceno");
            poslati = b.getString("poslati");
        }

        if(oznaceno.equals("kategorija")) {
            knjige1 = new ArrayList<Knjiga>();



            SQLiteDatabase db = KategorijeAkt.baza.getReadableDatabase();
            Cursor zaKategorije = db.rawQuery("select * from " + DATABASE_TABLE, null);
            int indexZaKategoriju = zaKategorije.getColumnIndexOrThrow(KATEGORIJA_NAZIV);
            while (zaKategorije.moveToNext()) {
                if (poslati.equals(zaKategorije.getString(indexZaKategoriju))) {
                    int indexIDKategorije = zaKategorije.getColumnIndexOrThrow(KATEGORIJA_ID);
                    IDKAtegorije = zaKategorije.getInt(indexIDKategorije);
                }
            }

            try {
                knjige1 = KategorijeAkt.baza.knjigeKategorije(Long.valueOf(String.valueOf(IDKAtegorije)));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < ListeFragment.listaKnjiga.brojElemenata(); i++) {
                if(ListeFragment.listaKnjiga.vratiKnjigu(i).isVrstaKnjige()) {
                    if (ListeFragment.listaKnjiga.vratiKnjigu(i).getKategorija().toUpperCase().equals(poslati.toUpperCase())) {
                        knjige1.add(ListeFragment.listaKnjiga.vratiKnjigu(i));
                    }
                }
            }


            mojAdapter = new AdapterZaListuKnjiga(getActivity(), knjige1);
            listaKnjiga.setAdapter(mojAdapter);
        }

        else{
            knjige2 = new ArrayList<Knjiga>();

            SQLiteDatabase db = KategorijeAkt.baza.getReadableDatabase();
            Cursor zaAutora = db.rawQuery("select * from " + DATABASE_TABLE2, null);
            int indexZaAutora = zaAutora.getColumnIndexOrThrow(AUTOR_IME);
            while (zaAutora.moveToNext()) {
                if (poslati.equals(zaAutora.getString(indexZaAutora))) {
                    int indexIDAutora = zaAutora.getColumnIndexOrThrow(AUTOR_ID);
                    IDAutora = zaAutora.getInt(indexIDAutora);
                }
            }

            try {
                knjige2 = KategorijeAkt.baza.knjigeAutora(Long.valueOf(String.valueOf(IDAutora)));
            } catch (Exception e) {
                e.printStackTrace();
            }

           for (int i = 0; i < ListeFragment.listaKnjiga.brojElemenata(); i++) {
                if(ListeFragment.listaKnjiga.vratiKnjigu(i).isVrstaKnjige()) {
                    if (ListeFragment.listaKnjiga.vratiKnjigu(i).getAutor().toUpperCase().equals(poslati.toUpperCase())) {
                        knjige2.add(ListeFragment.listaKnjiga.vratiKnjigu(i));
                    }
                }
            }


            mojAdapter1 = new AdapterZaListuKnjiga(getActivity(), knjige2);
            listaKnjiga.setAdapter(mojAdapter1);

        }

        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            parent.getChildAt(position - parent.getFirstVisiblePosition()).setBackgroundResource(R.color.bojaZaElementListe);


                int idKnjige = 0;
                SQLiteDatabase db = KategorijeAkt.baza.getReadableDatabase();
                Cursor kurs = db.rawQuery("select * from " + DATABASE_TABLE1, null);
                while(kurs.moveToNext()){
                    if(oznaceno.equalsIgnoreCase("kategorija")){
                        if(knjige1.get(position).getNaziv().equalsIgnoreCase(kurs.getString(kurs.getColumnIndexOrThrow(KNJIGA_NAZIV)))){
                            idKnjige = kurs.getInt(kurs.getColumnIndexOrThrow(KNJIGA_ID));
                        }
                    }
                    else {
                        if(knjige2.get(position).getNaziv().equalsIgnoreCase(kurs.getString(kurs.getColumnIndexOrThrow(KNJIGA_NAZIV)))){
                            idKnjige = kurs.getInt(kurs.getColumnIndexOrThrow(KNJIGA_ID));
                        }
                    }
                }
                SQLiteDatabase db1 = KategorijeAkt.baza.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(KNJIGA_PREGLEDANA, 1);
                String whereU = BazaOpenHelper.KNJIGA_ID + "=\'" + idKnjige + "\'";
                db1.update(BazaOpenHelper.DATABASE_TABLE1, contentValues, whereU, null);
                if(oznaceno.equalsIgnoreCase("kategorija")){
                    if(!knjige1.get(position).isOznacena()){
                        knjige1.get(position).setOznacena(true);
                    }
                }
                else {
                    if(!knjige2.get(position).isOznacena()){
                        knjige2.get(position).setOznacena(true);
                    }
                }


            }
        });

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListeFragment fl = new ListeFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.mjesto1, fl).commit();
            }
        });

    }
}
