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

package dynamicrefactoring.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.interfaz.SelectRefactoringWindow;

/**
 * Permite obtener el conjunto de refactorizaciones din�micas disponibles y
 * aplicables sobre un �nico �mbito (de clase, de m�todo, etc.).
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class ScopeLimitedLister {
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(ScopeLimitedLister.class);

	/**
	 * �mbito de refactorizaciones cuya entrada principal es un atributo.
	 */
	private final static String ATTRIBUTE_SCOPE = "moon.core.classdef.AttDec"; //$NON-NLS-1$

	/**
	 * �mbito de refactorizaciones cuya entrada principal es una clase.
	 */
	private final static String CLASS_SCOPE = "moon.core.classdef.ClassDef"; //$NON-NLS-1$

	/**
	 * �mbito de refactorizaciones cuya entrada principal es un argumento
	 * formal.
	 */
	private final static String FORMAL_ARG_SCOPE = "moon.core.classdef.FormalArgument"; //$NON-NLS-1$

	/**
	 * �mbito de refactorizaciones cuya entrada principal es un par�metro
	 * formal.
	 */
	private final static String FORMAL_PAR_SCOPE = "moon.core.genericity.FormalPar"; //$NON-NLS-1$

	/**
	 * �mbito de refactorizaciones cuya entrada principal es un m�todo.
	 */
	private final static String METHOD_SCOPE = "moon.core.classdef.MethDec"; //$NON-NLS-1$

	/**
	 * �mbito de refactorizaciones cuya entrada principal es un par�metro formal
	 * acotado.
	 */
	private final static String BOUNDED_PAR_SCOPE = "moon.core.genericity.BoundS"; //$NON-NLS-1$

	/**
	 * �mbito de refactorizaciones cuya entrada principal es un fragmento de
	 * c�digo.
	 */
	private final static String CODE_FRAGMENT_SCOPE = "moon.core.instruction.CodeFragment"; //$NON-NLS-1$

	/**
	 * Filtra la lista de refactorizaciones din�micas disponibles a aqu�llas que
	 * pertenezcan a un determinado �mbito (de atributo, de clase, de argumento
	 * formal, de par�metro formal o de m�todo).
	 * 
	 * @param scope
	 *            c�digo del �mbito para el que deben obtenerse las
	 *            refactorizaciones disponibles, seg�n se codifican en
	 *            {@link SelectRefactoringWindow}.
	 * 
	 * @return una tabla <i>hash</i> con la lista de refactorizaciones
	 *         aplicables al �mbito indicado. En la tabla se sigue el convenio
	 *         de utilizar como clave el nombre de la refactorizaci�n y como
	 *         valor la ruta del fichero que la contiene.
	 */
	public static HashMap<String, String> getAvailableRefactorings(Scope scope){
		
		// Refactorizaciones seleccionadas por ser del �mbito adecuado.
		HashMap<String, String> selected = new HashMap<String, String>();
		
		DynamicRefactoringLister listing = DynamicRefactoringLister.getInstance();
		
		try {
			// Se obtiene la lista de todas las refactorizaciones disponibles.
			HashMap<String, String> allRefactorings = 
				listing.getDynamicRefactoringNameList(
					RefactoringPlugin.getDynamicRefactoringsDir(), true, null);
			
			for (Map.Entry<String, String> nextRef : allRefactorings.entrySet()){
				
				try {
					// Se obtiene la definici�n de la siguiente refactorizaci�n.
					DynamicRefactoringDefinition definition = 
						DynamicRefactoringDefinition.getRefactoringDefinition(
							nextRef.getValue());

					for(String[] nextInput : definition.getInputs()){
						// Para su entrada de tipo "ra�z".
						if (nextInput[4] != null && nextInput[4].equals("true")){ //$NON-NLS-1$
							// Si el tipo de la entrada es el que corresponde al
							// �mbito seleccionado.
							try{
								if (Class.forName(convertScope(scope)).isAssignableFrom(Class.forName(nextInput[0]))){
									
									selected.put(definition.getName(), nextRef.getValue());
									break;
								}
							}catch(ClassNotFoundException exception){
								logger.error(Messages.ScopeLimitedLister_ErrorLoading
										+ ".\n" + exception.getMessage());
						    }
						}
					}
				}
				catch(RefactoringException e){
					logger.error(
						Messages.ScopeLimitedLister_NotListed
						+ ".\n" + e.getMessage()); //$NON-NLS-1$
				}				
			}
			
			return selected;
		}		
		catch(IOException e){
			logger.error(
				Messages.ScopeLimitedLister_NotListed
				+ ".\n" + e.getMessage()); //$NON-NLS-1$
			return null;
		}	
	}

	/**
	 * Devuelve el �mbito al que pertenece una refactorizaci�n.
	 * 
	 * @param definition
	 *            definici�n de la refactorizaci�n.
	 * @return �mbito de la refactorizaci�n.
	 */
	public Scope getRefactoringScope(DynamicRefactoringDefinition definition){
		for(String[] nextInput : definition.getInputs()){
			// Para su entrada de tipo "ra�z".
			if (nextInput[4] != null && nextInput[4].equals("true")){ //$NON-NLS-1$
				// Si el tipo de la entrada es el que corresponde al
				// �mbito seleccionado.
				return getScope(nextInput[0]);
			}
		}
		//FIXME: ELiminar null
		return null;
	}

	/**
	 * Convierte los c�digos de �mbito de refactorizaci�n utilizados en la capa
	 * de interfaz por los nombres completamente cualificados utilizados en la
	 * representaci�n XML de las refactorizaciones.
	 * 
	 * @param scope
	 *            c�digo del �mbito de la refactorizaci�n seg�n se especifican
	 *            en {@link SelectRefactoringWindow}.
	 * 
	 * @return el nombre completamente cualificado del tipo de objeto para el
	 *         cual se define el �mbito de refactorizaciones seg�n el valor de
	 *         #scope.
	 */
	private static String convertScope(Scope scope){
		//FIXME: Sustituir por nombre de enum SelectRefactoringWindow.SCOPE
		switch(scope){
		case ATTRIBUTE:
			return ATTRIBUTE_SCOPE;
		case CLASS:
			return CLASS_SCOPE;
		case FORMAL_ARG:
			return FORMAL_ARG_SCOPE;
		case FORMAL_PAR:
			return FORMAL_PAR_SCOPE;
		case METHOD:
			return METHOD_SCOPE;
		case BOUNDED_PAR:
			return BOUNDED_PAR_SCOPE;
		case CODE_FRAGMENT:
			return CODE_FRAGMENT_SCOPE;
		default:
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Convierte los nombres completamente cualificados utilizados en la
	 * representaci�n XML de las refactorizaciones por los c�digos de �mbito de
	 * refactorizaci�n utilizados en la capa de interfaz.
	 * 
	 * @param name
	 *            nombre completamente cualificado del tipo de objeto para el
	 *            cual se define el �mbito de refactorizaciones seg�n el valor
	 *            de #scope.
	 * 
	 * @return c�digo del �mbito de la refactorizaci�n seg�n se especifican en
	 *         {@link SelectRefactoringWindow}.
	 */
	private static Scope getScope(String name){
		try{
		if(Class.forName(ATTRIBUTE_SCOPE).isAssignableFrom(Class.forName(name)))
			return Scope.ATTRIBUTE;
		else 
			if(Class.forName(CLASS_SCOPE).isAssignableFrom(Class.forName(name)))
				return Scope.CLASS;
			else
				if(Class.forName(FORMAL_ARG_SCOPE).isAssignableFrom(Class.forName(name)))
					return Scope.FORMAL_ARG;
				else
					if(Class.forName(FORMAL_PAR_SCOPE).isAssignableFrom(Class.forName(name)))
						return Scope.FORMAL_PAR;
					else
						if(Class.forName(METHOD_SCOPE).isAssignableFrom(Class.forName(name)))
							return Scope.METHOD; 
						else
							if(Class.forName(BOUNDED_PAR_SCOPE).isAssignableFrom(Class.forName(name)))
								return Scope.BOUNDED_PAR;
							else
								if(Class.forName(CODE_FRAGMENT_SCOPE).isAssignableFrom(Class.forName(name)))
									return Scope.CODE_FRAGMENT;
		}catch(ClassNotFoundException exception){
			logger.error(Messages.ScopeLimitedLister_ErrorLoading
					+ ".\n" + exception.getMessage());
		}
		//FIXME: ELiminar null
		return null;
	}
}