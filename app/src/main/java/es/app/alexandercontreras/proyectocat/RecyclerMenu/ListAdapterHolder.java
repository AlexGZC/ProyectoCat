/*
 * Copyright (C) 2014 VenomVendor <info@VenomVendor.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package es.app.alexandercontreras.proyectocat.RecyclerMenu;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.app.alexandercontreras.proyectocat.R;

public class ListAdapterHolder extends RecyclerView.Adapter<ListAdapterHolder.ViewHolder> {

    private final FragmentActivity mActivity;
    private final List<Municipios> mUserDetails = new ArrayList<Municipios>();
    OnItemClickListener mItemClickListener;

    public ListAdapterHolder(FragmentActivity mActivity) {
        this.mActivity = mActivity;
        municipios();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.single_list_item, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder , int position) {
        holder.depar.setText("" + mUserDetails.get(position).getName());
        holder.zona.setText("Zona: " + mUserDetails.get(position).getZona());
    }

    @Override
    public int getItemCount() {
        return mUserDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView depar, zona;
        public ViewHolder(View view) {
            super(view);

            depar = (TextView) view.findViewById(R.id.list_name);
            zona = (TextView) view.findViewById(R.id.zona);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private void municipios() {
        ArrayList<String> mun = new ArrayList<>();
        mun.add("San Salvador");
        mun.add("La Libertad");
        mun.add("Chalatenango");
        mun.add("Cuscatlán");
        mun.add("La Paz");
        mun.add("Cabanas");
        mun.add("San Vicente");
        mun.add("Ahuachapán");
        mun.add("Sonsonate");
        mun.add("Santa Ana");
        mun.add("Usulután");
        mun.add("San Miguel");
        mun.add("Morazán");
        mun.add("La Unión");


        for (int i = 0; i < 14; i++) {
            final Municipios mDetails = new Municipios();
                mDetails.setName(""+mun.get(i));
            if(i>=0 && i<=6){
                mDetails.setZona("Central");

            }else if((i>=7 && i<=9)){
                mDetails.setZona("Occidental");
            }else if(i>=10 && i<=13){
                mDetails.setZona("Oriental");
            }
            mUserDetails.add(mDetails);
        }
    }


}
