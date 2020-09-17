import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class dictionary {
	private FileReader fileReader;
    private BufferedReader bufferedReader;
    private List<String> dictionaryListString = new ArrayList<String>();
    private List<List<String>> possibleResult = new ArrayList<List<String>>();
    private String inputString = "";
    
    
    public dictionary(String txtFileName) throws IOException {
    	try {
			fileReader = new FileReader(txtFileName);
			bufferedReader = new BufferedReader(fileReader);
			String word = null;
    		while((word = bufferedReader.readLine()) != null) {
                dictionaryListString.add(word);
    		}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public void searchWord(String input, Stack<String> words) {
    	int scanPositionEnd = 1;
    	int scanPositionStart = 0;
    	int end = input.length();
    	String possibleWordString = "";
    	int scanRange = 0;
    	for(;scanPositionEnd <= end; scanPositionEnd++) {
			String subString = input.substring(scanPositionStart, scanPositionEnd);
			if((dictionaryListString.contains(subString) || (subString.matches("a") && scanPositionEnd == 1)) && scanPositionEnd < input.length()) {
				words.push(subString);
				searchWord(input.substring(scanPositionEnd,end), words);
				break;
			}else if(dictionaryListString.contains(subString) && scanPositionEnd == input.length()) {
				words.push(subString);
				possibleResult.add(words);
				break;
			}else if(!dictionaryListString.contains(subString) && !words.isEmpty()) {
				scanRange = scanPositionEnd - scanPositionStart;
				String tmpString = "";
				String prevWordString =	words.peek();
				tmpString = prevWordString + subString;
				if(dictionaryListString.contains(tmpString) && scanPositionEnd == input.length()) {
					words.removeElement(prevWordString);
					words.push(tmpString);
					possibleResult.add(words);
					break;
				}else if (dictionaryListString.contains(tmpString) && scanPositionEnd < end) {
					if (scanRange <= 7) {
						possibleWordString = tmpString;
					}
				}else if (dictionaryListString.contains(possibleWordString) && scanRange > 7) {
					words.remove(prevWordString);
					words.push(possibleWordString);
					searchWord(input.substring(possibleWordString.length() - prevWordString.length()), words);
					break;
				}else if (scanPositionEnd == end) {
					if (dictionaryListString.contains(possibleWordString)) {
						words.removeElement(prevWordString);
						words.push(possibleWordString);
						searchWord(input.substring(possibleWordString.length() - prevWordString.length()), words);
					}else if(words.size()>=2) {
						if (dictionaryListString.contains(words.get(words.size() - 2) + words.peek() + subString)) {
							possibleWordString = words.get(words.size() - 2) + words.peek() + subString;
							words.removeElement(words.get(words.size() - 2));
							words.removeElement(prevWordString);
							words.push(possibleWordString);
							possibleResult.add(words);
						}
					}
					break;
				}else if (words.size() >= 2 ) {
					if (dictionaryListString.contains(words.get(words.size() - 2) + words.peek() + subString) && scanPositionEnd < end) {
						possibleWordString = words.get(words.size() - 2) + words.peek() + subString;
						words.removeElement(words.get(words.size() - 2));
						words.removeElement(prevWordString);
						words.push(possibleWordString);
						searchWord(input.substring(scanRange), words);
						break;
					}
				}
			}
		}
	}
    
	
    public String getResult() {
    	String tmpString = inputString;
    	String inputWithoutSpaceString = tmpString.replace(" ", "");
    	String resultString = "";
    	List<List<String>> tmpList = new ArrayList<List<String>>();
    	tmpList = possibleResult;
    	for (List<String> listString : possibleResult) {
			String possibleResultString = "";
			for (String string : listString) {
				possibleResultString += string;
			}
			if(possibleResultString.length() == inputWithoutSpaceString.length())
				resultString = inputString;
		}
    	tmpList.removeAll(possibleResult);
    	this.possibleResult = tmpList;
    	return resultString;
    }
    
    public void printLib() {
    	for (String string : dictionaryListString) {
			System.out.println(string);
		}
    }
    
    public void setInput(String input) {
		this.inputString = input;
		input = input.toLowerCase();
		input = input.replace(" ", "");
		this.searchWord(input, new Stack<String>());
	}


}
    