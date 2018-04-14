package ba.unsa.etf.rma.zerina.rmaspirala2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
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

/**
 * Created by zerin on 4/4/2018.
 */

public class FragmentLista extends Fragment {

    boolean postoji=false;
    public static ArrayList<String> lista = new ArrayList<String>();
    public static SveKnjige listaKnjiga = new SveKnjige();
    ArrayList<Autor>autori;
    boolean autorPostoji = false;
    MojAdapter adapter;
    boolean oznacenaKategorija = false;

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

        dDodajKategoriju.setEnabled(false);





        dKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new MojAdapter(getActivity(), lista);
                listView.setAdapter(adapter);

                dPretraga.setVisibility(View.VISIBLE);
                dDodajKategoriju.setVisibility(View.VISIBLE);
                tekstPretraga.setVisibility(View.VISIBLE);

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
                                    if(s != null && !s.isEmpty()) lista.add(s);
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


            }
        });

        autori = new ArrayList<Autor>();


        for(int i=0; i<listaKnjiga.brojElemenata();i++){
            autorPostoji = false;
            for(int j=0; j<autori.size();j++){
                if(listaKnjiga.vratiKnjigu(i).getAutor().toUpperCase().equals(autori.get(j).getIme().toUpperCase())){
                    autori.get(j).setImaAutora(true);
                    autori.get(j).setBrojKnjiga();
                    autorPostoji = true;
                }

            }
            if(autorPostoji == false){
                Autor a = new Autor(listaKnjiga.vratiKnjigu(i).getAutor());
                autori.add(a);
            }
        }




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

                    FragmentDodavanjeKnjige fdk = new FragmentDodavanjeKnjige();
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
                FragmentKnjige fk = new FragmentKnjige();
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
                    poslati = autori.get(position).getIme();
                }
                b.putString("oznaceno", oznaceno);
                b.putString("poslati", poslati);
                fk.setArguments(b);
                FragmentManager fm = getFragmentManager();


                if(KategorijeAkt.siri) fm.beginTransaction().replace(R.id.mjesto2,fk).commit();
                else fm.beginTransaction().replace(R.id.mjesto1,fk).commit();
            }
        });






    }
}
