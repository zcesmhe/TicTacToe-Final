package com.example.mounirhedna.tictactoe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mounir Hedna on 16/03/2016.
 */
public class Player implements Parcelable{

    private String name;
    private int score = 0;

    public Player(String name) {
        this.name = name;
    }

    protected Player(Parcel in) {//Parcel constructor for bundle
        name = in.readString();
        score = in.readInt();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {//Creates player from parcel
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public void win() {
        score++;
    } //Increases score when player wins

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int n) {
        score = n;
    }

    public String toString() { //Return name and score as string
        String s = name + " - " + Integer.toString(score);
        return s;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) { //Creates a parcel from player
        dest.writeString(name);
        dest.writeInt(score);
    }
}
