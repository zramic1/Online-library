package ba.unsa.etf.rma.zerina;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by zerin on 5/23/2018.
 */

public class FragmentPreporuci extends Fragment {


    ArrayList<Kontakt> kontakti = new ArrayList<Kontakt>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preporuci, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        ArrayList<String> imena = new ArrayList<String>();

        ArrayAdapter<String> adapter;

        final Spinner sKontakti = (Spinner) getView().findViewById(R.id.sKontakti​);
        Button dPosalji = (Button) getView().findViewById(R.id.dPosalji​);
        TextView eNaziv1 = (TextView) getView().findViewById(R.id.eNaziv1);
        TextView eAutor1 = (TextView) getView().findViewById(R.id.eAutor1);
        ImageView eNaslovna1 = (ImageView) getView().findViewById(R.id.eNaslovna1);
        TextView eDatumObjavljivanja1 = (TextView) getView().findViewById(R.id.eDatumObjavljivanja1);
        TextView eBrojStranica1 = (TextView) getView().findViewById(R.id.eBrojStranica1);
        TextView eOpis1 = (TextView) getView().findViewById(R.id.eOpis1);

        Bundle b = getArguments();
        if(b != null){
            kontakti = b.getParcelableArrayList("kontakti");
        }

        for (int i=0; i<kontakti.size(); i++){
            if(kontakti.get(i).getEmail().equals("")) continue;
            else imena.add(kontakti.get(i).getIme());

        }

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, imena);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKontakti.setAdapter(adapter);

        eNaziv1.setText(AdapterZaListuKnjiga.knjiga.getNaziv());
        if(AdapterZaListuKnjiga.knjiga.isVrstaKnjige()) {
            eAutor1.setText(AdapterZaListuKnjiga.knjiga.getAutor());
        }
        else {
            ArrayList<Autor> autors = AdapterZaListuKnjiga.knjiga.getAutori();

            StringBuilder builder = new StringBuilder();
            for (int i=0; i<autors.size(); i++) {
                builder.append(autors.get(i).getImeiPrezime() + "\n");
            }

            eAutor1.setText(builder.toString());
        }
        if(AdapterZaListuKnjiga.knjiga.isVrstaKnjige()) {
            eNaslovna1.setImageBitmap(AdapterZaListuKnjiga.knjiga.getSlika());
        }
        else{
            Picasso.with(getActivity()).load(AdapterZaListuKnjiga.knjiga.getSlika1().toString()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .into(eNaslovna1, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        if(!AdapterZaListuKnjiga.knjiga.isVrstaKnjige()){
            eDatumObjavljivanja1.setText(AdapterZaListuKnjiga.knjiga.getDatumObjavljivanja());
        }
        else {
            eDatumObjavljivanja1.setText("");
        }
        if(!AdapterZaListuKnjiga.knjiga.isVrstaKnjige()){
            String s = String.valueOf(AdapterZaListuKnjiga.knjiga.getBrojStrinica());
            eBrojStranica1.setText(s);
        }
        else {
            eBrojStranica1.setText("");
        }
        if(!AdapterZaListuKnjiga.knjiga.isVrstaKnjige()){
            eOpis1.setText(AdapterZaListuKnjiga.knjiga.getOpis());
        }
        else {
            eOpis1.setText("");
        }

        dPosalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ime = sKontakti.getSelectedItem().toString();
                String e = "";

                for(int i=0; i<kontakti.size(); i++){
                    if(kontakti.get(i).getIme().equals(ime)){
                        e = kontakti.get(i).getEmail();
                    }
                }

                String posalji = "Zdravo " + ime +",\n" + "Pročitaj knjigu " + AdapterZaListuKnjiga.nazivKnjige + " od " + AdapterZaListuKnjiga.imeAutora + "!";

                String [] emailLista = {e};

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, emailLista);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Preporuka knjige");
                intent.putExtra(Intent.EXTRA_TEXT, posalji);
                startActivity(Intent.createChooser(intent, "Izaberite aplikaciju:"));
            }
        });




    }


}
