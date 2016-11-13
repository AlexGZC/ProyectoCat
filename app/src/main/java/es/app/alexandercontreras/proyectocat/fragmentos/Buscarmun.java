package es.app.alexandercontreras.proyectocat.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.app.alexandercontreras.proyectocat.R;
import es.app.alexandercontreras.proyectocat.RecyclerMenu.MainActivity;


public class Buscarmun extends Fragment implements View.OnClickListener{
    Button btn;
    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.busc, container, false);
        btn = (Button) rootview.findViewById(R.id.btn_1);
        btn.setOnClickListener(this);
        return  rootview;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_1:
                Intent inte = new Intent(getActivity(),MainActivity
                        .class);
                startActivity(inte);
                break;
        }
    }
}
