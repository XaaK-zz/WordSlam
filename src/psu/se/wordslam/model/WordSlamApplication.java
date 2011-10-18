package psu.se.wordslam.model;

/**
 * WordSlam Application - used to store global state for an Android app
 */
public class WordSlamApplication extends android.app.Application {
	
	/**
	 * Singleton reference to the application object
	 */
	private static WordSlamApplication singleton;

	/**
	 * Reference to the Game object representing the current state of the game
	 */
	private Game m_Game;
	
	/**
	 * Singleton implementation
	 * @return single instance of the application object
	 */
	public WordSlamApplication getInstance()
	{
		return singleton;
	}
	
	/**
	 * Singleton implementation
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
	}
	
	/**
	 * Creates and initializes a new Game Object based on the passed type<br>
	 * 	Multi-player or Single-Player
	 * @param gameType GameType enum
	 */
	public void CreateNewGame(Game.GameType gameType)
	{
		this.m_Game = new Game(gameType);
	}
	
	/**
	 * Starts the game timer (TODO)<br>
	 *   NOTE: We will need to pass a reference to a callback function here to store when the time runs out
	 * @param MaxTime Number of milliseconds the game should last
	 */
	public void SetGameTimer(int MaxTime)
	{
		this.m_Game.SetTotalGameTime(MaxTime);
		this.m_Game.SetRemainingGameTime(MaxTime);
	}
	
	/**
	 * Accessor method for retrieving the inner Game object
	 * @return reference to the current Game object
	 */
	public Game GetGame()
	{
		return this.m_Game;
	}
	
	/**
	 * Placeholder.<br>
	 * Starts the Game Timer (i.e. actual game start) 
	 */
	public void StartGameTimer()
	{
		this.m_Game.StartTimer();
	}
}
