package ba.unsa.etf.rma.zerina;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by zerin on 5/14/2018.
 */

public class KategorijeAkt extends AppCompatActivity {

    public static boolean siri = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategorije_akt);

        FragmentManager fm = getFragmentManager();
        FrameLayout fragmentKnjige = (FrameLayout) findViewById(R.id.mjesto2);
        FrameLayout fragmentDodavanjeKnjige = (FrameLayout) findViewById(R.id.mjesto3);

        if(fragmentKnjige != null){
            siri = true;
            FragmentTransaction ft = fm.beginTransaction();
            KnjigeFragment fk = new KnjigeFragment();
            fragmentDodavanjeKnjige.setVisibility(View.INVISIBLE);

            ft.replace(R.id.mjesto2, fk);

            ListeFragment fl = new ListeFragment();
            ft.replace(R.id.mjesto1, fl, "lista");
            ft.commit();
        }

        else{
            siri = false;
            ListeFragment fragmentLista = (ListeFragment) fm.findFragmentByTag("lista");
            if(fragmentLista == null){
                fragmentLista = new ListeFragment();
                fm.beginTransaction().replace(R.id.mjesto1, fragmentLista, "lista").commit();
            }
            else {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }

    }
}
