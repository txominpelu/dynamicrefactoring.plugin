package test;

import java.nio.CharBuffer;
import java.io.Closeable;
import java.io.IOException;

public class Lists<E> {

	public static <S extends Readable & Closeable, T extends Appendable & Closeable> void copy(
			S src, T trg, int size) throws IOException {
		CharBuffer buf = CharBuffer.allocate(size);
		int i = src.read(buf);
		n(buf, i, src, trg);

		src.close();
		trg.close();
	}

	static <S extends Readable & Closeable, T extends Appendable & Closeable> void n(
			CharBuffer buf, int i, S src, T trg) throws IOException {

		while (i > 0) {
			buf.flip();
			trg.append(buf);
			buf.clear();
			i = src.read(buf);
		}
	}

}