package psu.se.wordslam.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class Alphabet {
	private static final int		NUM_VOWELS = 4;
	private static final int		NUM_EASY_CONS = 3;
	private static final int		NUM_HARD_CONS = 1;
	
	private static Random			rand = new Random();				
	private static final int		RANGE = 1012;	// max is exclusive
	

	
	// Returns a random letter, with more frequently used letters having
	// better odds.
	public static char getLetter() {
		int random = rand.nextInt(RANGE);
				
		if (random >= 0 && random < 83) 
			return 'A';
		else if (random >= 83 && random < 98)
			return 'B';
		else if (random >= 98 && random < 128)
			return 'C';
		else if (random >= 128 && random < 172)
			return 'D';
		else if (random >= 172 && random < 300)
			return 'E';
		else if (random >= 300 && random < 322)
			return 'F';
		else if (random >= 322 && random < 343)
			return 'G';
		else if (random >= 343 && random < 405)
			return 'H';
		else if (random >= 405 && random < 476)
			return 'I';
		else if (random >= 476 && random < 478)
			return 'J';
		else if (random >= 478 && random < 486)
			return 'K';
		else if (random >= 486 && random < 526)
			return 'L';
		else if (random >= 526 && random < 550)
			return 'M';
		else if (random >= 550 && random < 618)
			return 'N';
		else if (random >= 618 && random < 693)
			return 'O';
		else if (random >= 693 && random < 712)
			return 'P';
		else if (random >= 712 && random < 713)
			return 'Q';
		else if (random >= 713 && random < 773)
			return 'R';
		else if (random >= 773 && random < 836)
			return 'S';
		else if (random >= 836 && random < 927)
			return 'T';
		else if (random >= 927 && random < 955)
			return 'U';
		else if (random >= 955 && random < 965)
			return 'V';
		else if (random >= 965 && random < 989)
			return 'W';
		else if (random >= 989 && random < 991)
			return 'X';
		else if (random >= 991 && random < 1011)
			return 'Y';
		else		// if random == 1011
			return 'Z';
		
	}
	
	
	// OLD METHOD
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
