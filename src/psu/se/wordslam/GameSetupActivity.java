package psu.se.wordslam;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class GameSetupActivity extends Activity implements OnClickListener {
	private static final String 	PREF = "WordSlamPrefs"; // preferences name
	private SharedPreferences 		settings;

	//private Button		btnStartGame;
	//private Spinner		spnTimes;
	//protected int timeSelected = -1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.gamesetup);
	    
	    TextView settingsTitle = (TextView) findViewById(R.id.tvSettings);
	    Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/COMIXHVY.TTF");
	    settingsTitle.setTypeface(tf);

	    Spinner spinner = (Spinner) findViewById(R.id.spinnerMinutes);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.game_times, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);

	    Button btnStartGame = (Button) findViewById(R.id.btnStartGame);
	    btnStartGame.setOnClickListener(this);
        
	}

	// Single onClick handler, uses switch statement to determine which
	// button was clicked. 
	@Override
	public void onClick(View v) {
		//WordSlamApplication wordSlamApplication = (WordSlamApplication)getApplicationContext();
		Spinner spinner = (Spinner) findViewById(R.id.spinnerMinutes);
		//wordSlamApplication.CreateNewGame(GameType.SinglePlayer);
		//wordSlamApplication.SetGameTimer(spinner.getSelectedItemPosition() * 1000 * 60);
		//Intent gameIntent = new Intent(this, SingleGameActivity.class);
		//startActivity(gameIntent);
		
		int time = spinner.getSelectedItemPosition() * 1000 * 60;
		settings = getSharedPreferences(PREF, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("TIME", time);
		// Commit the changes made to preferences
        editor.commit(); 
		
		
        Intent intent = new Intent(this, MainMenuActivity.class);
		startActivity(intent); 		// return to MainMenuActivity
    }
}
