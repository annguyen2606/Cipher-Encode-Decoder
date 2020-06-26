import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class DiagonalTranspositionCipher {
	private int numOfColumn = 0;
	private String inputString = "";
	private String outputString = "";
	private dictionary dictionary;
	public DiagonalTranspositionCipher(String dictionaryName) throws IOException {
		this.dictionary = new dictionary(dictionaryName);
	}
	
	public void encodeInput(String inputString) {
		this.inputString = inputString;
		encodeDiagonal(inputString, 8);
	}
	public Boolean decodeInput(String inputString) {
		this.inputString = inputString;
		return decodeDiagonalInput();
	}
	
	private Boolean decodeReverseDiagonalInput() {
		String filteredInputString = this.inputString;
		filteredInputString = filteredInputString.replace(" ", "");
		char[] charArrayOfInput = filteredInputString.toCharArray();
		int numOfChar = charArrayOfInput.length;
		int numOfColumn = 2;
		int numOfRow = 0;
		
		for(; numOfColumn<numOfChar ;numOfColumn++) {
			int[] scanPoint = new int[] {0,0};
			if(numOfChar%numOfColumn == 0)
				numOfRow = numOfChar/numOfColumn;
			else
				numOfRow = (numOfChar/numOfColumn) + 1;
			String[][] arraysOfCharacters = new String[numOfRow][numOfColumn];
			String tmpOutputString = "";
			
			int startOfScanLine = 0;
			int endScanCount = numOfColumn+numOfRow - 1;
			int inputScanPosition = 0;
			int tmpScanCount = 0;
			while(tmpScanCount <= endScanCount) {
				if (inputScanPosition == filteredInputString.length()) {
					break;
				}
				if (scanPoint[1] < numOfColumn) {
					if (scanPoint[0] < numOfRow - 1) {
						arraysOfCharacters[scanPoint[0]][scanPoint[1]] = Character.toString(charArrayOfInput[inputScanPosition]);
						inputScanPosition++;
					}else if(scanPoint[0] == numOfRow - 1 && scanPoint[1] <= numOfChar%numOfColumn-1) {
						arraysOfCharacters[scanPoint[0]][scanPoint[1]] = Character.toString(charArrayOfInput[inputScanPosition]);
						inputScanPosition++;
					}else if (scanPoint[0] == numOfRow - 1 && numOfChar%numOfColumn == 0) {
						arraysOfCharacters[scanPoint[0]][scanPoint[1]] = Character.toString(charArrayOfInput[inputScanPosition]);
						inputScanPosition++;
					}
				}
				if(scanPoint[1] == startOfScanLine) {
					tmpScanCount++;
					if (numOfColumn > numOfRow) {
						startOfScanLine = scanPoint[0] = Math.min(numOfColumn-1,tmpScanCount);
					}else {
						startOfScanLine = scanPoint[0] = Math.min(numOfRow-1,tmpScanCount);
					}
					scanPoint[1] = tmpScanCount - scanPoint[0];
				}else {
					scanPoint[0]--;
					scanPoint[1]++;
				}
			}
			int whiteSpaceIndex = 0;
			List<Integer> listOfWhiteSpaceIndexes = new ArrayList<Integer>();
			while((whiteSpaceIndex = inputString.indexOf(' ', whiteSpaceIndex + 1))>0)
				listOfWhiteSpaceIndexes.add(whiteSpaceIndex);
			for (int i = 0; i < numOfRow; i++) {
				for (int j = 0; j < numOfColumn; j++) {
					for (Integer integer : listOfWhiteSpaceIndexes) {
						if (tmpOutputString.length() == integer) {
							tmpOutputString += " ";
						}
					}
					tmpOutputString += arraysOfCharacters[i][j];
					if(tmpOutputString.length() == inputString.length())
						break;
				}
			}
			String inputOfDictionaryString = tmpOutputString;
			inputOfDictionaryString = inputOfDictionaryString.replace(" ", "");
			dictionary.setInput(inputString);
			dictionary.searchWord(inputOfDictionaryString, new Stack<String>());
			if(dictionary.getResult().length() > 0) {
				outputString = tmpOutputString;
				this.numOfColumn = numOfColumn;
				outputString += ",D,-" +numOfColumn;
				outputString = outputString.toUpperCase();
				return true;
			}
		}return false;
	}
	
	public void encodeReverseDiagonal(String input, int numOfColumn) {
		String filteredInputString = input;
		filteredInputString = filteredInputString.replace(" ", "");
		char[] charArrayOfInput = filteredInputString.toCharArray();
		int numOfChar = charArrayOfInput.length;
		int numOfRow = 0;
		int[] scanPoint = new int[] {0,0};
		if(numOfChar%numOfColumn == 0)
			numOfRow = numOfChar/numOfColumn;
		else
			numOfRow = (numOfChar/numOfColumn) + 1;
		String[][] arraysOfCharacters = new String[numOfRow][numOfColumn];
		int startSubPosition = 0;
		for (int i = 1; i <= numOfRow; i++) {
			int endSubPostion = (i*numOfColumn) - 1;
			if(i == numOfRow)
				endSubPostion = charArrayOfInput.length - 1;
			int tmp = 0;
			for (; startSubPosition <= endSubPostion; startSubPosition++) {
				arraysOfCharacters[i-1][tmp] = Character.toString(charArrayOfInput[startSubPosition]);
				tmp++;
			}
		}
		
		String tmpOutputString = "";
		int startOfScanLine = 0;
		int endScanCount = numOfColumn+numOfRow - 1;
		int tmpScanCount = 0;
		while(tmpScanCount <= endScanCount) {
			if (tmpOutputString.length() == filteredInputString.length()) {
				break;
			}
			if (scanPoint[1] < numOfColumn) {
				if (scanPoint[0] < numOfRow - 1) {
					tmpOutputString += arraysOfCharacters[scanPoint[0]][scanPoint[1]];
				}else if(scanPoint[0] == numOfRow - 1 && scanPoint[1] <= numOfChar%numOfColumn-1)
					tmpOutputString += arraysOfCharacters[scanPoint[0]][scanPoint[1]];
				else if (scanPoint[0] == numOfRow - 1 && numOfChar%numOfColumn == 0) {
					tmpOutputString += arraysOfCharacters[scanPoint[0]][scanPoint[1]];
				}
			}
			if(scanPoint[1] == startOfScanLine) {
				tmpScanCount++;
				if (numOfColumn > numOfRow) {
					startOfScanLine = scanPoint[0] = Math.min(numOfColumn-1,tmpScanCount);
				}else {
					startOfScanLine = scanPoint[0] = Math.min(numOfRow-1,tmpScanCount);
				}
				scanPoint[1] = tmpScanCount - scanPoint[0];
			}else {
				scanPoint[0]--;
				scanPoint[1]++;
			}
		}
		System.out.println(tmpOutputString);
	}
	
	public void encodeDiagonal(String input, int numOfColumn) {
		String filteredInputString = input;
		filteredInputString = filteredInputString.replace(" ", "");
		char[] charArrayOfInput = filteredInputString.toCharArray();
		int numOfChar = charArrayOfInput.length;
		int numOfRow = 0;
		if(numOfChar%numOfColumn == 0)
			numOfRow = numOfChar/numOfColumn;
		else
			numOfRow = (numOfChar/numOfColumn) + 1;
		String[][] arraysOfCharacters = new String[numOfRow][numOfColumn];
		int startSubPosition = 0;
		for (int i = 1; i <= numOfRow; i++) {
			int endSubPostion = (i*numOfColumn) - 1;
			if(i == numOfRow)
				endSubPostion = charArrayOfInput.length - 1;
			int tmp = 0;
			for (; startSubPosition <= endSubPostion; startSubPosition++) {
				arraysOfCharacters[i-1][tmp] = Character.toString(charArrayOfInput[startSubPosition]);
				tmp++;
			}
		}
		int[] scanPoint = new int[] {numOfRow - 1,0};
		String tmpOutputString = "";
		int startOfScanLine = numOfRow - 1;
		int endScanCount = numOfColumn+numOfRow - 1;
		int tmpScanCount = 0;
		while(tmpScanCount <= endScanCount) {
			if (tmpOutputString.length() == filteredInputString.length()) {
				break;
			}
			if (scanPoint[1] < numOfColumn) {
				if (scanPoint[0] < numOfRow - 1) {
					tmpOutputString += arraysOfCharacters[scanPoint[0]][scanPoint[1]];
				}else if(scanPoint[0] == numOfRow - 1 && scanPoint[1] <= numOfChar%numOfColumn-1)
					tmpOutputString += arraysOfCharacters[scanPoint[0]][scanPoint[1]];
				else if (scanPoint[0] == numOfRow - 1 && numOfChar%numOfColumn == 0) {
					tmpOutputString += arraysOfCharacters[scanPoint[0]][scanPoint[1]];
				}
			}
			if(scanPoint[1] == tmpScanCount) {
				tmpScanCount++;
				startOfScanLine--;
				scanPoint[0] = Math.max(0, startOfScanLine);
				if(tmpScanCount <= numOfRow - 1)
					scanPoint[1] = 0;
				else {
					if (numOfColumn > numOfRow) {
						scanPoint[1] = tmpScanCount - numOfRow + 1;
					}else {
						scanPoint[1] = tmpScanCount - numOfColumn + 1;
					}
				}
			}else {
				scanPoint[1]++;
				scanPoint[0]++;
			}
		}
		System.out.println(tmpOutputString);
	}
	
	private Boolean decodeDiagonalInput() {
		String filteredInputString = this.inputString;
		filteredInputString = filteredInputString.replace(" ", "");
		char[] charArrayOfInput = filteredInputString.toCharArray();
		int numOfChar = charArrayOfInput.length;
		int numOfColumn = 2;
		int numOfRow = 0;
		
		for(; numOfColumn<numOfChar ;numOfColumn++) {
			int inputScanPosition = 0;
			if(numOfChar%numOfColumn == 0)
				numOfRow = numOfChar/numOfColumn;
			else
				numOfRow = (numOfChar/numOfColumn) + 1;
			String[][] arraysOfCharacters = new String[numOfRow][numOfColumn];
			
			int[] scanPoint = new int[] {numOfRow - 1,0};
			String tmpOutputString = "";
			int startOfScanLine = numOfRow - 1;
			int endScanCount = numOfColumn+numOfRow - 1;
			int tmpScanCount = 0;
			while(tmpScanCount <= endScanCount) {
				if (inputScanPosition == filteredInputString.length()) {
					break;
				}
				if (scanPoint[1] < numOfColumn) {
					if (scanPoint[0] < numOfRow - 1) {
						arraysOfCharacters[scanPoint[0]][scanPoint[1]] = Character.toString(charArrayOfInput[inputScanPosition]);
						inputScanPosition++;
					}else if(scanPoint[0] == numOfRow - 1 && scanPoint[1] <= numOfChar%numOfColumn-1) {
						arraysOfCharacters[scanPoint[0]][scanPoint[1]] = Character.toString(charArrayOfInput[inputScanPosition]);
						inputScanPosition++;
					}else if (scanPoint[0] == numOfRow - 1 && numOfChar%numOfColumn == 0) {
						arraysOfCharacters[scanPoint[0]][scanPoint[1]] = Character.toString(charArrayOfInput[inputScanPosition]);
						inputScanPosition++;
					}
				}
				if(scanPoint[1] == tmpScanCount) {
					tmpScanCount++;
					startOfScanLine--;
					scanPoint[0] = Math.max(0, startOfScanLine);
					if(tmpScanCount <= numOfRow - 1)
						scanPoint[1] = 0;
					else {
						scanPoint[1] = tmpScanCount - numOfRow + 1;
					}
				}else {
					scanPoint[1]++;
					scanPoint[0]++;
				}
			}
			int whiteSpaceIndex = 0;
			List<Integer> listOfWhiteSpaceIndexes = new ArrayList<Integer>();
			while((whiteSpaceIndex = inputString.indexOf(' ', whiteSpaceIndex + 1))>0)
				listOfWhiteSpaceIndexes.add(whiteSpaceIndex);
			for (int i = 0; i < numOfRow; i++) {
				for (int j = 0; j < numOfColumn; j++) {
					for (Integer integer : listOfWhiteSpaceIndexes) {
						if (tmpOutputString.length() == integer) {
							tmpOutputString += " ";
						}
					}
					tmpOutputString += arraysOfCharacters[i][j];
					if(tmpOutputString.length() == inputString.length())
						break;
				}
			}
			String inputOfDictionaryString = tmpOutputString;
			inputOfDictionaryString = inputOfDictionaryString.replace(" ", "");
			dictionary.setInput(inputString);
			dictionary.searchWord(inputOfDictionaryString, new Stack<String>());
			if(dictionary.getResult().length() > 0) {
				outputString = tmpOutputString;
				this.numOfColumn = numOfColumn;
				outputString += ",D," +numOfColumn;
				outputString = outputString.toUpperCase();
				return true;
			}
		}return false;
	}

	public String getOutputString() {
		return outputString;
	}

	public void setOutputString(String outputString) {
		this.outputString = outputString;
	}
}
