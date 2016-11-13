

package es.app.alexandercontreras.proyectocat.RecyclerMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.app.alexandercontreras.proyectocat.activity.municipios.Ahuachapan;
import es.app.alexandercontreras.proyectocat.activity.municipios.Cabanas;
import es.app.alexandercontreras.proyectocat.activity.municipios.Chalate;
import es.app.alexandercontreras.proyectocat.activity.municipios.Cusca;
import es.app.alexandercontreras.proyectocat.activity.municipios.LaLibertad;
import es.app.alexandercontreras.proyectocat.activity.municipios.LaPaz;
import es.app.alexandercontreras.proyectocat.activity.municipios.LaUnion;
import es.app.alexandercontreras.proyectocat.activity.municipios.Morazan;
import es.app.alexandercontreras.proyectocat.activity.municipios.SanMiguel;
import es.app.alexandercontreras.proyectocat.activity.municipios.SanVicente;
import es.app.alexandercontreras.proyectocat.activity.municipios.Sansalvador;
import es.app.alexandercontreras.proyectocat.activity.municipios.SantaAna;
import es.app.alexandercontreras.proyectocat.activity.municipios.Sonsonate;
import es.app.alexandercontreras.proyectocat.activity.municipios.Usulutan;
import es.app.alexandercontreras.proyectocat.R;


public class PlaceholderFragment extends Fragment {
    FragmentActivity mActivity;
    RecyclerView mRecyclerView;
    ListAdapterHolder adapter;
    Intent i;
    public PlaceholderFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapter = new ListAdapterHolder(mActivity);
        return rootView;
    }

    @Override
    public void onViewCreated(View view , Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.SetOnItemClickListener(new ListAdapterHolder.OnItemClickListener() {

            @Override
            public void onItemClick(View v , int position) {
                switch (position){
                    case 0:
                        i = new Intent(getActivity(), Sansalvador.class);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(getActivity(), LaLibertad.class);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(getActivity(), Chalate.class);
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(getActivity(), Cusca.class);
                        startActivity(i);
                        break;
                    case 4:
                        Intent in = new Intent(getActivity(), LaPaz.class);
                        startActivity(in);
                        break;
                    case 5:
                        i = new Intent(getActivity(), Cabanas.class);
                        startActivity(i);
                        break;
                    case 6:
                        i = new Intent(getActivity(), SanVicente.class);
                        startActivity(i);
                        break;
                    case 7:
                        i = new Intent(getActivity(), Ahuachapan.class);
                        startActivity(i);
                        break;
                    case 8:
                        i = new Intent(getActivity(), Sonsonate.class);
                        startActivity(i);
                        break;
                    case 9:
                        i = new Intent(getActivity(), SantaAna.class);
                        startActivity(i);
                        break;
                    case 10:
                        i = new Intent(getActivity(), Usulutan.class);
                        startActivity(i);
                        break;
                    case 11:
                        i = new Intent(getActivity(), SanMiguel.class);
                        startActivity(i);
                        break;
                    case 12:
                        i = new Intent(getActivity(), Morazan.class);
                        startActivity(i);
                        break;
                    case 13:
                        i = new Intent(getActivity(), LaUnion.class);
                        startActivity(i);
                        break;

                }
            }
        });
    }

}
