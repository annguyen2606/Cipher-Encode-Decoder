import java.io.IOException;

public class cipherObject {
	private dictionary dictionary;
	
	public cipherObject(String dictionaryName) throws IOException{
		dictionary = new dictionary(dictionaryName);
	}
}
