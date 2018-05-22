package ba.unsa.etf.rma.zerina;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * Created by zerin on 5/17/2018.
 */

public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone, MojResultReceiver.Receiver{

    ArrayList<String> l;
    ArrayAdapter<String>adapter;
    ArrayAdapter<String>adapterZaSpinner;
    Spinner sRezultat;
    ArrayList<String> nazivi = new ArrayList<String>();
    ArrayList<Knjiga> pomocnaListaKnjiga = new ArrayList<Knjiga>();
    boolean postojiKnjiga = false;
    Button dAdd;


   @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_online, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle b = getArguments();
        if(b != null){
            l = b.getStringArrayList("lista");
        }

        final Spinner sKategorije = (Spinner) getView().findViewById(R.id.sKategorije);
        final EditText tekstUpit = (EditText) getView().findViewById(R.id.tekstUpit);
        Button dRun = (Button) getView().findViewById(R.id.dRun​);
        dAdd = (Button) getView().findViewById(R.id.dAdd);
        sRezultat = (Spinner) getView().findViewById(R.id.sRezultat);
        Button dPovratak = (Button) getView().findViewById(R.id.dPovratak​1);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKategorije.setAdapter(adapter);

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListeFragment lf = new ListeFragment();
                FragmentManager fm = getFragmentManager();

                if(KategorijeAkt.siri){
                    FrameLayout m1 = (FrameLayout) getActivity().findViewById(R.id.mjesto1);
                    m1.setVisibility(View.VISIBLE);
                    FrameLayout m2 = (FrameLayout) getActivity().findViewById(R.id.mjesto2);
                    m2.setVisibility(View.VISIBLE);
                    FrameLayout m3 = (FrameLayout) getActivity().findViewById(R.id.mjesto3);
                    m3.setVisibility(View.GONE);
                }
                fm.beginTransaction().replace(R.id.mjesto1, lf).commit();
            }
        });


        dRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = tekstUpit.getText().toString();
                ArrayList<String> poslati = new ArrayList<String>();
                String rijec = "";
                if(!s.isEmpty()) {
                    if (s.contains(";")) {
                        for (int i = 0; i < s.length(); i++) {
                            if (s.charAt(i) != ';') rijec += s.charAt(i);
                            else if (s.charAt(i) == ';') {
                                poslati.add(rijec);
                                rijec = "";
                            }
                        }
                        poslati.add(rijec);
                        rijec = "";
                        nazivi.subList(0, nazivi.size()).clear();
                        for (int i = 0; i < poslati.size(); i++) {
                            new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(poslati.get(i));
                        }
                    } else if (s.contains("autor:")) {
                        for (int i = 0; i < s.length(); i++) {
                            if (s.charAt(i) != ':') rijec += s.charAt(i);
                            else if (s.charAt(i) == ':') {
                                if (rijec.equals("autor")) rijec = "";
                            }
                        }

                        new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone) FragmentOnline.this).execute(rijec);

                    } else if(s.contains("korisnik:")){

                        for (int i = 0; i < s.length(); i++) {
                            if (s.charAt(i) != ':') rijec += s.charAt(i);
                            else if (s.charAt(i) == ':') {
                                if (rijec.equals("korisnik")) rijec = "";
                            }
                        }

                        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), KnjigePoznanika.class);
                        MojResultReceiver mReciver = new MojResultReceiver(new Handler());
                        mReciver.setReceiver((MojResultReceiver.Receiver) FragmentOnline.this);
                        intent.putExtra("idKorisnika", rijec);
                        intent.putExtra("receiver", mReciver);
                        mReciver.send(KnjigePoznanika.STATUS_START, Bundle.EMPTY);
                        getActivity().startService(intent);

                    }

                    else {
                        nazivi.subList(0, nazivi.size()).clear();
                        new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(s);
                    }
                }

            }
        });

        dAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(sRezultat != null && sRezultat.getSelectedItem()!=null) {

                    String nazivKnjige = sRezultat.getSelectedItem().toString();
                    String kategorija = sKategorije.getSelectedItem().toString();

                    for (int i = 0; i < pomocnaListaKnjiga.size(); i++) {
                        if (nazivKnjige.equals(pomocnaListaKnjiga.get(i).getNaziv())) {
                            pomocnaListaKnjiga.get(i).setKategorija(kategorija);
                            ArrayList<Autor> autors = pomocnaListaKnjiga.get(i).getAutori();
                            postojiKnjiga = false;
                            for (int j = 0; j < ListeFragment.listaKnjiga.brojElemenata(); j++) {
                                if (nazivKnjige.equals(ListeFragment.listaKnjiga.vratiKnjigu(j).getNaziv())){
                                    if(!ListeFragment.listaKnjiga.vratiKnjigu(j).isVrstaKnjige()){
                                        ArrayList<Autor> autors1 = ListeFragment.listaKnjiga.vratiKnjigu(j).getAutori();
                                        if(autors.size() == autors1.size()){
                                            int brojac = 0;
                                            for(int l=0; l<autors.size(); l++){
                                                if(autors.get(l).getImeiPrezime().equalsIgnoreCase(autors1.get(l).getImeiPrezime())){
                                                    brojac++;
                                                }
                                                else {
                                                    postojiKnjiga = false;
                                                    break;
                                                }
                                            }
                                            if (brojac == autors.size()) postojiKnjiga = true;

                                        }
                                    }
                                    else if(autors.size() == 1){
                                        if(autors.get(0).getImeiPrezime().equalsIgnoreCase(ListeFragment.listaKnjiga.vratiKnjigu(j).getAutor())){
                                            postojiKnjiga = true;
                                        }
                                    }
                                }

                            }
                            if (!postojiKnjiga) {
                                ListeFragment.listaKnjiga.dodajKnjigu(pomocnaListaKnjiga.get(i));
                                Toast.makeText(getActivity(), R.string.Toast, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }

            }
        });

    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> k) {

       pomocnaListaKnjiga = k;

        for(int i=0; i<k.size(); i++){
            nazivi.add(k.get(i).getNaziv());
        }
        adapterZaSpinner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nazivi);
        adapterZaSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRezultat.setAdapter(adapterZaSpinner);
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> k) {

       pomocnaListaKnjiga = k;

        ArrayList<String> naziviZaAutora = new ArrayList<String>();
        for(int i=0; i<k.size(); i++){
            naziviZaAutora.add(k.get(i).getNaziv());
        }
        adapterZaSpinner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, naziviZaAutora);
        adapterZaSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRezultat.setAdapter(adapterZaSpinner);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == KnjigePoznanika.STATUS_START) {
            dAdd.setEnabled(false);

        } else if (resultCode == KnjigePoznanika.STATUS_FINISH) {
            dAdd.setEnabled(true);
            ArrayList<Knjiga> k = resultData.getParcelableArrayList("listaKnjiga");
            pomocnaListaKnjiga = k;

            ArrayList<String> nazivi = new ArrayList<String>();
            for (int i = 0; i < k.size(); i++) {
                nazivi.add(k.get(i).getNaziv());
            }
            adapterZaSpinner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nazivi);
            adapterZaSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sRezultat.setAdapter(adapterZaSpinner);

        } else if (resultCode == KnjigePoznanika.STATUS_ERROR) {

        }
    }
}
