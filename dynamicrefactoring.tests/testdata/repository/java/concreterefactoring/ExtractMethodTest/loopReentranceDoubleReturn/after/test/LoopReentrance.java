package test;

public class LoopReentrance {

	int attribute = 0;

	void method() {
		int i = 0;
		while (attribute < 10) {
			i = n(i);

		}
	}

	void method2() {
		int i = 0;
		while (attribute < 10) {
			int acc = 0;
			attribute++;
			int j = 0;
			while (j < 10) {
				acc += j;
				j++;
			}
			System.out.println("i, attribute == " + i++ + ", " + attribute
					+ " acc =" + j);
		}
	}

	int n(int i) {

		attribute++;
		i = i + 1;
		System.out.println("i = " + i + ", " + "" + "attribute = " + attribute);
		return i;
	}
}