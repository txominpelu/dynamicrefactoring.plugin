package test;

import java.util.List;

public class Lists<E> {

	public static <T> void copy(List<? super T> dst, List<? extends T> src) {
		n(dst, src);

	}

	static <T> void n(List<? super T> dst, List<? extends T> src) {
		for (int i = 0; i < src.size(); i++) {
			dst.set(i, src.get(i));
		}
	}

}