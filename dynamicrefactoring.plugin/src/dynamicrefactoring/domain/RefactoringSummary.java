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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * Proporciona un resumen informativo acerca de una refactorización ejecutada.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RefactoringSummary {
	
	/**
	 * Nombre de la refactorización.
	 */
	private String name;
	
	/**
	 * Fecha y hora a la que se completó la refactorización.
	 */
	private Date completionTime;
	
	/**
	 * Identificador de la operación de refactorización dentro del entorno de
	 * operaciones de Eclipse.
	 * 
	 * <p>Este identificador permite recuperar la operación que ejecutó esta
	 * refactorización y solicitar que se deshaga.</p>
	 */
	private String id;
	
	/**
	 * Constructor.
	 * 
	 * @param name nombre de la refactorización.
	 * @param completionTime fecha y hora a la que se completó la refactorización.
	 * @param id identificador de la operación de refactorización dentro del 
	 * entorno de operaciones de Eclipse.
	 */
	public RefactoringSummary(String name, Date completionTime, String id){
		this.name = name;
		this.completionTime = completionTime;
		this.id = id;
	}

	/**
	 * Obtiene el nombre de la refactorización.
	 * 
	 * @return el nombre de la refactorización.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtiene la fecha y la hora a la que se completó la refactorización.
	 * 
	 * @return la fecha y la hora a la que se completó la refactorización
	 */
	public Date getCompletionTime() {
		return completionTime;
	}
	
	/**
	 * Obtiene el identificador de la operación que ejecutó la refactorización.
	 * 
	 * @return el identificador de la operación que ejecutó la refactorización.
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Obtiene como cadena de caracteres la fecha y la hora a la que se completó 
	 * la refactorización.
	 * 
	 * <p>El formato utilizado es "MM/dd/yyyy - HH:mm:ss".</p>
	 * 
	 * <p>En la versión internacionalizada al castellano, se utiliza el 
	 * formato de fecha español "dd/MM/yyyy".</p>
	 * 
	 * @return la fecha y la hora a la que se completó la refactorización en formato
	 * de cadena de caracteres.
	 */
	public String getStrCompletionTime() {
		DateFormat dateFormat = new SimpleDateFormat(Messages.RefactoringSummary_DateFormat + " - HH:mm:ss");  //$NON-NLS-1$
		return dateFormat.format(completionTime);
	}
	
	/**
	 * Obtiene una representación textual de la refactorización.
	 * 
	 * @return una representación textual de la refactorización.
	 */
	@Override
	public String toString(){
		return getStrCompletionTime() + " - " + name + ".\n";  //$NON-NLS-1$ //$NON-NLS-2$
	}
}