package ba.unsa.etf.rma.zerina.spirala1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zerin on 3/25/2018.
 */

public class Dodavanje_knjige extends AppCompatActivity {


    ArrayAdapter<String> adapter;
    ArrayList<String> l;
    private int REQUEST_CODE = 1;
    ImageView naslovnaStr;
    Bitmap pom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodavanje_knjige_akt);



        Bundle b = getIntent().getExtras();
        ArrayList<String> l = b.getStringArrayList("lista");


        final EditText imeAutora = (EditText) findViewById(R.id.imeAutora);
        final EditText nazivKnjige = (EditText) findViewById(R.id.nazivKnjige);
        Button dNadjiSliku = (Button) findViewById(R.id.dNadjiSliku);
        Button dUpisiKnjigu = (Button) findViewById(R.id.dUpisiKnjigu);
        Button dPonisti = (Button) findViewById(R.id.dPonisti);
        final Spinner sKategorijaKnjige = (Spinner) findViewById(R.id.sKategorijaKnjige);
        naslovnaStr = (ImageView) findViewById(R.id.naslovnaStr);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKategorijaKnjige.setAdapter(adapter);


        dNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent();
                inte.setType("image/*");
                inte.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(inte, "Izaberi sliku"), REQUEST_CODE);
            }
        });



        dPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VratiNaPocetnuAktivnost();
            }
        });

        dUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Knjiga k = new Knjiga(pom, nazivKnjige.getText().toString(), imeAutora.getText().toString(), sKategorijaKnjige.getSelectedItem().toString());
                KategorijeAkt.listaKnjiga.dodajKnjigu(k);
                nazivKnjige.setText("");
                imeAutora.setText("");
                naslovnaStr.setImageBitmap(null);
                pom = null;

                Toast.makeText(Dodavanje_knjige.this, "Knjiga je uspješno dodana", Toast.LENGTH_SHORT).show();
            }
        });




    }

    public void VratiNaPocetnuAktivnost(){
        Intent i = new Intent(this, KategorijeAkt.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE  && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                pom = bitmap;
                naslovnaStr.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
