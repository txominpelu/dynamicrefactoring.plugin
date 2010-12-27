package paqueteA;

public class C extends B {

	public void met1(int j) {
		nuevoAtt = 3;
		int f = nuevoAtt;
	}

	public int met2() {
		met1(nuevoAtt);
		return nuevoAtt;
	}
}