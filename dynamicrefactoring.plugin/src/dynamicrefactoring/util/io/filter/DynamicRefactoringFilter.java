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

package dynamicrefactoring.util.io.filter;

import java.io.File;
import java.io.FilenameFilter;

import dynamicrefactoring.RefactoringConstants;

/**
 * Filtra el contenido de un directorio según la extensión de
 * los ficheros, que se debe corresponder con la utilizada por los ficheros
 * en que se definen refactorizaciones dinámicas.
 * 
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class DynamicRefactoringFilter implements FilenameFilter {
	 
	/**
	 * Determina si un fichero es filtrado o no.
	 * 
	 * @param dir directorio donde se encuentra el fichero.
	 * @param name nombre del fichero.
	 * 
	 * @return <code>true</code> si se acepta el nombre del fichero; 
	 * <code>false</code> en caso contrario.
	 */
	public boolean accept(File dir, String name) {    	
		return name.endsWith(RefactoringConstants.FILE_EXTENSION);
    }
}