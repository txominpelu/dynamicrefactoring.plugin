package dynamicrefactoring.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.RefactoringMechanismInstance;
import dynamicrefactoring.interfaz.TreeEditor;

/**
 * Clase con metodos que facilitan la creacion de los
 * arboles SWT con la descripcion de las refactorizaciones.
 * 
 * @author imediava
 *
 */
public final class RefactoringTreeManager {
	
	/**
	 * Evita que se puedan crear instancias de esta
	 * clase de utilidad.
	 */
	private RefactoringTreeManager(){}
	
	/**
	 * Cambia el color del texto de todos los componentes del subárbol.
	 * @param tItem subárbol
	 * @param c color nuevo que se quiere dar al texto
	 */
	public static void setForegroundTreeItem(TreeItem tItem, Color c){
		TreeItem childItems[]=tItem.getItems();
		tItem.setForeground(c);
		for(TreeItem childItem: childItems){
			setForegroundTreeItem(childItem,c);
		}
	}
	
	/**
	 * Elima todos los componentes del árbol.
	 * @param tree árbol del cual eliminar todos sus componentes
	 */
	public static void cleanTree(Tree tree) {
		if (tree.getItemCount() > 0) {
			TreeItem[] items = tree.getItems();
			for (int i = items.length - 1; i >= 0; i--)
				items[i].dispose();
		}
	}
	
	/**
	 * Rellena el árbol con las refactorizaciones disponibles.
	 * @param refactorings refactorizaciones disponibles
	 * @param tree árbol sobre el que añadir subárboles
	 * @throws RefactoringException
	 */
	public static void fillTree(Set<DynamicRefactoringDefinition> refactorings, Tree tree) {

		int refactOrderInBranch = 0;

		cleanTree(tree);
		
		ArrayList<DynamicRefactoringDefinition> refactNames= new ArrayList<DynamicRefactoringDefinition>(refactorings);
		Collections.sort(refactNames);
		
		for (DynamicRefactoringDefinition definition : refactNames) {

			createRefactoringDefinitionTreeItemFromParentTree(
					refactOrderInBranch, definition,tree);
			refactOrderInBranch++;

		}
	}

	/**
	 * Crea una representacion de una refactorizacion en formato de arbol
	 * agregandola al arbol que se pasa.
	 * 
	 * @param refactOrderInBranch
	 * 		      posicion en la que se agregagra la rama
	 * @param definition
	 *            definicion de la refactorizacion
	 * @param tree
	 *            arbol al que se agregara la rama con la refactorizacion
	 * 
	 * @throws RefactoringException
	 */
	public static void createRefactoringDefinitionTreeItemFromParentTree(
			int refactOrderInBranch, DynamicRefactoringDefinition definition,
			Tree tree) {
		String icon=RefactoringImages.PLUGIN_REF_ICON_PATH;
		if(definition.isEditable())
			icon=RefactoringImages.REF_ICON_PATH;
		TreeItem refactoring = TreeEditor.createBranch(tree,
				refactOrderInBranch, definition.getName(),
				icon); 
		createRefactoringTree(refactoring, definition);
	}

	/**
	 * Crea una representacion de una refactorizacion en formato de arbol
	 * agregandola al arbol que se pasa.
	 * 
	 * @param refactOrderInBranch
	 *            posicion en la que se agregagra la rama
	 * @param definition
	 *            definicion de la refactorizacion
	 * @param itemParent
	 *            item al que se agregara la rama con la refactorizacion
	 * 
	 * @throws RefactoringException
	 */
	public static void createRefactoringDefinitionTreeItemFromParentTreeItem(
			int refactOrderInBranch, DynamicRefactoringDefinition definition,
			TreeItem itemParent) {
		String icon=RefactoringImages.PLUGIN_REF_ICON_PATH;
		if(definition.isEditable())
			icon=RefactoringImages.REF_ICON_PATH;
		TreeItem refactoring = TreeEditor.createBranch(itemParent,
				refactOrderInBranch, definition.getName(),
				icon); 
		createRefactoringTree(refactoring, definition);
	}

	private static void createRefactoringTree(TreeItem refactoring,
			DynamicRefactoringDefinition definition) {
		// Descripcion de la refactorizacion
		TreeItem description = new TreeItem(refactoring, SWT.NONE);
		description.setText(Messages.RefactoringTreeManager_Description + ":"
				+ definition.getDescription());

		// Motivacion de la refactorizacion
		TreeItem motivation = new TreeItem(refactoring, SWT.NONE);
		motivation.setText(Messages.RefactoringTreeManager_Motivation + ":"
				+ definition.getMotivation());

		// Crea precondiciones, acciones y postcondiciones en el arbol
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
				RefactoringMechanismInstance.getMechanismListClassNames(definition.getPreconditions()),
				RefactoringImages.CHECK_ICON_PATH,
				Messages.RefactoringTreeManager_Preconditions);

		// Acciones
		createElementItemWithChildren(refactoring, RefactoringMechanismInstance.getMechanismListClassNames(definition.getActions()),
				RefactoringImages.RUN_ICON_PATH,
				Messages.RefactoringTreeManager_Action);

		// Postcondiciones
		createElementItemWithChildren(refactoring,
				RefactoringMechanismInstance.getMechanismListClassNames(definition.getPostconditions()), 
				RefactoringImages.VALIDATE_ICON_PATH,
				Messages.RefactoringTreeManager_Postconditions);
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
			List<String> childrenNames, String iconPath, String itemText) {
		TreeItem postconditionsChild = new TreeItem(itemParent, SWT.NONE);
		postconditionsChild.setText(itemText);
		postconditionsChild.setImage(RefactoringImages.getImageForPath(iconPath)); 
		TreeEditor.fillInTreeBranch(childrenNames, postconditionsChild, iconPath); 
	}

}