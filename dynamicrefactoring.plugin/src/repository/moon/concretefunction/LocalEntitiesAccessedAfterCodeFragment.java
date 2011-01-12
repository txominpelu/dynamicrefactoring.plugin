package repository.moon.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javamoon.core.instruction.JavaCodeFragment;
import moon.core.classdef.MethDec;
import moon.core.entity.Entity;
import moon.core.instruction.CodeFragment;
import moon.core.instruction.Instr;
import refactoring.engine.Function;

/**
 * Gets the set of entities accesed locally in an instruction set.
 * 
 * @author rmartico
 * @since JavaMoon-2.3.0
 */
public class LocalEntitiesAccessedAfterCodeFragment extends Function {
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
	public LocalEntitiesAccessedAfterCodeFragment(CodeFragment codeFragment) {
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
		long lastLine =  newBodyMethod.get(newBodyMethod.size()-1).getLine() + methDec.getLine();
		
		
		CodeFragment codeAfterCodeFragment = new JavaCodeFragment(codeFragment.getEndLine()+1,0,(int)lastLine,0,
													codeFragment.getClassDef(),"");
		
		List<Instr> codeAfter = codeAfterCodeFragment.getFlattenedInstructionsInMethod();
		if (codeAfter!=null && codeAfter.size()>0){
						
			Function functionBefore = new LocalEntitiesAccessed(codeFragment.getFlattenedInstructionsInMethod());
			List<Entity> candidateBefore = (List<Entity>) functionBefore.getCollection();
			
			
			Function functionAfter = new LocalReadEntitiesAccessed(codeAfter);
			List<Entity> candidateAfter = (List<Entity>) functionAfter.getCollection();
			
			
			Function functionLocalDeclared = new LocalEntitiesDeclared(codeFragment);
			List<Entity> localDeclared = (List<Entity>) functionLocalDeclared.getCollection();
			
			
			// intersection of sets...
			for (Entity entityR : candidateAfter){
				for (Entity entityRW : candidateBefore){
					if (entityR == entityRW){
						
						if (!entityR.getType().isReference() ||
								localDeclared.contains(entityR)){
						
							collection.add(entityR);
						}
					}
				}
			}
		}
		return collection;
		
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
 