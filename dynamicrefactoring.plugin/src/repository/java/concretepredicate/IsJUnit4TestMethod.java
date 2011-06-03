package repository.java.concretepredicate;

import java.util.List;

import javamoon.core.entity.JavaAnnotationReference;
import javamoon.core.entity.JavaRoutineDec;
import moon.core.classdef.MethDec;
import refactoring.engine.Predicate;

/**
 * Check if is a test method in JUnit4. It contains an annotation
 * @Test.
 * 
 * @author Raúl Marticorena
 *
 */
public class IsJUnit4TestMethod extends Predicate{

	/**
	 * Method.
	 */
	private MethDec methDec;
	
	/**
	 * Constructor.
	 * 
	 * @param instr instruction
	 */
	public IsJUnit4TestMethod(MethDec methDec){
		super("IsJUnit4TestMethod");
		this.methDec = methDec;
	}
	
	/**
	 * Checks if contains a fail instruction.
	 */
	@Override
	public boolean isValid() {		

		if (methDec instanceof JavaRoutineDec){
			List<JavaAnnotationReference> list = ((JavaRoutineDec) methDec).getAnnotations();
			for (JavaAnnotationReference jar : list){
				
				if (jar.getType().getUniqueName().toString().equals("org.junit.Test")){
					return true;
				}
			}
			return false;
		}
		else{
			return false;
		}
	}
	


}
