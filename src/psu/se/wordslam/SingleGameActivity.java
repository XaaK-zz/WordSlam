/*
Copyright (c) 2011 Sarah Cathey, Zachary Greenvoss, Vidhya Priyadharshnee, 
Aparna Sawant.
This project is protected under the < > license. 
Please see COPYING file in the distribution for license terms.
*/

package psu.se.wordslam;

import psu.se.wordslam.model.Game.GameType;
import psu.se.wordslam.model.WordSlamApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleGameActivity extends Activity implements OnClickListener, OnGestureListener {
	private Button					submitGame;
	private TextView				wordsFound;
	private TextView				wordsOpponentFound;
	private WordSlamApplication		wordSlamApplication;
	private Vector<GridButton> 		selectedButtons = new Vector<GridButton>();
	private Typeface 				tf; 
	
	//delta values used to determine next valid grid button
	private int deltaX;
    private int deltaY;
    
    //used to track timer values for animation of timer UI
    private long startTime;
    private long gameTime;
    
    //Thread to support client/server communication
	private Thread monitorThread;
	
	//Opponent's IP address
	private String opponentIP;
	
	//ServerSocket object for monitoring communication
	private ServerSocket serverSocket;
	
	//Queue storing the data to push to the opponent 
	private Queue<String> dataToSend;
	//Queue storing the data received from the opponent
	private Queue<String> dataReceived;
	
	//handler used to handle messages receieved from opponent
	private Handler multiplayerHandler = new Handler();
	
    private Handler btnColorHandler = new Handler();
    
    private Runnable clearButtonTask = new Runnable() {
    	public void run() {
    		for(GridButton btn : SingleGameActivity.this.selectedButtons)
			{
				btn.SetInactive();
			}
    		SingleGameActivity.this.selectedButtons.clear();
    	}
    };
    
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
    	    	   if(monitorThread != null)
    	    		   monitorThread.interrupt();
   				
    	    	   	//NEED to push user to final screen
    	    	   	Intent resultsIntent = new Intent(SingleGameActivity.this, 
   						ResultsActivity.class);
   	    			startActivity(resultsIntent);
    	       }
    	   }
    	};
    		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        wordSlamApplication = (WordSlamApplication)getApplicationContext();
        if(wordSlamApplication.GetGame().GetGameType() == GameType.MultiPlayer) {        	
        	setContentView(R.layout.gridtwo);
        	multiPlayerLayoutSetup();
        	//wordsOpponentFound = (TextView) findViewById(R.id.tvOpponentWordsFound);
        }
        else {
        	setContentView(R.layout.grid);
        	submitGame = (Button) findViewById(R.id.btnSubmitGame);
            submitGame.setOnClickListener(this);
        }
        
        //setup gesture view
        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGestureListener(this);
        
        wordsFound = (TextView) findViewById(R.id.tvWordsFound);

        
        // set font
        tf = Typeface.createFromAsset(getAssets(),"fonts/COMIXHVY.TTF");
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
		        btn.setTypeface(tf);
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
		
		if(wordSlamApplication.GetGame().GetGameType() == GameType.MultiPlayer)
		{
			//create communication queues
			dataToSend = new ConcurrentLinkedQueue<String>();
			dataReceived =  new ConcurrentLinkedQueue<String>();
			opponentIP = wordSlamApplication.GetGame().GetOpponentIPAddress();
			//Multiplayer game - need to kick off monitoring thread for socket communication
			this.monitorThread = new Thread(new MultiplayerThread());
			monitorThread.start();
		}
    }
    
    
    @Override
	public void onClick(View v) {
    	switch(v.getId()) {
			case R.id.btnSubmitGame:
				if(monitorThread != null)
					monitorThread.interrupt();
				 
				Intent resultsIntent = new Intent(SingleGameActivity.this, 
						ResultsActivity.class);
	    		startActivity(resultsIntent);
	    		break;
    	}
				
    }
     
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
		String location = "";
		for(GridButton btn : this.selectedButtons)
		{
			btn.SetInactive();
			word += btn.getText();
			location += "<" + btn.x + btn.y + ">,";
		}
		
		//Check word
		if (wordSlamApplication.dictionary_search(word.toLowerCase())) {
			if(!wordSlamApplication.m_Game.wordAlreadyFound(word.toLowerCase())) {
				for(GridButton btn : this.selectedButtons)
				{
					btn.SetCorrect();
				}
				btnColorHandler.removeCallbacks(this.clearButtonTask);
				btnColorHandler.postDelayed(this.clearButtonTask, 500);
				wordsFound.append(word + "\n"); // add to textview
				wordSlamApplication.m_Game.addFoundWord(word.toLowerCase()); // add to game
				
				if(wordSlamApplication.GetGame().GetGameType() == GameType.MultiPlayer) {
					//Send word found to opponent (add to send queue)
					//	Note: the Queue used is Thread-safe so no need for synchronized block
					this.dataToSend.add("1|" + location + "|" + word);
					
					//Set Score
					TextView score = (TextView) findViewById(R.id.tvScore);
					Integer x = wordSlamApplication.m_Game.getScore();
					score.setText(x.toString());
				}
			}
		}
		else {
			for(GridButton btn : this.selectedButtons)
			{
				btn.SetInCorrect();
			}
			btnColorHandler.removeCallbacks(this.clearButtonTask);
			btnColorHandler.postDelayed(this.clearButtonTask, 500);
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
			btnColorHandler.removeCallbacks(this.clearButtonTask);
			for(GridButton btn : this.selectedButtons)
			{
				btn.SetInactive();
			}
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
	
	@Override
    protected void onStop() {
        super.onStop();
        try {
            if(serverSocket != null)
        		serverSocket.close();
            if(this.monitorThread != null)
            	this.monitorThread.interrupt();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
	
	
	
	private void multiPlayerLayoutSetup() {
		wordsOpponentFound = (TextView) findViewById(R.id.tvOpponentWordsFound);
        wordsOpponentFound.setTypeface(tf);
        ((TextView) findViewById(R.id.tvGridTitleRight)).setTypeface(tf);
        ((TextView) findViewById(R.id.tvYourScore)).setTypeface(tf);
        ((TextView) findViewById(R.id.tvTheirScore)).setTypeface(tf);
        ((TextView) findViewById(R.id.tvScore)).setTypeface(tf);
        ((TextView) findViewById(R.id.tvScore2)).setTypeface(tf);
	}
	
	
	/**
	 * This is invoked via the socket/multiplayer thread when it is time to process the 
	 *	incoming data from the opponent.  Depending on the type of "command" sent by the 
	 *	opponent process - the UI will need to take different actions
	 */
	public void ProcessMultiplayerInput()
	{
		String command = this.dataReceived.poll();
		if(command != null)
		{
			//deal with various messages we can get from opponent process
			switch(command.charAt(0))
			{
			case '1':
				//word received from opponent
				//	Format: 1|<x,y>,<x,y>|word
				Log.d("UIThread", "process word command - " + command);
				String data[] = command.split("\\|");
				Log.d("UIThread", "data[2] - " + data[2]);
				
				WordSlamApplication.getInstance().GetGame().addOpponentFoundWord(data[2].toUpperCase());
				//Set Score
				TextView score = (TextView) findViewById(R.id.tvScore2);
				Integer x = wordSlamApplication.m_Game.GetOpponentScore();
				score.setText(x.toString());
				//Add word
				wordsOpponentFound.append(data[2].toUpperCase() + "\n");
				
				break;
			}
		}
	}
	
	////////////////////Threading Code for Multiplayer////////////////////////////////
	public class MultiplayerThread implements Runnable {
		public void run() {
			Socket socket = null;
			Socket clientSocket = null;
			String line = "";
			BufferedReader serverReader = null;
			BufferedReader clientReader = null;
			PrintWriter out = null;
			PrintWriter serverWriter = null;
			
			try {
                if (opponentIP != null) {
                	//Start server socket monitoring
                	//Open socket to other player
                	if(WordSlamApplication.getInstance().HostGame)
                	{
                		//socket = new Socket(opponentIP, 2021);
                		//listen for connections from other player
                		serverSocket = new ServerSocket(MultiplayerSetupActivity.SERVERPORT);
                		
                		//set server timeout value
                        serverSocket.setSoTimeout(1000);
                        
                        while (true) {
                            //listen for incoming data
                            try {
                            	if(clientSocket == null) {
                            		clientSocket = serverSocket.accept();
                            	}
                            	
                            } catch(SocketTimeoutException ex){
                            	clientSocket = null;
                        	}
                            
                            if(clientSocket != null)
                            {
                            	//Log.d("MultiplayerThread", "getting reader...");
                            	clientSocket.setSoTimeout(500);
                            	try {
                            		serverReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            		serverWriter = new PrintWriter(clientSocket.getOutputStream(),true);
                                    
                            		while ((line = serverReader.readLine()) != null) {
                            			Log.d("MultiplayerThread", "data received: " + line);
                            			//Add to data queue
                            			//	Note: this collection is thread-safe so no need for synchronized block
                            			dataReceived.add(line);
                            			//push the notification up to ui thread for processing
                            			NotifyUI();
                            		}
                            	}
                            	catch(SocketTimeoutException ex) {
                            		//Log.d("MultiplayerThread", "Exception receieving data: " + ex.toString());
                            		serverReader = null;
                            	}
                            	
                            	Thread.yield();
                            	if (Thread.currentThread().isInterrupted())	 
                            	      throw new InterruptedException("Interrupted");
                            	
                        		//Check for data to send - pull off of shared queue
                            	//	Note: the Queue used is Thread-safe so no need for synchronized block
                            	String send = dataToSend.poll();
                            	
                            	if(send != null) {
                            		Log.d("MultiplayerThread", "data to send: " + send);
                        			//out.println(send);
                        			//out.flush();
                            		serverWriter.println(send);
                            		serverWriter.flush();
                        		}
                            	
                            	Thread.yield();
                            	if (Thread.currentThread().isInterrupted())	
                            	      throw new InterruptedException("Interrupted");
                            	
                            }
                        }
                        
                	}
                	else
                	{
                		socket = new Socket(opponentIP, MultiplayerSetupActivity.SERVERPORT);
                		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                		
                		//set server timeout value
                		socket.setSoTimeout(1000);
                        
                		while(true) {
                			
	                		//Check for data to send - pull off of shared queue
	                    	//	Note: the Queue used is Thread-safe so no need for synchronized block
	                    	String send = dataToSend.poll();
	                    	
	                    	if(send != null) {
	                    		Log.d("MultiplayerThread", "data to send: " + send);
	                			//out.println(send);
	                			//out.flush();
	                    		out.println(send);
	                    		out.flush();
	                		}
	                    	
	                    	try {
	                    		clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                            
	                    		while ((line = clientReader.readLine()) != null) {
	                    			Log.d("MultiplayerThread", "data received: " + line);
	                    			//Add to data queue
	                    			//	Note: this collection is thread-safe so no need for synchronized block
	                    			dataReceived.add(line);
	                    			//push the notification up to ui thread for processing
	                    			NotifyUI();
	                    		}
	                            	
	                    	}
	                        catch(SocketTimeoutException ex) {
	                    		//Log.d("MultiplayerThread", "Exception receieving data: " + ex.toString());
	                        	clientReader = null;
	                    	}
	                        //Check for thread interuption
	                        Thread.yield();
                        	if (Thread.currentThread().isInterrupted())	
                        	      throw new InterruptedException("Interrupted");
	                	}
                	}
                } 
            }
            catch (InterruptedException ex) {
            	return;
            }
            catch (Exception e) {
            	Log.d("Server Error", e.toString());
            }
            finally {
            	try {
            		if(socket != null)
            			socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }

		/**
		 * Thread notification to the UI
		 */
		private void NotifyUI() {
			multiplayerHandler.post(new Runnable() {
                 @Override
                 public void run() {
                 	ProcessMultiplayerInput();
                 }
			});
		}
	}
}
