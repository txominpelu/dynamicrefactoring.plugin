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

package repository.moon.concretefunction;

import java.util.Collection;
import java.util.List;

import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import refactoring.engine.Function;

/**
 * Permite buscar un m�todo determinado en una clase bas�ndose en el nombre �nico
 * de aqu�l.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class MethodRetriever extends Function {
	
	/**
	 * Clase en la que se buscar� el m�todo.
	 */
	private ClassDef classDef;
	
	/**
	 * Nombre �nico del m�todo buscado.
	 */
	private String methodUniqueName;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MethodRetriever.
	 *
	 * @param classDef la clase en la que se buscar� el m�todo.
	 * @param methodUniqueName el nombre �nico del m�todo buscado.
	 */
	public MethodRetriever(ClassDef classDef, String methodUniqueName) {
		super();
		this.classDef = classDef;
		this.methodUniqueName = methodUniqueName;
	}

	/**
	 * Obtiene el m�todo de la clase {@link #classDef} cuyo nombre �nico se 
	 * corresponde con {@link #methodUniqueName}.
	 *
	 * @return si lo encuentra, el m�todo de la clase {@link #classDef} cuyo 
	 * nombre �nico se corresponde con {@link #methodUniqueName}. Si no, <code>null
	 * </code>.
	 */
	public Object getValue() {
		List<MethDec> classMethods = classDef.getMethDec();

		for(MethDec tmpMethod : classMethods)
			if(tmpMethod.getUniqueName().toString().equals(methodUniqueName))
				return tmpMethod;
		return null;
	}
	
	/**
	 * Sin implementaci�n.
	 *
	 * @return null.
	 */
	public Collection<MethDec> getCollection() {
		return null;
	}
}