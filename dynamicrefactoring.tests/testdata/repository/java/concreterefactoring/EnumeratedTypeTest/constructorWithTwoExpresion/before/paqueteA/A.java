package paqueteA;

public class A {

	public static final A DO = new A(0,2);
	public static final A RE = new A(1,1);
	public static final A MI = new A(2,0);
	
	private int valor;
	private int valor2;

	private A(int val, int val2) {
		valor = val;
		valor2 = val2;
	}
}
