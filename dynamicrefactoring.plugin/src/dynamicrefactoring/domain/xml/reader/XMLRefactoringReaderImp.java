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

package dynamicrefactoring.domain.xml.reader;

import java.io.File;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Define una interfaz para los lectores de ficheros XML donde se definen
 * refactorizaciones.
 * 
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Pe�a Fern�ndez</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public interface XMLRefactoringReaderImp {

	/**
	 * Nombre de la etiqueta ra�z de la especificaci�n XML de una
	 * refactorizaci�n.
	 */
	public static final String REFACTORING_TAG = "refactoring"; //$NON-NLS-1$

	/**
	 * Nombre del atributo con el nombre de la refactorizaci�n.
	 */
	public static final String NAME_ATTRIBUTE = "name"; //$NON-NLS-1$

	/**
	 * Nombre del atributo con el n�mero de versi�n de la refactorizaci�n.
	 */
	public static final String VERSION_ATTRIBUTE = "version"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta con la informaci�n b�sica de la refactorizaci�n.
	 */
	public static final String INFORMATION_ELEMENT = "information"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta con la descripci�n de la refactorizaci�n.
	 */
	public static final String DESCRIPTION_ELEMENT = "description"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta para la imagen de la refactorizaci�n.
	 */
	public static final String IMAGE_ELEMENT = "image"; //$NON-NLS-1$

	/**
	 * Nombre del atributo con la ruta a la imagen de la refactorizaci�n.
	 */
	public static final String SRC_IMAGE_ATTRIBUTE = "src"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta con la motivaci�n de la refactorizaci�n.
	 */
	public static final String MOTIVATION_ELEMENT = "motivation"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta con las categor�as de la refactorizaci�n.
	 */
	public static final String CATEGORIZATION_ELEMENT = "categorization"; //$NON-NLS-1$
	
	/**
	 * Nombre de la etiqueta con las categor�as de la refactorizaci�n.
	 */
	public static final String CLASSIFICATION_ELEMENT = "classification"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta con la motivaci�n de la refactorizaci�n.
	 */
	public static final String CATEGORY_ELEMENT = "category"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta con la clasificacion a la que pertenece una de las categorias
	 * de la refactorizacion.
	 */
	public static final String CLASSIFICATION_NAME_ATTRIBUTE = "name"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta con las palabras clave que describen
	 * la refactorizacion.
	 */
	public static final String KEYWORD_ELEMENT = "keyword";

	/**
	 * Etiqueta contenedora de las palabras clave.
	 */
	public static final String KEYWORDS_ELEMENT = "keywords";
	/**
	 * Nombre de la etiqueta bajo la cual se especifican las entradas de la
	 * refactorizaci�n.
	 */
	public static final String INPUTS_ELEMENT = "inputs"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta bajo la cual se especifica una entrada concreta de
	 * la refactorizaci�n.
	 */
	public static final String INPUT_ELEMENT = "input"; //$NON-NLS-1$

	/**
	 * Nombre del atributo con el tipo de una entrada de la refactorizaci�n.
	 */
	public static final String TYPE_INPUT_ATTRIBUTE = "type"; //$NON-NLS-1$

	/**
	 * Nombre del atributo con el nombre de una entrada de la refactorizaci�n.
	 */
	public static final String NAME_INPUT_ATTRIBUTE = "name"; //$NON-NLS-1$

	/**
	 * Apuntador al nombre de otra entrada de la que obtener el valor de �sta.
	 */
	public static final String FROM_INPUT_ATTRIBUTE = "from"; //$NON-NLS-1$

	/**
	 * Nombre del m�todo que permite obtener el valor que se debe asignar a la
	 * entrada o la lista de valores entre los que se puede elegir su valor.
	 */
	public static final String METHOD_INPUT_ATTRIBUTE = "method"; //$NON-NLS-1$

	/**
	 * Nombre del atributo que indica si una entrada es la entrada principal de
	 * la refactorizaci�n.
	 */
	public static final String ROOT_INPUT_ATTRIBUTE = "root"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta bajo la que se especifica el mecanismo de
	 * funcionamiento de la refactorizaci�n.
	 */
	public static final String MECHANISM_ELEMENT = "mechanism"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta bajo la que se listan las precondiciones de la
	 * refactorizaci�n.
	 */
	public static final String PRECONDITIONS_ELEMENT = "preconditions"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta que especifica una precondici�n concreta de la
	 * refactorizaci�n.
	 */
	public static final String PRECONDITION_ELEMENT = "precondition"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta bajo la que se listan las acciones de la
	 * refactorizaci�n.
	 */
	public static final String ACTIONS_ELEMENT = "actions"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta que especifica una acci�n concreta de la
	 * refactorizaci�n.
	 */
	public static final String ACTION_ELEMENT = "action"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta bajo la que se listan las postcondiciones de la
	 * refactorizaci�n.
	 */
	public static final String POSTCONDITIONS_ELEMENT = "postconditions"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta que especifica una postcondici�n concreta de la
	 * refactorizaci�n.
	 */
	public static final String POSTCONDITION_ELEMENT = "postcondition"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta que define un par�metro ambiguo para una
	 * precondici�n, acci�n o postcondici�n de la refactorizaci�n.
	 */
	public static final String PARAM_ELEMENT = "param"; //$NON-NLS-1$

	/**
	 * Nombre del atributo con el nombre de un par�metro ambiguo.
	 */
	public static final String NAME_PARAM_ATTRIBUTE = "name"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta con los ejemplos de la refactorizaci�n.
	 */
	public static final String EXAMPLES_ELEMENT = "examples"; //$NON-NLS-1$

	/**
	 * Nombre de la etiqueta que especifica un ejemplo concreto de la
	 * refactorizaci�n.
	 */
	public static final String EXAMPLE_ELEMENT = "example"; //$NON-NLS-1$

	/**
	 * Nombre del atributo con un ejemplo de la situaci�n antes de aplicar la
	 * refactorizaci�n.
	 */
	public static final String BEFORE_EXAMPLE_ATTRIBUTE = "before"; //$NON-NLS-1$

	/**
	 * Nombre del atributo con un ejemplo de la situaci�n despu�s de aplicar la
	 * refactorizaci�n.
	 */
	public static final String AFTER_EXAMPLE_ATTRIBUTE = "after"; //$NON-NLS-1$

	/**
	 * Devuelve la definici�n de la refactorizaci�n.
	 * 
	 * @return la definici�n de la refactorizaci�n.
	 */
	public DynamicRefactoringDefinition getDynamicRefactoringDefinition(File file);
}