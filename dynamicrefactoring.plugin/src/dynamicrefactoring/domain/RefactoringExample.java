package dynamicrefactoring.domain;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class RefactoringExample {

	private String before;
	private String after;

	/**
	 * Crea unos ficheros de ejemplo de una refactorizacion.
	 * 
	 * @param before ruta absoluta al fichero de ejemplo antes
	 * @param after ruta absoluta al fichero de ejemplo despues
	 */
	public RefactoringExample(String before, String after) {
		this.before = before;
		this.after = after;
		Preconditions.checkNotNull(before);
		Preconditions.checkNotNull(after);
	}
	

	public String getBefore() {
		return before;
	}

	public String getAfter() {
		return after;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RefactoringExample) {
			return getBefore().equals(((RefactoringExample) o).getBefore())
					&& getAfter().equals(((RefactoringExample) o).getAfter());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getBefore(), getAfter());
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("before", getBefore())
				.add("after", getAfter()).toString();
	}


}
