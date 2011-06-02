package dynamicrefactoring.interfaz.wizard;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringImages;

/**
 * Utilidades para el asistente de creacion
 * de refactorizaciones.
 * 
 * @author imediava
 *
 */
public final class RefactoringWizardUtils {

	/**
	 * Constructor privado para evitar que la
	 * clase sea instanciada.
	 */
	private RefactoringWizardUtils(){	}
	

	/**
	 * Añade un gestor de sugerencias al campo de texto
	 * que ofrece como sugerencias las pasadas en el parametro
	 * proposals.
	 * 
	 * @param textField campo de texto
	 * @param proposals sugerencias a realizar
	 */
	public static void addContentProposalToTextField(Text textField, java.util.List<String> proposals) {

		SimpleContentProposalProvider proposalsProvider = new SimpleContentProposalProvider(
				proposals.toArray(new String[proposals.size()]));
		proposalsProvider.setFiltering(true);

		ContentProposalAdapter adapter;
		try {
			adapter = new ContentProposalAdapter(textField,
					new TextContentAdapter(), proposalsProvider,
					KeyStroke.getInstance("Ctrl+Space"), null);
			adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
			adapter.setLabelProvider(new LabelProvider(){
				
				@Override
				public Image getImage(Object element){
					return RefactoringImages.getEclipseClassIcon();
				}
				
				@Override
				public String getText(Object element) {
					return ((IContentProposal)element).getContent();
				}
				
			});
		} catch (org.eclipse.jface.bindings.keys.ParseException e2) {
			throw Throwables.propagate(e2);
		}
	}

}
