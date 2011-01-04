package repository.moon.concretepredicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import moon.core.entity.Entity;
import moon.core.instruction.CodeFragment;
import refactoring.engine.Function;
import refactoring.engine.Predicate;
import repository.moon.concretefunction.LocalEntitiesAccessedAfterCodeFragment;
import repository.moon.concretefunction.LocalEntitiesDeclared;
import repository.moon.concretefunction.LocalEntitiesInLoopReentrance;

/**
 * Checks if there is just one return. Just one variable is written.
 * 
 * @author Raúl Marticorena
 * @since JavaMoon-2.3.0
 *
 */
public class JustOneReturn extends Predicate {

	private CodeFragment codeFragment;

	public JustOneReturn(CodeFragment codeFragment) {
		super("");
		this.codeFragment = codeFragment;
	}

	@Override
	public boolean isValid() {
		Function functionBefore = new LocalEntitiesAccessedAfterCodeFragment(codeFragment);		
		Collection col = functionBefore.getCollection();
		
		
		Function functionLoopReentrance = new LocalEntitiesInLoopReentrance(codeFragment);
		Collection col2 = functionLoopReentrance.getCollection();
		
		Function functionLocalDeclared = new LocalEntitiesDeclared(codeFragment);
		
		Iterator<Entity> it = col2.iterator();
		while(it.hasNext()){
			Entity e = it.next();
			if (!col.contains(e)){
				col.add(e);
			}
		}	
		
		List list = new ArrayList(col);
		if (list.size()>1){
			return false;
		}
		return true;		
	}	
}
