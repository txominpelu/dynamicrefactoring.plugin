package paqueteA;

public enum A {

	DO(0, 2),
	RE(1, 1),
	MI(2, 0);

	private int valor;
	private int valor2;

	private A(int val, int val2) {
		valor = val;
		valor2 = val2;
	}
}