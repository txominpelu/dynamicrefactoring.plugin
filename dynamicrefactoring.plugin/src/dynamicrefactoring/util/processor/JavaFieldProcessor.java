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

import java.util.Collection;
import java.util.List;

import javamoon.core.JavaModel;
import javamoon.core.JavaName;
import moon.core.classdef.AttDec;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * Proporciona funciones que permiten manejar un atributo de un tipo Java tal y
 * como lo define Eclipse en su representaci�n interna.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class JavaFieldProcessor extends JavaElementProcessor {

	/**
	 * El atributo Java que se debe procesar.
	 */
	private IField field;

	/**
	 * Constructor.
	 * 
	 * @param field
	 *            el atributo Java que se debe procesar.
	 */
	public JavaFieldProcessor(IField field) {
		super(field);
		this.field = field;
	}

	/**
	 * Obtiene el nombre �nico del atributo seg�n la convenci�n de nomenclatura
	 * �nica utilizada en el modelo MOON.
	 * 
	 * @return el nombre �nico del atributo seg�n la convenci�n de nomenclatura
	 *         �nica utilizada en el modelo MOON.
	 * @throws JavaModelException
	 */
	@Override
	public final String getUniqueName() {
		final List<AttDec> classAttributes = JavaModel.getInstance().getClassDef(new JavaName(field.getDeclaringType().getFullyQualifiedName())).getAttributes();
		Preconditions.checkArgument(!classAttributes.isEmpty(), String.format("The class %s doesn't have any attribute so the field %s cannot be found", field.getDeclaringType().getElementName(), field.getElementName()));
		Collection<AttDec> collection = Collections2.filter(
				classAttributes, new Predicate<AttDec>() {

					@Override
					public boolean apply(AttDec arg0) {
						return arg0.getName().toString()
								.equals(field.getElementName());
					}

				});
		Preconditions.checkArgument(!collection.isEmpty(), "The field %s cannot be found in %s.", field.getElementName(), field.getDeclaringType().getElementName());
		// Tomamos el primero porque no puede haber mas
		// (no puede haber mas de un atributo con el mismo nombre en una clase
		AttDec atributo = collection.iterator().next();
		return atributo.getUniqueName().toString();

	}

}
