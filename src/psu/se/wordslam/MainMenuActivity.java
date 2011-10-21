/*
Copyright (c) 2011 Sarah Cathey, Zachary Greenvoss, Vidhya Priyadharshnee, 
Aparna Sawant.
This project is protected under the < > license. 
Please see COPYING file in the distribution for license terms.
*/

package psu.se.wordslam;

import psu.se.wordslam.model.*;
import psu.se.wordslam.model.Game.GameType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

// NOTE: switching emulator to landscape mode: fn+ctrl+f11
//
// Main Menu -> Game Setup -> Game Board -> Final results
public class MainMenuActivity extends Activity implements OnClickListener {
	private static final String 		TAG = "WSMainMenu"; // for logging
	private static final int			REQUEST_SPG = 0; // single player game
	private static final int			REQUEST_TPG = 1; // two player game
	
	private Button 		btnNewOneGame;
	private Button		btnNewTwoGame;
	// considered bad practice to have quit button...
	//private Button		btnQuit;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnNewOneGame = (Button) findViewById(R.id.btnNewOneGame);
        btnNewOneGame.setOnClickListener(this);
        btnNewTwoGame = (Button) findViewById(R.id.btnNewTwoGame);
        btnNewTwoGame.setOnClickListener(this);
        //btnQuit = (Button) findViewById(R.id.btnQuit);
        //btnQuit.setOnClickListener(this);
        
    }
    
    
	// Single onClick handler, uses switch statement to determine which
	// button was clicked. 
	@Override
	public void onClick(View v) {
		WordSlamApplication wordSlamApplication = (WordSlamApplication)getApplicationContext();
		switch(v.getId()) {
			case R.id.btnNewOneGame:	
				wordSlamApplication.CreateNewGame(GameType.SinglePlayer);
	    		Intent gameIntent = new Intent(this, SingleGameActivity.class);
	    		startActivityForResult(gameIntent, REQUEST_SPG);
				break;
			case R.id.btnNewTwoGame:
				wordSlamApplication.CreateNewGame(GameType.MultiPlayer);
				// STUB
				break;
			//case R.id.btnQuit:
				//finish();
		}
    }
/*	
	
	// Reset main menu
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		setContentView(R.layout.main);
        
        btnNewOneGame = (Button) findViewById(R.id.btnNewOneGame);
        btnNewOneGame.setOnClickListener(this);
        btnNewTwoGame = (Button) findViewById(R.id.btnNewTwoGame);
        btnNewTwoGame.setOnClickListener(this);
        btnQuit = (Button) findViewById(R.id.btnQuit);
        btnQuit.setOnClickListener(this);
	}*/
}
