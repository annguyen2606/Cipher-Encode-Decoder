import java.io.IOException;
import java.util.Stack;

public class RotxCipher {
	private String outputString = "";
	private int cipherPosition = 0;
	private dictionary dictionary;
	
	public RotxCipher(String dictionaryName) throws IOException {
		this.dictionary = new dictionary(dictionaryName);
	}
	
	public RotxCipher(String dictionaryName, int shiftedPosition) throws IOException {
		this.dictionary = new dictionary(dictionaryName);
		this.cipherPosition = shiftedPosition;
	}
	
	public boolean decode(String input) {
		String shiftedInputString = "";
		for (int i= -13; i <= 13; i++) {
			if (i != 0) {
				this.setCipherPosition(i);
				shiftedInputString = this.reverseEncode(input);
				dictionary.setInput(shiftedInputString);
				shiftedInputString = shiftedInputString.replace(" ", "");
				dictionary.searchWord(shiftedInputString, new Stack<String>());
				if(dictionary.getResult().length() > 0) {
					outputString = dictionary.getResult();
					outputString += ",C," + this.cipherPosition;
					outputString = outputString.toUpperCase();
					return true;
				}
			}
		}
		return false;
		}
	
	public String encode(String input) {
		input = input.toUpperCase();
		
		//initiate a list of letters of input
		String outputString = "";
		for (int i = 0; i < input.length(); i++) {
			char letter = input.charAt(i);
			if(letter != ' ') {
				int tmp = (char) ((int)letter + cipherPosition);
				if(tmp > 90 )
					letter = (char)(((int)letter + cipherPosition - 26));
				else if(tmp < 65)
					letter = (char)(((int)letter + cipherPosition + 26));
				else if(tmp <= 90 && tmp >= 65)
					letter = (char)((int)letter + cipherPosition);
			}
			outputString += letter;
		}
		outputString = outputString.toLowerCase();

		return outputString;
	}
	
	public String reverseEncode(String input) {
		input = input.toUpperCase();
		
		//initiate a list of letters of input
		String outputString = "";
		for (int i = 0; i < input.length(); i++) {
			char letter = input.charAt(i);
			if(letter != ' ') {
				int tmp = (char) ((int)letter - cipherPosition);
				if(tmp > 90 )
					letter = (char)(((int)letter - cipherPosition - 26));
				else if(tmp < 65)
					letter = (char)(((int)letter - cipherPosition + 26));
				else if(tmp <= 90 && tmp >= 65)
					letter = (char)((int)letter - cipherPosition);
			}
			outputString += letter;
		}
		outputString = outputString.toLowerCase();

		return outputString;
	}

	public void setCipherPosition(int position) {
		this.cipherPosition = position;
	}
	
	public int getCipherPosition() {
		return cipherPosition;
	}

	public String getOutput() {
		return outputString;
	}
}
