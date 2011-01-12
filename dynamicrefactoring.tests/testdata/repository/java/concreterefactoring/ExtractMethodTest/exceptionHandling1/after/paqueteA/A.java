package paqueteA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class A {

	public static void method() {
		try {
			// begin
			n();

			// end
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error in file");
			e.printStackTrace();
		}
	}

	static void n() throws FileNotFoundException, IOException {

		FileInputStream fileIn = new FileInputStream(new File("./in"));
		FileOutputStream fileOut = new FileOutputStream(new File("./out"));
		while (fileIn.available() > 0) {
			int b = fileIn.read();
			fileOut.write(b);
		}
		fileIn.close();
		fileOut.close();
	}

}