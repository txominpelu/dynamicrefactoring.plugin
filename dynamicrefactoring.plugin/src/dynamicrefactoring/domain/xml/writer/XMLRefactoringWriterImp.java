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

package dynamicrefactoring.domain.xml.writer;

import java.io.File;

import org.jdom.Document;

/**
 * Define una interfaz para los escritores de ficheros XML donde se definen
 * refactorizaciones.
 * 
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Pe�a Fern�ndez</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public interface XMLRefactoringWriterImp {

	/**
	 * Escribe el fichero XML a partir de la definici�n de la refactorizaci�n.
	 * 
	 * @param dir
	 *            directorio donde se guardar� el fichero.
	 * 
	 * @throws XMLRefactoringWriterException
	 *             si se produce un error durante la escritura de la
	 *             refactorizaci�n en el fichero.
	 */
	public void writeRefactoring(File dir) throws XMLRefactoringWriterException;

	/**
	 * Obtiene una representacion en formato de un documento JDOM de la
	 * refactorizacion.
	 * 
	 * @return documento de jdom con la representacion de la refactorizacion
	 */
	public Document getDocumentOfRefactoring();

}