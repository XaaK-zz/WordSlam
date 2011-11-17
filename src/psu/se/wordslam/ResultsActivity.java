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
import android.widget.Toast;

public class ResultsActivity extends Activity implements OnClickListener {
	private Button				mainMenu;
	private WordSlamApplication wordSlamApplication;
	private Typeface 			tf; 


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        tf = Typeface.createFromAsset(getAssets(),"fonts/COMIXHVY.TTF");
        wordSlamApplication = (WordSlamApplication)getApplicationContext();
        if (wordSlamApplication.GetGame().GetGameType() == GameType.MultiPlayer) {        	
        	setContentView(R.layout.resultstwo);
        	twoPlayer();
        } else {
        	setContentView(R.layout.results);
        	singlePlayer();
        }
    }

    
    private void singlePlayer() {	
    	TextView			tvFoundWords;	
    	TextView			tvMissedWords;
    	ArrayList<String> 	foundWords;
    	ArrayList<String> 	allWords;
    	
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
		
		// remove all found words from "allWords" list to get missed words
		allWords = wordSlamApplication.m_Game.getAllWords();
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
    	TextView			tvYourFoundWords;	
    	TextView			tvTheirFoundWords;
    	ArrayList<String>	yourFoundWords;
    	ArrayList<String>	theirFoundWords;
    	
    	mainMenu = (Button) findViewById(R.id.btnMainMenu);
		mainMenu.setOnClickListener(this);
		tvYourFoundWords = (TextView) findViewById(R.id.tvResultsYourFound);
		tvTheirFoundWords = (TextView) findViewById(R.id.tvResultsTheirFound);
    	
		// set font
		tvYourFoundWords.setTypeface(tf);
		tvYourFoundWords.setTypeface(tf);
		TextView titleYourFound = (TextView) findViewById(R.id.tvYourWordsFoundTitle);
		titleYourFound.setTypeface(tf);
		TextView titleTheirFound = (TextView) findViewById(R.id.tvTheirWordsFoundTitle);
		titleTheirFound.setTypeface(tf);
		
		// Display found words
		yourFoundWords = wordSlamApplication.m_Game.getFoundWords();
		// write to results screen
		for (String s: yourFoundWords) {
			tvYourFoundWords.append(s + "\n"); // add to textview
		}
		
		theirFoundWords = wordSlamApplication.m_Game.getOpponentFoundWords();
		for (String s: theirFoundWords) {
			tvTheirFoundWords.append(s + "\n");	// add to textview
		}
		
		// Display scores
		TextView yourScore = (TextView) findViewById(R.id.tvYourScore);
		yourScore.setTypeface(tf);
		Integer x = wordSlamApplication.m_Game.getScore();
		yourScore.setText(x.toString());
		
		TextView theirScore = (TextView) findViewById(R.id.tvTheirScore);
		theirScore.setTypeface(tf);
		Integer x2 = wordSlamApplication.m_Game.GetOpponentScore();
		theirScore.setText(x2.toString());
		
		// Determine winner 
		LinearLayout winner;
		if (x > x2) {
			winner = (LinearLayout) findViewById(R.id.linearLayoutYourScore);
			winner.setBackgroundResource(R.drawable.winner);
			winner.setVisibility(View.VISIBLE);
		} else if (x < x2) {
			winner = (LinearLayout) findViewById(R.id.linearLayoutTheirScore);
			winner.setBackgroundResource(R.drawable.winner);
			winner.setVisibility(View.VISIBLE);
		} else {  			// Tie game
			Toast.makeText(this, "Tie Game!", Toast.LENGTH_SHORT).show();
		}

    }
    
    
    @Override
	public void onClick(View v) {
    	Intent intent = new Intent(this, MainMenuActivity.class);
		startActivity(intent);
    }

}
