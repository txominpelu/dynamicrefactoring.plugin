package dynamicrefactoring.domain.metadata.condition;

import java.util.Set;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;

public class ElementAdapter implements Element {
	
	@Override
	public String getName() {
		return "";
	}
	
	@Override
	public boolean belongsTo(Category category) {
		return false;
	}
	
	@Override
	public Set<Category> getCategories() {
		return null;
	}

	@Override
	public boolean belongsTo(String keyWord) {
		return false;
	}

	@Override
	public boolean containsText(String text) {
		return false;
	}

	@Override
	public boolean containsInputType(String inputType) {
		return false;
	}

	@Override
	public boolean containsRootInputType(String rootInputType) {
		return false;
	}

	@Override
	public boolean containsPrecondition(String precondition) {
		return false;
	}

	@Override
	public boolean containsAction(String action) {
		return false;
	}

	@Override
	public boolean containsPostcondition(String postcondition) {
		return false;
	}

}
