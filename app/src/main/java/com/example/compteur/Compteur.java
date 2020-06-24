package com.example.compteur;

import android.os.Parcel;
import android.os.Parcelable;

public class Compteur implements Parcelable {

    private int compteur;
    private String nom;

    public Compteur(){

    }

    public Compteur(int compteur, String nom) {
        this.compteur = compteur;
        this.nom = nom;
    }

    protected Compteur(Parcel in) {
        compteur = in.readInt();
        nom = in.readString();
    }

    public static final Creator<Compteur> CREATOR = new Creator<Compteur>() {
        @Override
        public Compteur createFromParcel(Parcel in) {
            return new Compteur(in);
        }

        @Override
        public Compteur[] newArray(int size) {
            return new Compteur[size];
        }
    };

    public int getCompteur() {
        return compteur;
    }

    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString(){
        return this.nom + this.compteur;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(compteur);
        parcel.writeString(nom);
    }
}
