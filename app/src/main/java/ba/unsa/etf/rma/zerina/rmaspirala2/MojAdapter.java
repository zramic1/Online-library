package ba.unsa.etf.rma.zerina.rmaspirala2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import ba.unsa.etf.rma.zerina.R;

/**
 * Created by zerin on 4/4/2018.
 */

public class MojAdapter extends BaseAdapter implements Filterable {


    Context c;
    ArrayList<String> kategorije, pom;
    MojFilter mf;


    public void setKategorije(ArrayList<String> kategorije) {
        this.kategorije = kategorije;
    }

    public ArrayList<String> getKategorije() {
        return kategorije;
    }

    public MojAdapter(Context c, ArrayList<String>kategorije){
        this.c = c;
        this.kategorije = kategorije;
        this.pom = kategorije;
    }


    @Override
    public int getCount() {
        return kategorije.size();
    }

    @Override
    public Object getItem(int position) {
        return kategorije.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.element_kategorije, null);

        TextView tw = (TextView) v.findViewById(R.id.textView2);
        tw.setText(kategorije.get(position));

        return v;
    }

    @Override
    public Filter getFilter() {

        if(mf==null){
            mf = new MojFilter();
        }
        return mf;
    }

    class MojFilter extends Filter{


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filter = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();

                ArrayList<String> filteri = new ArrayList<String>();

                for (int i = 0; i < pom.size(); i++) {

                    if (pom.get(i).toUpperCase().contains(constraint)) {
                        filteri.add(pom.get(i));
                    }
                }

                filter.count = filteri.size();
                filter.values = filteri;
            }
            else{
                filter.count = pom.size();
                filter.values = pom;
            }
            return filter;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            kategorije = (ArrayList<String>)results.values;
            notifyDataSetChanged();


        }
    }
}
