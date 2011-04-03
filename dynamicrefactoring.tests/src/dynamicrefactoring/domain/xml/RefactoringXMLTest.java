package dynamicrefactoring.domain.xml;

import java.util.ArrayList;
import java.util.List;

public abstract class RefactoringXMLTest {
	
	public static final String CLASS = "Class";
	public static final String NEW_NAME = "New_name";
	
	public List<String> getPostConditionsParameters() {
		List<String> postConditionParameters = new ArrayList<String>();
		postConditionParameters.add("Old_name");
		return postConditionParameters;
	}

	public List<String> getActionsParameters() {
		List<String> actionParameters = new ArrayList<String>();
		actionParameters.add(CLASS);
		actionParameters.add(NEW_NAME);
		return actionParameters;
	}

	public List<String> getPreconditionsParameters() {
		List<String> preconditionParameters = new ArrayList<String>();
		preconditionParameters.add(NEW_NAME);
		return preconditionParameters;
	}

}
