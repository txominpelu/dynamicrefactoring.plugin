package test;

import java.util.Collection;

public class Lists<E> {
	
	private int size;
	
	private E[] elementData;

	public boolean addAll(Collection<? extends E> c) {
		Object[] a = c.toArray();
		int numNew = a.length;
		ensureCapacity(size + numNew);
		System.arraycopy(a, 0, elementData, size, numNew);
		size += numNew;
		return numNew != 0;
	}

	private void ensureCapacity(int i) {

	}
}