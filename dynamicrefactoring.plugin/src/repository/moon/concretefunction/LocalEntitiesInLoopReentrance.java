package repository.moon.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javamoon.core.DefinitionLanguage;
import moon.core.classdef.MethDec;
import moon.core.entity.Entity;
import moon.core.instruction.CodeFragment;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.Instr;
import refactoring.engine.Function;

/**
 * Gets the set of entities accesed locally in an instruction set.
 * 
 * @author rmartico
 * @since JavaMoon-2.3.0
 */
public class LocalEntitiesInLoopReentrance extends Function {
	/**
	 * Instr.
	 */
	private CodeFragment codeFragment;
	
	/**
	 * List.
	 */
	private Collection<Entity> collection;
	/**
	 * Constructor.
	 * 
	 * @param classDef class 
	 */
	public LocalEntitiesInLoopReentrance(CodeFragment codeFragment) {
		this.codeFragment = codeFragment;
		collection = new ArrayList<Entity>();	
	}

	/**
	 * Gets the set of local entities accessed after fragment.
	 * 
	 * @return entities
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection getCollection() {				

		// duplicate code. Change or move... see RemoveCodeFragment
		MethDec methDec = codeFragment.getMethDec();
		
		List<Instr> newBodyMethod = codeFragment.getMethDec().getFlattenedInstructions();		
			
		List<Instr> methodBody = methDec.getInstructions();
		for (int i = 0; i<methodBody.size();i++){
			visit(methodBody.get(i), methodBody, i);
		}		
		return collection;
		
	}
	
	private void visit(Instr instr, List<Instr> methodBody, int index){
		
		if (instr instanceof CompoundInstr){
		
			List<Instr> list = ((CompoundInstr) instr).getInstructions();
			for (int i = 0; i<list.size();i++){
				visit(list.get(i), list, i);
			}			
		}
		else if (instr.toString().equals(DefinitionLanguage.WHILE)
				|| instr.toString().equals(DefinitionLanguage.FOR)
				|| instr.toString().equals(DefinitionLanguage.DO)) {

			
			long beginLoop = instr.getLine();			
		
			
			while (!(methodBody.get(index) instanceof CompoundInstr)) {
		
				index++;
			} // while
				
			CompoundInstr ci = (CompoundInstr) methodBody.get(index);
			int begin = (int) ci.getFirstInstr().getLine();
		
			int end = (int) ci.getLastInstr().getLine();
		
			
		
			
			// if compound instruction contains the code fragment...
			if (begin + codeFragment.getMethDec().getLine() <= codeFragment.getLine()
					&& end + codeFragment.getMethDec().getLine()>= codeFragment.getEndLine()
					
						&& beginLoop + codeFragment.getMethDec().getLine() < codeFragment.getLine()
				) {
		
				
				LocalEntitiesDeclared led = new LocalEntitiesDeclared(codeFragment);
				Collection<Entity> declared = led.getCollection();
		
				
				LocalWrittenEntitiesAccessed lwea = new LocalWrittenEntitiesAccessed(
						codeFragment.getFlattenedInstructionsInMethod());
				Collection<Entity> listWritten = lwea.getCollection();
		

				for (Entity entity : listWritten) {
		
					if (!declared.contains(entity)) {
		
						collection.add(entity);
					}
				} // for
			} // if
		} // if
		
		return;
		
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public Object getValue() {
		return null;
	}
}
 