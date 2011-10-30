/*
Copyright (c) 2011 Sarah Cathey, Zachary Greenvoss, Vidhya Priyadharshnee, 
Aparna Sawant.
This project is protected under the < > license. 
Please see COPYING file in the distribution for license terms.
*/

package psu.se.wordslam;

import java.util.ArrayList;

import psu.se.wordslam.model.WordSlamApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends Activity implements OnClickListener {
	private Button				mainMenu;
	private TextView			tvFoundWords;
	private TextView			tvAllWords;
	private WordSlamApplication wordSlamApplication;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
     
        wordSlamApplication = (WordSlamApplication)getApplicationContext();
        
		mainMenu = (Button) findViewById(R.id.btnMainMenu);
		mainMenu.setOnClickListener(this);
		tvFoundWords = (TextView) findViewById(R.id.tvResults);
		tvAllWords = (TextView) findViewById(R.id.tvResultsMissed);
		
		

		// for each string in found words
		ArrayList<String> foundWords = wordSlamApplication.m_Game.getFoundWords();
		// write to results screen
		for (String s: foundWords) {
			tvFoundWords.append(s + "\n"); // add to textview
		}
		
    }
    
    @Override
	public void onClick(View v) {
    	Intent intent = new Intent(this,MainMenuActivity.class);
		startActivity(intent);
    }

}
