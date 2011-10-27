package psu.se.wordslam.model;

import psu.se.wordslam.model.WordSlamApplication;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

public class HashDictionary 
{
 HashSet<String> dic = new HashSet<String>();
 public static void main(String args[])
 {
	 
	 HashDictionary hash_dictionary = new HashDictionary();	 
	 hash_dictionary.dictionary_build();
	 long startTime = System.currentTimeMillis(); 
	 
	 System.out.println(("Search for god")+hash_dictionary.dictionary_search("god"));
	 System.out.println(("Search for lotus")+hash_dictionary.dictionary_search("lotus"));
	 System.out.println(("Search for zygo")+hash_dictionary.dictionary_search("zygo"));
	 System.out.println(("Search for fan")+hash_dictionary.dictionary_search("fan"));
	 System.out.println(("Search for badge")+hash_dictionary.dictionary_search("badge"));
	 System.out.println(("Search for gown")+hash_dictionary.dictionary_search("gown"));
	 System.out.println(("Search for win")+hash_dictionary.dictionary_search("win"));
	 System.out.println(("Search for fred")+hash_dictionary.dictionary_search("fred"));

	 

	 long endTime = System.currentTimeMillis();	
	 System.out.println("Execution time is :"+ (endTime-startTime));
 }
 public void dictionary_build()
 {
	  try
	  	{
		  	  //Context context = WordSlamApplication.getInstance();
		      
		      InputStream inputStream = null;	    
		      inputStream = WordSlamApplication.assetManager.open("/dictionary.txt");
			  
		      // Open the file that is the first 
			  //FileInputStream fstream = new FileInputStream("../assets/dictionary.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(inputStream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  			 
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null) {
				  // Add the string to array list
				  dic.add(strLine);				  
			  }
			  //Close the input stream
			  in.close();
		  }catch (Exception e){//Catch exception if any
		  System.err.println("Error: " + e.getMessage());
	  }
 }

 
 public boolean dictionary_search(String input){
	 boolean index =  dic.contains(input);
	 return index;	 
 }
 
 
}