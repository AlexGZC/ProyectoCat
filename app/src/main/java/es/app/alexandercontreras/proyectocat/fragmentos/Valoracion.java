package es.app.alexandercontreras.proyectocat.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.app.alexandercontreras.proyectocat.R;
import es.app.alexandercontreras.proyectocat.valoracion.MainActivity;


public class Valoracion extends Fragment implements View.OnClickListener{
    Button btnplaya;
    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.valoracion, container, false);

        btnplaya = (Button) rootview.findViewById(R.id.btn_1);
        btnplaya.setOnClickListener(this);
        return  rootview;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_1:
                Intent inte = new Intent(getActivity(),MainActivity.class);
                startActivity(inte);
                break;
        }
    }
}
