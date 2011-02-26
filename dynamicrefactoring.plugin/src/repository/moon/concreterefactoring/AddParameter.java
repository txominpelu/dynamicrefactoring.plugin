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
import moon.core.Name;
import moon.core.classdef.MethDec;
import moon.core.classdef.Type;
import repository.java.concreteaction.AddImportClause;
import repository.moon.MOONRefactoring;
import repository.moon.concreteaction.AddFormalArg;
import repository.moon.concretepredicate.ExistsMethodWithNameInClass;
import repository.moon.concretepredicate.HasFormalArgWithName;
import repository.moon.concretepredicate.HasNotFormalArgWithName;
import repository.moon.concretepredicate.MethodIsNotInSubNorSuperclass;
import repository.moon.concretepredicate.NotExistsLocalDecWithName;
import repository.moon.concretepredicate.NotExistsMethodWithNameInClass;

/**
 * Permite a�adir un nuevo par�metro a la signatura de un m�todo.<p>
 *
 * Verifica que no exista ya otro par�metro en el m�todo con el mismo nombre y
 * que no existen variables locales a ese m�todo con nombre igual al del nuevo 
 * par�metro. Adem�s, comprueba que no exista ya un m�todo con la signatura que 
 * tendr�a el m�todo afectado una vez llevada a cabo la refactorizaci�n, ni en 
 * la propia clase, ni en clases a las que se deba extender el cambio de 
 * signatura a causa de las relaciones de herencia.<p>
 *
 * Si la comprobaci�n no falla, a�ade el par�metro formal en la definici�n del 
 * m�todo en la clase correspondiente. Adem�s, incluye en todas las llamadas al
 * m�todo el valor por defecto para el tipo del argumento, como par�metro real.<p>
 *
 * Finalmente, comprueba que el proceso se ha llevado a cabo con �xito.<p>
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class AddParameter extends MOONRefactoring {

	/**
	 * Nombre de la refactorizaci�n concreta.
	 */
	private static final String NAME = "AddParameter"; //$NON-NLS-1$
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de AddParameter.
	 * 
	 * @param method el m�todo a cuya signatura se va a a�adir el par�metro.
	 * @param type el tipo {@link moon.core.classdef.Type} del par�metro.
	 * @param name el nombre del nuevo par�metro formal.
	 * @param model el modelo sobre el que se ejecuta la refactorizaci�n
	 */
	public AddParameter(MethDec method, Type type, Name name, Model model) {
		
		super(NAME, model);
		
		Name newMethodUniqueName = 
			method.getUniqueName().concat('%' + type.toString());
				
		this.addPrecondition(new HasNotFormalArgWithName(method, name));
		
		this.addPrecondition(new NotExistsLocalDecWithName(method, name));
			
		this.addPrecondition(new NotExistsMethodWithNameInClass(method.getClassDef(),
			newMethodUniqueName)); 
		
		this.addPrecondition(new MethodIsNotInSubNorSuperclass(
			method.getClassDef(), newMethodUniqueName.toString()));		
		
		
		this.addAction(new AddFormalArg(method, name, type));
		
		this.addAction(new AddImportClause(method.getClassDef(), type));
		
				
		this.addPostcondition(new HasFormalArgWithName(method, name));
		
		this.addPostcondition(new ExistsMethodWithNameInClass(method.getClassDef(), 
			newMethodUniqueName));
	}
}