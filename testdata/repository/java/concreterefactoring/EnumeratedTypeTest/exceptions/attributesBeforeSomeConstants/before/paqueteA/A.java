package paqueteA;

public class A {

	public static final A DO = new A(0);

	private int valor;
	
	public static final A RE = new A(1);
	public static final A MI = new A(2);
	
	private int valor;

	private A(int val) {
		valor = val;
	}
}
