package psu.se.wordslam.model;

import java.util.Vector;
import android.graphics.Point;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList; 

/**
 * Model class for representing a 5x5 grid of letters
 *
 */
public class LetterGrid 
{
	private char[][] m_gridData;
	//private static ArrayList<Character> alphabet;
	
	/**
	 * Basic Constructor
	 */
	public LetterGrid()
	{
		this.m_gridData = new char[5][5];
	}
	
	/**
	 * Fills the board with random characters returned by static class Alphabet
	 */
	public void GenerateRandomBoard()
	{
		//Random oRandom = new Random();
		//int randomIndex;
		
		for(int y=0;y<5;y++)
		{
			for(int x=0;x<5;x++)
			{
				//randomIndex = oRandom.nextInt(WordSlamApplication.easyAlphabet.size());
				//this.m_gridData[x][y] = (char)WordSlamApplication.easyAlphabet.get(randomIndex);
				//this.m_gridData[x][y] = (char)(oRandom.nextInt(26) + 'A');
				this.m_gridData[x][y] = Alphabet.getLetter();
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

	/////////////////////////////////////////////////////////////////////////

	private String[] gridToRowArray()
	{
		String[] gridArray = new String[5];
		String result = "";
		for(int y=0;y<5 ;y++)
		{
			result = "";
			for(int x=0; x<5 ;x++)
			{
				result += this.m_gridData[x][y];
			}
			gridArray[y] = result.toLowerCase();
		}
		
		return gridArray;
	}
	
	private String[] gridToColArray(){
		String[] gridArray = new String[5];
		String result = "";
		for(int y=0;y<5 ;y++)
		{
			result = "";
			for(int x=0; x<5 ;x++)
			{
				result += this.m_gridData[y][x];
			}
			gridArray[y] = result.toLowerCase();
		}
		
		return gridArray;
	}
	
	private String[] gridToDiagArray(){		
		String result_1 = "";
		String result_2 = "";
		String result_3 = "";
		String result_4 = "";
		String result_5 = "";
		String result_6 = "";
		String result_7 = "";		
		for(int x=0; x<5; x++)
		{
			result_1 += this.m_gridData[x][x];
			if (x+1<5){
				result_2 += this.m_gridData[x][x+1];
				result_5 += this.m_gridData[x+1][x];
			}
			if (x+2<5){
				result_3 += this.m_gridData[x][x+2];
				result_6 += this.m_gridData[x+2][x];
			}
			if (x+3<5){
				result_4 += this.m_gridData[x][x+3];
				result_7 += this.m_gridData[x+3][x];
			}				
		}		
		String[] gridArray = {result_1.toLowerCase() , result_2.toLowerCase() , result_3.toLowerCase() , result_4.toLowerCase() , result_5.toLowerCase() , result_6.toLowerCase() , result_7.toLowerCase()};		
		
		return gridArray;
	}
	
	public ArrayList<String> getValidWordsinGrid(HashSet<String> hd){
		String gridWord;
		ArrayList<String> grid_valid_words = new ArrayList<String>();
		String[] gridRowArray = this.gridToRowArray();
		
		System.out.println("printing grid row wise");
		for (int i=0; i<gridRowArray.length; i++){
		      System.out.println(gridRowArray[i]);
		      gridWord = gridRowArray[i];
		      grid_valid_words.addAll(getValidWordsinString(gridWord, hd));
		}
		
		System.out.println("printing grid col wise");
		String[] gridColArray = this.gridToColArray();
		for (int i=0; i<gridColArray.length; i++){
		      System.out.println(gridColArray[i]);
		      gridWord = gridColArray[i];
		      grid_valid_words.addAll(getValidWordsinString(gridWord, hd));
		}
		
		System.out.println("printing grid diagonally");
		String[] gridDiagArray = this.gridToDiagArray();
		for (int i=0; i<gridDiagArray.length; i++){
		      System.out.println(gridDiagArray[i]);
		      gridWord = gridDiagArray[i];
		      grid_valid_words.addAll(getValidWordsinString(gridWord, hd));
		}
			
		return grid_valid_words;		
	}
	
	private List<String> getValidWordsinString(String a, HashSet<String> hd){		
		List<String> valid_words = new ArrayList<String>();
		List<String> words = new ArrayList<String>();
		words = findValidWords(a, valid_words, hd);
		return removeDuplicates(valid_words);
	}
	
	/**
	 * Find all possible permutations of 5 letter word
	 * @param Array[] of 5 letters
	 * @return Array[][] of all possible word combinations in the input
	 */
	private List<String> findValidWords(String a, List<String> valid_words, HashSet<String> hd){
		List<String> words = new ArrayList<String>();
		List<String> sub_words = new ArrayList<String>();
		int i,j;
		char fix_letter;
		int n = a.length();
		String sub_a = new String();
		if (n==1)
		{
			words.add(a);
		}
		else{
			for (i=0; i<n; i++)
			{
				fix_letter = a.charAt(i);
				sub_a = pullChar(i,a);
				sub_words = findValidWords(sub_a, valid_words, hd);
				for (j = 0; j < sub_words.size(); j++) {
					words.add(fix_letter+sub_words.get(j));
					//if (hd.dictionary_search(fix_letter+sub_words.get(j))){
					if(hd.contains(fix_letter+sub_words.get(j))){
						valid_words.add(fix_letter+sub_words.get(j));
					}
				}				
			}				
		}		
		return words;
	}
	
	private String pullChar(int t, String a){
		String sub_a = new String();
		sub_a ="";
		int j;
		int n = a.length();
		for (j=0; j<n; j++){
			if (j!=t){
				sub_a += a.charAt(j);
			}			
		}
		return sub_a;
	}	
	
	public List<String> removeDuplicates(List<String> a){
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(a);
		a.clear();
		a.addAll(hs);
		return a;
	}

}
