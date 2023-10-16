package com.example.app_firebase_yt;

import android.os.Parcel;
import android.os.Parcelable;

public class Luchador implements Parcelable {

    private int id;
    private String nombre;
    public Luchador() {
    }

    public Luchador(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return nombre;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nombre);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.nombre = source.readString();
    }

    protected Luchador(Parcel in) {
        this.id = in.readInt();
        this.nombre = in.readString();
    }

    public static final Parcelable.Creator<Luchador> CREATOR = new Parcelable.Creator<Luchador>() {
        @Override
        public Luchador createFromParcel(Parcel source) {
            return new Luchador(source);
        }

        @Override
        public Luchador[] newArray(int size) {
            return new Luchador[size];
        }
    };
}
