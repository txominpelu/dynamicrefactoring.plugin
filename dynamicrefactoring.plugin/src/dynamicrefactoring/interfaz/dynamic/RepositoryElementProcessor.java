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

package dynamicrefactoring.interfaz.dynamic;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import dynamicrefactoring.util.RepositoryElementLister;

/**
 * Proporciona funciones de procesamiento de los elementos del repositorio 
 * que componen una refactorizaci�n din�mica.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RepositoryElementProcessor {
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(RepositoryElementProcessor.class);

	/**
	 * Determina si un nombre de acci�n corresponde con una acci�n del repositorio
	 * dependiente de Java.
	 * 
	 * @param actionName nombre de la acci�n cuya dependencia del lenguaje se 
	 * quiere determinar.
	 * 
	 * @return <code>true</code> si la acci�n es dependiente de Java; <code>false
	 * </code> en caso contrario.
	 */
	public static boolean isActionJavaDependent(String actionName){
		RepositoryElementLister lister = 
			RepositoryElementLister.getInstance();
		
		try {
			HashMap<String, String> javaActions = lister.getJavaActionList();
			return javaActions.containsKey(actionName);
		}
		catch (IOException exception){
			
			logger.error(
				Messages.RepositoryElementProcessor_ErrorAccessingRepository + 
				":\n\n" + exception.getMessage()); //$NON-NLS-1$
			//FIXME: Este logger y el return false ocultan errores del programador
			return false;
		}
	}
	
	/**
	 * Determina si un nombre de predicado corresponde con un
	 * predicado del repositorio dependiente de Java.
	 * 
	 * @param predicateName nombre del predicado cuya dependencia del lenguaje se 
	 * quiere determinar.
	 * 
	 * @return <code>true</code> si el predicado es dependiente de Java; 
	 * <code>false</code> en caso contrario.
	 */
	public static boolean isPredicateJavaDependent(String predicateName){
		RepositoryElementLister lister = 
			RepositoryElementLister.getInstance();
		
		try {
			HashMap<String, String> javaPredicates = lister.getJavaPredicateList();
			return javaPredicates.containsKey(predicateName);
		}
		catch (IOException exception){
			logger.error(
				Messages.RepositoryElementProcessor_ErrorAccessingRepository + 
				":\n\n" + exception.getMessage()); //$NON-NLS-1$
			return false;
		}
	}
}