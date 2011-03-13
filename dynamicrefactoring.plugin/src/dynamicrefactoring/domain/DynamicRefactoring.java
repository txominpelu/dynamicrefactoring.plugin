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

import java.io.File;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

import moon.core.Model;
import refactoring.engine.Action;
import refactoring.engine.Predicate;
import repository.moon.MOONRefactoring;
import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.interfaz.dynamic.RepositoryElementProcessor;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderFactory;
import dynamicrefactoring.reader.XMLRefactoringReader;
import dynamicrefactoring.reader.XMLRefactoringReaderFactory;
import dynamicrefactoring.reader.XMLRefactoringReaderImp;

/**
 * Representa una refactorizaci�n din�mica.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Pe�a Fern�ndez</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class DynamicRefactoring extends MOONRefactoring {

	/**
	 * Definici�n de la refactorizaci�n.
	 */
	private DynamicRefactoringDefinition refactoringDefinition;

	/**
	 * Tabla con los par�metros de entrada a la refactorizaci�n.
	 */
	private HashMap<String, Object> inputParameters;

	/**
	 * Constructor.
	 * 
	 * @param refactoringFilePath
	 *            ruta del fichero con la refactorizaci�n.
	 * @param model
	 *            modelo MOON sobre el que se refactoriza.
	 * @param parameters
	 *            tabla de par�metros de entrada de la refactorizaci�n.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al construir la refactorizaci�n
	 *             din�mica.
	 */
	public DynamicRefactoring(String refactoringFilePath, Model model,
			HashMap<String, Object> parameters) throws RefactoringException {

		super(refactoringFilePath, model);

		try {
			XMLRefactoringReaderFactory f = new JDOMXMLRefactoringReaderFactory();
			XMLRefactoringReaderImp implementor = f
					.makeXMLRefactoringReaderImp();
			XMLRefactoringReader reader = new XMLRefactoringReader(implementor);

			refactoringDefinition = reader
					.getDynamicRefactoringDefinition(new File(
							refactoringFilePath));
		} catch (Exception e) {
			Object[] messageArgs = { refactoringFilePath };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.DynamicRefactoring_ErrorReading);

			throw new RefactoringException(formatter.format(messageArgs)
					+ ":\n\n" + //$NON-NLS-1$
					e.getMessage());
		}
		inputParameters = parameters;
		createRefactoring();
	}

	/**
	 * Crea las precondiciones, acciones y postcondiciones que forman la
	 * refactorizaci�n.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al crear alguno de los componentes de
	 *             la refactorizaci�n.
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
	 * Crea una precondici�n y se la a�ade a la refactorizaci�n.
	 * 
	 * @param name
	 *            nombre simple de la precondici�n.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al crear la precondici�n o al a�adirla
	 *             a la refactorizaci�n.
	 */
	private void createPrecondition(String name) throws RefactoringException {

		try {
			String predicatePack = ""; //$NON-NLS-1$
			if (RepositoryElementProcessor.isPredicateJavaDependent(name))
				predicatePack = RefactoringConstants.JAVA_PREDICATES_PACKAGE;
			else
				predicatePack = RefactoringConstants.PREDICATES_PACKAGE;

			Class<?> preconditionClass = Class.forName(predicatePack + name);

			Constructor<?> constructor = preconditionClass.getConstructors()[0];
			Object[] parameters = getNecessaryParameters(constructor,
					RefactoringConstants.PRECONDITION);

			// Se crea la instancia de la precondici�n y se la a�ade a la
			// refactorizaci�n.
			addPrecondition((Predicate) constructor.newInstance(parameters));
		} catch (Exception e) {
			Object[] messageArgs = { name };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.DynamicRefactoring_ErrorAddingPrecondition);

			throw new RefactoringException(formatter.format(messageArgs)
					+ ":\n" + //$NON-NLS-1$
					e.getMessage());
		}

	}

	/**
	 * Crea una acci�n concreta y se la a�ade a la refactorizaci�n.
	 * 
	 * @param name
	 *            nombre simple de la acci�n.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al crear la acci�n concreta o al
	 *             a�ad�rsela a la refactorizaci�n.
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

			// Se crea la instancia de la acci�n concreta y se a�ade a la
			// refactorizaci�n.
			addAction((Action) constructor.newInstance(parameters));
		} catch (Exception e) {
			Object[] messageArgs = { name };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.DynamicRefactoring_ErrorAddingAction);

			throw new RefactoringException(formatter.format(messageArgs)
					+ ":\n" + //$NON-NLS-1$
					e.getMessage());
		}
	}

	/**
	 * Crea una postcondici�n concreta y se la a�ade a la refactorizaci�n.
	 * 
	 * @param name
	 *            nombre simple de la postcondici�n.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al crear la postcondici�n concreta o
	 *             al a�ad�rsela a la refactorizaci�n.
	 */
	private void createPostcondition(String name) throws RefactoringException {

		try {
			String predicatePack = ""; //$NON-NLS-1$
			if (RepositoryElementProcessor.isPredicateJavaDependent(name))
				predicatePack = RefactoringConstants.JAVA_PREDICATES_PACKAGE;
			else
				predicatePack = RefactoringConstants.PREDICATES_PACKAGE;

			Class<?> postconditionClass = Class.forName(predicatePack + name);

			Constructor<?> constructor = postconditionClass.getConstructors()[0];
			Object[] parameters = getNecessaryParameters(constructor,
					RefactoringConstants.POSTCONDITION);

			// Se crea la instancia de la postcondicion concreta y se a�ade
			// a la refactorizaci�n.
			addPostcondition((Predicate) constructor.newInstance(parameters));
		} catch (Exception e) {
			Object[] messageArgs = { name };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.DynamicRefactoring_ErrorAddingPostcondition);

			throw new RefactoringException(formatter.format(messageArgs)
					+ ":\n" + //$NON-NLS-1$
					e.getMessage());
		}
	}

	/**
	 * Obtiene los par�metros necesarios para la llamada al constructor pasado
	 * como par�metro.
	 * 
	 * @param constructor
	 *            constructor cuya lista de par�metros necesarios se desea
	 *            obtener.
	 * @param type
	 *            si se trata de {@link RefactoringConstants#PRECONDITION},
	 *            {@link RefactoringConstants#ACTION} o
	 *            {@link RefactoringConstants#POSTCONDITION}.
	 * 
	 * @return un <i>array</i> con los objetos que pasar como par�metros al
	 *         constructor.
	 * 
	 * @throws RefactoringException
	 *             si se produce alg�n error durante la construcci�n de la lista
	 *             de par�metros del constructor.
	 */
	private Object[] getNecessaryParameters(Constructor<?> constructor, int type)
			throws RefactoringException {

		ArrayList<Object> parameters = new ArrayList<Object>();

		// Se obtienen los tipos de los argumentos del constructor.
		Class<?>[] constrParameters = constructor.getParameterTypes();

		// Se obtiene el nombre del predicado o acci�n.
		String elementName = getTypeName(constructor, type);

		try {
			// Para cada argumento del constructor.
			for (int i = 0; i < constrParameters.length; i++) {

				// Se obtiene la clase de declaraci�n del par�metro.
				Class<?> parameterType = constrParameters[i];

				// Se busca el valor para el par�metro de la posici�n i.
				Object param = getInput(elementName, i, type);

				if (param == null) {
					Object[] messageArgs = { parameterType, elementName };
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter
							.applyPattern(Messages.DynamicRefactoring_ErrorObtainingValue);

					throw new RefactoringException(
							formatter.format(messageArgs) + ".\n"); //$NON-NLS-1$
				}

				parameters.add(param);
			}
			return parameters.toArray();
		} catch (Exception e) {
			Object[] messageArgs = { elementName };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.DynamicRefactoring_ErrorObtainingParameters);
			throw new RefactoringException(formatter.format(messageArgs)
					+ ".\n"); //$NON-NLS-1$
		}
	}

	/**
	 * Obtiene el nombre completo cualificado (paquete+clase) de un predicado o
	 * acci�n a partir de su constructor.
	 * 
	 * @param constructor
	 *            constructor del elemento cuyo nombre se ha de obtener.
	 * @param type
	 *            tipo de elemento del repositorio del que se trata (uno de
	 *            {@link RefactoringConstants#PRECONDITION},
	 *            {@link RefactoringConstants#POSTCONDITION} o
	 *            {@link RefactoringConstants#ACTION}.
	 * 
	 * @return el nombre simple del predicado o acci�n.
	 */
	private String getTypeName(Constructor<?> constructor, int type) {

		switch (type) {
		case RefactoringConstants.PRECONDITION:
		case RefactoringConstants.POSTCONDITION:
		case RefactoringConstants.ACTION:
			return constructor.getName();
		default:
			throw new RuntimeException();
		}
	}

	/**
	 * Devuelve una definici�n de los par�metros de entrada de la
	 * refactorizaci�n, de forma que a partir de esta definici�n se pueda
	 * construir, por ejemplo, una interfaz de usuario para esta
	 * refactorizaci�n.
	 * 
	 * <p>
	 * Cada entrada de la lista devuelta contiene un array de cadenas cuyo
	 * primer componente es el tipo, y el segundo el nombre del par�metro.
	 * </p>
	 * 
	 * @param refactoringFilePath
	 *            ruta del fichero XML con la refactorizaci�n.
	 * 
	 * @return lista de arrays de cadenas con la definici�n de cada entrada.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al obtener la definici�n de los
	 *             par�metros de entrada.
	 */
	public static ArrayList<String[]> getDefinitionInputParameters(
			String refactoringFilePath) throws RefactoringException {
		return DynamicRefactoringDefinition.getRefactoringDefinition(
				refactoringFilePath).getInputs();
	}

	/**
	 * Obtiene el nombre de la refactorizaci�n.
	 * 
	 * @return el nombre de la refactorizacion.
	 */
	public String getName() {
		return refactoringDefinition.getName();
	}

	/**
	 * Obtiene el valor asignado a la entrada que ocupa una cierta posici�n para
	 * un predicado o acci�n concreto.
	 * 
	 * @param elementName
	 *            nombre simple del predicado o acci�n.
	 * @param position
	 *            posici�n de la entrada en la lista de par�metros.
	 * @param type
	 *            c�digo del tipo de elemento (
	 *            {@link RefactoringConstants#PRECONDITION},
	 *            {@link RefactoringConstants#POSTCONDITION} o
	 *            {@link RefactoringConstants#ACTION}).
	 * 
	 * @return el valor asignado a la entrada.
	 */
	private Object getInput(String elementName, int position, int type) {
		// Se obtienen los argumentos del predicado o acci�n.
		ArrayList<String[]> parameters = refactoringDefinition
				.getAmbiguousParameters(elementName, type);
		// Se obtiene el argumento de la posici�n especificada.
		String[] parameter = parameters.get(position);

		// Se obtiene el valor asociado a la entrada con el nombre dado.
		return inputParameters.get(parameter[0]);
	}

	/**
	 * Obtiene la definici�n de las entradas de una refactorizaci�n.
	 * 
	 * @return Descripci�n de los par�metros de entrada a la refactorizaci�n.
	 */
	public ArrayList<String[]> getInputs() {
		return refactoringDefinition.getInputs();
	}

	/**
	 * Obtiene las entradas de una refactorizaci�n.
	 * 
	 * @return Tabla con los par�metros de entrada a la refactorizaci�n.
	 */
	public HashMap<String, Object> getInputsParameters() {
		return inputParameters;
	}
}