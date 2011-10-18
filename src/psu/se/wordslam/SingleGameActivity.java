/*
Copyright (c) 2011 Sarah Cathey, Zachary Greenvoss, Vidhya Priyadharshnee, 
Aparna Sawant.
This project is protected under the < > license. 
Please see COPYING file in the distribution for license terms.
*/

package psu.se.wordslam;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SingleGameActivity extends Activity {
	private static final String 	TAG = "WSSinglePlayer";
	private static final int		REQUEST_RESULTS = 0;
	
	private ArrayList<Button> 	buttons;
	private Button				submit;
	private Random 				random = new Random();
	private int					possiblePoints;
	private int					totalPoints;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.processing);
        // call grid.java to get board
        
        setContentView(R.layout.grid);
        submit = (Button) findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent resultsIntent = new Intent(SingleGameActivity.this, 
						ResultsActivity.class);
	    		startActivityForResult(resultsIntent, REQUEST_RESULTS);
				
			}
		});
        

        
		int range = 'Z' - 'A' + 1;		// Max - min + 1
		buttons = new ArrayList<Button>();
		for (int i = 1; i < 26; ++i) {
			String btnId = "button" + i;
			//Log.d(TAG, "btnId: " + btnId);
			int resId = getResources().getIdentifier(btnId, "id", 
					"psu.se.wordslam");
			Button btn = (Button) findViewById(resId);
			char ch = (char)(random.nextInt(range) + 'A');
			btn.setText(Character.toString(ch));
			
			//btn.setOnClickListener(this);
			buttons.add(btn);	
		}	
    }

    
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	Intent returnIntent = new Intent();
    	setResult(Activity.RESULT_OK, returnIntent);
        finish();	// return to MainMenuActivity
    }
	
}
