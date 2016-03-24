package com.example.mounirhedna.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    Button a1, a2, a3, b1, b2, b3, c1, c2, c3, newGame, endGame, quitGame; //Creating button variables.
    Button[] buttons;
    boolean xTurn = true; //True = X's turn, False = O's turn.
    boolean winnerFound = false;
    boolean gameOver = false;
    int numTurns = 0;
    int draws = 0;
    Bundle bundle;
    Player player1;
    Player player2;
    TextView playerTurn;
    TextView p1Name;
    TextView p2Name;
    TextView player1Score;
    TextView player2Score;
    TextView drawScore;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(),"Xpressive Bold.ttf");
        TextView myTextview = (TextView)findViewById(R.id.playerTurn );

        myTextview.setTypeface(myTypeface);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle = this.getIntent().getExtras();//Create players from previous activity
        player1 = bundle.getParcelable("player1");
        player2 = bundle.getParcelable("player2");

        playerTurn = (TextView) findViewById(R.id.playerTurn);//Initialises to player1 turn
        playerTurn.setText(player1.getName() + "'s Turn");

        p1Name = (TextView) findViewById(R.id.p1Name);//Sets up player names
        p1Name.setText(player1.getName());
        player1Score = (TextView) findViewById(R.id.p1Score);
        player1Score.setText(Integer.toString(player1.getScore()));

        p2Name = (TextView) findViewById(R.id.p2Name);
        p2Name.setText(player2.getName());
        player2Score = (TextView) findViewById(R.id.p2Score);
        player2Score.setText(Integer.toString(player2.getScore()));

        drawScore = (TextView) findViewById(R.id.drawScore);//Initialises number of draws
        drawScore.setText(Integer.toString(draws));

        a1 = (Button) findViewById(R.id.A1); // Linking the button variables
        a2 = (Button) findViewById(R.id.A2); //by ID with buttons created.
        a3 = (Button) findViewById(R.id.A3);
        b1 = (Button) findViewById(R.id.B1);
        b2 = (Button) findViewById(R.id.B2);
        b3 = (Button) findViewById(R.id.B3);
        c1 = (Button) findViewById(R.id.C1);
        c2 = (Button) findViewById(R.id.C2);
        c3 = (Button) findViewById(R.id.C3);
        newGame = (Button) findViewById(R.id.newGame);
        endGame = (Button) findViewById(R.id.endGame);
        quitGame = (Button) findViewById(R.id.quitGame);

        buttons = new Button[]{a1, a2, a3, b1, b2, b3, c1, c2, c3}; //Button array to hold all
                                                                    //buttons created.

        /*for (Button b : buttons) {
            b.setOnClickListener(new View.OnClickListener() { //Creates and on click listener for
                                                              //each button in the array.
                @Override
                public void onClick(View v) {//This method is called every time a button is clicked.
                    Button b = (Button) v; //Button is a subclass of view so can be type casted.
                    buttonClicked(b);
                }
            });
        }*/
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Ends game and write player name and score to file
                String scores = "";
                scores += player1.getName().toString() + " " + Integer.toString(player1.getScore()) + "\n";
                scores += player2.getName().toString() + " " + Integer.toString(player2.getScore()) + "\n";
                FileOutputStream fos = null;

                try {
                    fos = openFileOutput("highscores.txt", Context.MODE_APPEND);
                    try {
                        fos.write(scores.getBytes());
                        fos.flush();
                        fos.close();     //Close file writer
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //Allows user to move to highscores page
                Intent nextPage = new Intent(MainActivity.this, Highscore.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("player1", player1);
                bundle.putParcelable("player2", player2);
                nextPage.putExtras(bundle);

                startActivity(nextPage);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {//Check screen config
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            message("Changed to Portrait View");
            setContentView(R.layout.activity_main);
        }

        else {
            message("Changed to Landscape View");
            setContentView(R.layout.activity_main);
        }
    }

    public void buttonClick(View view) {//Performs buttonclicked method when button is clicked
        buttonClicked((Button) view);
    }

    public void backButtonClick(View view) {//Goes back when back button is clicked
        finish();
        System.exit(0);
    }

    public void resetGame() {//Resets game and removes all X's and O's from screen
        for(Button b : buttons) {
            b.setText("");
            b.setClickable(true);
        }
        xTurn = true;
        winnerFound = false;
        gameOver = false;
        numTurns = 0;
        playerTurn.setText(player1.getName() + "'s Turn");
    }

    public void buttonClicked(Button b) {

        if(xTurn) { //Change text to X.
            if(b.getText() == "X" || b.getText() == "O") { //Checks if box is chosen
                message("Box already chosen!");
            }
            else {
                b.setText("X");
                numTurns++;
                nextTurn();
            }
        }
        else { //Change text to O.
            if(b.getText() == "X" || b.getText() == "O") {
                message("Box already chosen!");
            }
            else {
                b.setText("O");
                numTurns++;
                nextTurn();
            }
        }

        checkForWinner(); //Checks if player has won.
        checkForDraw();//Checks is the game is a draw.

        if(winnerFound) { //Congratulates the winner.
            if(xTurn) {
                message(player2.getName() + " Wins!");
                player2.win();
                player2Score.setText(Integer.toString(player2.getScore()));
                endGame();
            }
            else {
                message(player1.getName() + " Wins!");
                player1.win();
                player1Score.setText(Integer.toString(player1.getScore()));
                endGame();
            }
        }
    }

    private void message(String message) {//Message pops up for user
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void nextTurn() {//Switches to next players turn
        if(xTurn) {
            xTurn = !xTurn;
            playerTurn.setText(player2.getName() + "'s Turn");
        }
        else {
            xTurn = !xTurn;
            playerTurn.setText(player1.getName() + "'s Turn");
        }
    }

    private void checkForWinner() {//Checks if there are 3 X's or O's in a row/column/diagonal

        if(a1.getText() == a2.getText() && a1.getText() == a3.getText() && a1.getText() != "") {
            winnerFound = true;
        }
        else if(b1.getText() == b2.getText() && b1.getText() == b3.getText() && b1.getText() != ""){
            winnerFound = true;
        }
        else if(c1.getText() == c2.getText() && c1.getText() == c3.getText() && c1.getText() != ""){
            winnerFound = true;
        }
        else if(a1.getText() == b1.getText() && a1.getText() == c1.getText() && a1.getText() != ""){
            winnerFound = true;
        }
        else if(a2.getText() == b2.getText() && a2.getText() == c2.getText() && a2.getText() != ""){
            winnerFound = true;
        }
        else if(a3.getText() == b3.getText() && a3.getText() == c3.getText() && a3.getText() != ""){
            winnerFound = true;
        }
        else if(a1.getText() == b2.getText() && a1.getText() == c3.getText() && a1.getText() != ""){
            winnerFound = true;
        }
        else if(a3.getText() == b2.getText() && a3.getText() == c1.getText() && a3.getText() != ""){
            winnerFound = true;
        }
    }

    private void endGame() {//Sets all buttons to unclickable
        for(Button b : buttons) {
            b.setClickable(false);
        }
    }

    private void checkForDraw() { //Checks if board is full and there is no winner
        if(numTurns == 9 && !winnerFound) {
            message("Draw!");
            draws++;
            drawScore.setText(Integer.toString(draws));
            endGame();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
