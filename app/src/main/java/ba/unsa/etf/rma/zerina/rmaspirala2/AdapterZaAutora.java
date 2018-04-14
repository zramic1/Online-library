package ba.unsa.etf.rma.zerina.rmaspirala2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zerin on 4/5/2018.
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


        TextView nazivAutora = (TextView) v.findViewById(R.id.nazivAutora);
        TextView brojKnjiga = (TextView) v.findViewById(R.id.upisiBrojKnjiga);

        nazivAutora.setText(autorZaIspis.get(position).getIme());
        String s = String.valueOf(autorZaIspis.get(position).getBrojKnjiga());
        brojKnjiga.setText(s);

        return v;
    }
}
