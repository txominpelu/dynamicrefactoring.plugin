import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class OldTest {

	@Before
	public void setUp() {
	}

	@Test
	public void testMetodo() {
		fail();
	}

	@Test
	public void testMetodo2() {
		assertTrue(true);
		assertEquals(1, 1);
	}

	@After
	public void tearDown() {
	}
}