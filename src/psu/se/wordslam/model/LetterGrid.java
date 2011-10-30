package psu.se.wordslam.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import android.graphics.Point;

/**
 * Model class for representing a 5x5 grid of letters
 *
 */
public class LetterGrid 
{
	private char[][] m_gridData;
	private static ArrayList<Character> alphabet;
	
	/**
	 * Basic Constructor
	 */
	public LetterGrid()
	{
		this.m_gridData = new char[5][5];
	}
	
	/**
	 * Fills the board with random characters drawn from easyAlphabet
	 */
	public void GenerateRandomBoard()
	{
		Random oRandom = new Random();
		int randomIndex;
		
		for(int y=0;y<5;y++)
		{
			for(int x=0;x<5;x++)
			{
				randomIndex = oRandom.nextInt(WordSlamApplication.easyAlphabet.size());
				this.m_gridData[x][y] = (char)WordSlamApplication.easyAlphabet.get(randomIndex);
				//this.m_gridData[x][y] = (char)(oRandom.nextInt(26) + 'A');
			}
		}
	}
	
	/**
	 * Method primarily designed for testing<br>
	 * 	Will allow unit tests to insert a specific grid of data into the system
	 */
	public void Fill(char[][] data)
	{
		this.m_gridData = data;
	}
	
	/**
	 * First stab at an API for detecting words.
	 * @param touchPoints set of x,y coordinates on the grid
	 * @return boolean if this the set of letters constitutes a valid word, false otherwise
	 */
	public boolean CheckWord(Vector<Point> touchPoints)
	{
		StringBuilder oSB = new StringBuilder();
		
		for(Point pt : touchPoints)
		{
			oSB.append(this.m_gridData[pt.x][pt.y]);
		}
		//Now have word -> have to check if it is a word
		return true;
	}
	
	/**
	 * Utility method to retrieve a grid character at position x,y
	 * @param x Horizontal position (0-4)
	 * @param y Vertical positions (0-4)
	 * @return character at the specified position in the grid
	 */
	public char GetCharacterAtPosition(int x, int y)
	{
		return this.m_gridData[x][y];
	}
	
	/**
	 * Utility method for displaying a grid to the console
	 */
	public String toString()
	{
		String result = "";
		
		for(int y=0;y<5;y++)
		{
			for(int x=0;x<5;x++)
			{
				result += this.m_gridData[x][y];
			}
			result += "\n";
		}
		
		return result;
	}
}
