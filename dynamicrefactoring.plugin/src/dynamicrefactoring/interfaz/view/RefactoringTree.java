package dynamicrefactoring.interfaz.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.swtdesigner.ResourceManager;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.interfaz.TreeEditor;

/**
 * Vista que representa una refactorizacion en un arbol.
 * 
 * @author imediava
 * 
 */
class RefactoringTree {

	/**
	 * Arbol en el que se muestran las refactorizaciones.
	 */
	private Tree tree;
	/**
	 * Logger al que se reportaran los logs.
	 */
	private Logger logger;


	/**
	 * Crear una vista que representa una refactorizacion en un arbol.
	 * 
	 * @param parent
	 *            elemento en el que se agregara el arbol
	 * @param listener
	 *            listener al que se reportaran los eventos de raton del arbol
	 * @param logger
	 *            logger
	 */
	protected RefactoringTree(Composite parent, MouseListener listener,
			Logger logger) {
		this.logger = logger;
		tree = new Tree(parent, SWT.NULL);
		tree.addMouseListener(listener);
	}

	/**
	 * Crea un treeItem en el arbol con los hijos pasados.
	 * 
	 * @param itemParent
	 *            padre del item que se va a crear
	 * @param childrenNames
	 *            nombres de los hijos
	 * @param iconPath
	 *            ruta del icono que tendran tanto el padre como los hijos
	 * @param itemText
	 *            texto que tendra el item padre
	 */
	static void createElementItemWithChildren(TreeItem itemParent,
			ArrayList<String> childrenNames, String iconPath, String itemText) {
		TreeItem postconditionsChild = new TreeItem(itemParent, SWT.NONE);
		postconditionsChild.setText(itemText);
		postconditionsChild.setImage(ResourceManager.getPluginImage(
				RefactoringPlugin.getDefault(), iconPath)); //$NON-NLS-1$ //$NON-NLS-2$
		TreeEditor.fillInTreeBranch(childrenNames, postconditionsChild,
				iconPath); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Crea las precondiciones, acciones y postcondiciones en el arbol.
	 * 
	 * @param refactoring
	 *            item raiz de la refactorizacion
	 * @param definition
	 *            definicion de la refactorizacion
	 */
	static void createRefactoringMechanishmTree(TreeItem refactoring,
			DynamicRefactoringDefinition definition) {
		// Precondiciones
		createElementItemWithChildren(
				refactoring, definition.getPreconditions(), "icons"
				+ System.getProperty("file.separator")
						+ "check.gif",
				Messages.AvailableRefactoringView_Preconditions);
	
		// Acciones
		createElementItemWithChildren(
				refactoring, definition.getActions(), "icons" //$NON-NLS-1$
						+ System.getProperty("file.separator")
						+ "run.gif",
				Messages.AvailableRefactoringView_Action);
	
		// Postcondiciones
		createElementItemWithChildren(
				refactoring, definition.getPostconditions(),
				"icons" //$NON-NLS-1$
						+ System.getProperty("file.separator")
						+ "validate.gif",
				Messages.AvailableRefactoringView_Postconditions);
	}

	/**
	 * Rellena el árbol con las refactorizaciones disponibles para el elemento
	 * seleccionado.
	 * 
	 * @param refactorings
	 *            refactorizaciones disponibles.
	 */
	public void fillTree(HashMap<String, String> refactorings) {
		cleanView();// Limpiamos el contenido actual de al vista
		int i = 0;
		for (Map.Entry<String, String> nextRef : refactorings.entrySet()) {
			TreeItem refactoring = TreeEditor.createBranch(tree, i,
					nextRef.getKey(),
					"icons" + System.getProperty("file.separator") + "ref.png"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			i++;
			try {
				if (nextRef.getValue() != "") { //$NON-NLS-1$
	
					// Obtiene la descripcion de la refactorizacion
					DynamicRefactoringDefinition definition = DynamicRefactoringDefinition
							.getRefactoringDefinition(nextRef.getValue());
	
					// Descripcion de la refactorizacion
					TreeItem description = new TreeItem(refactoring, SWT.NONE);
					description
							.setText(Messages.AvailableRefactoringView_Description
									+ ":" + definition.getDescription());
	
					// Motivacion de la refactorizacion
					TreeItem motivation = new TreeItem(refactoring, SWT.NONE);
					motivation
							.setText(Messages.AvailableRefactoringView_Motivation
									+ ":" + definition.getMotivation());
	
					// Crea precondiciones, acciones y postcondiciones en el
					// arbol
					createRefactoringMechanishmTree(refactoring, definition);
				}
			} catch (RefactoringException e) {
				logger.error(Messages.AvailableRefactoringView_NotRepresented
								+ ".\n" + e.getMessage()); //$NON-NLS-1$
			}
	
		}
	}

	/**
	 * Devuelve la vista a su estado inicial, vaciando la tabla y la tabla
	 * asociativa de refactorizaciones almacenadas.
	 */
	public void cleanView() {
		if (tree.getItemCount() > 0) {
			TreeItem[] items = tree.getItems();
			for (int i = items.length - 1; i >= 0; i--)
				items[i].dispose();
		}
	}

	/**
	 * Obtiene la lista de items actualmente seleccionados en el arbol.
	 * 
	 * @return elementos seleccionados en el arbol
	 */
	public TreeItem[] getSelection() {
		return tree.getSelection();
	}

}
