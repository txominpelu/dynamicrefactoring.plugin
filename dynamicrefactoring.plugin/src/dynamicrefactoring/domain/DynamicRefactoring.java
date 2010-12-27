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

package dynamicrefactoring.domain;

import dynamicrefactoring.RefactoringConstants;

import dynamicrefactoring.interfaz.dynamic.RepositoryElementProcessor;

import dynamicrefactoring.reader.*;

import java.text.MessageFormat;
import java.util.*;
import java.io.File;
import java.lang.reflect.*;

import moon.core.Model;

import refactoring.engine.*;
import repository.moon.MOONRefactoring;

/**
 * Representa una refactorización dinámica.
 *
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class DynamicRefactoring extends MOONRefactoring {

	/**
	 * Definición de la refactorización.
	 */
	private DynamicRefactoringDefinition refactoringDefinition;

	/**
	 * Tabla con los parámetros de entrada a la refactorización.
	 */
	private HashMap<String, Object> inputParameters;
	
	/**
	 * Constructor.
	 * 
	 * @param refactoringFilePath ruta del fichero con la refactorización.
	 * @param model modelo MOON sobre el que se refactoriza.
	 * @param parameters tabla de parámetros de entrada de la refactorización.
	 * 
	 * @throws RefactoringException si se produce un error al construir
	 * la refactorización dinámica.
	 */
	public DynamicRefactoring(String refactoringFilePath, Model model,
		HashMap<String, Object> parameters) throws RefactoringException {
		
		super(refactoringFilePath, model);
		
		try {
			XMLRefactoringReaderFactory f = 
				new JDOMXMLRefactoringReaderFactory();
			XMLRefactoringReaderImp implementor = 
				f.makeXMLRefactoringReaderImp(new File(refactoringFilePath));
			XMLRefactoringReader reader = new XMLRefactoringReader(implementor);
			
			refactoringDefinition = reader.getDynamicRefactoringDefinition();
		}
		catch (Exception e) {
			Object[] messageArgs = {refactoringFilePath};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(
				Messages.DynamicRefactoring_ErrorReading);
			
			throw new RefactoringException(
				formatter.format(messageArgs) + ":\n\n" + //$NON-NLS-1$
				e.getMessage());
		}
		inputParameters = parameters;
		createRefactoring();
	}
	
	/**
	 * Crea las precondiciones, acciones y postcondiciones que forman la
	 * refactorización.
	 * 
	 * @throws RefactoringException si se produce un error al crear 
	 * alguno de los componentes de la refactorización. 
	 */
	private void createRefactoring() throws RefactoringException {
		
		for (String namePart : refactoringDefinition.getPreconditions())
			createPrecondition(namePart);
		
		for (String namePart : refactoringDefinition.getActions())
			createAction(namePart);

		for (String namePart : refactoringDefinition.getPostconditions())
			createPostcondition(namePart);	
	}
	
	/**
	 * Crea una precondición y se la añade a la refactorización.
	 * 
	 * @param name nombre simple de la precondición.
	 * 
	 * @throws RefactoringException si se produce un error al crear la
	 * precondición o al añadirla a la refactorización. 
	 */
	private void createPrecondition(String name) 
		throws RefactoringException {
		
		try {
			String predicatePack = ""; //$NON-NLS-1$
			if (RepositoryElementProcessor.isPredicateJavaDependent(name))
				predicatePack = RefactoringConstants.JAVA_PREDICATES_PACKAGE;
			else 
				predicatePack = RefactoringConstants.PREDICATES_PACKAGE;
			
			Class<?> preconditionClass = Class.forName(
				predicatePack + name);
			
			Constructor<?> constructor = preconditionClass.getConstructors()[0];
			Object[] parameters = getNecessaryParameters(constructor, 
				RefactoringConstants.PRECONDITION);
	
			// Se crea la instancia de la precondición y se la añade a la 
			// refactorización.
			addPrecondition((Predicate) constructor.newInstance(parameters));
		}
		catch (Exception e) {
			Object[] messageArgs = {name};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(
				Messages.DynamicRefactoring_ErrorAddingPrecondition);
			
			throw new RefactoringException(
				formatter.format(messageArgs) + ":\n" + //$NON-NLS-1$
				e.getMessage());
		}
			
	}

	/**
	 * Crea una acción concreta y se la añade a la refactorización.
	 * 
	 * @param name nombre simple de la acción.
	 * 
	 * @throws RefactoringException si se produce un error al crear la
	 * acción concreta o al añadírsela a la refactorización. 
	 */
	private void createAction(String name) throws RefactoringException {
		
		try {
			String actionPack = ""; //$NON-NLS-1$
			if (RepositoryElementProcessor.isActionJavaDependent(name))
				actionPack = RefactoringConstants.JAVA_ACTIONS_PACKAGE;
			else 
				actionPack = RefactoringConstants.ACTIONS_PACKAGE;
			
			Class<?> actionClass = Class.forName(actionPack + name);
			
			Constructor<?> constructor = actionClass.getConstructors()[0];
			Object[] parameters = getNecessaryParameters(constructor, 
				RefactoringConstants.ACTION);
	
			// Se crea la instancia de la acción concreta y se añade a la 
			// refactorización.
			addAction((Action) constructor.newInstance(parameters));
		}
		catch (Exception e) {
			Object[] messageArgs = {name};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(
				Messages.DynamicRefactoring_ErrorAddingAction);
			
			throw new RefactoringException(
				formatter.format(messageArgs) + ":\n" + //$NON-NLS-1$
				e.getMessage());
		}
	}
	
	/**
	 * Crea una postcondición concreta y se la añade a la refactorización.
	 * 
	 * @param name nombre simple de la postcondición.
	 * 
	 * @throws RefactoringException si se produce un error al crear la
	 * postcondición concreta o al añadírsela a la refactorización. 
	 */
	private void createPostcondition(String name) throws RefactoringException {
		
		try {
			String predicatePack = ""; //$NON-NLS-1$
			if (RepositoryElementProcessor.isPredicateJavaDependent(name))
				predicatePack = RefactoringConstants.JAVA_PREDICATES_PACKAGE;
			else 
				predicatePack = RefactoringConstants.PREDICATES_PACKAGE;
			
			Class<?> postconditionClass = Class.forName(
				predicatePack + name);
			
			Constructor<?> constructor = postconditionClass.getConstructors()[0];
			Object[] parameters = getNecessaryParameters(constructor, 
				RefactoringConstants.POSTCONDITION);
	
			// Se crea la instancia de la postcondicion concreta y se añade 
			// a la refactorización.
			addPostcondition((Predicate) constructor.newInstance(parameters));
		}
		catch (Exception e) {
			Object[] messageArgs = {name};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(
				Messages.DynamicRefactoring_ErrorAddingPostcondition);
			
			throw new RefactoringException(
				formatter.format(messageArgs) + ":\n" + //$NON-NLS-1$
				e.getMessage());
		}
	}

	/**
	 * Obtiene los parámetros necesarios para la llamada al constructor pasado
	 * como parámetro.
	 * 
	 * @param constructor constructor cuya lista de parámetros necesarios se 
	 * desea obtener.
	 * @param type si se trata de 
	 * {@link RefactoringConstants#PRECONDITION}, 
	 * {@link RefactoringConstants#ACTION} o 
	 * {@link RefactoringConstants#POSTCONDITION}.
	 * 
	 * @return un <i>array</i> con los objetos que pasar como parámetros al 
	 * constructor.
	 * 
	 * @throws RefactoringException si se produce algún error durante la
	 * construcción de la lista de parámetros del constructor. 
	 */
	private Object[] getNecessaryParameters(Constructor<?> constructor, int type) 
		throws RefactoringException {
		
		ArrayList<Object> parameters = new ArrayList<Object>();
		
		// Se obtienen los tipos de los argumentos del constructor.
		Class<?>[] constrParameters = constructor.getParameterTypes();
		
		// Se obtiene el nombre del predicado o acción.
		String elementName = getTypeName(constructor, type);
		
		try {
			// Para cada argumento del constructor.
			for (int i = 0; i < constrParameters.length; i++) {
				
				// Se obtiene la clase de declaración del parámetro.
				Class<?> parameterType = constrParameters[i];

				// Se busca el valor para el parámetro de la posición i.
				Object param = getInput(elementName, i, type);
									
				if(param == null){
					Object[] messageArgs = {parameterType, elementName};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(
						Messages.DynamicRefactoring_ErrorObtainingValue);
					
					throw new RefactoringException(
						formatter.format(messageArgs)+ ".\n"); //$NON-NLS-1$
				}
				
				parameters.add(param);
			} 
			return parameters.toArray();
		}
		catch (Exception e) {
			Object[] messageArgs = {elementName};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(
				Messages.DynamicRefactoring_ErrorObtainingParameters);
			throw new RefactoringException(formatter.format(messageArgs) + ".\n"); //$NON-NLS-1$
		}
	}

	/**
	 * Obtiene el nombre de un predicado o acción a partir de su constructor.
	 * 
	 * @param constructor constructor del elemento cuyo nombre se ha de obtener.
	 * @param type tipo de elemento del repositorio del que se trata (uno de 
	 * {@link RefactoringConstants#PRECONDITION}, 
	 * {@link RefactoringConstants#POSTCONDITION} o
	 * {@link RefactoringConstants#ACTION}.
	 * 
	 * @return el nombre simple del predicado o acción.
	 */
	private String getTypeName(Constructor<?> constructor, int type) {
		
		String constructorName = constructor.getName();
		
		switch(type){
		case RefactoringConstants.PRECONDITION:
		case RefactoringConstants.POSTCONDITION:			
		case RefactoringConstants.ACTION:
			int index = constructorName.lastIndexOf('.') + 1;
			return constructorName.substring(index);
		default:
			throw new RuntimeException();	
		}
	}

	/**
	 * Devuelve una definición de los parámetros de entrada de la 
	 * refactorización, de forma que a partir de esta definición se pueda 
	 * construir, por ejemplo, una interfaz de usuario para esta 
	 * refactorización.
	 * 
	 * <p>Cada entrada de la lista devuelta contiene un array de cadenas cuyo 
	 * primer componente es el tipo, y el segundo el nombre del parámetro.</p>
	 * 
	 * @param refactoringFilePath ruta del fichero XML con la refactorización.
	 * 
	 * @return lista de arrays de cadenas con la definición de cada entrada.
	 * 
	 * @throws RefactoringException si se produce un error al obtener
	 * la definición de los parámetros de entrada. 
	 */
	public static ArrayList<String[]> getDefinitionInputParameters(
		String refactoringFilePath) throws RefactoringException {
		return DynamicRefactoringDefinition.getRefactoringDefinition(refactoringFilePath).getInputs();
	}
	
	/**
	 * Obtiene el nombre de la refactorización.
	 * 
	 * @return el nombre de la refactorizacion.
	 */
	public String getName(){
		return refactoringDefinition.getName();
	}
	
	/**
	 * Obtiene el valor asignado a la entrada que ocupa una cierta posición para
	 * un predicado o acción concreto.
	 * 
	 * @param elementName nombre simple del predicado o acción.
	 * @param position posición de la entrada en la lista de parámetros.
	 * @param type código del tipo de elemento 
	 * ({@link RefactoringConstants#PRECONDITION}, 
	 * {@link RefactoringConstants#POSTCONDITION} o
	 * {@link RefactoringConstants#ACTION}).
	 * 
	 * @return el valor asignado a la entrada.
	 */
	private Object getInput(String elementName, int position, int type){
		// Se obtienen los argumentos del predicado o acción.
		ArrayList<String[]> parameters = 
			refactoringDefinition.getAmbiguousParameters(elementName, type);
		// Se obtiene el argumento de la posición especificada.
		String[] parameter = parameters.get(position);
		
		// Se obtiene el valor asociado a la entrada con el nombre dado.
		return inputParameters.get(parameter[0]);
	}
	
	/**
	 * Obtiene la definición de las entradas de una refactorización.
	 * 
	 * @return Descripción de los parámetros de entrada a la refactorización.
	 */
	public ArrayList<String[]> getInputs(){
		return refactoringDefinition.getInputs();
	}
	
	/**
	 * Obtiene las entradas de una refactorización.
	 * 
	 * @return Tabla con los parámetros de entrada a la refactorización.
	 */
	public HashMap<String, Object> getInputsParameters(){
		return inputParameters;
	}
}