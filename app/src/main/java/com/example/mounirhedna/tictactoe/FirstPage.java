package com.example.mounirhedna.tictactoe;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FirstPage extends AppCompatActivity {

    Player player1, player2;//Creating players to be initialised in playGame()
    EditText player1Name, player2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) { //Checks configuration
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//Tells user screen config
            message("Changed to Portrait View");
            setContentView(R.layout.activity_first_page);
        }
        else {
            message("Changed to Landscape View");
            setContentView(R.layout.activity_first_page);

        }
    }

    public void playButtonMethod(View view) {//Creates method for play button in XML
        playGame();
    }

    public void highscoreButtonMethod(View view) {//Creates method for highscore button in XML
        Intent nextPage = new Intent(FirstPage.this, Highscore.class);
        startActivity(nextPage);
    }

    public void quitButtonMethod(View view) { //Creates method for quit button in XML
        finish();
        System.exit(0);
    }

    private void message(String message) { //Outputs a message to user
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void playGame(){

        player1Name = (EditText) findViewById(R.id.player1);//Create player 1
        player1 = new Player(player1Name.getText().toString());


        player2Name = (EditText) findViewById(R.id.player2);//Create player 2
        player2 = new Player(player2Name.getText().toString());

        Intent nextPage = new Intent(FirstPage.this, MainActivity.class);//Link to next page

        Bundle bundle = new Bundle();//Stores players to move into next page
        bundle.putParcelable("player1", player1);
        bundle.putParcelable("player2", player2);
        nextPage.putExtras(bundle);

        startActivity(nextPage);//Moves players to next page
    }

}
