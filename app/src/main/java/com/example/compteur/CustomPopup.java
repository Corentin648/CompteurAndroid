package com.example.compteur;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomPopup extends Dialog {

    //fields
    private String title;
    private Button yesButton, noButton;
    private TextView titleView, nomView, compteurView;
    private EditText valeurNom, valeurCompteur;



    public CustomPopup(Activity activity) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.my_popup_template);

        this.title = "Mon titre";
        this.yesButton = findViewById(R.id.yesButton);
        this.noButton = findViewById(R.id.noButton);
        this.titleView = findViewById(R.id.title);
        this.nomView = findViewById(R.id.nouveauNom);
        this.compteurView = findViewById(R.id.nomCompteur);
        this.valeurNom = findViewById(R.id.valeurNouveauNom);
        this.valeurCompteur = findViewById(R.id.valeurNouveauCompteur);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Button getYesButton() {
        return yesButton;
    }

    public Button getNoButton() {
        return noButton;
    }

    public void build() {
        show();
        titleView.setText(this.title);
    }

    public EditText getValeurNom() {
        return valeurNom;
    }

    public EditText getValeurCompteur() {
        return valeurCompteur;
    }
}

