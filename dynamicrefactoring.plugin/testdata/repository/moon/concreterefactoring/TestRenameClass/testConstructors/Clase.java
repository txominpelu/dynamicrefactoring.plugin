public class Clase {

	public int a;

	public Integer metodoA() {
		return 0;
	}

	public int metodoB() {
		return metodoA() * 2 / metodoA();
	}

	public void metodoC() {
		a = metodoA().intValue() - metodoB();
	}
	
	public Clase(){
	}
	
	public Clase(int a){
		this.a = a;
	}
}