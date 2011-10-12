package psu.se.wordslam;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


// Displays a 5 x 5 grid of buttons containing randomly generated letters
// "A" - "Z"
//
// Most likely it will make sense to store the buttons in a 5 x 5 array 
// rather than a linear array... (positions)
public class WordSlamActivity extends Activity implements OnClickListener {
	private static final String TAG = "GridView";
	private ArrayList<Button> 	buttons;
	private Random 				random = new Random();
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        
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
			btn.setOnClickListener(this);
			buttons.add(btn);
		}	
		
    }
    
    
	@Override
	public void onClick(View v) {
		/*
		switch(v.getId()) {
			case R.id.btnScan:	// invoke zing scanner application
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.setPackage("com.google.zxing.client.android");
				intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
				startActivityForResult(intent, 0);
				break;
			case R.id.btnUpdate:
				// STUB
				break;
		}*/
    }
}