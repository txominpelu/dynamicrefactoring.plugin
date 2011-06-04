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

package repository.moon.concreterefactoring;

import moon.core.Model;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import moon.core.entity.SignatureEntity;
import repository.moon.MOONRefactoring;
import repository.moon.concreteaction.RemoveFormalArg;
import repository.moon.concretepredicate.ExistsClass;
import repository.moon.concretepredicate.ExistsFormalArgInMethod;
import repository.moon.concretepredicate.ExistsMethodWithNameInClass;
import repository.moon.concretepredicate.HasNotFormalArgWithName;
import repository.moon.concretepredicate.MethodIsNotInSubNorSuperclass;
import repository.moon.concretepredicate.NotExistsMethodWithNameInClass;
import repository.moon.concretepredicate.SignatureEntityIsNotUsedInMethod;

/**
 * Permite eliminar un determinado parámetro de la signatura de un método.<p>
 *
 * Verifica que exista un parámetro en el método con el nombre indicado y
 * que no exista ya un método con la signatura que tendría el método afectado
 * una vez llevada a cabo la refactorización, ni en la propia clase, ni en
 * clases a las que se deba extender el cambio de signatura a causa de las
 * relaciones de herencia. También comprueba que el argumento formal no sea
 * utilizado en el cuerpo del propio método.<p>
 *
 * Si las comprobaciones no fallan, elimina el parámetro formal en la definición 
 * del método en la clase correspondiente. Además, elimina en todas las llamadas 
 * al método el parámetro real correspondiente al argumento formal eliminado.<p>
 *
 * Finalmente, comprueba que el proceso se ha llevado a cabo con éxito.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class RemoveParameter extends MOONRefactoring {

	/**
	 * Nombre de la refactorización concreta.
	 */
	private static final String NAME = "RemoveParameter"; //$NON-NLS-1$
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RemoveParameter.
	 *
	 * @param formalArg el parámetro formal que se desea eliminar.
	 * @param model el modelo que contiene la clase afectada por el cambio.
	 */
	public RemoveParameter(FormalArgument formalArg, Model model) {
		super(NAME, model);
		
		MethDec method = formalArg.getMethDec();
		
		String oldMethodUniqueName = method.getUniqueName().toString();
		String newMethodUniqueName;
		
		int position = 0;
		int paramPosition = method.getIndexFormalArg(
			formalArg.getUniqueName().toString());
			
		for(int i = 0; i < paramPosition; i++)
			position = oldMethodUniqueName.indexOf('%', position + 1);

		position = oldMethodUniqueName.indexOf('%', position + 1);
		newMethodUniqueName = 
			oldMethodUniqueName.substring(0,position) +
			oldMethodUniqueName.substring(
				position + ('%' + formalArg.getType().toString()).length(), 
				oldMethodUniqueName.length());
						
		
		this.addPrecondition(new ExistsClass(method.getClassDef()));
		
		this.addPrecondition(
			new ExistsFormalArgInMethod(formalArg, method));
		
		this.addPrecondition(new MethodIsNotInSubNorSuperclass(
			method.getClassDef(), newMethodUniqueName));
		
		this.addPrecondition(
			new NotExistsMethodWithNameInClass(
				method.getClassDef(), model.getMoonFactory().createName(
						newMethodUniqueName)));
			
		this.addPrecondition(new SignatureEntityIsNotUsedInMethod(
			(SignatureEntity)formalArg, method));
		
				
		this.addAction(new RemoveFormalArg(formalArg, method));
		
		
		this.addPostcondition(
			new HasNotFormalArgWithName(method, formalArg.getName()));
			
		this.addPostcondition(new ExistsMethodWithNameInClass(
			method.getClassDef(), model.getMoonFactory().createName(
					newMethodUniqueName)));
	}
}