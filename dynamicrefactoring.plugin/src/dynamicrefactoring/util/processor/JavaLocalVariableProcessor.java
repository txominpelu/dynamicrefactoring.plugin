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

import javamoon.core.JavaModel;
import javamoon.core.JavaName;
import moon.core.classdef.ClassDef;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;

import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import dynamicrefactoring.util.selection.SelectionInfo;

/**
 * Proporciona funciones que permiten manejar una variable local Java tal y como
 * la define Eclipse en su representación interna.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class JavaLocalVariableProcessor extends JavaElementProcessor {

	/**
	 * La variable local (o argumento formal) Java que se debe procesar.
	 */
	private ILocalVariable variable;

	/**
	 * Constructor.
	 * 
	 * @param variable
	 *            la variable local (o argumento formal) Java que se debe
	 *            procesar.
	 */
	public JavaLocalVariableProcessor(ILocalVariable variable) {
		super(variable);
		this.variable = variable;
	}

	/**
	 * Determina si una variable local de Eclipse se corresponde con un
	 * argumento formal de un método o con una variable local en si.
	 * 
	 * @return {@link SelectionInfo#FORMAL_ARGUMENT} si se trata de un argumento
	 *         formal de un método; {@link SelectionInfo#LOCAL_VARIABLE} si se
	 *         trata de una variable local de método.
	 */
	public int discernLocalVariable() {
		// El elemento padre de la variable, debería ser un método.
		if (variable.getParent() instanceof IMethod)
			try {
				String names[] = ((IMethod) variable.getParent())
						.getParameterNames();
				// Si entre los argumentos formales del método hay alguno con el
				// mismo nombre que la variable, ha de ser ésta misma.
				for (int i = 0; i < names.length; i++)
					if (names[i].equals(variable.getElementName()))
						return SelectionInfo.FORMAL_ARGUMENT;

				return SelectionInfo.LOCAL_VARIABLE;
			} catch (Exception e) {
				return 0;
			}
		return 0;
	}

	/**
	 * Obtiene un procesador de información para el método al que pertenece la
	 * variable local.
	 * 
	 * @return un procesador de información para el método al que pertenece la
	 *         variable local.
	 * 
	 * @see JavaMethodProcessor
	 */
	public JavaMethodProcessor getMethodProcessor() {
		if (variable.getParent() instanceof IMethod)
			return new JavaMethodProcessor((IMethod) variable.getParent());
		return null;
	}

	/**
	 * Name convention: namespace.classname#methodnameo#formalargumentname.
	 * 
	 * @see JavaElementProcessor#getUniqueName()
	 */
	@Override
    public String getUniqueName(){
		final IMethod iMetodo = (IMethod)variable.getParent();
    	ClassDef clase = JavaModel.getInstance().getClassDef(new JavaName(iMetodo.getDeclaringType().getFullyQualifiedName()));
    	Collection<MethDec> collection = Collections2.filter(
				
						clase.getMethDec(), new Predicate<MethDec>() {

					@Override
					public boolean apply(MethDec arg0) {
						return arg0.getName().toString()
								.equals(iMetodo.getElementName());
					}

				});
		// Tomamos el primero porque no puede haber mas
		// (no puede haber mas de un atributo con el mismo nombre en una clase
		MethDec moonMethod = collection.iterator().next();
		
		Collection<FormalArgument> collectionParameter = Collections2.filter(
				
				moonMethod.getFormalArgument(), new Predicate<FormalArgument>() {

			@Override
			public boolean apply(FormalArgument arg0) {
				return arg0.getName().toString()
						.equals(variable.getElementName());
			}

		});
		FormalArgument formalArg = collectionParameter.iterator().next();
		return formalArg.getUniqueName().toString();
    }
}