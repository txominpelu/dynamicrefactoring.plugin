/*<Dynamic Refactoring Plugin For Eclipse 2.0 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings>

Copyright (C) 2009  Laura Fuente De La Fuente

This file is part of Foobar

Foobar is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.util.selection;

import dynamicrefactoring.util.processor.JavaLocalVariableProcessor;

import org.eclipse.core.resources.IProject;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import org.eclipse.jdt.ui.JavaUI;

import org.eclipse.jface.text.TextSelection;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Proporciona funciones capaces de determinar el tipo de elemento que se 
 * encuentra seleccionado en una ventana sobre un editor de texto, así como
 * otra información adicional relativa al mismo.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TextSelectionInfo extends SelectionInfo {
	
	/**
	 * Selección de texto cuya información se desea consultar.
	 */
	private TextSelection textSelection;
	
	/**
	 * Constructor.
	 * 
	 * @param selection la selección sobre la que se desea obtener información.
	 * @param window la ventana sobre la que se ha efectuado la selección.
	 * 
	 * @throws Exception si #selection no es una selección sobre un árbol.
	 */
	public TextSelectionInfo (TextSelection selection, 
		IWorkbenchWindow window) throws Exception {
		
		super(selection, window);
		
		if (! (selection instanceof TextSelection))
			throw new Exception(
				Messages.TextSelectionInfo_InvalidSelection
				+ Messages.TextSelectionInfo_TextExpected + ".\n"); //$NON-NLS-1$
		
		textSelection = selection;
	}	
	
	/**
	 * Determina el tipo de elemento seleccionado en una ventana.
	 * Solo tiene en consideración los tipos indicados por #typeName, #methodName 
	 * #formalArgumentName, #fieldName y #parameterName.
	 * 
	 * @return el nombre completamente cualificado del tipo de elemento 
	 * seleccionado, o <code>null</code> si no es ninguno de los admitidos. 
	 */
	@Override
	public String getSelectionType(){
		
		if (this.selectionName == null){
			IJavaElement selectedElement = this.getSelectedJavaElement();
		
			if (selectedElement != null){
				if (selectedElement.getElementType() != IJavaElement.LOCAL_VARIABLE)			
					this.selectionName = 
						decodeElementType(selectedElement.getElementType());
				// Las variables locales necesitan un tratamiento especial.
				else 
					this.selectionName =
						decodeElementType(new JavaLocalVariableProcessor(
							(ILocalVariable)selectedElement).discernLocalVariable());
			}
		}
		if(selectionName!=null)
			return selectionName;
		return "";
	}
	
	/**
	 * @see SelectionInfo#isClassSelection()
	 */
	@Override
	public boolean isClassSelection(){
		// Se debe haber seleccionado un fragmento que Eclipse considere parte
		// de una selección de clase.
		if (getSelectionType().equals(typeName)){
			// Eclipse considera también las cláusulas de importación como 
			// selección de clase. Hay que filtrar esos casos.
			IJavaElement selectedElement = getSelectedJavaElement();
			if(selectedElement != null){
				String selectedElementName = selectedElement.getElementName();
				ICompilationUnit unit = getCompilationUnit();
				if(unit != null && unit.findPrimaryType() != null){
					String typeName = unit.findPrimaryType().getElementName();
					return typeName.equals(selectedElementName);
				}
			}		
		}
		return false;
	}
	
	/**
	 * @see SelectionInfo#isMethodSelection()
	 */
	@Override
	public boolean isMethodSelection(){
		// Se debe haber seleccionado un fragmento que Eclipse considere parte
		// de una selección de método.
		if (getSelectionType().equals(methodName)){
			// Eclipse considera también las llamadas a métodos como selección
			// de método. Hay que filtrar esos casos.
			try {
				IType type = getCompilationUnit().findPrimaryType();
				IMethod[] methods = type.getMethods();
				
				for (int i = 0; i < methods.length; i++)
					if(methods[i].getElementName().equals(
						getSelectedJavaElement().getElementName()))
						return true;
			}
			catch (Exception e){
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Obtiene el proyecto al que pertenece el elemento seleccionado en una 
	 * ventana.
	 * 
	 * @return el proyecto al que pertenece el elemento seleccioando.
	 */
	@Override
	public IProject getProjectForSelection(){
		
		ICompilationUnit clase = getCompilationUnit();
		
		if (clase != null)
			return getJavaProjectForName(clase.getJavaProject().getElementName());
				
		return null;
	}
	
	/**
	 * Obtiene el elemento Java representado por una selección textual.
	 * 
	 * @return el elemento Java representado por una selección textual.
	 */
	public IJavaElement getSelectedJavaElement(){
		
		ICompilationUnit clase = getCompilationUnit();
		
		if(clase != null){
		
			int offset = textSelection.getOffset();
			int length = textSelection.getLength();
			
			try {
				IJavaElement[] elements = clase.codeSelect(offset, length);
				return elements[0];
			}
			catch (Exception e){
				return null;
			}
		}
		return null;
	}
		
	/**
	 * Obtiene la unidad de compilación sobre la que se está trabajando.
	 * 
	 * @return la unidad de compilación sobre la que se está trabajando.
	 */
	public ICompilationUnit getCompilationUnit(){
		
		IEditorInput input = 
			window.getPages()[0].getActiveEditor().getEditorInput(); 
		
		IJavaElement element = JavaUI.getEditorInputJavaElement(input); 
		if (element.getElementType() == IJavaElement.COMPILATION_UNIT)
			return (ICompilationUnit) element;
		
		return null;
		
	}
	
	/**
	 * Obtiene el tipo primario sobre cuya representación textual se está
	 * trabajando (es decir, la clase a la que pertenece la selección, sin
	 * tener en cuenta clases internas).
	 * 
	 * @return el tipo primario sobre cuya representación textual se trabaja.
	 */
	public IType getPrimaryType(){
		
		ICompilationUnit clase = getCompilationUnit();
		
		if (clase != null)			
			return clase.findPrimaryType();
		
		return null;
	}

	/**
	 * @see SelectionInfo#getJavaProjectForSelection()
	 */
	@Override
	public IJavaProject getJavaProjectForSelection(){
		if(getSelectedJavaElement()==null){
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IEditorInput input = 
				window.getPages()[0].getActiveEditor().getEditorInput(); 
			return JavaUI.getEditorInputJavaElement(input).getJavaProject();
		}
		return getSelectedJavaElement().getJavaProject();
	}
}