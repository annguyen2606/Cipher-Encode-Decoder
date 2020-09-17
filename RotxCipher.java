import java.io.IOException;
import java.util.Stack;

public class RotxCipher {
	private String outputString = "";
	private int cipherPosition = 0;
	private dictionary dictionary;
	
	public RotxCipher(dictionary enDictionary){
		this.dictionary = enDictionary;
	}
	
	public RotxCipher(dictionary enDictionary, int shiftedPosition) throws IOException {
		this.dictionary = enDictionary;
		this.cipherPosition = shiftedPosition;
	}
	
	public boolean decode(String input) {
		String shiftedInputString = "";
		for (int i= -13; i <= 13; i++) {
			if (i != 0) {
				this.cipherPosition = i;
				shiftedInputString = this.reverseEncode(input);
				dictionary.setInput(shiftedInputString);
				if(dictionary.getResult().length() > 0) {
					outputString = shiftedInputString;
					outputString += ",C," + this.cipherPosition;
					outputString = outputString.toUpperCase();
					return true;
				}
			}
		}
		return false;
	}
	
	public String decodeManual(String input, int stepSize) {
		input = input.toLowerCase();
		input = input.replace(" ", "");
		//initiate a list of letters of input
		String outputString = "";
		for (int i = 0; i < input.length(); i++) {
			char letter = input.charAt(i);
			if(letter != ' ') {
				int tmp = ((int)letter - stepSize);
				if(tmp > 122 )
					tmp = tmp - 26;
				else if(tmp < 97)
					tmp = tmp + 26;
				
				outputString += (char)tmp;
			}else {
				outputString += " ";
			}
		}
		outputString = outputString.toLowerCase();

		return outputString;
	}
	
	public String encode(String input, int cipherPosition) {
		input = input.toLowerCase();
		
		//initiate a list of letters of input
		String outputString = "";
		for (int i = 0; i < input.length(); i++) {
			char letter = input.charAt(i);
			if(letter != ' ') {
				int tmp = ((int)letter - cipherPosition);
				if(tmp > 122 )
					tmp = tmp - 26;
				else if(tmp < 97)
					tmp = tmp + 26;
				
				outputString += (char)tmp;
			}else {
				outputString += " ";
			}
		}
		outputString = outputString.toLowerCase();


		return outputString;
	}
	
	private String reverseEncode(String input) {
		
		String outputString = "";
		for (int i = 0; i < input.length(); i++) {
			char letter = input.charAt(i);
			if(letter != ' ') {
				int tmp = ((int)letter - cipherPosition);
				if(tmp > 122 )
					tmp = tmp - 26;
				else if(tmp < 97)
					tmp = tmp + 26;
				
				outputString += (char)tmp;
			}else {
				outputString += " ";
			}
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
