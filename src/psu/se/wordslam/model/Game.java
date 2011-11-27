package psu.se.wordslam.model;

import java.util.ArrayList;

/**
 * Represents a game in the system
 */
public class Game 
{
	private LetterGrid 			m_Grid;
	private ArrayList<String> 	wordsFound;
	private ArrayList<String> 	wordsOpponentFound;
	private ArrayList<String> 	allWords;
	private int 				score;
	private int 				opponentScore;
	private boolean				bonus;
	private String				opponentIPAddress;
	
	private GameType m_GameType;
	
	private int m_TotalGameTimeInMS;

	private int m_GameTimeRemaininginMS;

	private boolean m_IsCutThroat;
	
	/**
	 * Basic constructor - sets defaults for everything
	 */
	public Game(GameType gameType)
	{
		this.m_GameType = gameType;
		this.m_Grid = new LetterGrid();
		this.m_Grid.GenerateRandomBoard();
		this.wordsFound = new ArrayList<String>();
		this.allWords = new ArrayList<String>();
		wordsOpponentFound = new ArrayList<String>();
		// get all words from dictionary and add to allWords list
		this.allWords = this.m_Grid.getValidWordsinGrid(WordSlamApplication.getInstance().dic);
		
		this.score = 0;
		opponentScore = 0;
		this.m_TotalGameTimeInMS = 5000;
		this.m_GameTimeRemaininginMS = 5000;
		this.m_IsCutThroat = false;
	}
	
	/**
	 * Constructor allowing the setting of the CutThroat property 
	 * @param gameType
	 * @param cutThroat
	 */
	public Game(GameType gameType, boolean cutThroat, String columnData, int MaxTime)
	{
		this.m_GameType = gameType;
		this.m_Grid = new LetterGrid();
		if(columnData == null)
			this.m_Grid.GenerateRandomBoard();
		else
			this.m_Grid.RowArrayToGrid(columnData);
		this.wordsFound = new ArrayList<String>();
		wordsOpponentFound = new ArrayList<String>();
		this.allWords = new ArrayList<String>();
		// get all words from dictionary and add to allWords list
		this.allWords = this.m_Grid.getValidWordsinGrid(WordSlamApplication.getInstance().dic);
		
		this.score = 0;
		this.m_TotalGameTimeInMS = MaxTime;
		this.m_GameTimeRemaininginMS = MaxTime;
		this.m_IsCutThroat = cutThroat;
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
	 * Retrieval method for the remaining Grid property
	 * @return LetterGrid object
	 */
	public LetterGrid GetGrid()
	{
		return this.m_Grid;
	}
	
	/**
	 * Retrieval method for the CutThroat property 
	 * @return Boolean flag indicating this is a cutthroat game or not
	 */
	public boolean IsCutThroat()
	{
		return this.m_IsCutThroat;
	}
	
	/**
	 * GameType enumeration
	 */
	public enum GameType
	{
		SinglePlayer,
		MultiPlayer
	}
	
	/**
	 * Retrieval method for all words found by player
	 * @return wordsFound list
	 */
	public ArrayList<String> getFoundWords() 
	{
		return wordsFound;
	}
	
	public ArrayList<String> getOpponentFoundWords() 
	{
		return wordsOpponentFound;
	}
	
	
	/**
	 * Retrieval method for all words on board
	 * @return allWords list
	 */
	public ArrayList<String> getAllWords()
	{
		return allWords;
	}
	
	
	/**
	 * Adds a word to the list of all words found by player
	 * @param word Word found by player
	 */
	public void addFoundWord(String word)
	{
		wordsFound.add(word);	
		
		//Update Score
		this.score += word.length();
	}
	
	public void addOpponentFoundWord(String word)
	{
		this.wordsOpponentFound.add(word);
		
		opponentScore +=word.length();
	}
	
	/**
	 * Utility method to check if a word already exists in the found list
	 * @param word Word to check
	 * @return True is the word is already in the found list.<br>
	 * 			False otherwise
	 */
	public boolean wordAlreadyFound(String word)
	{
		if(this.IsCutThroat())
			return this.wordsOpponentFound.contains(word) || wordsFound.contains(word);
		else
			return wordsFound.contains(word);
	}
	
	/**
	 * Retrieval method for player's score
	 * @return score 
	 */
	public int getScore() {
		return score;
	}
	
	public int GetOpponentScore() {
		return this.opponentScore;
	}
	
	/**
	 * Retrieval method determining if player scored a bonus
	 * @return bonus 
	 */
	public boolean hasBonus() {
		return bonus;
	}

	public void SetOpponentIP(String ipAddress) {
		this.opponentIPAddress = ipAddress;
	}
	
	public String GetOpponentIPAddress() {
		return this.opponentIPAddress;
	}
	
}
