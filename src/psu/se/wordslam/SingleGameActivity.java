/*
Copyright (c) 2011 Sarah Cathey, Zachary Greenvoss, Vidhya Priyadharshnee, 
Aparna Sawant.
This project is protected under the < > license. 
Please see COPYING file in the distribution for license terms.
*/

package psu.se.wordslam;

import java.util.ArrayList;
import java.util.Vector;

import psu.se.wordslam.model.WordSlamApplication;


import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GestureOverlayView.OnGesturingListener;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SingleGameActivity extends Activity implements OnClickListener, OnGestureListener {
	private static final String 	TAG = "WSSinglePlayer";
	private static final int		REQUEST_RESULTS = 0;
	

	private Button				submitGame;
	private TextView			wordsFound;
	//private String				aWord = "";
	private WordSlamApplication wordSlamApplication;
	private Vector<GridButton> selectedButtons = new Vector<GridButton>();
	
	//private int				possiblePoints;
	//private int				totalPoints;
	//private GestureDetector mGestureDetector;
	//private View.OnTouchListener gestureListener;
	//GestureLibrary mLibrary;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.grid);
        
        wordSlamApplication = (WordSlamApplication)getApplicationContext();
		
        //setup gesture view
        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGestureListener(this);
        
        submitGame = (Button) findViewById(R.id.btnSubmitGame);
        submitGame.setOnClickListener(this);
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
				btn.setText(Character.toString(ch));
				btn.setOnClickListener(this);
				btn.setX(x);	// set button's x coord. on board
				btn.setY(y);	// set button's y coord. on board
				++btnNum;
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
    	}
				
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	Intent returnIntent = new Intent();
    	setResult(Activity.RESULT_OK, returnIntent);
        finish();	// return to MainMenuActivity
    }

	@Override
	public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		GridButton temp = this.GetButton((int)event.getRawX(), (int)event.getRawY());
		if(temp != null)
		{
			if(!selectedButtons.contains(temp))
			{
				temp.SetActive();
				selectedButtons.add(temp);
			}
		}
	}

	@Override
	public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
		String word = "";
		for(GridButton btn : this.selectedButtons)
		{
			btn.SetInactive();
			word += btn.getText();
		}
		//Check word
		if (wordSlamApplication.dictionary_search(word.toLowerCase())) {
			Toast.makeText(SingleGameActivity.this, "It's a Word!", 
					Toast.LENGTH_SHORT).show();
			wordsFound.append(word + "\n"); // add to textview
		}
		else{
			Toast.makeText(SingleGameActivity.this, "Oops, not a word...", Toast.LENGTH_SHORT).show();
		}
	}
	
	private GridButton GetButton(int xPos, int yPos)
	{
		int btnNum = 1;
		Rect rect = new Rect();
		int[] pos = {0,0};
		
	    for (int x = 0; x < 5; ++x) {
			for (int y = 0; y < 5; ++y) {
				String btnId = "GridButton" + btnNum;
				int resId = getResources().getIdentifier(btnId, "id", "psu.se.wordslam");
				GridButton btn = (GridButton) findViewById(resId);
				
				btn.getLocationInWindow(pos);
				//shrink box for better detection
				rect.left = pos[0] + 10;
				rect.top = pos[1] + 10;
				rect.bottom = rect.top + btn.getHeight() - 10;
				rect.right = rect.left + btn.getWidth() - 10;
				
				if(rect.intersects(xPos, yPos, xPos+5, yPos+5))
				{
					return btn;
				}
				btnNum++;
			}
	    }
	    return null;
	}
	
	@Override
	public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
		GridButton temp = this.GetButton((int)event.getRawX(), (int)event.getRawY());
		if(temp != null)
		{
			selectedButtons.clear();
			temp.SetActive();
			selectedButtons.add(temp);
		}
	}

	@Override
	public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
		//have to implement this method
		return;
	}
}
