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

package dynamicrefactoring.util.processor;

import org.eclipse.jdt.core.IType;

/**
 * Proporciona funciones que permiten manejar un tipo Java tal y como lo
 * define Eclipse en su representación interna.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class JavaClassProcessor extends JavaElementProcessor {
	
	/**
	 * La clase Java que se debe procesar.
	 */
	private IType type;
	
	/**
	 * Constructor.
	 * 
	 * @param type tipo de la clase que se debe procesar.
	 */
	public JavaClassProcessor(IType type){
		super(type);
		this.type = type;
	}
	
	/** 
	 * Obtiene el nombre único de la clase Java dentro de la estructura del 
	 * espacio de nombres al que pertenece.
	 * 
	 * @return el nombre único de la clase Java dentro de la estructura del
	 * espacio de nombres al que pertenece.
	 */
	@Override
	public String getUniqueName(){
		String uniqueName = ""; //$NON-NLS-1$
		if (type.getPackageFragment().getElementName().equals("")) //$NON-NLS-1$
			uniqueName += "<anonymous>."; //$NON-NLS-1$
		return uniqueName + type.getFullyQualifiedName();
	}

}
