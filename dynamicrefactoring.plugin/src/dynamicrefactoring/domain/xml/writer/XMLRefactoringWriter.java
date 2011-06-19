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

/**
 * Permite escribir en un fichero XML una refactorización dinámica.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class XMLRefactoringWriter {

	/**
	 * El implementador utilizado por esta clase.
	 */
	private XMLRefactoringWriterImp implementor;
	
	/**
	 * Constructor.
	 * 
	 * @param implementor el implmentador utilizado por la clase para las 
	 * operaciones de escritura.
	 */
	public XMLRefactoringWriter(XMLRefactoringWriterImp implementor) {		
		this.implementor = implementor;
	}

	/**
	 * Escribe el fichero XML a partir de la definición de la refactorización.
	 * 
	 * @param dir
	 *            directorio donde se guardará el fichero.
	 * 
	 * @throws XMLRefactoringWriterException
	 *             si ocurre algun error en la escritura
	 */
	public void writeRefactoring(File dir) throws XMLRefactoringWriterException {
		implementor.writeRefactoring(dir);
	}
}