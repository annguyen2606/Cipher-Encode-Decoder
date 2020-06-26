import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModifiedRotxCipher extends RotxCipher {
	private FileReader fileReader;
    private BufferedReader bufferedReader;
    private List<String> modifiedAlphabetStrings = new ArrayList<String>();
	private List<String> elementNameStrings = new ArrayList<String>();
	private HashMap<String, HashMap<Character, Integer>> cipherAlphabetHashMap = new HashMap<String, HashMap<Character,Integer>>();
	public ModifiedRotxCipher(String dictionaryName) throws IOException {
		super(dictionaryName);
		fileReader = new FileReader("words.txt");
		bufferedReader = new BufferedReader(fileReader);
		String wordString= "";
		while ((wordString = bufferedReader.readLine()) != null) {
			elementNameStrings.add(wordString);
		}
		FileReader alphabetsFileReader = new FileReader("subtitution_alphabets.txt");
		bufferedReader = new BufferedReader(alphabetsFileReader);
		while ((wordString = bufferedReader.readLine()) != null) {
			modifiedAlphabetStrings.add(wordString);
		}
		
		for (int i = 0; i < elementNameStrings.size(); i++) {
			char[] alphabetCharacters = modifiedAlphabetStrings.get(i).toCharArray();
			HashMap<Character, Integer> modifiedAlphabetHashMap = new HashMap<Character, Integer>();
			for (int j = 0; j < alphabetCharacters.length; j++) {
				modifiedAlphabetHashMap.put(alphabetCharacters[j], j);
			}
			cipherAlphabetHashMap.put(elementNameStrings.get(i), modifiedAlphabetHashMap);
		}
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
}
