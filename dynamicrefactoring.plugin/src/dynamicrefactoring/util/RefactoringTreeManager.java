package dynamicrefactoring.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.swtdesigner.ResourceManager;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.interfaz.TreeEditor;
import dynamicrefactoring.interfaz.view.Messages;

public class RefactoringTreeManager {
	
	/**
	 * Elima los componentes del árbol.
	 */
	public static void cleanTree(Tree tree) {
		if (tree.getItemCount() > 0) {
			TreeItem[] items = tree.getItems();
			for (int i = items.length - 1; i >= 0; i--)
				items[i].dispose();
		}
	}
	
	/**
	 * Rellena el árbol con las refactorizaciones disponibles para el elemento
	 * seleccionado.
	 * 
	 * @param refactorings
	 *            refactorizaciones disponibles.
	 * @throws RefactoringException 
	 */
	public static void fillTree(HashMap<String, String> refactorings, Tree tree) throws RefactoringException {
		
		int refactOrderInBranch = 0;
		String refactName = null;
		String refactDefinitionFile = null;
		
		cleanTree(tree);
		
		for (Map.Entry<String, String> nextRef : refactorings.entrySet()) {
			refactName = nextRef.getKey();
			refactDefinitionFile = nextRef.getValue();
			
			createRefactoringDefinitionTreeItemFromParentTree(
								refactOrderInBranch, refactName,
								refactDefinitionFile, tree);
			refactOrderInBranch++;

		}
	}
	
	/**
	 * Crea una representacion de una refactorizacion en formato de arbol
	 * agregandola al arbol que se pasa.
	 * 
	 * @param refactOrderInBranch
	 *            posicion en la que se agregagra la rama
	 * @param definition
	 *            definicion de la refactorizacion
	 * @param arbol
	 *            arbol al que se agregara la rama con la refactorizacion
	 * 
	 * @throws RefactoringException
	 *             si hay algun fallo leyendo el fichero con la refactorizacion
	 */
	public static void createRefactoringDefinitionTreeItemFromParentTree(
			int refactOrderInBranch, DynamicRefactoringDefinition definition,
			Tree arbol) {
		TreeItem refactoring = TreeEditor.createBranch(arbol,
				refactOrderInBranch, definition.getName(),
				"icons" + System.getProperty("file.separator") + "ref.png"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		createTree(refactoring, definition);
	}

	/**
	 * Crea una representacion de una refactorizacion en formato de arbol
	 * agregandola al arbol que se pasa.
	 * 
	 * @param refactOrderInBranch
	 *            posicion en la que se agregagra la rama
	 * @param refactName
	 *            nombre de la refactorizacion
	 * @param refactDefinitionFile
	 *            ruta al fichero xml con la definicion de la
	 *            refactorizacion
	 * @param arbol
	 *            arbol al que se agregara la rama con la refactorizacion
	 * 
	 * @throws RefactoringException
	 *             si hay algun fallo leyendo el fichero con la
	 *             refactorizacion
	 */
	public static void createRefactoringDefinitionTreeItemFromParentTree(
			int refactOrderInBranch, String refactName,
			String refactDefinitionFile, Tree arbol)
			throws RefactoringException {
		TreeItem refactoring = TreeEditor.createBranch(arbol,
				refactOrderInBranch, refactName,
				"icons" + System.getProperty("file.separator") + "ref.png"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		createRefactoringDefinitionItems(refactDefinitionFile, refactoring);
	}

	/**
	 * Crea una representacion de una refactorizacion en formato de arbol
	 * agregandola al arbol que se pasa.
	 * 
	 * @param refactOrderInBranch
	 *            posicion en la que se agregagra la rama
	 * @param refactName
	 *            nombre de la refactorizacion
	 * @param refactDefinitionFile
	 *            ruta al fichero xml con la definicion de la
	 *            refactorizacion
	 * @param item
	 *            item al que se agregara la rama con la refactorizacion
	 * 
	 * @throws RefactoringException
	 *             si hay algun fallo leyendo el fichero con la
	 *             refactorizacion
	 */
	public static void createRefactoringDefinitionTreeItemFromParentTree(
			int refactOrderInBranch, String refactName,
			String refactDefinitionFile, TreeItem item)
			throws RefactoringException {
		TreeItem refactoring = TreeEditor.createBranch(item,
				refactOrderInBranch, refactName,
				"icons" + System.getProperty("file.separator") + "ref.png"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		createRefactoringDefinitionItems(refactDefinitionFile, refactoring);
	}

	/**
	 * Crean de forma efectiva los items de la definicion.
	 * 
	 * @param refactDefinitionFile
	 * @param refactoring
	 * @throws RefactoringException
	 */
	private static void createRefactoringDefinitionItems(
			String refactDefinitionFile, TreeItem refactoring)
			throws RefactoringException {
		if (refactDefinitionFile != "") { //$NON-NLS-1$

			// Obtiene la descripcion de la refactorizacion
			DynamicRefactoringDefinition definition = DynamicRefactoringDefinition
					.getRefactoringDefinition(refactDefinitionFile);

			createTree(refactoring, definition);
		}
	}

	private static void createTree(TreeItem refactoring,
			DynamicRefactoringDefinition definition) {
		// Descripcion de la refactorizacion
		TreeItem description = new TreeItem(refactoring, SWT.NONE);
		description.setText(Messages.AvailableRefactoringView_Description + ":"
				+ definition.getDescription());

		// Motivacion de la refactorizacion
		TreeItem motivation = new TreeItem(refactoring, SWT.NONE);
		motivation.setText(Messages.AvailableRefactoringView_Motivation + ":"
				+ definition.getMotivation());

		// Crea precondiciones, acciones y postcondiciones en el
		// arbol
		createRefactoringMechanishmTree(refactoring, definition);
	}

	/**
	 * Crea las precondiciones, acciones y postcondiciones en el arbol.
	 * 
	 * @param refactoring
	 *            item raiz de la refactorizacion
	 * @param definition
	 *            definicion de la refactorizacion
	 */
	private static void createRefactoringMechanishmTree(
			TreeItem refactoring, DynamicRefactoringDefinition definition) {
		// Precondiciones
		createElementItemWithChildren(refactoring,
				definition.getPreconditions(),
				"icons" + System.getProperty("file.separator")
						+ "check.gif",
				Messages.AvailableRefactoringView_Preconditions);

		// Acciones
		createElementItemWithChildren(refactoring, definition.getActions(),
				"icons" //$NON-NLS-1$
						+ System.getProperty("file.separator") + "run.gif",
				Messages.AvailableRefactoringView_Action);

		// Postcondiciones
		createElementItemWithChildren(refactoring,
				definition.getPostconditions(), "icons" //$NON-NLS-1$
						+ System.getProperty("file.separator")
						+ "validate.gif",
				Messages.AvailableRefactoringView_Postconditions);
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
	private static void createElementItemWithChildren(TreeItem itemParent,
			ArrayList<String> childrenNames, String iconPath, String itemText) {
		TreeItem postconditionsChild = new TreeItem(itemParent, SWT.NONE);
		postconditionsChild.setText(itemText);
		postconditionsChild.setImage(ResourceManager.getPluginImage(
				RefactoringPlugin.getDefault(), iconPath)); //$NON-NLS-1$ //$NON-NLS-2$
		TreeEditor.fillInTreeBranch(childrenNames, postconditionsChild,
				iconPath); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}