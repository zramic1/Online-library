package ba.unsa.etf.rma.zerina.spirala1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity{

    boolean postoji=false;
    ArrayList<String> lista;
    public static SveKnjige listaKnjiga = new SveKnjige();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategorije_akt);


        final EditText tekstPretraga = (EditText) findViewById(R.id.tekstPretraga);
        final Button dPretraga = (Button) findViewById(R.id.dPretraga);
        final Button dDodajKategoriju = (Button) findViewById(R.id.dDodajKategoriju);
        Button dDodajKnjigu = (Button) findViewById(R.id.dDodajKnjigu);
        final ListView listView = (ListView) findViewById(R.id.listaKategorija);

        dDodajKategoriju.setEnabled(false);

        String[] kategorija = {"Krimi", "Ljubavna", "Triler", "Drama", "Komedija", "Sci-Fi"};
        lista = new ArrayList<String>();
        for(int i=0; i<kategorija.length; i++){
            lista.add(kategorija[i]);
        }

        final MojAdapter adapter = new MojAdapter(this, lista);
        listView.setAdapter(adapter);





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

                                lista.add(tekstPretraga.getText().toString());
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


        dDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrebaciListu11();

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent in = new Intent(KategorijeAkt.this, ListaKnjigaAkt.class);
                in.putExtra("kategorija", listView.getItemAtPosition(position).toString());
                startActivity(in);


            }
        });



    }

    public void PrebaciListu11 (){
        Intent i = new Intent(this, Dodavanje_knjige.class);
        Bundle b = new Bundle();
        b.putStringArrayList("lista", lista);
        i.putExtras(b);
        startActivity(i);
    }

}
