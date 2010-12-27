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

package dynamicrefactoring.reader;

import java.io.File;

/**
 * Define una interfaz para las fábricas que generan instancias que implementan
 * la interfaz <code>XMLRefactoringReaderImp</code>.
 * 
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public interface XMLRefactoringReaderFactory {

	/**
	 * Devuelve una instancia de un lector de refactorizaciones en XML que
	 * implementa la interfaz <code>XMLRefactoringReaderImp</code>.
	 * 
	 * @param file el fichero del que leerá el lector de refactorizaciones en XML.
	 * 
	 * @return la instancia generada.
	 * 
	 * @throws XMLRefactoringReaderException si se produce un error al crear el
	 * lector de refactorizaciones concreto.
	 */
	public XMLRefactoringReaderImp makeXMLRefactoringReaderImp(File file)
			throws XMLRefactoringReaderException;
}