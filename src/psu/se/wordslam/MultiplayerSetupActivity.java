package psu.se.wordslam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Enumeration;

import psu.se.wordslam.model.Game.GameType;
import psu.se.wordslam.model.WordSlamApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Note: Large portions of the networking code was implemented using code from: 
 * 	http://thinkandroid.wordpress.com/2010/03/27/incorporating-socket-programming-into-your-applications/
 *
 */
public class MultiplayerSetupActivity extends Activity implements OnClickListener {

	public static String SERVERIP;
	public static final int SERVERPORT = 2020;
	
	private Handler handler = new Handler();
	private ServerSocket serverSocket;
	private TextView txtHostGameLog; 
	private TextView txtJoinGameLog; 
	
	//Threads to support client/server communication
	private Thread serverThread;
	private Thread clientThread;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.multisetup);

	    Spinner spinner = (Spinner) findViewById(R.id.hostGameTime);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.multi_game_times, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);

	    //Setup button click handlers
	    Button btnHostGame = (Button) findViewById(R.id.btnStartHostGame);
	    btnHostGame.setOnClickListener(this);
        
	    Button btnJoinGame = (Button) findViewById(R.id.btnLookGames);
	    btnJoinGame.setOnClickListener(this);
        
	    //Get this textView for logging
	    txtHostGameLog = (TextView) findViewById(R.id.txtHostGameLog);
	    txtJoinGameLog = (TextView) findViewById(R.id.txtJoinGameLog);
	    
	    SERVERIP = getLocalIpAddress();
	}
	
	@Override
	public void onClick(View v) {
		WordSlamApplication wordSlamApplication = (WordSlamApplication)getApplicationContext();
		switch(v.getId()) {
			case R.id.btnStartHostGame:	
				 //Start/Cancel the Host thread process
				 Button btnHostGame = (Button) findViewById(R.id.btnStartHostGame);
				 if(btnHostGame.getText().toString().compareTo("Start Game") == 0) {
					
					 //Update UI//////////////////////////////////////////////////////
					 btnHostGame.setText("Cancel");
					 RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutJoinGame);
					 layout.setVisibility(View.GONE);
					 LinearLayout layoutBar = (LinearLayout) findViewById(R.id.layoutDivider);
					 layoutBar.setVisibility(View.GONE);
					 RelativeLayout layoutHostSettings = (RelativeLayout) findViewById(R.id.layoutHostGameSettings);
					 layoutHostSettings.setVisibility(View.VISIBLE);
					
					 Spinner spinner = (Spinner) findViewById(R.id.hostGameTime);
					 spinner.setEnabled(false);
					 
					 CheckBox cutthroat = (CheckBox) findViewById(R.id.hostGameCheckBoxCT);
					 cutthroat.setEnabled(false);
					 
					 //Clear log
					 txtHostGameLog.setText("");
					 ///////////////////////////////////////////////////////////////////
					 
					 //Create Game object with settings
					 wordSlamApplication.CreateNewGame(GameType.MultiPlayer,cutthroat.isChecked(), null, 
							 (1 + spinner.getSelectedItemPosition()) * 1000 * 60);
					 
					 //start thread for listening to server events
					 this.serverThread = new Thread(new ServerThread());
					 serverThread.start();
				 }
				 else {
					 //Canceled host game - kill server thread
					 serverThread.interrupt();
					 
					 //Update UI///////////////////////////////////////////////////////////
					 btnHostGame.setText("Start Game");
						
					 RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutJoinGame);
					 layout.setVisibility(View.VISIBLE);
					 LinearLayout layoutBar = (LinearLayout) findViewById(R.id.layoutDivider);
					 layoutBar.setVisibility(View.VISIBLE);
					 RelativeLayout layoutHostSettings = (RelativeLayout) findViewById(R.id.layoutHostGameSettings);
					 layoutHostSettings.setVisibility(View.GONE);
						
					 Spinner spinner = (Spinner) findViewById(R.id.hostGameTime);
					 spinner.setEnabled(true);
					 
					 CheckBox cutthroat = (CheckBox) findViewById(R.id.hostGameCheckBoxCT);
					 cutthroat.setEnabled(true);
					 /////////////////////////////////////////////////////////////////////////
				 }
				 
				break;
			case R.id.btnLookGames:
				//Join Game clicked
				Button btnLookGame = (Button) findViewById(R.id.btnLookGames);
				if(btnLookGame.getText().toString().compareTo("Look For Games") == 0) {
					
					//Update UI////////////////////////////////////////////////////////////////
					btnLookGame.setText("Cancel");
					
					RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutHostGame);
					layout.setVisibility(View.GONE);
					LinearLayout layoutBar = (LinearLayout) findViewById(R.id.layoutDivider);
					layoutBar.setVisibility(View.GONE);
					RelativeLayout layoutHostSettings = (RelativeLayout) findViewById(R.id.layoutJoinGameSettings);
					layoutHostSettings.setVisibility(View.VISIBLE);
					///////////////////////////////////////////////////////////////////////////
					
					//Start client thread to look for servers
					this.clientThread = new Thread(new ClientThread());
					clientThread.start();
				}
				else {
					//Cancel looking for games
					btnLookGame.setText("Look For Games");
					
					//Update UI/////////////////////////////////////////////////////////////////
					RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutHostGame);
					layout.setVisibility(View.VISIBLE);
					LinearLayout layoutBar = (LinearLayout) findViewById(R.id.layoutDivider);
					layoutBar.setVisibility(View.VISIBLE);
					RelativeLayout layoutHostSettings = (RelativeLayout) findViewById(R.id.layoutJoinGameSettings);
					layoutHostSettings.setVisibility(View.GONE);
					////////////////////////////////////////////////////////////////////////////
				}
				
				break;
		}
    }

	/**
	 * Returns the local IP address of the system
	 * @return IP address of the system
	 */
	private String getLocalIpAddress() {
		try {
	            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	                NetworkInterface intf = en.nextElement();
	                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                    InetAddress inetAddress = enumIpAddr.nextElement();
	                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
	                }
	            }
	        } catch (SocketException ex) {
	            Log.e("ServerActivity", ex.toString());
	        }
	        return null;
	    }

	@Override
    protected void onStop() {
        super.onStop();
        try {
            if(serverSocket != null)
        		serverSocket.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }

	public class ServerThread implements Runnable {
		String displayText;
		
		/**
		 * Simple utility method for pushing log updates to the UI thread
		 * @param text Data to add to the UI
		 */
		private void PostUpdate(String text) {
			displayText = text;
			handler.post(new Runnable() {
                 @Override
                 public void run() {
                 	txtHostGameLog.append(displayText + "\n");
                 }
			});
		}
			
        public void run() {
            try {
                if (SERVERIP != null) {
                	PostUpdate("Listening on IP: " + SERVERIP);
                	//Start server socket monitoring
                    serverSocket = new ServerSocket(SERVERPORT);
                    while (true) {
                        // LISTEN FOR INCOMING CLIENTS
                        Socket client = null;
                        serverSocket.setSoTimeout(500);
                        
                        //Loop for connections and check to see if the cancel button was clicked
                        do {
                        	try {
                        		client = serverSocket.accept();
                        	}
                        	catch(SocketTimeoutException ex){
                        		client = null;
                        	}
                        	Thread.yield();
                        	if (Thread.currentThread().isInterrupted())	//If cancel button clicked this is true 
                        	      throw new InterruptedException("Interrupted");
                        } while(client == null);
                        
                        //Get the IP Address of client
                        String clientIP = client.getInetAddress().getHostAddress();
                        WordSlamApplication.getInstance().SetOpponentIP(clientIP);
                        PostUpdate("Client Connected - " + clientIP);
	                	
                        try {
                        	//Get in/out streams for connected client
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            PrintWriter output = new PrintWriter(client.getOutputStream(),true);
                            String line = null;
                            while ((line = in.readLine()) != null) {
                                if(line.compareTo("WORDSLAM") == 0)	//Wordslam game client connected
                                {
                                	//push game state to client
                                	output.println("OK");
                                	output.flush();
                                	//Need to post game board and game type to client
                                	String gridData[] = WordSlamApplication.getInstance().GetGame().GetGrid().gridToRowArray();
                                	boolean isCutThroat = WordSlamApplication.getInstance().GetGame().IsCutThroat();
                                	int length = WordSlamApplication.getInstance().GetGame().GetTotalGameTime();
                                	output.println(isCutThroat + "|" + length + "|" + Arrays.toString(gridData));
                                	output.flush();
                                }
                                PostUpdate("Starting Game");
			                	
                                //This kicks off the move to the game activity
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                    	//Switch to new Activity
                                    	Intent gameIntent = new Intent(MultiplayerSetupActivity.this,SingleGameActivity.class);
                        				startActivity(gameIntent);
                                    }
                                },500);
                                //leave the thread
                                return;
                            }
                            break;
                        } catch (Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                	txtHostGameLog.append("Oops. Connection interrupted. Please reconnect your phones.\n");
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        	txtHostGameLog.append("Couldn't detect internet connection.\n");
                        }
                    });
                }
            }
            catch (InterruptedException ex) {
            	return;
            }
            catch (Exception e) {
            	Log.d("Server Error", e.toString());
                
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    	txtHostGameLog.setText("Error\n");
                    }
                });
                e.printStackTrace();
            }
            finally {
            	try {
            		if(serverSocket != null)
            			serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
    }

	public class ClientThread implements Runnable {

		InetAddress serverAddr;
		String displayData;
		
		/**
		 * Simple utility method for pushing log updates to the UI thread
		 * @param text Data to add to the UI
		 */
		private void PostUpdate(String text) {
			displayData = text;
			handler.post(new Runnable() {
                 @Override
                 public void run() {
                 	txtJoinGameLog.append(displayData + "\n");
                 }
			});
		}
		
        public void run() {
            try {
                //NOTE: eventually I would like to see this poll the local address space - but this is 
            	//	difficult to achieve on the Android enumlator.
            	//For now simply connect to the local machine
            	serverAddr = InetAddress.getByName("10.0.2.2");
                PostUpdate("Connecting to " + serverAddr.getHostAddress());
                
                //Connect
                Socket socket = new Socket(serverAddr, SERVERPORT);
                while (true) {
                    try {
                    	PostUpdate("Connected...");
                    	
                    	//Get in/out streams
                    	PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        
                        //Ping the server to see if this is a valid wordslam server
                        out.println("WORDSLAM");
                        
                        String line = in.readLine();
                        if(line != null && line.compareTo("OK") == 0) {
                        	PostUpdate("Connected to server");
                        	
                        	//Read game state from server and use it to create board
                        	//	Next line from server will be <IsCutThroat>|<GameTime>|[ColumnData,ColumnData,...]
                        	line = in.readLine();
                        	
                        	String data[] = line.split("\\|");
                        	if(data.length < 3)
                        	{
                        		//Failure
                        		PostUpdate("Failed to get server data");
                        		//kill thread
                        		break;
                        	}
                        	//Get the column data 
                        	String[] columnData = data[2].substring(1,data[2].length()-1).split(",");
                        	StringBuilder sb = new StringBuilder();
                        	for(String col : columnData) {
                        		for(char x : col.toCharArray()) {
                        			if(x != ' ')
                        				sb.append(x);
                        		}
                        	}
                        	
                    		//Create game object
                    		WordSlamApplication.getInstance().CreateNewGame(GameType.MultiPlayer, 
                    			Boolean.parseBoolean(data[0].trim()),	//CutThroat Flag
                    			sb.toString(),							//Column Data
                    			Integer.parseInt(data[1].trim()));		//Timer
                    		WordSlamApplication.getInstance().SetOpponentIP(serverAddr.getHostAddress());
                        	
                    		PostUpdate("Client game created.\nStarting Game.");
                    		
                    		//Kick off process to move to game activity
                        	handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                	//Switch to new Activity
                                	Intent gameIntent = new Intent(MultiplayerSetupActivity.this,SingleGameActivity.class);
                    				startActivity(gameIntent);
                                }
                            },500);
                        	//close the socket and return from thread
                        	socket.close();
                            return;
                        }
                        
                        Log.d("ClientActivity", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                        break;
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                //connected = false;
                PostUpdate("Error Connecting.");
            }
        }
    }
}
