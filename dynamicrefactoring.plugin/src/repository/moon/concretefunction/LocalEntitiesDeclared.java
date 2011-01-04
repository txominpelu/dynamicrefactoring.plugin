package repository.moon.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import moon.core.classdef.LocalDec;
import moon.core.instruction.CodeFragment;
import refactoring.engine.Function;

/**
 * Gets the set of entities declared in an instruction set.
 * 
 * @author Raúl Marticorena
 * @since JavaMoon-2.3.0
 * @see repository.moon.concretefunction.LocalWrittenEntitiesAccessed
 * @see repository.moon.concretefunction.LocalReadEntitiesAccessed
 */
public class LocalEntitiesDeclared extends Function {
	/**
	 * Instr.
	 */
	private CodeFragment codeFragment;

	/**
	 * Constructor.
	 * 
	 * @param classDef class 
	 */
	public LocalEntitiesDeclared(CodeFragment codeFragment) {
		this.codeFragment = codeFragment;
	}

	/**
	 * Gets the set of entities of the class, including inheritance.
	 * 
	 * @return entities
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection getCollection() {		
		
		int begin = (int) codeFragment.getLine();
		int end = codeFragment.getEndLine();
		
		List<LocalDec> listLocalDec = codeFragment.getMethDec().getLocalDecs(); 
		List<LocalDec> newList = new ArrayList<LocalDec>();
		for(LocalDec ld : listLocalDec){
			
			if (ld.getLine()>=begin && ld.getLine()<=end){
				newList.add(ld);
			}
		}
		
		return newList;
		
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}


}
 