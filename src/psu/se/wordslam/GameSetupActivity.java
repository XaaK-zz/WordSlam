package psu.se.wordslam;

import psu.se.wordslam.model.WordSlamApplication;
import psu.se.wordslam.model.Game.GameType;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class GameSetupActivity extends Activity implements OnClickListener {

	//private Button		btnStartGame;
	//private Spinner		spnTimes;
	//protected int timeSelected = -1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.gamesetup);

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
		WordSlamApplication wordSlamApplication = (WordSlamApplication)getApplicationContext();
		Spinner spinner = (Spinner) findViewById(R.id.spinnerMinutes);
		wordSlamApplication.CreateNewGame(GameType.SinglePlayer);
		wordSlamApplication.SetGameTimer(spinner.getSelectedItemPosition() * 1000);
		Intent gameIntent = new Intent(this, SingleGameActivity.class);
		startActivityForResult(gameIntent, 0);
    }
}
