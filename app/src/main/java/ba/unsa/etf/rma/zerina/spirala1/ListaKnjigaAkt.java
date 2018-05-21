package ba.unsa.etf.rma.zerina.spirala1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ba.unsa.etf.rma.zerina.R;


public class ListaKnjigaAkt extends AppCompatActivity {

    ArrayList<Knjiga> knjige1;
    int br = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_knjiga_akt);

        String kategorija = getIntent().getStringExtra("kategorija");
        knjige1 = new ArrayList<Knjiga>();

        for(int i=0;i<KategorijeAkt.listaKnjiga.brojElemenata();i++){
            if(KategorijeAkt.listaKnjiga.vratiKnjigu(i).getKategorija().toUpperCase().equals(kategorija.toUpperCase())){
                knjige1.add(KategorijeAkt.listaKnjiga.vratiKnjigu(i));
            }
        }

        final ListView listaKnjiga = (ListView) findViewById(R.id.listaKnjiga);
        Button dPovratak = (Button) findViewById(R.id.dPovratak);

        final AdapterZaListuKnjiga mojAdapter = new AdapterZaListuKnjiga(this, knjige1);
        listaKnjiga.setAdapter(mojAdapter);

        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for(int i=0;i<KategorijeAkt.listaKnjiga.brojElemenata();i++){
                    if(knjige1.get(position).getNaziv().toUpperCase().equals(KategorijeAkt.listaKnjiga.vratiKnjigu(i).getNaziv().toUpperCase())){
                        KategorijeAkt.listaKnjiga.vratiKnjigu(i).setOznacena(true);
                    }
                }
                listaKnjiga.setAdapter(mojAdapter);
            }
        });




        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (ListaKnjigaAkt.this, KategorijeAkt.class);
                startActivity(i);
            }
        });

    }





}
