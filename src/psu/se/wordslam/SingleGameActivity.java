/*
Copyright (c) 2011 Sarah Cathey, Zachary Greenvoss, Vidhya Priyadharshnee, 
Aparna Sawant.
This project is protected under the < > license. 
Please see COPYING file in the distribution for license terms.
*/

package psu.se.wordslam;

import psu.se.wordslam.model.WordSlamApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SingleGameActivity extends Activity implements OnClickListener {
	private static final String 	TAG = "WSSinglePlayer";
	private static final int		REQUEST_RESULTS = 0;
	

	private Button				submitWord;
	private Button				submitGame;
	private TextView			wordsFound;
	private String				aWord = "";
	private WordSlamApplication wordSlamApplication;
	//private int				possiblePoints;
	//private int				totalPoints;
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordSlamApplication = (WordSlamApplication)getApplicationContext();
		
        //setContentView(R.layout.processing);
        // any processing work?
        
        setContentView(R.layout.grid);
        submitGame = (Button) findViewById(R.id.btnSubmitGame);
        submitGame.setOnClickListener(this);
        submitWord = (Button) findViewById(R.id.btnSubmitWord);
        submitWord.setOnClickListener(this);
        wordsFound = (TextView) findViewById(R.id.tvWordsFound);
        wordsFound.setMovementMethod(new ScrollingMovementMethod());
        
        // For each button on game board, set its letter, connect the listener,
        // and set its x and y coordinates.
		int btnNum = 1;
		for (int x = 0; x < 5; ++x) {
			for (int y = 0; y < 5; ++y) {
				String btnId = "GridButton" + btnNum;
				int resId = getResources().getIdentifier(btnId, "id", "psu.se.wordslam");
				GridButton btn = (GridButton) findViewById(resId);
				char ch = wordSlamApplication.GetGame().GetGrid().GetCharacterAtPosition(x, y);
				//char ch = wordSlamApplication.GetGame().GetGrid().GetCharacterAtPosition((i-1) % 5, (i-1)/5);
				btn.setText(Character.toString(ch));
				btn.setOnClickListener(this);
				btn.setX(x);	// set button's x coord. on board
				btn.setY(y);	// set button's y coord. on board
				++btnNum;
				Log.d(TAG, "Button id: " + btnId + ", (" + x + "," + y + ")");
			}
		}
    }
    
    
    @Override
	public void onClick(View v) {
    	switch(v.getId()) {
			case R.id.btnSubmitGame:
				Intent resultsIntent = new Intent(SingleGameActivity.this, 
						ResultsActivity.class);
	    		startActivityForResult(resultsIntent, REQUEST_RESULTS);
	    		break;
	    	case R.id.btnSubmitWord:
	    		// pass list of coords to get boolean return
	    		
	    		// if true (is a word) 
	    			Toast.makeText(SingleGameActivity.this, "It's a Word!", 
	    					Toast.LENGTH_SHORT).show();
	    			wordsFound.append(aWord + "\n");
	    		// else (not a word)
	    			// Toast.makeText(SingleGameActivity.this, "Oops, not a word...", 
					//Toast.LENGTH_SHORT).show();
	    		aWord = "";		// reset "aWord"
	    		// clear all pressed buttons
	    		resetButtons();
	    		break;
			default:			// a GridButton
				// button
				GridButton btn = (GridButton) findViewById(v.getId());
				aWord = aWord.concat((String) btn.getText()); // add letter to word
    	}
				
    }

    
    // Reset any pressed buttons (orange) to default drawable (grey)
    private void resetButtons() {
    	for (int i = 1; i < 26; ++i) {
    		String btnId = "GridButton" + i;
			int resId = getResources().getIdentifier(btnId, "id", "psu.se.wordslam");
			GridButton btn = (GridButton) findViewById(resId);
			if (btn.isClicked())
				btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_normal));
    	}
    }
    
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	Intent returnIntent = new Intent();
    	setResult(Activity.RESULT_OK, returnIntent);
        finish();	// return to MainMenuActivity
    }
	
}
