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

import com.google.common.base.Objects;

/**
 * Representa una entrada de la refactorización din�mica en la interfaz.
 * 
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public final class InputParameter {

	/**
	 * El nombre completamente cualificado del tipo de declaración del parámetro
	 * de entrada.
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
	 * obtener el valor de este parámetro de entrada mediante la aplicación del
	 * método {@link #method}.
	 */
	private String from;

	/**
	 * El atributo que indica si una entrada es la entrada principal de la
	 * refactorización o no.
	 */
	private boolean isMain;

	/**
	 * Constructor.
	 * 
	 * @param type
	 *            nombre completamente cualificado del tipo de la entrada.
	 * @param name
	 *            nombre identificador del parámetro de entrada.
	 * @param from
	 *            nombre identificador de la entrada a partir de la que se puede
	 *            obtener el valor de esta nueva entrada mediante la aplicación
	 *            del método #method.
	 * @param method
	 *            método que permite obtener el valor de la entrada.
	 * @param root
	 *            si la entrada es la entrada principal de la refactorización.
	 */
	private InputParameter(String type, String name, String from,
			String method, boolean root) {
		this.type = type;
		this.name = name;
		this.from = (from != null) ? from : ""; //$NON-NLS-1$
		this.method = (method != null) ? method : ""; //$NON-NLS-1$
		this.isMain = root; //$NON-NLS-1$
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
	 * Obtiene el nombre identificador de la entrada a partir de la que se puede
	 * obtener el valor de esta entrada.
	 * 
	 * @return el nombre identificador de la entrada a partir de la que se puede
	 *         obtener el valor de esta entrada.
	 * 
	 * @see #setFrom(String)
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Obtiene el nombre completamente cualificado del tipo del parámetro.
	 * 
	 * @return el nombre completamente cualificado del tipo del parámetro.
	 * 
	 * @see #setType(String)
	 */
	public String getType() {
		return type;
	}

	/**
	 * Obtiene el nombre del método que permite obtener los posibles valores del
	 * parámetro de entrada a partir del objeto #from.
	 * 
	 * @return el nombre del método que permite obtener los posibles valores del
	 *         parámetro de entrada a partir del objeto {@link #from}.
	 * 
	 * @see #setMethod(String)
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Obtiene el valor del atributo que indica si la entrada es la entrada
	 * principal de la refactorización o no.
	 * 
	 * @return el valor del atributo que indica si la entrada es la entrada
	 *         principal de la refactorización o no.
	 * 
	 */
	public boolean isMain() {
		return isMain;
	}

	/**
	 * Genera un builder de InputParameters preconfigurado con los parametros de
	 * la actual.
	 * 
	 * @return builder con los parametros del InputParameter actual
	 */
	public final Builder getBuilder() {
		return new Builder(getType()).name(getName()).from(getFrom())
				.method(getMethod()).main(isMain());
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

	/**
	 * Dos parametros son iguales cuando todos sus parametros son iguales.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof InputParameter) {
			final InputParameter otroParam = (InputParameter) o;
			return getType().equals(otroParam.getType())
					&& getName().equals(otroParam.getName())
					&& getFrom().equals(otroParam.getFrom())
					&& getMethod().equals(otroParam.getMethod())
					&& otroParam.isMain() == isMain();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getType(), getName(), getFrom(), getMethod(),
				isMain());
	}

	/**
	 * Builder de InputParameters.
	 * 
	 * @author imediava
	 * 
	 */
	public static class Builder {

		private String type;
		private String name = "";
		private String from = "";
		private String method = "";
		private boolean isMain = false;

		/**
		 * Construye un builder de InputParameters especifando el tipo del
		 * parametro a crear.
		 * 
		 * @param type
		 *            tipo del parametro a crear
		 */
		public Builder(String type) {
			this.type = type;
		}

		/**
		 * Construye el InputParameter.
		 * 
		 * @return input parameter con los parametros pasados
		 */
		public InputParameter build() {
			return new InputParameter(type, name, from, method, isMain);
		}

		/**
		 * Asigna el nombre que tomaria el parametro a crear.
		 * 
		 * @param name
		 *            nuevo nombre
		 * @return builder con los nuevos parametros
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Asigna el from.
		 * 
		 * @param from
		 *            nuevo from
		 * @return builder con los nuevos parametros
		 */
		public Builder from(String from) {
			this.from = from;
			return this;
		}

		/**
		 * Asigna el method.
		 * 
		 * @param method
		 *            nuevo metodo
		 * @return builder con los nuevos parametros
		 */
		public Builder method(String method) {
			this.method = method;
			return this;
		}

		/**
		 * Asigna el parametro isMain que indica si esta input es la principal o
		 * no.
		 * 
		 * @param isMain
		 *            si la entrada va a ser principal
		 * @return builder
		 */
		public Builder main(boolean isMain) {
			this.isMain = isMain;
			return this;
		}

	}

}