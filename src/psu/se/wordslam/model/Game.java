package psu.se.wordslam.model;

/**
 * Represents a game in the system
 */
public class Game 
{
	private LetterGrid m_Grid;
	
	private GameType m_GameType;
	
	private int m_TotalGameTimeInMS;

	private int m_GameTimeRemaininginMS;

	/**
	 * Basic constructor - sets defaults for everything
	 */
	public Game()
	{
		this.m_GameType = GameType.SinglePlayer;
		this.m_Grid = new LetterGrid();
		this.m_Grid.GenerateRandomBoard();
		this.m_TotalGameTimeInMS = 5000;
		this.m_TotalGameTimeInMS = 5000;
	}
	
	/**
	 * Retrieval method for the GameType property
	 * @return GameType internal value
	 */
	public GameType GetGameType()
	{
		return this.m_GameType;
	}
	
	/**
	 * Setter method for the GameType property
	 * @param gameType Incoming GameType enumeration value
	 */
	public void SetGameType(GameType gameType)
	{
		this.m_GameType = gameType;
	}
	
	/**
	 * Retrieval method for the Total Game Time property
	 * @return Total game time value
	 */
	public int GetTotalGameTime()
	{
		return this.m_TotalGameTimeInMS;
	}
	
	/**
	 * Setter method for the Total Game Time property
	 * @param gameTime Incoming Game Time value
	 */
	public void SetTotalGameTime(int gameTime)
	{
		this.m_TotalGameTimeInMS = gameTime;
	}
	
	/**
	 * Retrieval method for the remaining Game Time property
	 * @return Remaining game time value
	 */
	public int GetRemainingGameTime()
	{
		return this.m_GameTimeRemaininginMS;
	}

	/**
	 * Setter method for the Remaining Game Time property
	 * @param gameTime Incoming Game Time value
	 */
	public void SetRemainingGameTime(int gameTime)
	{
		this.m_GameTimeRemaininginMS = gameTime;
	}
	
	/**
	 * GameType enumeration
	 */
	public enum GameType
	{
		SinglePlayer,
		MultiPlayer
	}
}
