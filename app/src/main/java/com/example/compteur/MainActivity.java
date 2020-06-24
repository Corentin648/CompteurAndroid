package com.example.compteur;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    static final String LISTE_COMPTEUR = "test";

    CustomListAdapter customListAdapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myRef.setValue("Mange tes os");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //List<Compteur> laListe = this.getListData();
        List<Compteur> laListe = new ArrayList<Compteur>();
        this.listView = (ListView) findViewById(R.id.listView);
        this.customListAdapter = new CustomListAdapter(this, laListe);

        try {
            super.onRestoreInstanceState(savedInstanceState);
            Parcelable[] parcelables = savedInstanceState.getParcelableArray(LISTE_COMPTEUR);
            for(int j = 0; j < parcelables.length; j++){
                this.customListAdapter.addData((Compteur) parcelables[j]);
            }
        } catch (NullPointerException e){

        }

        this.listView.setAdapter(this.customListAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        System.out.println("le test du q");
        Parcelable[] parcelables = new Parcelable[30];
        int i = 0;
        for (Compteur compteur : this.customListAdapter.getListData()){
            parcelables[i] = compteur;
            i++;
        }
        savedInstanceState.putParcelableArray(LISTE_COMPTEUR, parcelables);
        super.onSaveInstanceState(savedInstanceState);
    }

    /*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Parcelable[] parcelables = savedInstanceState.getParcelableArray(LISTE_COMPTEUR);

        for(int j = 0; j < parcelables.length; j++){
            this.customListAdapter.addData((Compteur) parcelables[j]);
        }
    }
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Associer le menu à la toolBar */
        getMenuInflater().inflate(R.menu.main_menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Ajouter des actions aux items du menu de la toolBar */
        switch (item.getItemId()) {
            case R.id.menu_activity_main_params:
                Toast.makeText(this, "Il n'y a rien à paramétrer ici, passez votre chemin...", Toast.LENGTH_LONG).show();
                this.customListAdapter.addData(new Compteur(0, "applis"));
                this.listView.setAdapter(this.customListAdapter);
                return true;
            case R.id.menu_activity_main_search:
                final CustomPopup customPopup = new CustomPopup(MainActivity.this);
                customPopup.setTitle("Ajouter un nouveau compteur");
                customPopup.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nouveauNom = customPopup.getValeurNom().getText().toString();
                        int nouveauCompteur = Integer.parseInt(customPopup.getValeurCompteur().getText().toString());
                        customListAdapter.addData(new Compteur(nouveauCompteur, nouveauNom));
                        listView.setAdapter(customListAdapter);
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