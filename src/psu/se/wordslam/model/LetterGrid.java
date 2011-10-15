package psu.se.wordslam.model;

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
	
	/**
	 * Basic Constructor
	 */
	public LetterGrid()
	{
		this.m_gridData = new char[5][5];
	}
	
	/**
	 * Fills the board with random characters
	 */
	public void GenerateRandomBoard()
	{
		Random oRandom = new Random();
		
		for(int y=0;y<5;y++)
		{
			for(int x=0;x<5;x++)
			{
				this.m_gridData[x][y] = (char)(oRandom.nextInt(26) + 'a');
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
