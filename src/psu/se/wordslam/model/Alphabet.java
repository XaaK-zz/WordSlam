package psu.se.wordslam.model;

import java.util.ArrayList;
import java.util.Collections;

public class Alphabet {
	private static final int		NUM_VOWELS = 4;
	private static final int		NUM_EASY_CONS = 3;
	private static final int		NUM_HARD_CONS = 1;
	
	
	public static ArrayList<Character> buildEasyAlphabet() {
		ArrayList<Character> easyAlphabet = new ArrayList<Character>();
		
		for (int i = 0; i < NUM_VOWELS; ++i) {
			easyAlphabet.add('A');
			easyAlphabet.add('E');
			easyAlphabet.add('I');
			easyAlphabet.add('O');
			easyAlphabet.add('U');
		}
		for (int i = 0; i < NUM_EASY_CONS; ++i) {
			easyAlphabet.add('B');
			easyAlphabet.add('C');
			easyAlphabet.add('D');
			easyAlphabet.add('F');
			easyAlphabet.add('G');
			easyAlphabet.add('H');
			easyAlphabet.add('L');
			easyAlphabet.add('M');
			easyAlphabet.add('N');
			easyAlphabet.add('P');
			easyAlphabet.add('R');
			easyAlphabet.add('S');
			easyAlphabet.add('T');
		}
		for (int i = 0; i < NUM_HARD_CONS; ++i) {
			easyAlphabet.add('J');
			easyAlphabet.add('K');
			easyAlphabet.add('Q');
			easyAlphabet.add('V');
			easyAlphabet.add('W');
			easyAlphabet.add('X');
			easyAlphabet.add('Z');
		}
		//Collections.shuffle(easyAlphabet); // needed?
		return easyAlphabet;
	}

}
