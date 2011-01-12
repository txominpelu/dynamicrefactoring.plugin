package test;

public class Lists<E> {

	private int modCount;

	private E[] elementData;

	private int size;

	public E remove(int index) {
		RangeCheck(index);
		modCount++;
		E oldValue = n(index);

		elementData[--size] = null;
		return oldValue;
	}

	private void RangeCheck(int i) {
	}

	E n(int index) {

		E oldValue = (E) elementData[index];
		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(elementData, index + 1, elementData, index,
					numMoved);
		return oldValue;
	}

}