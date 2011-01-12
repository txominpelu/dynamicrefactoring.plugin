import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class OldOtherTest {

	@Test
	public void testException() {
		try {
			int i = 0;
			assertEquals(0, i);
		}

		catch (IOException ex) {
		}

		finally {
		}
	}
}