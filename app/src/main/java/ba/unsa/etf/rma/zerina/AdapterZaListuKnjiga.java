package ba.unsa.etf.rma.zerina;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;



/**
 * Created by zerin on 5/14/2018.
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
        if(knjigeZaIspis.get(position).isVrstaKnjige()) {
            eAutor.setText(knjigeZaIspis.get(position).getAutor());
        }
        else {
            ArrayList<Autor> autors = knjigeZaIspis.get(position).getAutori();

            StringBuilder builder = new StringBuilder();
            for (int i=0; i<autors.size(); i++) {
                builder.append(autors.get(i).getImeiPrezime() + "\n");
            }

            eAutor.setText(builder.toString());
        }
        if(knjigeZaIspis.get(position).isVrstaKnjige()) {
            eNaslovna.setImageBitmap(knjigeZaIspis.get(position).getSlika());
        }
        else{
            Picasso.with(getC()).load(knjigeZaIspis.get(position).getSlika1().toString()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .into(eNaslovna, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        if(knjigeZaIspis.get(position).isOznacena())
            v.setBackgroundResource(R.color.bojaZaElementListe);

        return v;
    }
}
