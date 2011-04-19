package dynamicrefactoring.interfaz.view.classifeditor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "dynamicrefactoring.interfaz.view.classifeditor.messages"; //$NON-NLS-1$
	public static String ClassificationDataSection_CannotBeUniCategory;
	public static String ClassificationDataSection_CannotBeUniCategoryErrorDescription;
	public static String ClassificationDataSection_Description;
	public static String ClassificationDataSection_EnterNewDescription;
	public static String ClassificationDataSection_Modify;
	public static String ClassificationDataSection_ModifyDescription;
	public static String ClassificationDataSection_Multi;
	public static String ClassificationDataSection_Name;
	public static String ClassificationDataSection_SelectedClassification;
	public static String ClassificationDataSection_SelectedClassificationData;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
