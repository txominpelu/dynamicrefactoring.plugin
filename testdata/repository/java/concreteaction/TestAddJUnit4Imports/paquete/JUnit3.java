package paquete;

import junit.framework.TestCase;

public class JUnit3 extends TestCase {

	public void setUp() {
	}

	public void testMetodo() {
		fail();
	}

	public void testMetodo2() {
		assertTrue(true);
		assertEquals(1, 1);
	}

	public void tearDown() {
	}
}