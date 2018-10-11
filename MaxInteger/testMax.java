package MaxInteger;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class testMax {

	@Test
	void testMax1() {
		int a;
		a = mathss.max(6, 5);
		assertEquals(6, a);
	}
	
	@Test
	void testMax2() {
		int a;
		a = mathss.max(99999, 7825);
		assertEquals(99999, a);
	}
	
	@Test
	void testMax3() {
		int a;
		a = mathss.max(1024, 1025);
		assertEquals(1025, a);
		
	}
	@Test
	void testMax4() {
		int a;
		a = mathss.max(1999,2085);
		assertEquals(2085, a);
		
	}
	
	@Test
	void testMax5() {
		int a;
		a = mathss.max(79668, 123456);
		assertEquals(123456, a);
		
	}
}
