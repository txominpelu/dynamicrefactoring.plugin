public class Clase {

	public int a;

	public Integer metodoA() {
		return 0;
	}

	public int metodoB() {
		return 1;
	}

	public void metodoC() {
		a = metodoA().intValue() - metodoB();
	}
}