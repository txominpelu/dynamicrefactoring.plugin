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


/**
 * Excepción lanzada cuando se produce un error al leer el fichero con la
 * refactorización.
 * 
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
@SuppressWarnings({"serial" }) //$NON-NLS-1$
public class XMLRefactoringReaderException extends Exception {

	/**
	 * Constructor.
	 */
	public XMLRefactoringReaderException() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param s el mensaje asociado al origen de la excepcion
	 */
	public XMLRefactoringReaderException(String s) {
		super(s);
	}

	/**
	 * Constructor.
	 * 
	 * @param exception la excepcion causa de la actual
	 */
	public XMLRefactoringReaderException(Exception exception) {
		super(exception);
	}
}