import java.util.List;
import java.util.Scanner;

public class cipher {

	private static Scanner scanner;

	public static void main(String[] args) {
		System.out.println("Cipher decode/encode");
		System.out.println("Commands:");
		System.out.println("en: encoding, en -mode- -argument-");
		System.out.println("de: decoding, de -mode- -argument-");
		System.out.println("-mode- : 'manual','auto' ");
		System.out.println("-argument- : cipher input/output for 'manual' or file path for 'auto'");
		scanner = new Scanner(System.in);
		try {
			/*String line = scanner.nextLine();
			String[] input = line.split(" ");*/
			/*if(input[0].matches("de") && input[1].matches("manual")) {
				RotxCipher rotxCipher = new RotxCipher("dict_en.txt");
				rotxCipher.setCipherPosition(3);
				
				System.out.println(input[2]);
			}*/
			/*RotxCipher rotxCipher = new RotxCipher("dict_en.txt");
			if(rotxCipher.decodeTextFile("znwsk jaeiw ca"))
				System.out.println(rotxCipher.getOutput());
			ModifiedRotxCipher modifiedRotxCipher = new ModifiedRotxCipher("dict_en.txt");
			List<String> elementNameStrings = modifiedRotxCipher.getElementNameStrings();
			List<String> elementStrings = modifiedRotxCipher.getModifiedAlphabetStrings();
			for (int i = 0; i < elementNameStrings.size(); i++) {
				System.out.println(i +" "+elementNameStrings.get(i) + " " + elementStrings.get(i));
			}*/
			DiagonalTranspositionCipher diagonalTranspositionCipher = new DiagonalTranspositionCipher("dict_en.txt");
			if (diagonalTranspositionCipher.decodeInput("tchoa mneiu dpewa i")){
				System.out.println(diagonalTranspositionCipher.getOutputString());
			}
		} catch (Exception e) {
			System.out.println(e.initCause(e) + "???");
		}
	}
}
