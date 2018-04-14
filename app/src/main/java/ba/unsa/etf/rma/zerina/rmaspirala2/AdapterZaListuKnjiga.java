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
 * Created by zerin on 4/4/2018.
 */

public class AdapterZaListuKnjiga extends BaseAdapter {

    Context c;
    ArrayList<Knjiga> knjigeZaIspis;

    public AdapterZaListuKnjiga(Context c, ArrayList<Knjiga> knjigeZaIspis) {
        this.c = c;
        this.knjigeZaIspis = knjigeZaIspis;
    }

    public Context getC() {
        return c;
    }

    public ArrayList<Knjiga> getKnjige() {
        return knjigeZaIspis;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public void setKnjige(ArrayList<Knjiga> knjige) {
        this.knjigeZaIspis = knjige;
    }






    @Override
    public int getCount() {
        return knjigeZaIspis.size();
    }

    @Override
    public Object getItem(int position) {
        return knjigeZaIspis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.element_liste_knjiga, null);

        ImageView eNaslovna = (ImageView) v.findViewById(R.id.eNaslovna);
        TextView eNaziv = (TextView) v.findViewById(R.id.eNaziv);
        TextView eAutor = (TextView) v.findViewById(R.id.eAutor);

        eNaziv.setText(knjigeZaIspis.get(position).getNaziv());
        eAutor.setText(knjigeZaIspis.get(position).getAutor());
        eNaslovna.setImageBitmap(knjigeZaIspis.get(position).getSlika());

        if(knjigeZaIspis.get(position).isOznacena())
            v.setBackgroundResource(R.color.bojaZaElementListe);

        return v;
    }
}
