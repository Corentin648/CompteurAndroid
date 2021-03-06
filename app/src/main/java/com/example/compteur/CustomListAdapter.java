package com.example.compteur;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private List<Compteur> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private MainActivity activity;

    public CustomListAdapter(Context aContext,  List<Compteur> listData, MainActivity activity) {
        this.context = aContext;
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(aContext);
        this.activity = activity;
    }

    public List<Compteur> getListData() {
        return listData;
    }

    public void addData(Compteur nouveauCompteur){
        this.listData.add(nouveauCompteur);
    }

    public void removeData(Compteur aSupprimer){
        this.listData.remove(aSupprimer);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.compteur_barre, null);
            holder = new ViewHolder();
            holder.nomCompteur = (TextView) convertView.findViewById(R.id.nomCompteur);
            holder.boutonPlus = (Button) convertView.findViewById(R.id.boutonPlus);
            holder.compteur = (TextView) convertView.findViewById(R.id.compteur);
            holder.boutonMoins = (Button) convertView.findViewById(R.id.boutonMoins);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Compteur compt = this.listData.get(position);

        holder.nomCompteur.setText(compt.getNom());

        holder.boutonPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                compt.setCompteur(compt.getCompteur()+1);
                holder.compteur.setText(String.valueOf(compt.getCompteur()));
                activity.database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Compteur compteur = snapshot.getValue(Compteur.class);
                            if (compt.getNom().equals(compteur.getNom()) && compt.getCompteur()-1 == compteur.getCompteur()){
                                snapshot.getRef().removeValue();
                                activity.database.getReference().push().setValue(new Compteur(compteur.getCompteur()+1, compteur.getNom()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        holder.compteur.setText(String.valueOf(compt.getCompteur()));

        holder.boutonMoins.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (compt.getCompteur() > 0) {
                    compt.setCompteur(compt.getCompteur() - 1);
                    holder.compteur.setText(String.valueOf(compt.getCompteur()));
                } else {
                    Toast.makeText(context, "Le compteur est déjà à 0", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView nomCompteur;
        Button boutonPlus;
        TextView compteur;
        Button boutonMoins;
    }
}
