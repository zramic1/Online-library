package ba.unsa.etf.rma.zerina;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import javax.crypto.KeyAgreement;


/**
 * Created by zerin on 5/14/2018.
 */

public class AdapterZaAutora extends BaseAdapter {


    Context c;
    ArrayList<Autor> autorZaIspis;


    public AdapterZaAutora(Context c, ArrayList<Autor> autorZaIspis) {
        this.c = c;
        this.autorZaIspis = autorZaIspis;
    }


    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public ArrayList<Autor> getAutorZaIspis() {
        return autorZaIspis;
    }

    public void setAutorZaIspis(ArrayList<Autor> autorZaIspis) {
        this.autorZaIspis = autorZaIspis;
    }



    @Override
    public int getCount() {
        return autorZaIspis.size();
    }

    @Override
    public Object getItem(int position) {
        return autorZaIspis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.element_liste_autor, null);

        int brojKnjigaAutora = 0;

        int idAutora = 0;
        SQLiteDatabase db = KategorijeAkt.baza.getReadableDatabase();
        String [] rezultat = {BazaOpenHelper.AUTOR_ID, BazaOpenHelper.AUTOR_IME};
        String where = BazaOpenHelper.AUTOR_IME + "=\'" + autorZaIspis.get(position).getImeiPrezime() + "\'";
        Cursor kursor = db.query(BazaOpenHelper.DATABASE_TABLE2, rezultat, where, null, null, null, null);
        while(kursor.moveToNext()){
            idAutora = kursor.getInt(kursor.getColumnIndexOrThrow(BazaOpenHelper.AUTOR_ID));
        }

        String [] rezultat1 = {BazaOpenHelper.AUTORSTVO_ID, BazaOpenHelper.AUTORSTVO_IDAUTORA, BazaOpenHelper.AUTORSTVO_IDKNJIGE};
        String where1 = BazaOpenHelper.AUTORSTVO_IDAUTORA + "=\'" + idAutora + "\'";
        Cursor kursor1 = db.query(BazaOpenHelper.DATABASE_TABLE3, rezultat1, where1, null, null, null, null);
        brojKnjigaAutora = kursor1.getCount();



        TextView nazivAutora = (TextView) v.findViewById(R.id.nazivAutora);
        TextView brojKnjiga = (TextView) v.findViewById(R.id.upisiBrojKnjiga);

        nazivAutora.setText(autorZaIspis.get(position).getImeiPrezime());
        String s = String.valueOf(brojKnjigaAutora);
        brojKnjiga.setText(s);



        return v;
    }
}
