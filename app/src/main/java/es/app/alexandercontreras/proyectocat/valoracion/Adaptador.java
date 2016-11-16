package es.app.alexandercontreras.proyectocat.valoracion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import es.app.alexandercontreras.proyectocat.R;

public class Adaptador extends
        RecyclerView.Adapter<Adaptador.TeamViewHolder> {
    ArrayList<Sitios> sitioses = new ArrayList<>();
    Context context;

    public Adaptador(ArrayList<Sitios> sitioses, Context context){
        this.sitioses = sitioses;
        this.context = context;
    }
    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false
                );
        return new TeamViewHolder(row);
    }
    @Override
    public void onBindViewHolder(TeamViewHolder holder, final int position) {
        Sitios sitios = sitioses.get(position);
        holder.teamDescription.setText(sitios.getDescripcion());
        holder.teamName.setText(sitios.getNombre());
        holder.estre.setRating(sitios.getEstrellas());
    }
    @Override
    public int getItemCount() {
        return sitioses.size();
    }
    public class TeamViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageLogo;
        private TextView teamName;
        private TextView teamDescription;
        private RatingBar estre;
        public TeamViewHolder(View itemView) {
            super(itemView);

            teamName = (TextView) itemView.findViewById(R.id.nombre);
            teamDescription = (TextView)
                    itemView.findViewById(R.id.teamDescription);
            estre = (RatingBar) itemView.findViewById(R.id.estrella);
        }
        public ImageView getImageLogo() {
            return imageLogo;
        }
        public void setImageLogo(ImageView imageLogo) {
            this.imageLogo = imageLogo;
        }
        public TextView getTeamName() {
            return teamName;
        }
        public void setTeamName(TextView teamName) {
            this.teamName = teamName;
        }
        public TextView getTeamDescription() {
            return teamDescription;
        }
        public void setTeamDescription(TextView teamDescription) {
            this.teamDescription = teamDescription;

        }
    }
}