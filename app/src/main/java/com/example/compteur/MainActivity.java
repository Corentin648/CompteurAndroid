package com.example.compteur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public FirebaseDatabase database;
    CustomListAdapter customListAdapter;
    ListView listView;
    List<Compteur> laListe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.database = FirebaseDatabase.getInstance();

        setContentView(R.layout.activity_main);

        this.laListe = new ArrayList<Compteur>();

        //database.getReference().child("compteur-3c180").push().setValue(new Compteur(0,"bananes"));


        /* On parcourt la liste des compteurs présents dans la bdd */
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Compteur comp = snapshot.getValue(Compteur.class);
                    laListe.add(comp);
                    System.out.println(laListe.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /* On met en place la ToolBar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //List<Compteur> autreListe = this.getListData();
        this.listView = (ListView) findViewById(R.id.listView);


        this.customListAdapter = new CustomListAdapter(this, this.laListe, this);


        this.listView.setAdapter(this.customListAdapter);


        /* Supprimer un compteur par un clic long */
        this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            Compteur compteurASupprimer;

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                compteurASupprimer = (Compteur) adapterView.getAdapter().getItem(position);

                AlertDialog.Builder myPopup = new AlertDialog.Builder(MainActivity.this);
                myPopup.setTitle("Yo la zone");
                myPopup.setMessage("Voulez-vous supprimer ce compteur ?");
                myPopup.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        customListAdapter.removeData(compteurASupprimer);
                        listView.setAdapter(customListAdapter);

                        database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    Compteur comp = snapshot.getValue(Compteur.class);
                                    if (compteurASupprimer.getNom().equals(comp.getNom()) && compteurASupprimer.getCompteur() == comp.getCompteur()){
                                        snapshot.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getApplicationContext(), "Compteur supprimé", Toast.LENGTH_SHORT).show();
                    }
                });
                myPopup.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Annulation de suppression", Toast.LENGTH_SHORT).show();
                    }
                });
                myPopup.show();
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Associer le menu à la toolBar */
        getMenuInflater().inflate(R.menu.main_menu_activity, menu);
        return true;
    }

    /**
     * Ajouter des actions aux items du menu de la ToolBar
     * @param item
     * @return booleen
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            /* Cas du bouton Supprimer */
            case R.id.menu_activity_main_params:
                Toast.makeText(this, "Il n'y a rien à paramétrer ici, passez votre chemin...", Toast.LENGTH_LONG).show();
                this.customListAdapter.addData(new Compteur(0, "applis"));
                this.listView.setAdapter(this.customListAdapter);
                return true;

            /* Cas du bouton Ajouter */
            case R.id.menu_activity_main_search:
                final CustomPopup customPopup = new CustomPopup(MainActivity.this);
                customPopup.setTitle("Ajouter un nouveau compteur");
                customPopup.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nouveauNom = customPopup.getValeurNom().getText().toString();
                        int nouveauCompteur = Integer.parseInt(customPopup.getValeurCompteur().getText().toString());
                        customListAdapter.addData(new Compteur(nouveauCompteur, nouveauNom));

                        /* Ajout du compteur à la base de données */
                        database.getReference().push().setValue(new Compteur(nouveauCompteur, nouveauNom));

                        /* Mise à jour de la ListView */
                        listView.setAdapter(customListAdapter);

                        /* Affichage d'un Toast et fermeture du popup */
                        Toast.makeText(getApplicationContext(), "Nouveau compteur ajouté", Toast.LENGTH_SHORT).show();
                        customPopup.dismiss();
                    }
                });
                customPopup.getNoButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Annulation d'ajout", Toast.LENGTH_SHORT).show();
                        customPopup.dismiss();
                    }
                });
                customPopup.build();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private List<Compteur> getListData(){
        List<Compteur> list = new ArrayList<Compteur>();

        Compteur compteur1 = new Compteur(0,"bananes");
        Compteur compteur2 = new Compteur(0, "pompes");

        list.add(compteur1);
        list.add(compteur2);

        return list;
    }

}