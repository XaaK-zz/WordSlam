package psu.se.wordslam.model;
import android.widget.Toast;  
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import android.content.res.Resources; 
import android.content.res.AssetManager;

/**
 * WordSlam Application - used to store global state for an Android app
 */
public class WordSlamApplication extends android.app.Application {
	
	/**
	 * Singleton reference to the application object
	 */
	private static WordSlamApplication 	singleton;
	public HashSet<String> 				dic;	
	public static AssetManager 			assetManager;
	private Resources	 				resources; 
	public String 						dummy;
	public boolean HostGame;
	
	/**
	 * Reference to the Game object representing the current state of the game
	 */
	public Game m_Game;
	
	/**
	 * Singleton implementation
	 * @return single instance of the application object
	 */
	public static WordSlamApplication getInstance()
	{
		return singleton;
	}
	
	/**
	 * Singleton implementation
	 */
	@Override
	public void onCreate() {
		resources = getResources();  
		dic = new HashSet<String>(); 
		dictionary_build();
		
		super.onCreate();
		singleton = this;
		this.m_Game = null;
	}
	
	/**
	 * Creates and initializes a new Game Object based on the passed type<br>
	 * 	Multi-player or Single-Player
	 * @param gameType GameType enum
	 */
	public void CreateNewGame(Game.GameType gameType, int time)
	{
		this.m_Game = new Game(gameType);	
		this.SetGameTimer(time);
	}
	
	/**
	 * Creates and initializes a new Game Object based on the passed type<br>
	 * 	Multi-player or Single-Player
	 * @param gameType GameType enum
	 * @param isCutThroat Flag indicating this is a cutThroat multiplayer game or not
	 */
	public void CreateNewGame(Game.GameType gameType, boolean isCutThroat, String columnData, int maxTime)
	{
		this.m_Game = new Game(gameType,isCutThroat, columnData, maxTime);	
	}
	
	/**
	 * Starts the game timer (TODO)<br>
	 *   NOTE: We will need to pass a reference to a callback function here to store when the time runs out
	 * @param MaxTime Number of milliseconds the game should last
	 */
	public void SetGameTimer(int MaxTime)
	{
		if(MaxTime < 0)
			MaxTime = 1000 * 60;	//1 minute default
		this.m_Game.SetTotalGameTime(MaxTime);
		this.m_Game.SetRemainingGameTime(MaxTime);
	}
	
	public void SetOpponentIP(String ipAddress) 
	{
		this.m_Game.SetOpponentIP(ipAddress);
	}
	
	/**
	 * Accessor method for retrieving the inner Game object
	 * @return reference to the current Game object
	 */
	public Game GetGame()
	{
		return this.m_Game;
	}

	public void dictionary_build()
	{
			String strLine;
			try
	 	  	{	
				  InputStream inputStream = resources.getAssets().open("dictionary.txt");
				  InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	 			  BufferedReader br = new BufferedReader(inputStreamReader);	 			 			  
	 			  			 
	 			  //Read File Line By Line
	 			  while ((strLine = br.readLine()) != null) {
	 				  // Add the string to array list
	 				  dic.add(strLine);	
	 			  }
	 			  //Close the input stream
	 			  inputStream.close();
	 	  	} catch (Exception e) {
	 	  		Toast toast = Toast.makeText(this, "File: not found!", Toast.LENGTH_LONG); 	  		toast.show(); 
	        }
	}
	  
	public boolean dictionary_search(String input){
	 	return  dic.contains(input);	 
	}
	  
}
