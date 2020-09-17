import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import javax.activation.MailcapCommandMap;

import com.sun.accessibility.internal.resources.accessibility;
import com.sun.scenario.animation.shared.InfiniteClipEnvelope;

import sun.security.ssl.KerberosClientKeyExchange;

public class ModifiedRotxCipher extends RotxCipher {
	private FileReader fileReader;
    private BufferedReader bufferedReader;
    private List<String> modifiedAlphabetStrings = new ArrayList<String>();
	private List<String> elementNameStrings = new ArrayList<String>();
	private String elementString = "";
	private dictionary dictionary;
	private String outputString;
	private int cipherPosition;
	private String inputString;
	private HashMap<String, HashMap<Character, Integer>> cipherAlphabetHashMap = new HashMap<String, HashMap<Character,Integer>>();
	public ModifiedRotxCipher(dictionary enDictionary) throws IOException {
		super(enDictionary);
		fileReader = new FileReader("words.txt");
		bufferedReader = new BufferedReader(fileReader);
		String wordString= "";
		while ((wordString = bufferedReader.readLine()) != null) {
			elementNameStrings.add(wordString);
		}
		
		//initiate a new list with subitions alphabets
		FileReader alphabetsFileReader = new FileReader("subtitution_alphabets.txt");
		bufferedReader = new BufferedReader(alphabetsFileReader);
		while ((wordString = bufferedReader.readLine()) != null) {
			modifiedAlphabetStrings.add(wordString);
		}
		
		for (int i = 0; i < elementNameStrings.size(); i++) {
			//cast the subtition alphabet to char[]
			char[] alphabetCharacters = modifiedAlphabetStrings.get(i).toCharArray();
			HashMap<Character, Integer> modifiedAlphabetHashMap = new HashMap<Character, Integer>();
			
			//put element in char[] in hashmap with its corresponding value
			for (int j = 0; j < alphabetCharacters.length; j++) {
				modifiedAlphabetHashMap.put(alphabetCharacters[j], j+97);
			}
			
			//put subtition alphabet in hashmap with its corresponding keywords
			cipherAlphabetHashMap.put(elementNameStrings.get(i), modifiedAlphabetHashMap);
		}
		this.dictionary = enDictionary;
	}
	
	
	//this similar to rotx. however, when try to decode. the reference alphabet will be the subtition alphabet 
	public boolean decode(String input) {
		this.inputString = input;
		String filteredInputString = input;
		filteredInputString = filteredInputString.replace(" ", "");
		for (Map.Entry<String, HashMap<Character, Integer>> entry: cipherAlphabetHashMap.entrySet()) {
			HashMap<Character, Integer> modifiedHashMap = entry.getValue();
			int range = entry.getKey().length();
			String tmpOutputString = this.reverseEncode(filteredInputString,modifiedHashMap, range);
			if(tmpOutputString.length() > 0) {
				this.elementString = entry.getKey();
				String output = "";
				int numbOfWhiteSpace = 0;
				for (int i = 0; i < inputString.length(); i++) {
					if(inputString.charAt(i) != ' ' && numbOfWhiteSpace == 0)
						output += tmpOutputString.charAt(i);
					else if(inputString.charAt(i) != ' ' && numbOfWhiteSpace > 0)
						output += tmpOutputString.charAt(i - numbOfWhiteSpace);
					else if(inputString.charAt(i) == ' ') {
						output += " ";
						numbOfWhiteSpace ++;
					}
				}
				output = output.toUpperCase() + ",M," + elementString + "," + cipherPosition;
				outputString = output;
				return true;
			}	
		}
				
		return false;
	}

	public String encode(String input, String addedElement, int shiftPosition) {
		String tmpInputString = input;
		tmpInputString = tmpInputString.replace(" ", "");
		HashMap<Character, Integer> shiftedAlphabetHashMap = cipherAlphabetHashMap.get(addedElement);
		
		int[] vals = new int[tmpInputString.length()];
		for (int j = 0; j < tmpInputString.length(); j++) {
			vals[j] = (int)tmpInputString.charAt(j) + shiftPosition;
			if(vals[j] > 122)
				vals[j] = vals[j] - 26;
			else if (vals[j] < 97)
				vals[j] = vals[j] + 26;
		}
		
		String tmpOutputString = "";
		for (int val : vals) {
			for(Map.Entry<Character, Integer> entry : shiftedAlphabetHashMap.entrySet()) {
				if(val == entry.getValue()) {
					tmpOutputString += entry.getKey();
					break;
				}
			}
		}
		
		return tmpOutputString;
	}
	
	public String decodeManual(String input, String keyName, int stepSize) {
		
		//initialise a list of integer values of inputs
		HashMap<Character, Integer> modifiedAlphabetHashMap = cipherAlphabetHashMap.get(keyName);
		int[] intVals = new int[input.length()];
		for (int i = 0; i < intVals.length; i++) {
			for(Map.Entry<Character, Integer> entry : modifiedAlphabetHashMap.entrySet()) {
				if (input.charAt(i) == entry.getKey()) {
					intVals[i] = entry.getValue();
				}
			}
		}
		
		int[] vals = new int[intVals.length];
		for (int j = 0; j < intVals.length; j++) {
			vals[j] = intVals[j] - stepSize;
			if(vals[j] > 122)
				vals[j] = vals[j] - 26;
			else if (vals[j] < 97)
				vals[j] = vals[j] + 26;
		}
		
		String tmpOutputString = "";
		for (int val : vals) {
				tmpOutputString += (char)val;
		}
		return tmpOutputString;
	}
	
	
	private String reverseEncode(String input, HashMap<Character, Integer> modifiedAlphabetHashMap, int range) {
		
		//initialise a list of integer values of inputs
		int[] intVals = new int[input.length()];
		for (int i = 0; i < intVals.length; i++) {
			for(Map.Entry<Character, Integer> entry : modifiedAlphabetHashMap.entrySet()) {
				if (input.charAt(i) == entry.getKey()) {
					intVals[i] = entry.getValue();
				}
			}
		}
		
		for (int i = range*(-1); i <= range; i++) {
			this.cipherPosition = i;
			int[] vals = new int[intVals.length];
			for (int j = 0; j < intVals.length; j++) {
				vals[j] = intVals[j] - cipherPosition;
				if(vals[j] > 122)
					vals[j] = vals[j] - 26;
				else if (vals[j] < 97)
					vals[j] = vals[j] + 26;
			}
			
			String tmpOutputString = "";
			for (int val : vals) {
					tmpOutputString += (char)val;
			}
			dictionary.setInput(tmpOutputString);
			if(dictionary.getResult().length() > 0)
				return tmpOutputString;
		}
			
			return "";
	}
		
	
	public List<String> getModifiedAlphabetStrings() {
		return modifiedAlphabetStrings;
	}


	public void setModifiedAlphabetStrings(List<String> modifiedAlphabetStrings) {
		this.modifiedAlphabetStrings = modifiedAlphabetStrings;
	}


	public List<String> getElementNameStrings() {
		return elementNameStrings;
	}
	public void setElementNameStrings(List<String> elementNameStrings) {
		this.elementNameStrings = elementNameStrings;
	}
	
	public String getOutputString() {
		return outputString;
	}

	public void setCipherPosition(int cipherPosition) {
		this.cipherPosition = cipherPosition;
	}
}
