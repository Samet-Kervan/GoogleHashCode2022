import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HashCode {
	public HashCode(String fileName) {
		readFile(fileName);
	}
	private void readFile(String fileName) {
		File file = new File(fileName);
		try {
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()) {
				String read = sc.nextLine();
				String[] array = read.split(" ");
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("File does not exists. Try after fixing the problem");
			System.exit(0);
		}
	}
}
