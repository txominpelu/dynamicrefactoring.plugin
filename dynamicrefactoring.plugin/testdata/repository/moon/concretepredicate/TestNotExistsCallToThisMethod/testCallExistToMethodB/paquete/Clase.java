package paquete;

public class Clase {

	public Clase atributo;

	public int metodoA(Integer a) {
		return a.intValue();
	}

	public void metodoB(Clase b) {
		atributo = b;
	}

	public int metodoC(Integer c) {
		return 0 + c;
	}

	public int metodoD(Integer d) {
		metodoA(d);
		return 0;
	}
}