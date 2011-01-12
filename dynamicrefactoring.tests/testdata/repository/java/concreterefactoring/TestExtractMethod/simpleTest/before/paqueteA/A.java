package paqueteA;

public class A{
	
	public void m(){
		int a = 0;
		a = 5;
		int b = 0;
		m2(a,m3(a));		
		System.out.println(b);

		a = 7;
		System.out.println(b);		
	}

	public void m2(int c, int d){
	}

	public int m3(int e){
		return e;
	}
	
}
