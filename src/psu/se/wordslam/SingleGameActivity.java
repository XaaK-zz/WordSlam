/*
Copyright (c) 2011 Sarah Cathey, Zachary Greenvoss, Vidhya Priyadharshnee, 
Aparna Sawant.
This project is protected under the < > license. 
Please see COPYING file in the distribution for license terms.
*/

package psu.se.wordslam;

import psu.se.wordslam.model.WordSlamApplication;

import java.util.Vector;
import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SingleGameActivity extends Activity implements OnClickListener, OnGestureListener {
	private static final int	REQUEST_RESULTS = 0;
	
	private Button				submitGame;
	private TextView			wordsFound;
	private WordSlamApplication wordSlamApplication;
	private Vector<GridButton> 	selectedButtons = new Vector<GridButton>();
	
	//delta values used to determine next valid grid button
	private int deltaX;
    private int deltaY;
    
    //used to track timer values for animation of timer UI
    private long startTime;
    private long gameTime;
    
    //used to animate timer bar
    private Handler mHandler = new Handler();
    
    //timer event code
    private Runnable mUpdateTimeTask = new Runnable() {
    	   public void run() {
    	       final long start = startTime;
    	       long currentLength = System.currentTimeMillis() - start;
    	       long remainingTime = gameTime - currentLength;
    	       float x = (float)remainingTime / (float)gameTime;
    	       
    	       if(remainingTime > 0)
    	       {
    	    	   //still have time remaining - update UI
    	    	   LinearLayout timerUI = (LinearLayout) findViewById(R.id.timerUI);
    	    	   timerUI.getLayoutParams().height =  (int) (x * 400);
    	    	   
    	    	   //update color
    	    	   if(remainingTime < 15000)
    	    	      timerUI.setBackgroundColor(Color.RED);
    	    	   else if(remainingTime < 30000)
    	    		   timerUI.setBackgroundColor(Color.YELLOW);
   	    	   
    	    	   timerUI.requestLayout();
    	    	   timerUI.invalidate();
    	    	   mHandler.postDelayed(this, 500);
    	       }
    	       else
    	       {
    	    	   //TODO
    	    	   //NEED to push user to final screen
    	       }
    	   }
    	};
    	
    	
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

        
       // wordsFound.setMovementMethod(ScrollingMovementMethod.getInstance());
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/COMIXHVY.TTF");
        wordsFound.setTypeface(tf);
        TextView tv = (TextView) findViewById(R.id.tvGridTitle);
        tv.setTypeface(tf);

        
        // For each button on game board, set its letter, connect the listener,
        // and set its x and y coordinates.
		int btnNum = 1;
		for (int y = 0; y < 5; ++y) {
			for (int x = 0; x < 5; ++x) {
				String btnId = "GridButton" + btnNum;
				int resId = getResources().getIdentifier(btnId, "id", "psu.se.wordslam");
				GridButton btn = (GridButton) findViewById(resId);
				char ch = wordSlamApplication.GetGame().GetGrid().GetCharacterAtPosition(x, y);
				btn.setText(Character.toString(ch));
		        //btn.setTypeface(tf);
				btn.setOnClickListener(this);
				btn.setX(x);	// set button's x coord. on board
				btn.setY(y);	// set button's y coord. on board
				++btnNum;
			}
		}
		
		gameTime = wordSlamApplication.GetGame().GetTotalGameTime();
		
		if(gameTime > 0)
		{
			//setup timer handler
			startTime = System.currentTimeMillis();
			mHandler.removeCallbacks(mUpdateTimeTask);
			mHandler.postDelayed(mUpdateTimeTask, 500);
		}
    }
    
    
    @Override
	public void onClick(View v) {
    	switch(v.getId()) {
			case R.id.btnSubmitGame:
				Intent resultsIntent = new Intent(SingleGameActivity.this, 
						ResultsActivity.class);
	    		//startActivityForResult(resultsIntent, REQUEST_RESULTS);
				startActivity(resultsIntent);
	    		break;
    	}
				
    }
    
    
/*    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	Intent returnIntent = new Intent();
    	setResult(Activity.RESULT_OK, returnIntent);
        finish();	// return to MainMenuActivity
    }
*/
    
    
    @Override
	public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		GridButton temp = this.GetButton((int)event.getRawX(), (int)event.getRawY());
		if(temp != null)
		{
			if(!selectedButtons.contains(temp))
			{
				if(selectedButtons.size() == 1)
				{
					//this is the second button - can set direction
					deltaX = selectedButtons.get(0).x - temp.x;
					deltaY = selectedButtons.get(0).y - temp.y;
					
					//Ensure we are not moving backwards and that we did not "skip" a button by moving too fast
					if(deltaX > 0)
						return;
					else if(deltaX < -1)
						return;
					else if(java.lang.Math.abs(deltaY) > 1)
						return;
					
				}
				else if(selectedButtons.size() > 0)
				{
					 //compare to last one
					 int tempX = selectedButtons.get(selectedButtons.size()-1).x - temp.x;
					 int tempY = selectedButtons.get(selectedButtons.size()-1).y - temp.y;
					 
					 //Ensure we are on the same path
					 if(tempX != deltaX || tempY != deltaY)
						 return;
				}
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
		//close out selected buttons
		selectedButtons.clear();
		
		//Check word
		if (wordSlamApplication.dictionary_search(word.toLowerCase())) {
			Toast.makeText(SingleGameActivity.this, "It's a Word!", 
					Toast.LENGTH_SHORT).show();
			wordsFound.append(word + "\n"); // add to textview
			wordSlamApplication.m_Game.addFoundWord(word); // add to game
		}
		else {
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
				rect.left = pos[0] + 20;
				rect.top = pos[1] + 20;
				rect.bottom = rect.top + btn.getHeight() - 40;
				rect.right = rect.left + btn.getWidth() - 40;
				
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
		selectedButtons.clear();
		return;
	}
}
