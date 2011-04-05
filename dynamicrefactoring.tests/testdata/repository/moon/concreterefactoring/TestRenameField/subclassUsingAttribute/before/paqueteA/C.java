package paqueteA;

public class C extends B {

	public void met1(int j) {
		a = 3;
		int f = a;
	}

	public int met2() {
		met1(a);
		return a;
	}
}