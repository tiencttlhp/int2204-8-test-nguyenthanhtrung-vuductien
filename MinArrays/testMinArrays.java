package MinArrays;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class testMinArrays {

	@Test
	void test1() {
		int A[] = {5,4,3,2,1};
		int kq = Maths.MinArray(A);
		assertEquals(1, kq);
	}
	
	@Test
	void test2() {
		int A[] = {100,200,150,1975,99999,98,1789,53};
		int kq = Maths.MinArray(A);
		assertEquals(53, kq);
	}
	
	@Test
	void test3() {
		int A[] = {195,-9,-19,-39,-99,198,275};
		int kq = Maths.MinArray(A);
		assertEquals(-99, kq);
	}
	
	@Test
	void test4() {
		int A[] = {7890,12345,654,8754,1987,280899};
		int kq = Maths.MinArray(A);
		assertEquals(654, kq);
	}
	
	@Test
	void test5() {
		int A[] = {1896,8654,143,196,1};
		int kq = Maths.MinArray(A);
		assertEquals(1, kq);
	}

}
