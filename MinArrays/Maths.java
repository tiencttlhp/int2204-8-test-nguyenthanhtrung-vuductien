package MinArrays;

public class Maths {
	public static int MinArray(int []a) {
		int MIN = a[0], n= a.length;
		for (int i=1; i<n; i++)
			if (MIN >  a[i]) MIN = a[i];
		return MIN;
	}
}
