package test;

import java.util.ArrayList;
import java.util.List;

public class Lists<E> {

	public static <T> List<T> toList(T[] arr) {
		List<T> list = new ArrayList<T>();
		n(arr, list);

		return list;
	}

	static <T> void n(T[] arr, List<T> list) {

		for (T elt : arr) {
			list.add(elt);
		}
	}

}