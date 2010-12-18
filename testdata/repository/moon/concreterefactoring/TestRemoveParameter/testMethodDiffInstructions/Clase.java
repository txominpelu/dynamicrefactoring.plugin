public class Clase {
	
	int b = 0;

	public int metodoA(int p) {
		return 1;
	}

	public int metodoB() {
		int c = metodoA(0);
		b = c;
		return metodoA(0);
	}
}