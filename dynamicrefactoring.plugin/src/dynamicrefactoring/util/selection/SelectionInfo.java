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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchWindow;

import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.util.io.JavaFileManager;

/**
 * Proporciona funciones capaces de determinar el tipo de elemento que se
 * encuentra seleccionado en una ventana y otra información adicional relativa
 * al mismo.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public abstract class SelectionInfo {

	/**
	 * Constante que representa una declaración de argumento formal.
	 * 
	 * @see IJavaElement#LOCAL_VARIABLE
	 */
	public static final int FORMAL_ARGUMENT = -1;

	/**
	 * Constante que representa una declaración de variable local.
	 * 
	 * @see IJavaElement#LOCAL_VARIABLE
	 */
	public static final int LOCAL_VARIABLE = -2;

	/**
	 * Selección genérica cuya información se desea consultar.
	 */
	private ISelection selection;

	/**
	 * Ventana del espacio de trabajo sobre la que se ha hecho la selección.
	 */
	protected IWorkbenchWindow window;

	/**
	 * Nombre completamente cualificado del tipo de selección.
	 */
	protected String selectionName;
	
	/**
	 * Nombre completamente cualificado del elemento "Tipo" o "Clase".
	 */
	protected final static String TYPE_NAME = "org.eclipse.jdt.internal.core.SourceType"; //$NON-NLS-1$

	/**
	 * Nombre completamente cualificado del elemento "Método" o "Función".
	 */
	protected final static String METHOD_NAME = "org.eclipse.jdt.internal.core.SourceMethod"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del elemento "Atributo de instancia".
	 */
	protected final static String FIELD_NAME = "org.eclipse.jdt.internal.core.SourceField"; //$NON-NLS-1$

	/**
	 * Nombre completamente cualificado del elemento "Parámetro formal".
	 */
	protected final static String PARAMETER_NAME = "org.eclipse.jdt.internal.core.TypeParameter"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del elemento "Argumento formal".
	 */
	protected final static String FORMAL_ARGUMENT_NAME = "dynamicrefactoring.SourceFormalArgument"; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param selection
	 *            la selección sobre la que se desea obtener información.
	 * @param window
	 *            la ventana sobre la que se ha efectuado la selección.
	 */
	public SelectionInfo (ISelection selection, 
		IWorkbenchWindow window){
		
		this.selection = selection;
		this.selectionName = null;
		this.window = window;
	}

	/**
	 * Determina el tipo de elemento seleccionado en una ventana. Solo tiene en
	 * consideración los tipos indicados por {@link #TYPE_NAME},
	 * {@link #METHOD_NAME}, {@link #FIELD_NAME}, {@link #PARAMETER_NAME} y
	 * {@link #FORMAL_ARGUMENT_NAME}.
	 * 
	 * @return el nombre completamente cualificado del tipo de elemento
	 *         seleccionado, o <code>null</code> si no es ninguno de los
	 *         admitidos.
	 */
	public abstract String getSelectionType();

	/**
	 * Comprueba si el nombre completamente cualificado de tipo de selección se
	 * corresponde con alguno de los tipos de selección válidos como entrada
	 * para la herramienta de refactorización.
	 * 
	 * @return <code>true</code> si es uno de los tipos contemplados como
	 *         válidos; <code>false</code> en caso contrario.
	 */
	public boolean isValidSelectionType(){
		ArrayList<String> validTypes = new ArrayList<String>();
			
		validTypes.add(TYPE_NAME);
		validTypes.add(FIELD_NAME);
		validTypes.add(METHOD_NAME);
		validTypes.add(PARAMETER_NAME);
		validTypes.add(FORMAL_ARGUMENT_NAME);
		
		return validTypes.contains(getSelectionType()) 
			|| (isTextSelection() && ((TextSelection)selection).getLength()>0);
	}

	/**
	 * Comprueba si el nombre completamente cualificado del tipo de la selección
	 * corresponde a un atributo seleccionado.
	 * 
	 * @return <code>true</code> si se trata de un atributo seleccionado;
	 *         <code>false</code> en caso contrario.
	 */
	public boolean isFieldSelection(){
		return (getSelectionType().equals(FIELD_NAME));
	}

	/**
	 * Comprueba si un nombre completamente cualificado del tipo de la selección
	 * corresponde a un método seleccionado.
	 * 
	 * @return <code>true</code> si se trata de un método seleccionado;
	 *         <code>false</code> en caso contrario.
	 */
	public boolean isMethodSelection(){
		return (getSelectionType().equals(METHOD_NAME));
	}

	/**
	 * Comprueba si un nombre completamente cualificado del tipo de la selección
	 * corresponde a una clase seleccionada.
	 * 
	 * @return <code>true</code> si se trata de una clase seleccionada;
	 *         <code>false</code> en caso contrario.
	 */
	public boolean isClassSelection(){
		return (getSelectionType().equals(TYPE_NAME));
	}

	/**
	 * Comprueba si un nombre completamente cualificado del tipo de la selección
	 * corresponde a un argumento formal de un método seleccionado.
	 * 
	 * @return <code>true</code> si se trata de un argumento formal de un
	 *         método; <code>false</code> en caso contrario.
	 */
	public boolean isFormalArgumentSelection(){
		return (getSelectionType().equals(FORMAL_ARGUMENT_NAME));
	}

	/**
	 * Comprueba si un nombre completamente cualificado del tipo de la selección
	 * corresponde a un parámetro formal.
	 * 
	 * @return <code>true</code> si se trata de un parámetro formal;
	 *         <code>false</code> en caso contrario.
	 */
	public boolean isFormalParameterSelection(){
		return (getSelectionType().equals(PARAMETER_NAME));
	}

	/**
	 * Comprueba si la selección es parte de una representación en árbol.
	 * 
	 * @return <code>true</code> si se trata de una selección en un árbol;
	 *         <code>false</code> en caso contrario.
	 */
	public boolean isTreeSelection(){
		return (selection instanceof TreeSelection);
	}

	/**
	 * Comprueba si la selección es parte de una representación textual.
	 * 
	 * @return <code>true</code> si se trata de una selección de texto;
	 *         <code>false</code> en caso contrario.
	 */
	public boolean isTextSelection(){
		return (selection instanceof TextSelection);
	}
	
	/**
	 * Obtiene el nombre completamente cualificado de los tipos de elemento 
	 * descritos por la interfaz <code>IJavaElement</code>.
	 * 
	 * @param type el valor codificado por IJavaElement para el tipo de elemento Java.
	 * 
	 * @return el nombre completamente cualificado del tipo equivalente.
	 */
	protected String decodeElementType(int type){
		switch(type){
		case IJavaElement.TYPE:
			return TYPE_NAME;
		case IJavaElement.FIELD:
			return FIELD_NAME;
		case IJavaElement.METHOD:
			return METHOD_NAME;
		case IJavaElement.TYPE_PARAMETER:
			return PARAMETER_NAME;
		case FORMAL_ARGUMENT:
			return FORMAL_ARGUMENT_NAME;
		default:
			return "";		 //$NON-NLS-1$
		}
	}

	/**
	 * Obtiene el proyecto al que pertenece el elemento seleccionado.
	 * 
	 * @return el proyecto al que pertenece el elemento seleccionado.
	 */
	public abstract IProject getProjectForSelection();

	/**
	 * Obtiene la lista de directorios de fuentes del proyecto al que pertenece
	 * el elemento seleccionado.
	 * 
	 * Método plantilla (patrón de diseño Método Plantilla).
	 * 
	 * @return la lista de directorios de fuentes del proyecto al que pertenece
	 *         el elemento seleccionado.
	 */
	public List<String> getSourceDirsForSelection(){
		IJavaProject project = getJavaProjectForSelection();
		
		return JavaFileManager.getSourceDirsForProject(project);
	}
	
	/**
	 * Obtiene el proyecto del espacio de trabajo con el nombre especificado.
	 * 
	 * @param name el nombre del proyecto que se quiere recuperar.
	 * 
	 * @return el proyecto del espacio de trabajo con el nombre especificado por #name.
	 */
	protected IProject getJavaProjectForName(String name) {
		IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();

		IProject myProject = wsroot.getProject(name);
		return myProject;
	}

	/**
	 * Obtiene el proyecto al que pertenece la selección actual como un <code>
	 * IJavaElement</code>, de manera que se pueda manejar aprovechando las
	 * posibilidades de Eclipse para el manejo de elementos Java.
	 * 
	 * Operación primitiva (patrón de diseño Método Plantilla).
	 * 
	 * @return el proyecto al que pertenece la selección actual como un <code>
	 * IJavaElement</code>.
	 */
	public abstract IJavaProject getJavaProjectForSelection();

	/**
	 * Obtiene la selección asociada al procesador de información.
	 * 
	 * @return la selección asociada al procesador de información.
	 */
	public ISelection getSelection(){
		return selection;
	}

	/**
	 * Devuelve si existe un ambito de aplicacion para la seleccion.
	 * 
	 * @return si existe o no un
	 */
	public boolean existsScopeForSelection() {
		return getSelectionScope() != null;
	}

	/**
	 * Obtiene el ambito asociado a la seleccion.
	 * 
	 * @return ambito de la seleccion o null si la seleccion no pertenece a
	 *         ningun ambito (! existsScopeForSelection())
	 */
	public Scope getSelectionScope() {
		if (isClassSelection())
			return Scope.CLASS;
		else if (isMethodSelection())
			return Scope.METHOD;
		else if (isFieldSelection())
			return Scope.ATTRIBUTE;
		else if (isFormalArgumentSelection())
			return Scope.FORMAL_ARG;
		else if (isFormalParameterSelection())
			return Scope.FORMAL_PAR;
		else if (isTextSelection())
			return Scope.CODE_FRAGMENT;

		return null;
	}
}