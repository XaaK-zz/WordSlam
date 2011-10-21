package psu.se.wordslam.testclient;

import junit.framework.Assert;
import junit.framework.TestCase;

import psu.se.wordslam.model.LetterGrid;

public class TestGrid extends TestCase {

	
	public void testGenerateRandomBoard() {
		LetterGrid grid = new LetterGrid();
		grid.GenerateRandomBoard();
		System.out.println(grid);
	}
	
	public void testFillBoard()
	{
		LetterGrid grid = new LetterGrid();
		char[][] test =  
			{
				{'a','b','c','d','e'},
				{'a','b','c','d','e'},
				{'a','b','c','d','e'},
				{'a','b','c','d','e'},
				{'a','b','c','d','e'}
			};
		
		grid.Fill(test);
		System.out.println(grid);
		//Assert.assertEquals(true, grid.CheckWord(""""""),"Failed to find word.");
		
	}
}
