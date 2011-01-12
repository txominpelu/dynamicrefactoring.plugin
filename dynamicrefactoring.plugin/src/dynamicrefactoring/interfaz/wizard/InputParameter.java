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

package dynamicrefactoring.interfaz.wizard;

/** 
 * Representa una entrada de la refactorización dinámica en la interfaz.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class InputParameter {

	/**
	 * El nombre completamente cualificado del tipo de declaración del 
	 * parámetro de entrada.
	 */
	private String type;

	/**
	 * El nombre identificador de la entrada.
	 */
	private String name;

	/**
	 * El método que permite obtener el valor de la entrada.
	 */
	private String method;

	/**
	 * El nombre identificador de la entrada a partir de la cual se puede
	 * obtener el valor de este parámetro de entrada mediante la aplicación
	 * del método {@link #method}.
	 */
	private String from;

	/**
	 * El atributo que indica si una entrada es la entrada principal de la
	 * refactorización o no.
	 */
	private String root;

	/**
	 * Constructor.
	 * 
	 * @param type el nombre completamente cualificado del tipo de la entrada. 
	 */
	public InputParameter(String type) {
		setType(type);
		this.name = new String(""); //$NON-NLS-1$
		this.method = new String(""); //$NON-NLS-1$
		this.from = new String(""); //$NON-NLS-1$
		this.root = "false"; //$NON-NLS-1$
	}		

	/**
	 * Constructor.
	 * 
	 * @param type nombre completamente cualificado del tipo de la entrada.
	 * @param name nombre identificador del parámetro de entrada.
	 * @param from nombre identificador de la entrada a partir de la que se
	 * puede obtener el valor de esta nueva entrada mediante la aplicación del
	 * método #method. 
	 * @param method método que permite obtener el valor de la entrada.
	 * @param root si la entrada es la entrada principal de la refactorización.
	 */
	public InputParameter(String type, String name, String from, String method,
			String root) {
		setType(type);
		setName(name);
		this.from = (from != null) ? from : ""; //$NON-NLS-1$
		this.method = (method != null) ? method : ""; //$NON-NLS-1$
		this.root = (root != null) ? root : "false"; //$NON-NLS-1$
	}

	/**
	 * Obtiene el nombre identificador del parámetro de entrada.
	 * 
	 * @return el nombre identificador del parámetro de entrada.
	 * 
	 * @see #setName(String)
	 */
	public String getName() {
		return name;
	}

	/**
	 * Establece el nombre identificador del parámetro de entrada.
	 * 
	 * @param name el nombre identificador del parámetro de entrada.
	 * 
	 * @see #getName()
	 */
	public void setName(String name) {
		this.name = (name != null) ? name : ""; //$NON-NLS-1$
	}

	/**
	 * Obtiene el nombre identificador de la entrada a partir de la que se
	 * puede obtener el valor de esta entrada.
	 * 
	 * @return el nombre identificador de la entrada a partir de la que se
	 * puede obtener el valor de esta entrada.
	 * 
	 * @see #setFrom(String)
	 */
	public String getFrom(){
		return from;
	}

	/**
	 * Establece el nombre identificador del parámetro de entrada a partir del
	 * cual se puede obtener el valor de esta entrada.
	 * 
	 * @param from el nombre identificador del parámetro de entrada a partir del
	 * que se puede obtener el valor de esta entrada.
	 * 
	 * @see #getFrom()
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Obtiene el nombre completamente cualificado del tipo del parámetro.
	 * 
	 * @return el nombre completamente cualificado del tipo del parámetro.
	 * 
	 * @see #setType(String)
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * Establece el nombre completamente cualificado del tipo del parámetro 
	 * ambiguo.
	 * 
	 * @param type nombre completamente cualificado del tipo del parámetro 
	 * ambiguo.
	 * 
	 * @see #getType
	 */
	public void setType(String type) {
		this.type = (type != null) ? type : ""; //$NON-NLS-1$
	}

	/**
	 * Obtiene el nombre del método que permite obtener los posibles valores
	 * del parámetro de entrada a partir del objeto #from.
	 * 
	 * @return el nombre del método que permite obtener los posibles valores
	 * del parámetro de entrada a partir del objeto {@link #from}.
	 * 
	 * @see #setMethod(String)
	 */
	public String getMethod(){
		return method;
	}

	/**
	 * Determina el nombre del método que permite obtener los posibles valores
	 * del parámetro de entrada a partir del objeto #from.
	 * 
	 * @param method el nombre del método que permite obtener los posibles
	 * valores del parámetro de entrada a partir del objeto {@link #from}.
	 * 
	 * @see #getMethod()
	 */
	public void setMethod(String method){
		this.method = method;
	}

	/**
	 * Obtiene el valor del atributo que indica si la entrada es la entrada
	 * principal de la refactorización o no.
	 * 
	 * @return el valor del atributo que indica si la entrada es la entrada
	 * principal de la refactorización o no.
	 * 
	 * @see #setRoot(String)
	 */
	public String getRoot(){
		return root;
	}

	/**
	 * Establece el valor del atributo que indica si la entrada es la entrada
	 * principal de la refactorización o no.
	 * 
	 * @param root el valor del atributo que indica si la entrada es la entrada
	 * principal de la refactorización o no.
	 * 
	 * @see #getRoot()
	 */
	public void setRoot(String root){
		this.root = root;
	}
	
	/**
	 * Devuelve la representación del parámetro como cadena de caracteres.
	 * 
	 * @return una cadena de caracteres con la representación del parámetro.
	 */
	@Override
	public String toString() {
		return name + "(" + type + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}