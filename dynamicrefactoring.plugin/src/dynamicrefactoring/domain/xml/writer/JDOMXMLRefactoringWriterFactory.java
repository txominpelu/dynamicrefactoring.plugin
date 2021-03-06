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

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Fábrica concreta para obtener instancias que implementan la interfaz
 * <code>XMLRefactoringWriterImp</code> basíndose en JDOM para escribir los 
 * ficheros XML.
 * 
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class JDOMXMLRefactoringWriterFactory 
	implements XMLRefactoringWriterFactory {

	/**
	 * Devuelve un objeto que implementa la interfaz definida por <code>
	 * XMLRefactoringWriterImp</code> basíndose en JDOM para la escritura de 
	 * refactorizaciones en ficheros XML.
	 * 
	 * @param refactoringDefinition definición de refactorización que se debe
	 * escribir.
	 * 
	 * @return la instancia generada.
	 */
	@Override
	public XMLRefactoringWriterImp makeXMLRefactoringWriterImp(DynamicRefactoringDefinition refactoringDefinition) {

		return new JDOMXMLRefactoringWriterImp(refactoringDefinition);
	}
}