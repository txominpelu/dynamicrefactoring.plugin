package paqueteA;

public class A {
	
	public void meth1(){
	}
	
	public static final A DO = new A(0);
	public static final A RE = new A(1);
	public static final A MI = new A(2);

	private int valor;
	
	public A(int val) {
		valor = val;
	}
}
