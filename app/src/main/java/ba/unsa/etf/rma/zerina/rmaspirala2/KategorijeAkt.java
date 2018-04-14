package ba.unsa.etf.rma.zerina.rmaspirala2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

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
            FragmentKnjige fk = new FragmentKnjige();
            fragmentDodavanjeKnjige.setVisibility(View.INVISIBLE);

            ft.replace(R.id.mjesto2, fk);

            FragmentLista fl = new FragmentLista();
            ft.replace(R.id.mjesto1, fl, "lista");
            ft.commit();
        }

       else{
            siri = false;
            FragmentLista fragmentLista = (FragmentLista) fm.findFragmentByTag("lista");
            if(fragmentLista == null){
                fragmentLista = new FragmentLista();
                fm.beginTransaction().replace(R.id.mjesto1, fragmentLista, "lista").commit();
            }
            else {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }
}
