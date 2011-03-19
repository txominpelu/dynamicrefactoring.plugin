package dynamicrefactoring.interfaz.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.texteditor.IUpdate;

public class TextViewerAction extends Action implements IUpdate {
	private int operationCode=-1;
	private ITextOperationTarget operationTarget;

	public TextViewerAction(ITextViewer textViewer, int operationCode) {
		this.operationCode = operationCode;
		operationTarget = textViewer.getTextOperationTarget();
		update();
	}
	
	@Override
	public void run() {
		if (operationCode != -1 && operationTarget != null) {
			operationTarget.doOperation(operationCode);
		}
	}

	@Override
	public void update() {
		boolean wasEnabled = isEnabled();
		boolean isEnabled = (operationTarget != null && operationTarget.canDoOperation(operationCode));
		setEnabled(isEnabled);
		if(wasEnabled != isEnabled) {
			firePropertyChange(ENABLED, wasEnabled ? Boolean.TRUE : Boolean.FALSE, isEnabled ? Boolean.TRUE : Boolean.FALSE);
		}
	}
}