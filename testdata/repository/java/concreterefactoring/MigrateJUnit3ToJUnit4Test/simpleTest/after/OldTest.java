import junit.textui.TestRunner;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class OldTest {
	protected int fValue1;
	protected int fValue2;

	@Before
	protected void setUp() {
		fValue1 = 2;
		fValue2 = 3;
	}

	public static Test suite() {
		return new TestSuite(OldTest.class);
	}

	@Test
	public void testAdd() {
		double result = fValue1 + fValue2;
		assertTrue(result == 6);
	}

	@Test
	public void testDivideByZero() {
		int zero = 0;
		int result = 8 / zero;
	}

	@Test
	public void testEquals() {
		assertEquals(12, 12);
		assertEquals(12L, 12L);
		assertEquals(new Integer(12), new Integer(12));

		assertEquals("Size", 12, 13);
		assertEquals("Capacity", 12.0, 11.99, 0.0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}