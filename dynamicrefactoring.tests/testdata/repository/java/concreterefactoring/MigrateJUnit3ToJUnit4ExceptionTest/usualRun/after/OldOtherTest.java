import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class OldOtherTest {

	@Test(expected = IOException.class)
	public void testException() throws Exception {

		int i = 0;
		assertEquals(0, i);

	}
}