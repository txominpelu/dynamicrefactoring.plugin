package dynamicrefactoring.interfaz.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Proporciona un organizador de pestañas, en el cual se muestra
 * la información relativa a la refactorización.
 * @author XPMUser
 */
public class RefactoringSummaryPanel {

	
	/**
	 * Organizador de pestañas.
	 */
	private TabFolder refTabFolder;
	
	/**
	 * Definición de la refactorización.
	 */
	private DynamicRefactoringDefinition refactoring;
	
	public RefactoringSummaryPanel(Composite parent){
		Button pru=new Button(parent, SWT.NONE);
		pru.setText("kk");
		FormData refFormData = new FormData();
		refFormData.top = new FormAttachment(0, 5);
		refFormData.left = new FormAttachment(0, 5);
		refFormData.right=new FormAttachment(100, -5);
		refFormData.bottom=new FormAttachment(100, -5);
		pru.setLayoutData(refFormData);
		
//		refTabFolder=new TabFolder(parent, SWT.BORDER);
//		for (int i=0; i<6; i++) {
//			TabItem item = new TabItem (refTabFolder, SWT.NONE);
//			item.setText ("TabItem " + i);
//			Button button = new Button (refTabFolder, SWT.PUSH);
//			button.setText ("Page " + i);
//			item.setControl (button);
//		}
		//refTabFolder.pack ();
	}

	/**
	 * Establece la refactorización a mostrar.
	 * @param ref definición de la refactorización a mostrar
	 */
	public void setRefactoringDefinition(DynamicRefactoringDefinition ref) {
		refactoring=ref;
	}
	
	public void showRefactoringSummary(){
		System.out.println("pinchado: " + refactoring.getName());
	}
	
}
