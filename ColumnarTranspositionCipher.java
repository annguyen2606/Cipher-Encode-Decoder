import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.sun.corba.se.spi.orb.StringPair;
import com.sun.org.apache.xml.internal.utils.SuballocatedByteVector;

public class ColumnarTranspositionCipher{
	private int numOfColumn;
	private String outputString = "";
	private String inputString = "";
	private dictionary dictionary;
	public ColumnarTranspositionCipher(dictionary enDictionary){
		this.dictionary = enDictionary;
	}
	
	public boolean decode(String input) {
		String tmpString = input;
		tmpString = tmpString.toLowerCase();
		tmpString = tmpString.replace(" ", "");
		int numOfColumn = 2;
		int lengthOfInput = tmpString.length();
		for (; numOfColumn < lengthOfInput - 1; numOfColumn++) {
			int numOfRow = lengthOfInput/numOfColumn;
			if(lengthOfInput%numOfColumn > 0)
				numOfRow = numOfRow + 1;
			
			//initiate 2D array for input
			String[][] arrayOfInput = new String[numOfRow][numOfColumn];
			
			//initiate positions of blank block in last row
			List<Integer> positionsOfPlainBlocks = new ArrayList<Integer>();
			
			if(lengthOfInput%numOfColumn > 0) {
				//there would be blank blocks if this condition matched
				for(int i= lengthOfInput%numOfColumn; i < numOfColumn; i++)
					positionsOfPlainBlocks.add(i);
			}
			
			List<Integer[]> positionsOfCharaterBlock = new ArrayList<Integer[]>();
			String tmpOutputString = "";
			//put sequence of character character blocks in column by column
			for (int i = 0; i < numOfColumn; i++) {
				for (int j = 0; j < numOfRow; j++) {
					if(j == numOfRow - 1 && positionsOfPlainBlocks.size() > 0) {
						if (positionsOfPlainBlocks.contains(i)) {
							//Skip when meet blank block at the last row
							break;
						}else {
							positionsOfCharaterBlock.add(new Integer[] {j,i});
						}
					}else {
						positionsOfCharaterBlock.add(new Integer[] {j,i});
					}
				}
			}
			
			//put character in to new array base on sequence of the character
			for (Integer[] integers : positionsOfCharaterBlock) {
				arrayOfInput[integers[0]][integers[1]] =  String.valueOf(tmpString.charAt(positionsOfCharaterBlock.indexOf(integers)));
			}
			
			//read the array row by row
			for (int i = 0; i < numOfRow; i++) {
				for (int j = 0; j < numOfColumn; j++) {
					if(i == numOfRow - 1 && !positionsOfPlainBlocks.isEmpty()) {
						if(positionsOfPlainBlocks.contains(j))
							break;
						else
							tmpOutputString += arrayOfInput[i][j];
					}else {
						tmpOutputString += arrayOfInput[i][j];
					}
				}
			}
			dictionary.setInput(tmpOutputString);
			if(dictionary.getResult().length() > 0) {
				String output = "";
				int numOfWhiteSpace = 0;
				for (int i = 0; i < input.length(); i++) {
					if(input.charAt(i) == ' ') {
						output += " ";
						numOfWhiteSpace ++;
					}
					else {
						output += tmpOutputString.charAt(i - numOfWhiteSpace);
					}
				}
				output = output.toUpperCase() + ",T," + numOfColumn;
				outputString = output;
				return true;
			}
		}return false;
	}
	
	public String encode(String input, int colSize) {
		String tmpString = input;
		tmpString = tmpString.toLowerCase();
		tmpString = tmpString.replace(" ", "");
		int numOfColumn = colSize;
		int lengthOfInput = tmpString.length();
		
		int numOfRow = lengthOfInput/numOfColumn;
		if(lengthOfInput%numOfColumn > 0)
			numOfRow = numOfRow + 1;
		String tmpOutputString = "";
		String[] rowStrings = new String[numOfRow];
		for (int i = 1; i <= rowStrings.length; i++) {
			if(i == rowStrings.length) {
				rowStrings[i-1] = input.substring((i-1)*numOfColumn);
			}else {
				rowStrings[i-1] = input.substring((i-1)*numOfColumn, i*numOfColumn);
			}
		}
		for (int i = 0; i < numOfColumn; i++) {
			for (int j = 0; j < numOfRow; j++) {
				if(j == numOfRow - 1 && lengthOfInput%numOfColumn > 0) {
					if (i >= lengthOfInput%numOfColumn) {
						break;
					}else {
						tmpOutputString += rowStrings[j].charAt(i);
					}
				}else {
					tmpOutputString += rowStrings[j].charAt(i);
				}
			}
		}
		return tmpOutputString;
	}
	
	public String decodeManual(String input, int colSize) {
		String tmpString = input;
		tmpString = tmpString.toLowerCase();
		tmpString = tmpString.replace(" ", "");
		int numOfColumn = colSize;
		int lengthOfInput = tmpString.length();
		
		int numOfRow = lengthOfInput/numOfColumn;
		if(lengthOfInput%numOfColumn > 0)
			numOfRow = numOfRow + 1;
		String tmpOutputString = "";
		String[][] arrayOfInput = new String[numOfRow][numOfColumn];
		List<Integer> positionsOfPlainBlocks = new ArrayList<Integer>();
		if(lengthOfInput%numOfColumn > 0) {
			for(int i= lengthOfInput%numOfColumn; i < numOfColumn; i++)
				positionsOfPlainBlocks.add(i);
		}
		
		List<Integer[]> positionsOfCharaterBlock = new ArrayList<Integer[]>();
		
		for (int i = 0; i < numOfColumn; i++) {
			for (int j = 0; j < numOfRow; j++) {
				if(j == numOfRow - 1 && positionsOfPlainBlocks.size() > 0) {
					if (positionsOfPlainBlocks.contains(i)) {
						break;
					}else {
						positionsOfCharaterBlock.add(new Integer[] {j,i});
					}
				}else {
					positionsOfCharaterBlock.add(new Integer[] {j,i});
				}
			}
		}
		
		for (Integer[] integers : positionsOfCharaterBlock) {
			arrayOfInput[integers[0]][integers[1]] =  String.valueOf(tmpString.charAt(positionsOfCharaterBlock.indexOf(integers)));
		}
		for (int i = 0; i < numOfRow; i++) {
			for (int j = 0; j < numOfColumn; j++) {
				if(i == numOfRow - 1 && !positionsOfPlainBlocks.isEmpty()) {
					if(positionsOfPlainBlocks.contains(j))
						break;
					else
						tmpOutputString += arrayOfInput[i][j];
				}else {
					tmpOutputString += arrayOfInput[i][j];
				}
			}
		}
		return tmpOutputString;
	}
	public String getOutputString() {
		return outputString;
	}

}
