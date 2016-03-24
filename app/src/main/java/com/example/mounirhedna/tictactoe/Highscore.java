package com.example.mounirhedna.tictactoe;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Highscore extends AppCompatActivity {

    String scores;
    BufferedReader inputReader;
    StringBuffer stringBuffer;
    ArrayList<Player> players;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        players = new ArrayList<Player>();
        stringBuffer = new StringBuffer();

        try {
            inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput("highscores.txt")));//Reads from file "highscores.txt"
            try {
                while((scores = inputReader.readLine()) != null) {
                    players.add(createPlayer(scores));//Reads each line from file, creates a player
                                                      //and stores in arraylist.
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Player> sortedList = sort(players);

        for(int i = 0; i < sortedList.size(); i++) {//Creates string output for highscore list
            stringBuffer.append(sortedList.get(i).getName()
                     + " - " + sortedList.get(i).getScore() + "\n" );
        }

        Player[] playerArray = new Player[sortedList.size()];
        sortedList.toArray(playerArray);
        String[] playerString = new String[playerArray.length];//Converting Arraylist to Array to
        for(int i = 0; i < playerArray.length; i++) {          // use in adapter
            playerString[i] = playerArray[i].toString();
        }

        adapter = new ArrayAdapter<String>(Highscore.this,
                android.R.layout.simple_list_item_1,
                playerString);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);//Displays highscores in list view

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {//Check screen config
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//Tells user screen config
            message("Changed to Portrait View");
        }
        else {
            message("Changed to Landscape View");
        }
    }

    public void backButtonClicked(View view) {//Runs method when back button is clicked
        finish();
        System.exit(0);
    }

    private void message(String message) {//Displays message to user
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Player createPlayer(String s) {//Converts a string of name and score into player
        String[] nameAndScore = s.split(" ");
        String name = "";
        String score;
        if(nameAndScore.length > 2) {//Handles names with a space
            score = nameAndScore[nameAndScore.length-1];
            for(int i = 0; i < nameAndScore.length-1; i++) {
                name += nameAndScore[i] + " ";
            }
        }
        else {
            name = nameAndScore[0];
            score = nameAndScore[1];
        }
        Player p = new Player(name);
        p.setScore(Integer.parseInt(score));
        return p;
    }

    private ArrayList<Player> sort(ArrayList<Player> p) {//Sorts an array list of players by score
        int numSwaps = 0;                                // from highest to lowest
        for(int i = 0; i < players.size()-1; i++) {
            if(p.get(i).getScore() < p.get(i+1).getScore() ) {
                Player temp = p.get(i);
                p.set(i, p.get(i+1));
                p.set(i+1, temp);
                numSwaps++;
            }
        }
        if(numSwaps == 0) {
            return p;
        }
        else {
            sort(p); //Recursion
        }
        return p;
    }

}
