import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class A {
	
	public void method() {
		try {
			n();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void n() throws FileNotFoundException, IOException {
		// begin
		FileInputStream fileIn = new FileInputStream(new File("./in"));
		FileOutputStream fileOut = new FileOutputStream(new File("./out"));
		while (fileIn.available() > 0) {
			int b = fileIn.read();
			fileOut.write(b);
		}
		fileIn.close();
		fileOut.close();
		// end
	}
}