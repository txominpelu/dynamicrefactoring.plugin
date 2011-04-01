package paqueteA;

public class A {

	public void m() {
		int a = 0;
		int b = 0;
		b = n(a, b);

		a = 7;
		System.out.println(b);
	}

	int n(int a, int b) {

		a = 5;
		System.out.println(b);
		return b;
	}

}