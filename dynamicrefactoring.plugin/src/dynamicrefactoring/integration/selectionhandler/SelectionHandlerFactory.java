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
package dynamicrefactoring.integration.selectionhandler;

import dynamicrefactoring.util.selection.SelectionInfo;
import dynamicrefactoring.util.selection.TextSelectionInfo;
import dynamicrefactoring.util.selection.TreeSelectionInfo;

/**
 * Proporciona funciones capaces de obtener cualquiera de los objetos que 
 * permiten hallar el objeto MOON equivalente al seleccionado en Eclipse,
 * en función de su tipo.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class SelectionHandlerFactory {
	
	/**
	 * Instancia única de la fábrica (patrón Singleton).
	 */
	private static SelectionHandlerFactory myInstance;
	
	/**
	 * Constructor.
	 * 
	 * Privado según la estructura del patrón de diseño Singleton.
	 */
	private SelectionHandlerFactory(){}

	/**
	 * Obtiene la instancia única de la fábrica (patrón Singleton).
	 *
	 * @return la instancia única de la fábrica.
	 */
	public static SelectionHandlerFactory getInstance(){
		if (myInstance == null)
			myInstance = new SelectionHandlerFactory();
		return myInstance;
	}

	/**
	 * Obtiene un integrador para MOON concreto para una selección de un método 
	 * dada.
	 * 
	 * Si la selección forma parte de un árbol de selección, devuelve una
	 * instancia de <code>TreeMethodSelectionHandler</code>.
	 * Si la selección forma parte de una representación textual, devuelve una
	 * instancia de <code>TextMethodSelectionHandler</code>.
	 * 
	 * Método fábrica (patrón de diseño Método fábrica).
	 * 
	 * @param infoProvider el proveedor de información para cuya selección 
	 * se quiere obtener un integrador con MOON concreto.
	 * 
	 * @return un integrador para MOON concreto para una selección de un método.
	 * 
	 * @throws Exception si se produce un error al intentar crear cualquiera de 
	 * los integradores concretos.
	 * 
	 * @see TextMethodSelectionHandler#TextMethodSelectionHandler(TextSelectionInfo)
	 * @see TreeMethodSelectionHandler#TreeMethodSelectionHandler(TreeSelectionInfo)
	 */
	public MethodSelectionHandler createMethodSelectionHandler(
		SelectionInfo infoProvider) throws Exception{
		
		if(infoProvider instanceof TreeSelectionInfo){
			return new TreeMethodSelectionHandler(
				(TreeSelectionInfo) infoProvider);
		}
		if(infoProvider instanceof TextSelectionInfo){
			return new TextMethodSelectionHandler(
				(TextSelectionInfo) infoProvider);
		}
		
		return null;
	}

	/**
	 * Obtiene un integrador para MOON concreto para una selección de una clase 
	 * dada.
	 * 
	 * Si la selección forma parte de un árbol de selección, devuelve una
	 * instancia de <code>TreeClassSelectionHandler</code>.
	 * Si la selección forma parte de una representación textual, devuelve una
	 * instancia de <code>TextClassSelectionHandler</code>.
	 * 
	 * Método fábrica (patrón de diseño Método fábrica).
	 * 
	 * @param infoProvider el proveedor de información para cuya selección 
	 * se quiere obtener un integrador con MOON concreto.
	 * 
	 * @return un integrador para MOON concreto para una selección de una clase.
	 * 
	 * @throws Exception si se produce un error al intentar crear cualquiera de 
	 * los integradores concretos. 
	 */
	public ClassSelectionHandler createClassSelectionHandler(
		SelectionInfo infoProvider) throws Exception{
		
		if(infoProvider instanceof TreeSelectionInfo)
			return new TreeClassSelectionHandler(
				(TreeSelectionInfo) infoProvider);
		if(infoProvider instanceof TextSelectionInfo)
			return new TextClassSelectionHandler(
				(TextSelectionInfo) infoProvider);
		
		return null;
	}

	/**
	 * Obtiene un integrador para MOON concreto para una selección de un atributo 
	 * dada.
	 * 
	 * Si la selección forma parte de un árbol de selección, devuelve una
	 * instancia de <code>TreeFieldSelectionHandler</code>.
	 * Si la selección forma parte de una representación textual, devuelve una
	 * instancia de <code>TextFieldSelectionHandler</code>.
	 * 
	 * Método fábrica (patrón de diseño Método fábrica).
	 * 
	 * @param infoProvider el proveedor de información para cuya selección 
	 * se quiere obtener un integrador con MOON concreto.
	 * 
	 * @return un integrador para MOON concreto para una selección de un 
	 * atributo.
	 * 
	 * @throws Exception si se produce un error al intentar crear cualquiera de 
	 * los integradores concretos.
	 * 
	 * @see TextFieldSelectionHandler#TextFieldSelectionHandler(TextSelectionInfo)
	 * @see TreeFieldSelectionHandler#TreeFieldSelectionHandler(TreeSelectionInfo)
	 */
	public FieldSelectionHandler createFieldSelectionHandler(
		SelectionInfo infoProvider) throws Exception{
		
		if(infoProvider instanceof TreeSelectionInfo)
			return new TreeFieldSelectionHandler(
				(TreeSelectionInfo) infoProvider);
		if(infoProvider instanceof TextSelectionInfo)
			return new TextFieldSelectionHandler(
				(TextSelectionInfo) infoProvider);
		
		return null;
	}
	
	/**
	 * Obtiene un integrador para MOON concreto para una selección de un 
	 * argumento formal dada.
	 * 
	 * Si la selección forma parte de una representación textual, devuelve una
	 * instancia de <code>FormalArgumentSelectionHandler</code>.
	 * 
	 * Método fábrica (patrón de diseño Método fábrica).
	 * 
	 * @param infoProvider el proveedor de información para cuya selección 
	 * se quiere obtener un integrador con MOON concreto.
	 * 
	 * @return un integrador para MOON concreto para una selección de un 
	 * argumento formal.
	 * 
	 * @throws Exception si se produce un error al intentar crear cualquiera de 
	 * los integradores concretos.
	 * 
	 * @see FormalArgumentSelectionHandler#FormalArgumentSelectionHandler(TextSelectionInfo)
	 */
	public FormalArgumentSelectionHandler createFormalArgumentSelectionHandler(
		SelectionInfo infoProvider) throws Exception{
		
		if(infoProvider instanceof TextSelectionInfo)
			return new FormalArgumentSelectionHandler(
				(TextSelectionInfo) infoProvider);
		
		return null;
	}
	
	/**
	 * Obtiene un integrador para MOON concreto para una selección de un 
	 * parámetro formal dada.
	 * 
	 * Si la selección forma parte de una representación textual, devuelve una
	 * instancia de <code>FormalParameterSelectionHandler</code>.
	 * 
	 * Método fábrica (patrón de diseño Método fábrica).
	 * 
	 * @param infoProvider el proveedor de información para cuya selección 
	 * se quiere obtener un integrador con MOON concreto.
	 * 
	 * @return un integrador para MOON concreto para una selección de un 
	 * parámetro formal.
	 * 
	 * @throws Exception si se produce un error al intentar crear el
	 * integrador concreto.
	 */
	public FormalParameterSelectionHandler createFormalParameterSelectionHandler(
		SelectionInfo infoProvider) throws Exception{
		
		if(infoProvider instanceof TextSelectionInfo)
			return new FormalParameterSelectionHandler(
				(TextSelectionInfo) infoProvider);
		
		return null;
	}
	
	/**
	 * Obtiene un integrador para MOON concreto para una selección de un fragmento de
	 * texto dada.
	 * 
	 * Si la selección forma parte de una representación textual, devuelve una
	 * instancia de <code>TextCodeFragmentSelectionHandler</code>.
	 * 
	 * Método fábrica (patrón de diseño Método fábrica).
	 * 
	 * @param infoProvider el proveedor de información para cuya selección 
	 * se quiere obtener un integrador con MOON concreto.
	 * 
	 * @return un integrador para MOON concreto para una selección de un 
	 * parámetro formal.
	 * 
	 * @throws Exception si se produce un error al intentar crear el
	 * integrador concreto.
	 */
	public TextCodeFragmentSelectionHandler createTextCodeFragmentSelectionHandler(
		SelectionInfo infoProvider) throws Exception{
		
		if(infoProvider instanceof TextSelectionInfo)
			return new TextCodeFragmentSelectionHandler(
				(TextSelectionInfo) infoProvider);
		
		return null;
	}
	
	/**
	 * Obtiene un integrador para MOON concreto para una selección de un 
	 * objeto dado, que deberá ser un método, una clase, un atributo, un
	 * argumento formal o un parámetro formal.
	 * 
	 * <p>Tiene en cuenta el tipo de selección contenido en el argumento
	 * recibido (tipo de objeto seleccionado, y si se trata de una selección
	 * sobre un árbol o sobre texto).</p>
	 * 
	 * Método fábrica (patrón de diseño Método fábrica).
	 * 
	 * @param infoProvider el proveedor de información para cuya selección 
	 * se quiere obtener un integrador con MOON concreto.
	 * 
	 * @return un integrador para MOON concreto para una selección de un 
	 * objeto, o <code>null</code> si la selección no es de ninguno de los
	 * tipos esperados.
	 * 
	 * @throws Exception si se produce un error al intentar crear el
	 * integrador concreto.
	 */
	public ISelectionHandler createHandler(SelectionInfo infoProvider) 
		throws Exception{
		
		if (infoProvider.isMethodSelection())
			return createMethodSelectionHandler(infoProvider);																
		if (infoProvider.isClassSelection())
			return createClassSelectionHandler(infoProvider);
		if (infoProvider.isFieldSelection())
			return createFieldSelectionHandler(infoProvider);
		if (infoProvider.isFormalArgumentSelection())
			return createFormalArgumentSelectionHandler(infoProvider);
		if (infoProvider.isFormalParameterSelection())
			return createFormalParameterSelectionHandler(infoProvider);
		if (infoProvider.isTextSelection())
			return createTextCodeFragmentSelectionHandler(infoProvider);
		
		return null;
	}
}