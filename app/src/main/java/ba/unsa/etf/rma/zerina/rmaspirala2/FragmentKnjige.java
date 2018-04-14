package ba.unsa.etf.rma.zerina.rmaspirala2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by zerin on 4/5/2018.
 */

public class FragmentKnjige extends Fragment {

    ArrayList<Knjiga>knjige1;
    ArrayList<Knjiga>knjige2;
    String oznaceno = "";
    String poslati = "";
    AdapterZaListuKnjiga mojAdapter1;
    AdapterZaListuKnjiga mojAdapter;


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

            for (int i = 0; i < FragmentLista.listaKnjiga.brojElemenata(); i++) {
                if (FragmentLista.listaKnjiga.vratiKnjigu(i).getKategorija().toUpperCase().equals(poslati.toUpperCase())) {
                    knjige1.add(FragmentLista.listaKnjiga.vratiKnjigu(i));
                }
            }



            mojAdapter = new AdapterZaListuKnjiga(getActivity(), knjige1);
            listaKnjiga.setAdapter(mojAdapter);
        }

        else{
            knjige2 = new ArrayList<Knjiga>();

            for (int i = 0; i < FragmentLista.listaKnjiga.brojElemenata(); i++) {
                if (FragmentLista.listaKnjiga.vratiKnjigu(i).getAutor().toUpperCase().equals(poslati.toUpperCase())) {
                    knjige2.add(FragmentLista.listaKnjiga.vratiKnjigu(i));
                }
            }

            mojAdapter1 = new AdapterZaListuKnjiga(getActivity(), knjige2);
            listaKnjiga.setAdapter(mojAdapter1);

        }

        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(oznaceno.equals("kategorija")) {
                    for (int i = 0; i < FragmentLista.listaKnjiga.brojElemenata(); i++) {
                        if (knjige1.get(position).getNaziv().toUpperCase().equals(FragmentLista.listaKnjiga.vratiKnjigu(i).getNaziv().toUpperCase())) {
                            FragmentLista.listaKnjiga.vratiKnjigu(i).setOznacena(true);
                        }
                    }
                    listaKnjiga.setAdapter(mojAdapter);
                }
                else{
                    for (int i = 0; i < FragmentLista.listaKnjiga.brojElemenata(); i++) {
                        if (knjige2.get(position).getNaziv().toUpperCase().equals(FragmentLista.listaKnjiga.vratiKnjigu(i).getNaziv().toUpperCase())) {
                            FragmentLista.listaKnjiga.vratiKnjigu(i).setOznacena(true);
                        }
                    }
                    listaKnjiga.setAdapter(mojAdapter1);
                }
            }
        });

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentLista fl = new FragmentLista();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.mjesto1, fl).commit();
            }
        });

    }
}
