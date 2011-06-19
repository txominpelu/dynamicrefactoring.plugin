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

import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import moon.core.Model;
import refactoring.engine.Action;
import refactoring.engine.Predicate;
import repository.moon.MOONRefactoring;
import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.xml.XMLRefactoringUtils;
import dynamicrefactoring.util.PluginStringUtils;

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
	private Map<String, Object> inputParameters;

	/**
	 * Constructor.
	 * 
	 * @param refactoringDefinition  definicion de la refactorizacion
	 * 
	 * @param model
	 *            modelo MOON sobre el que se refactoriza.
	 * @param parameters
	 *            tabla de parámetros de entrada de la refactorización.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al construir la refactorización
	 *             dinámica.
	 */
	public DynamicRefactoring(DynamicRefactoringDefinition refactoringDefinition,  Model model,
			Map<String, Object> parameters) throws RefactoringException {

		super(refactoringDefinition.getName(), model);
		this.refactoringDefinition = refactoringDefinition;
		inputParameters = parameters;
		createRefactoring();
	}

	/**
	 * Crea las precondiciones, acciones y postcondiciones que forman la
	 * refactorización.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al crear alguno de los componentes de
	 *             la refactorización.
	 */
	private void createRefactoring() throws RefactoringException {

		for (RefactoringMechanismInstance mecanismo : refactoringDefinition.getPreconditions()){
			createPrecondition(mecanismo.getClassName());
		}

		
		for (RefactoringMechanismInstance mecanismo: refactoringDefinition.getActions()){
			createAction(mecanismo.getClassName());
		}

		for (RefactoringMechanismInstance mecanismo : refactoringDefinition.getPostconditions()){
			createPostcondition(mecanismo.getClassName());
		}
	}

	/**
	 * Crea una precondición y se la añade a la refactorización.
	 * 
	 * @param name
	 *            nombre simple de la precondición.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al crear la precondición o al
	 *             añadirla a la refactorización.
	 */
	private void createPrecondition(String name) throws RefactoringException {

		try {

			Class<?> preconditionClass = Class.forName(PluginStringUtils
					.getMechanismFullyQualifiedName(
							RefactoringMechanismType.PRECONDITION, name));

			Constructor<?> constructor = preconditionClass.getConstructors()[0];
			Object[] parameters = getNecessaryParameters(constructor,
					RefactoringMechanismType.PRECONDITION);

			// Se crea la instancia de la precondición y se la añade a la
			// refactorización.
			addPrecondition((Predicate) constructor.newInstance(parameters));
		} catch (Exception e) {
			Object[] messageArgs = { name };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.DynamicRefactoring_ErrorAddingPrecondition);

			throw new RefactoringException(formatter.format(messageArgs)
					+ ":\n" + //$NON-NLS-1$
					e.getMessage(), e);
		}

	}

	/**
	 * Crea una acción concreta y se la añade a la refactorización.
	 * 
	 * @param name
	 *            nombre simple de la acción.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al crear la acción concreta o al
	 *             añadírsela a la refactorización.
	 */
	private void createAction(String name) throws RefactoringException {

		try {
			String actionPack = ""; //$NON-NLS-1$
			if (RefactoringMechanismType.ACTION.isElementJavaDependent(name))
				actionPack = RefactoringConstants.JAVA_ACTIONS_PACKAGE;
			else
				actionPack = RefactoringConstants.ACTIONS_PACKAGE;

			Class<?> actionClass = Class.forName(actionPack + name);

			Constructor<?> constructor = actionClass.getConstructors()[0];
			Object[] parameters = getNecessaryParameters(constructor,
					RefactoringMechanismType.ACTION);

			// Se crea la instancia de la acción concreta y se añade a la
			// refactorización.
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
	 * Crea una postcondición concreta y se la añade a la refactorización.
	 * 
	 * @param name
	 *            nombre simple de la postcondición.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al crear la postcondición concreta o
	 *             al añadírsela a la refactorización.
	 */
	private void createPostcondition(String name) throws RefactoringException {

		try {

			Class<?> postconditionClass = Class.forName(PluginStringUtils
					.getMechanismFullyQualifiedName(
							RefactoringMechanismType.POSTCONDITION, name));

			Constructor<?> constructor = postconditionClass.getConstructors()[0];
			Object[] parameters = getNecessaryParameters(constructor,
					RefactoringMechanismType.POSTCONDITION);

			// Se crea la instancia de la postcondicion concreta y se añade
			// a la refactorización.
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
	 * Obtiene los parámetros necesarios para la llamada al constructor pasado
	 * como parámetro.
	 * 
	 * @param constructor
	 *            constructor cuya lista de parámetros necesarios se desea
	 *            obtener.
	 * @param type
	 *            si se trata de precondición, acción o postcondición
	 * 
	 * @return un <i>array</i> con los objetos que pasar como parámetros al
	 *         constructor.
	 * 
	 * @throws RefactoringException
	 *             si se produce algún error durante la construcción de la lista
	 *             de parámetros del constructor.
	 */
	private Object[] getNecessaryParameters(Constructor<?> constructor,
			RefactoringMechanismType type)
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
				Object param = getInput(
						dynamicrefactoring.util.PluginStringUtils
								.getClassName(elementName),
						i, type);

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
	 * acción a partir de su constructor.
	 * 
	 * @param constructor
	 *            constructor del elemento cuyo nombre se ha de obtener.
	 * @param type
	 *            tipo de elemento del repositorio del que se trata (uno de
	 *            precondición, acción o postcondicion).
	 * 
	 * @return el nombre simple del predicado o acción.
	 */
	private String getTypeName(Constructor<?> constructor,
			RefactoringMechanismType type) {

		switch (type) {
		case PRECONDITION:
		case POSTCONDITION:
		case ACTION:
			return constructor.getName();
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
	 * <p>
	 * Cada entrada de la lista devuelta contiene un array de cadenas cuyo
	 * primer componente es el tipo, y el segundo el nombre del parámetro.
	 * </p>
	 * 
	 * @param refactoringFilePath
	 *            ruta del fichero XML con la refactorización.
	 * 
	 * @return lista de arrays de cadenas con la definición de cada entrada.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al obtener la definición de los
	 *             parámetros de entrada.
	 */
	public static List<InputParameter> getDefinitionInputParameters(
			String refactoringFilePath) throws RefactoringException {
		return XMLRefactoringUtils.getRefactoringDefinition(
				refactoringFilePath).getInputs();
	}

	/**
	 * Obtiene el nombre de la refactorización.
	 * 
	 * @return el nombre de la refactorizacion.
	 */
	public String getName() {
		return refactoringDefinition.getName();
	}

	/**
	 * Obtiene el valor asignado a la entrada que ocupa una cierta posición para
	 * un predicado o acción concreto.
	 * 
	 * @param elementName
	 *            nombre simple del predicado o acción.
	 * @param position
	 *            posición de la entrada en la lista de parámetros.
	 * @param type
	 *            código del tipo de elemento precondición, acción o
	 *            postcondición.
	 * 
	 * @return el valor asignado a la entrada.
	 */
	private Object getInput(String elementName, int position,
			RefactoringMechanismType type) {
		// Se obtiene el valor asociado a la entrada con el nombre dado.
		final String inputParameterName = refactoringDefinition.getMechanismsWithName(elementName, type).get(0).getInputParameters().get(position);
		return inputParameters.get(inputParameterName);
	}

	/**
	 * Obtiene la definición de las entradas de una refactorización.
	 * 
	 * @return Descripción de los parámetros de entrada a la
	 *         refactorización.
	 */
	public List<InputParameter> getInputs() {
		return refactoringDefinition.getInputs();
	}

	/**
	 * Obtiene las entradas de una refactorización.
	 * 
	 * @return Tabla con los parámetros de entrada a la refactorización.
	 */
	public Map<String, Object> getInputsParameters() {
		return inputParameters;
	}
}