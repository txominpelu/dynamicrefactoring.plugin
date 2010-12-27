import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

public class OldTest extends TestCase {
	protected int fValue1;
	protected int fValue2;

	protected void setUp() {
		fValue1 = 2;
		fValue2 = 3;
	}

	public static Test suite() {
		return new TestSuite(OldTest.class);
	}

	public void testAdd() {
		double result = fValue1 + fValue2;
		assertTrue(result == 6);
	}

	public void testDivideByZero() {
		int zero = 0;
		int result = 8 / zero;
	}

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