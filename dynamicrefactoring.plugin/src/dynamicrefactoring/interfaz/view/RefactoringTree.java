package dynamicrefactoring.interfaz.view;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


import dynamicrefactoring.domain.RefactoringException;

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
	 * Rellena el árbol con las refactorizaciones disponibles para el elemento
	 * seleccionado.
	 * 
	 * @param refactorings
	 *            refactorizaciones disponibles.
	 */
	protected void fillTree(HashMap<String, String> refactorings) {
		cleanView();// Limpiamos el contenido actual de al vista
		int refactOrderInBranch = 0;
		for (Map.Entry<String, String> nextRef : refactorings.entrySet()) {
			String refactName = nextRef.getKey();
			String refactDefinitionFile = nextRef.getValue();
			try {
				RefactoringTreeUtils
						.createRefactoringDefinitionTreeItemFromParentTree(
								refactOrderInBranch, refactName,
								refactDefinitionFile, tree);
			} catch (RefactoringException e) {
				logger.error(Messages.AvailableRefactoringView_NotRepresented
						+ ".\n" + e.getMessage()); //$NON-NLS-1$
			}
			refactOrderInBranch++;

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
