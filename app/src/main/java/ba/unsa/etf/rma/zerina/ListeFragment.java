package ba.unsa.etf.rma.zerina;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static ba.unsa.etf.rma.zerina.BazaOpenHelper.AUTOR_IME;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.DATABASE_TABLE;
import static ba.unsa.etf.rma.zerina.BazaOpenHelper.DATABASE_TABLE2;


/**
 * Created by zerin on 5/14/2018.
 */

public class ListeFragment extends Fragment {

    boolean postoji=false;
    public static ArrayList<String> lista = new ArrayList<String>();
    public static SveKnjige listaKnjiga = new SveKnjige();
    public static ArrayList<Autor>autori = new ArrayList<Autor>();
    boolean autorPostoji = false;
    MojAdapter adapter;
    boolean oznacenaKategorija = false;
    boolean autorPostoji2 = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.liste_fragment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final EditText tekstPretraga = (EditText) getView().findViewById(R.id.tekstPretraga);
        final Button dPretraga = (Button) getView().findViewById(R.id.dPretraga);
        final Button dDodajKategoriju = (Button) getView().findViewById(R.id.dDodajKategoriju);
        Button dDodajKnjigu = (Button) getView().findViewById(R.id.dDodajKnjigu);
        final ListView listView = (ListView) getView().findViewById(R.id.listaKategorija);
        Button dAutor = (Button) getView().findViewById(R.id.dAutori);
        Button dKategorije = (Button) getView().findViewById(R.id.dKategorije);
        Button dDodavanjeOnline = (Button) getView().findViewById(R.id.dDodajOnline);

        dDodajKategoriju.setEnabled(false);


        adapter = new MojAdapter(getActivity(), lista);

        dKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listView.setAdapter(adapter);

                dPretraga.setVisibility(View.VISIBLE);
                dDodajKategoriju.setVisibility(View.VISIBLE);
                tekstPretraga.setVisibility(View.VISIBLE);


            }
        });

        dPretraga.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                adapter.getFilter().filter(tekstPretraga.getText());

                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).toUpperCase().contains(tekstPretraga.getText().toString().toUpperCase())) {
                        postoji=true;
                    }
                }
                if(postoji==false) {
                    dDodajKategoriju.setEnabled(true);
                    dDodajKategoriju.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String s = tekstPretraga.getText().toString();
                            if(s != null && !s.isEmpty()) {
                                long index = KategorijeAkt.baza.dodajKategoriju(s);
                                if(index != -1) {

                                    String ind = String.valueOf(index);

                                    String[] koloneRezultat1 = {BazaOpenHelper.KATEGORIJA_ID, BazaOpenHelper.KATEGORIJA_NAZIV};
                                    String where1 = BazaOpenHelper.KATEGORIJA_ID + "=\'" + ind + "\'";
                                    SQLiteDatabase db = KategorijeAkt.baza.getWritableDatabase();
                                    Cursor k = db.query(DATABASE_TABLE, koloneRezultat1, where1, null, null, null, null);
                                    int indexKoloneNaziv = k.getColumnIndexOrThrow(BazaOpenHelper.KATEGORIJA_NAZIV);
                                    while (k.moveToNext()) {
                                        lista.add(k.getString(indexKoloneNaziv));
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                            tekstPretraga.setText("");
                            dPretraga.performClick();

                        }
                    });
                }
                else dDodajKategoriju.setEnabled(false);
                postoji=false;
            }
        });



        dAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AdapterZaAutora adapter1 = new AdapterZaAutora(getActivity(), autori);
                listView.setAdapter(adapter1);

                dPretraga.setVisibility(View.GONE);
                dDodajKategoriju.setVisibility(View.GONE);
                tekstPretraga.setVisibility(View.GONE);

            }
        });




        dDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lista.size() > 0) {

                    DodavanjeKnjigeFragment fdk = new DodavanjeKnjigeFragment();
                    Bundle b = new Bundle();
                    b.putStringArrayList("lista", lista);
                    fdk.setArguments(b);
                    FragmentManager fm = getFragmentManager();

                    if (KategorijeAkt.siri == false)
                        fm.beginTransaction().replace(R.id.mjesto1, fdk).commit();
                    else {
                        FrameLayout m1 = (FrameLayout) getActivity().findViewById(R.id.mjesto1);
                        m1.setVisibility(View.GONE);
                        FrameLayout m2 = (FrameLayout) getActivity().findViewById(R.id.mjesto2);
                        m2.setVisibility(View.GONE);
                        FrameLayout m3 = (FrameLayout) getActivity().findViewById(R.id.mjesto3);
                        m3.setVisibility(View.VISIBLE);
                        fm.beginTransaction().replace(R.id.mjesto3, fdk).commit();
                    }
                }
                else  Toast.makeText(getActivity(), R.string.nePostojiKategorija, Toast.LENGTH_SHORT).show();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KnjigeFragment fk = new KnjigeFragment();
                oznacenaKategorija = false;

                for(int i=0;i<lista.size();i++){
                    if(lista.get(i).toUpperCase().equals(listView.getItemAtPosition(position).toString().toUpperCase())){
                        oznacenaKategorija = true;

                    }
                }
                Bundle b = new Bundle();
                String oznaceno = "";
                String poslati = "";
                if(oznacenaKategorija)
                {
                    poslati = listView.getItemAtPosition(position).toString();
                    oznaceno = "kategorija";
                }
                else {
                    oznaceno = "autor";
                    poslati = autori.get(position).getImeiPrezime();
                }
                b.putString("oznaceno", oznaceno);
                b.putString("poslati", poslati);
                fk.setArguments(b);
                FragmentManager fm = getFragmentManager();

                if(KategorijeAkt.siri) fm.beginTransaction().replace(R.id.mjesto2,fk).commit();
                else fm.beginTransaction().replace(R.id.mjesto1,fk).commit();
            }
        });


        dDodavanjeOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lista.size() > 0) {
                    FragmentOnline fo = new FragmentOnline();
                    Bundle b = new Bundle();
                    b.putStringArrayList("lista", lista);
                    fo.setArguments(b);
                    FragmentManager fm = getFragmentManager();

                    if (!KategorijeAkt.siri)
                        fm.beginTransaction().replace(R.id.mjesto1, fo).commit();

                    else {
                        FrameLayout m1 = (FrameLayout) getActivity().findViewById(R.id.mjesto1);
                        m1.setVisibility(View.GONE);
                        FrameLayout m2 = (FrameLayout) getActivity().findViewById(R.id.mjesto2);
                        m2.setVisibility(View.GONE);
                        FrameLayout m3 = (FrameLayout) getActivity().findViewById(R.id.mjesto3);
                        m3.setVisibility(View.VISIBLE);
                        fm.beginTransaction().replace(R.id.mjesto3, fo).commit();
                    }
                }
                else{
                    Toast.makeText(getActivity(), R.string.nePostojiKategorija, Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

}
