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
import java.util.Iterator;
import java.util.Vector;

import moon.core.instruction.CompoundInstr;
import moon.core.instruction.Instr;
import refactoring.engine.Function;

/**
 * Permite obtener las instrucciones simples que componen una instrucción
 * compuesta.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class SimpleInstructionCollector extends Function {
	
	/**
	 * Instrucción compuesta cuyas componentes se desea obtener.
	 */
	private CompoundInstr compoundInstr;
	
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia de SimpleInstructionCollector.
	 *
	 * @param compound la instrucción compuesta cuyas componentes se desean 
	 * obtener.
	 */
	public SimpleInstructionCollector(CompoundInstr compound) {
		super();
		this.compoundInstr = compound;
	}

	/**
	 * Sin implementación.
	 *
	 * @return null.
	 */
	public Object getValue() {
		return null;
	}
	
	/**
	 * Obtiene el conjunto de instrucciones simples (no compuestas) que componen
	 * una instrucción compuesta.
	 *
	 * @return el conjunto de instrucciones simples (no compuestas) que componen 
	 * una instrucción compuesta.
	 */
	public Collection<Instr> getCollection() {
		Instr instr;
		
		Collection<Instr> instructions = new Vector<Instr>(10,1);
		
		Iterator<Instr> instrIterator = compoundInstr.getInstructions().iterator();
		
		while(instrIterator.hasNext()){			
			instr = (Instr)instrIterator.next();
			
			if(! (instr instanceof CompoundInstr))
				instructions.add(instr);			
			else {
				SimpleInstructionCollector collector = 
					new SimpleInstructionCollector((CompoundInstr) instr);
				instructions.addAll(collector.getCollection());
			}
		}
				
		return instructions;
	}
}