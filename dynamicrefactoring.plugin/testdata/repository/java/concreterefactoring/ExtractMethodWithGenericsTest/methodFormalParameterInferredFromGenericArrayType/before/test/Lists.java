package test;

import java.util.Arrays;

public class Lists<E> {
	
	private int size;
	
	private E[] elementData;

	public <T> T[] toArray(T[] a) {
		if (a.length < size)
			return (T[]) Arrays.copyOf(elementData, size, a.getClass());
		System.arraycopy(elementData, 0, a, 0, size);
		if (a.length > size)
			a[size] = null;
		return a;
	}
}