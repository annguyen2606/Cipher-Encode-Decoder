import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import javax.crypto.Cipher;

public class cipher {

	private static Scanner scanner;
	private int step;
	public static void main(String[] args) throws IOException {
		scanner = new Scanner(System.in);
		String inStrings = scanner.nextLine();
		
		Scanner scanner2 = new Scanner(inStrings);
		switch (scanner2.next()) {
		case "en":
			try {
				switch (scanner2.next()) {
				case "rot":
					int step = scanner2.nextInt();
					if(step  >= -13 && step <= 13) {
						if (scanner2.hasNext()) {
							dictionary rotxDictionary = new dictionary("dict_en.txt");
							RotxCipher rotxCipher = new RotxCipher(rotxDictionary);
							System.out.println(rotxCipher.encode(scanner2.next(), step));
						}else {
							System.out.println("no input typed");
						}
					}else {
						System.out.println("-13 <= step <= 13");
					}
					break;
				case "mod":
					String keyString = scanner2.next();
					dictionary modDictionary = new dictionary("dict_en.txt");
					ModifiedRotxCipher modifiedRotxCipher = new ModifiedRotxCipher(modDictionary);
					if (modifiedRotxCipher.getElementNameStrings().contains(keyString)) {
						if (scanner2.hasNext()) {
							int stepMode = scanner2.nextInt();
							if (Math.abs(stepMode) <= keyString.length()) {
								if (scanner2.hasNext()) {
									String inputString = scanner2.next();
									System.out.println(modifiedRotxCipher.encode(inputString, keyString, stepMode));
								}else {
									System.out.println("no input typed in");
								}
							}else {
								System.out.println("step size has to be equal to length of key");
							}
						} else {
							System.out.println("no step size typed in");
						}
					}else {
						System.out.println("key element not valid yet");
					}
					
					break;
					
				case "col":
					if(scanner2.hasNext()) {
						int colSize = scanner2.nextInt();
						dictionary colDictionary = new dictionary("dict_en.txt");
						ColumnarTranspositionCipher columnarTranspositionCipher = new ColumnarTranspositionCipher(colDictionary);
						if (scanner2.hasNext() && colSize > 0) {
							String inputColString = scanner2.next();
							System.out.println(columnarTranspositionCipher.encode(inputColString,colSize));
						} else if(colSize < 0) {
							System.out.println("column size has to be greater than 0");
						}else {
							System.out.println("no input typed in");
						}
					}else {
						System.out.println("no column size typed in");
					}
					break;
				case "dia":
					if(scanner2.hasNext()) {
						int colSize = scanner2.nextInt();
						dictionary traDictionary = new dictionary("dict_en.txt");
						DiagonalTranspositionCipher diagonalTranspositionCipher = new DiagonalTranspositionCipher(traDictionary);
						if(colSize < 0) {
							if (scanner2.hasNext()) {
								String inputDiaString = scanner2.next();
								diagonalTranspositionCipher.encodeReverseDiagonal(inputDiaString,colSize*(-1));
							} else {
								System.out.println("No input typed in");	
							}
						}else {
							if (scanner2.hasNext()) {
								
							} else {
								System.out.println("No input typed in");	
							}
						}
					}else {
						System.out.println("no column size typed in");
					}
					break;
				default:
					
					System.out.println("not valid");
					cipher.main(null);
					break;
				}
			} catch (Exception e) {
				if(e.getClass() == NumberFormatException.class)
					System.out.println("step has to be integer");
				else
					System.out.println(e.getMessage());
			}
			break;
		case "de":
			if(scanner2.hasNext()) {
				String modeDecodeString = scanner2.next();
				switch (modeDecodeString) {
				case "auto":
					try {
						dictionary rotxDictionary = new dictionary("dict_en.txt");
						dictionary modDictionary = new dictionary("dict_en.txt");
						dictionary traDictionary = new dictionary("dict_en.txt");
						dictionary colDictionary = new dictionary("dict_en.txt");
						List<String> inputStrings = new ArrayList<String>();
						FileReader fileReader = new FileReader("input.txt");
						BufferedReader bufferedReader = new BufferedReader(fileReader);
						String word = null;
			    		while((word = bufferedReader.readLine()) != null) {
			                inputStrings.add(word);
			    		}
			    		
			    		ColumnarTranspositionCipher columnarTranspositionCipher = new ColumnarTranspositionCipher(colDictionary);
			    		DiagonalTranspositionCipher diagonalTranspositionCipher = new DiagonalTranspositionCipher(traDictionary);
			    		RotxCipher rotxCipher = new RotxCipher(rotxDictionary);
			    		ModifiedRotxCipher modifiedRotxCipher = new ModifiedRotxCipher(modDictionary); 
			    		int tmp = 1;

						for (String string : inputStrings) {
							if (columnarTranspositionCipher.decode(string)) {
								System.out.println(columnarTranspositionCipher.getOutputString());
								tmp++;
							}else if (diagonalTranspositionCipher.decodeInput(string)) {
								System.out.println(diagonalTranspositionCipher.getOutputString());
								tmp ++;
							}else if(rotxCipher.decode(string)) {
								System.out.println(rotxCipher.getOutput());
								tmp++;
							}else if(modifiedRotxCipher.decode(string)) {
								System.out.println(modifiedRotxCipher.getOutputString());
								tmp++;
							}else {
								System.out.println(inputStrings.indexOf(string));
							}
						}System.out.println(tmp);
					} catch (Exception e) {
						System.out.println(e.initCause(e) + "???");
					}
					break;
				case "manual":
					if(scanner2.hasNext()) {
						String mode = scanner2.next();
						switch (mode) {
						case "rot":
							if (scanner2.hasNext()) {
								int stepSize = scanner2.nextInt();
								if (scanner2.hasNext()) {
									String rotManualInputString = scanner2.next();
									dictionary rotxDictionary = new dictionary("dict_en.txt");
									RotxCipher rotxCipher = new RotxCipher(rotxDictionary);
									System.out.println(rotxCipher.decodeManual(rotManualInputString, stepSize));
								} else {
									System.out.println("no encrypted input given");
								}
							}else {
								System.out.println("no stepsize given");
							}
							break;
						case "mod":							
							if(scanner2.hasNext()) {
								String keyName = scanner2.next();
								dictionary modDictionary = new dictionary("dict_en.txt");
								ModifiedRotxCipher modifiedRotxCipher = new ModifiedRotxCipher(modDictionary);
								if (modifiedRotxCipher.getElementNameStrings().contains(keyName)) {
									if (scanner2.hasNext()) {
										int stepSize = scanner2.nextInt();
										if (scanner2.hasNext()) {
											String decodeManualModInputString = scanner2.next();
											System.out.println(modifiedRotxCipher.decodeManual(decodeManualModInputString, keyName, stepSize));
										} else {
											System.out.println("no encrypted text given");
										}
									} else {
										System.out.println("no step size given");
									}
								} else {
									System.out.println("key name not valid yet");
								}
								
							}else {
								System.out.println("no key element give");
							}
							break;
						case "col":
							if (scanner2.hasNext()) {
								int columnSize = scanner2.nextInt();
								if (scanner2.hasNext()) {
									String decodeManualColString = scanner2.next();
									if (columnSize > 0) {
										dictionary colDictionary = new dictionary("dict_en.txt");
										ColumnarTranspositionCipher columnarTranspositionCipher = new ColumnarTranspositionCipher(colDictionary);
										System.out.println(columnarTranspositionCipher.decodeManual(decodeManualColString, columnSize));
									} else {
										System.out.println("column size has to be greater than 0");
									}
								} else {
									System.out.println("no input given");
								}
							} else {
								System.out.println("no column size given");
							}
							break;
						
						case "dia":
							if (scanner2.hasNext()) {
								int columnSize = scanner2.nextInt();
								if (columnSize < 0 || columnSize > 0) {
									dictionary traDictionary = new dictionary("dict_en.txt");
									DiagonalTranspositionCipher diagonalTranspositionCipher = new DiagonalTranspositionCipher(traDictionary);
									if (scanner2.hasNext()) {
										String decodeManualDiaString = scanner2.next();
										if (columnSize > 0) {
											System.out.println(diagonalTranspositionCipher.decodeManualDiagonalInput(decodeManualDiaString, columnSize));
										} else {
											System.out.println(diagonalTranspositionCipher.decodeManualReverseDiagonal(decodeManualDiaString, columnSize * (-1)));
										}
									} else {
										System.out.println("no encrypted text given");
									}
								} else {

								}
							} else {
								System.out.println("no column size given");
							}
							break;
						default:
							System.out.println("no command '" + mode +"'");
							break;
						}
						
					}else {
						System.out.println("no mode given");
					}
					break;
				default:
					System.out.println("no command " + modeDecodeString);
					break;
				}
			}else {
				System.out.println("no mode for decoding given");
			}
			break;
		case "-help":
			System.out.println("Cipher decode/encode");
			System.out.println("Commands:");
			System.out.println("en: encoding, en -cipher -key -step -input");
			System.out.println("de: decoding, de -mode -cipher -input");
			System.out.println("-mode : 'manual' or 'auto' for decoding");
			System.out.println("-key : key name for modified cipher");
			System.out.println("-step: step size or column size");
			System.out.println("-cipher: rot , mod , col , dia");
			System.out.println("-input: encrypted text or plain text without space");
			System.out.println("Example:");
			System.out.println("Encode ROTX cipher: en rot 3 lookatme");
			System.out.println("Encode MODROT cipher: en mod cryptanalysis 3 lookatme");
			System.out.println("Encode Columnar Transposition Cipher: en col 3 lookatme");
			System.out.println("Encode Diagonal Transposition Cipher: en dia 3 lookatme");
			System.out.println("Encode Reverse Diagonal Transposition Cipher: en dia -3 lookatme");
			System.out.println("Decode from text file: de auto");
			System.out.println("Decode ROTX cipher manual: de manual rot 3 lookatme");
			System.out.println("Decode ModRot cipher manual: de manual mod cryptanalysis 3 lookatme");
			System.out.println("Decode Columnar Transposition cipher manual: de manual col 3 lookatme");
			System.out.println("Decode Diagonal Transposition cipher manual: de manual dia 3 lookatme");
			System.out.println("Decode Reverse Diagonal Transposition cipher manual: de manual dia -3 lookatme");
			break;
		default:
			System.out.println("unknown command");
			break;
		}
		cipher.main(null);
	}
}
