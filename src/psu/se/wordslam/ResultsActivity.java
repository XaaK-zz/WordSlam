/*
Copyright (c) 2011 Sarah Cathey, Zachary Greenvoss, Vidhya Priyadharshnee, 
Aparna Sawant.
This project is protected under the < > license. 
Please see COPYING file in the distribution for license terms.
*/

package psu.se.wordslam;

import java.util.ArrayList;

import psu.se.wordslam.model.WordSlamApplication;
import psu.se.wordslam.model.Game.GameType;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultsActivity extends Activity implements OnClickListener {
	private Button				mainMenu;
	private TextView			tvFoundWords;	
	private TextView			tvMissedWords;
	private WordSlamApplication wordSlamApplication;
	private Typeface 			tf; 
	private ArrayList<String> 	foundWords;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        tf = Typeface.createFromAsset(getAssets(),"fonts/COMIXHVY.TTF");
        wordSlamApplication = (WordSlamApplication)getApplicationContext();
        if (wordSlamApplication.GetGame().GetGameType() == GameType.MultiPlayer) {        	
        	setContentView(R.layout.gridtwo);
        	sharedLayout();
        	twoPlayer();
        } else {
        	setContentView(R.layout.results);
        	sharedLayout();
        	singlePlayer();
        }
    }
    
    
    private void sharedLayout() {
		mainMenu = (Button) findViewById(R.id.btnMainMenu);
		mainMenu.setOnClickListener(this);
		tvFoundWords = (TextView) findViewById(R.id.tvWordsFoundList);
		tvMissedWords = (TextView) findViewById(R.id.tvResultsMissedList);
		
		
		// set font
		tvFoundWords.setTypeface(tf);
		tvMissedWords.setTypeface(tf);
		TextView titleFound = (TextView) findViewById(R.id.tvWordsFoundTitle);
		titleFound.setTypeface(tf);
		TextView titleMissed = (TextView) findViewById(R.id.tvWordsMissedTitle);
		titleMissed.setTypeface(tf);
		
		TextView score = (TextView) findViewById(R.id.tvScore);
		score.setTypeface(tf);
		Integer x = wordSlamApplication.m_Game.getScore();
		score.setText(x.toString());
		
		// for each string in found words
		foundWords = wordSlamApplication.m_Game.getFoundWords();
		// write to results screen
		for (String s: foundWords) {
			tvFoundWords.append(s + "\n"); // add to textview
		}
    }
    
    
    private void singlePlayer() {	
		// remove all found words from "allWords" list to get missed words
		ArrayList<String> allWords = wordSlamApplication.m_Game.getAllWords();
		for (String s: foundWords) {
			allWords.remove(s);
		}
		// If all words on the board were found by the player, signal bonus
		if (allWords.isEmpty()) {
			TextView tvBonus = (TextView) findViewById(R.id.tvBonus);
			tvBonus.setBackgroundResource(R.drawable.bonus);
			tvBonus.setVisibility(View.VISIBLE);
		} else {
			for (String s: allWords) {
				tvMissedWords.append(s + "\n"); // add to textview
				TextView tvBonus = (TextView) findViewById(R.id.tvBonus);
				tvBonus.setVisibility(View.INVISIBLE);
			}
		}
    }
    
    
    private void twoPlayer() {
		TextView theirScore = (TextView) findViewById(R.id.tvTheirScore);
		theirScore.setTypeface(tf);
		// Integer x2 = get opponents score
		// theirScore.setText(x2.toString());
		
		// Determine winner
		/* 
		LinearLayout winner;
		if (x > x2) {
			winner = (LinearLayout) findViewById(R.id.linearLayoutYourScore);
			winner.setBackgroundDrawable(R.drawable.winner);
		} else if (x < x2) {
			winner = (LinearLayout) findViewById(R.id.linearLayoutTheirScore);
			winner.setBackgroundDrawable(R.drawable.winner);
		} else {  			// Tie game
			// ??? toast?
		}
		
		ArrayList<String> theirWords = get their found words
		for (String s: theirWords) {
			tvMissedWords.append(s + "\n");	// add to textview
		}
		*/
    }
    
    
    @Override
	public void onClick(View v) {
    	Intent intent = new Intent(this, MainMenuActivity.class);
		startActivity(intent);
    }

}
