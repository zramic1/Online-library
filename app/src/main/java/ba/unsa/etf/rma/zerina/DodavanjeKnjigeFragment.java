package ba.unsa.etf.rma.zerina;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zerin on 5/14/2018.
 */

public class DodavanjeKnjigeFragment extends Fragment {

    ArrayAdapter<String> adapter;
    ArrayList<String> l;
    private int REQUEST_CODE = 1;
    ImageView naslovnaStr;
    Bitmap pom;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dodavanje_knjige_fragment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle b = getArguments();
        if(b != null){
            l = b.getStringArrayList("lista");
        }

        final EditText imeAutora = (EditText) getView().findViewById(R.id.imeAutora);
        final EditText nazivKnjige = (EditText) getView().findViewById(R.id.nazivKnjige);
        Button dNadjiSliku = (Button) getView().findViewById(R.id.dNadjiSliku);
        Button dUpisiKnjigu = (Button) getView().findViewById(R.id.dUpisiKnjigu);
        Button dPonisti = (Button) getView().findViewById(R.id.dPonisti);
        final Spinner sKategorijaKnjige = (Spinner) getView().findViewById(R.id.sKategorijaKnjige);
        naslovnaStr = (ImageView) getView().findViewById(R.id.naslovnaStr);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKategorijaKnjige.setAdapter(adapter);

        dPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListeFragment fl = new ListeFragment();
                FragmentManager fm = getFragmentManager();

                if(KategorijeAkt.siri){
                    FrameLayout m1 = (FrameLayout) getActivity().findViewById(R.id.mjesto1);
                    m1.setVisibility(View.VISIBLE);
                    FrameLayout m2 = (FrameLayout) getActivity().findViewById(R.id.mjesto2);
                    m2.setVisibility(View.VISIBLE);
                    FrameLayout m3 = (FrameLayout) getActivity().findViewById(R.id.mjesto3);
                    m3.setVisibility(View.GONE);
                }

                fm.beginTransaction().replace(R.id.mjesto1, fl).commit();

            }
        });

        dNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent();
                inte.setType("image/*");
                inte.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(inte, "Izaberi sliku"), REQUEST_CODE);
            }
        });

        dUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazivK = nazivKnjige.getText().toString();
                String imeA = imeAutora.getText().toString();

                if(pom != null && !nazivK.isEmpty() && !imeA.isEmpty()) {
                    Knjiga k = new Knjiga(pom, nazivK, imeA, sKategorijaKnjige.getSelectedItem().toString());
                    ListeFragment.listaKnjiga.dodajKnjigu(k);
                    nazivKnjige.setText("");
                    imeAutora.setText("");
                    naslovnaStr.setImageBitmap(null);
                    pom = null;

                    Toast.makeText(getActivity(), R.string.Toast, Toast.LENGTH_SHORT).show();
                }
                else  Toast.makeText(getActivity(), R.string.unesiteSvePodatke, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE  && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                pom = bitmap;
                naslovnaStr.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
