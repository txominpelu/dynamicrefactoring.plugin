package repository.java.concreteaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javamoon.core.JavaName;
import javamoon.core.classdef.JavaType;
import javamoon.core.entity.JavaAnnotationReference;
import javamoon.core.entity.JavaFunctionDec;
import javamoon.core.entity.JavaRoutineDec;
import javamoon.core.entity.JavaThrows;
import javamoon.core.expression.JavaCallExpr;
import javamoon.core.expression.constant.JavaTypeConstant;
import moon.core.classdef.ClassType;
import moon.core.classdef.MethDec;
import moon.core.classdef.Type;
import moon.core.expression.Expr;
import refactoring.engine.Action;
import repository.java.concretefunction.CatchExceptionWithFailCollector;
import repository.java.concretefunction.CatchExceptionWithoutFailCollector;

/**
 * Adds the expected=<exception> to a test method and throws
 * of other exceptions. 
 * 
 * @author Raúl Marticorena
 *
 */
public class AddJUnit4AnnotationTestExceptionValue extends Action {

	/**
	 * Routine.
	 */
	private JavaRoutineDec jrd;
	
	/**
	 * List of exceptions.
	 */
	private List<JavaThrows> undoExceptions;
	
	/**
	 * Constructor.
	 * 
	 * @param md routine
	 */
	public AddJUnit4AnnotationTestExceptionValue(MethDec md){
		this.jrd = (JavaRoutineDec) md;
		this.undoExceptions = new ArrayList<JavaThrows>();
	}
	
	/**
	 * Runs the action.
	 *
	 */
	@Override
	public void run() {
		// FIXEM move find to a Function in the repository and rename...
		ClassType exception = (ClassType) new CatchExceptionWithoutFailCollector(jrd).getValue();
		if (exception!=null){
			List<JavaAnnotationReference> list = jrd.getAnnotations();
			for (JavaAnnotationReference jar : list){
				if (jar.getType().getUniqueName().toString().equals("org.junit.Test")){
					// obtain the "attribute" or really the function "exception" in the annotation type...
					Type type =  jar.getType();
					List<MethDec> listMethDec = type.getClassDef().getMethDecByName(new JavaName("expected"));					
					MethDec md = listMethDec.get(0);
					
					// Create expr MyException.class
					List<Expr> listExpr = new ArrayList<Expr>();
					// FIXME
					 
					Expr exprAtom = new JavaTypeConstant(exception,(int) jar.getLine(),-1, md.getClassDef());
					listExpr.add(exprAtom);
					
					// Add expr to annotation
					JavaCallExpr jcel1 = new JavaCallExpr(((JavaFunctionDec)md).getFunctionResultEntity(), listExpr);
					jar.add(jcel1);
				}
			}
		}
		List<ClassType> listExceptions = (List<ClassType>) new CatchExceptionWithFailCollector(jrd).getCollection();
		for (ClassType ct : listExceptions){
			JavaThrows jt = new JavaThrows((JavaType)ct,-1,-1);
			jrd.add(jt);
			undoExceptions.add(jt);
		}
		

	}


	/**
	 * Undo.
	 */
	@Override
	public void undo() {
		// Remove annotation value
		List<JavaAnnotationReference> list = jrd.getAnnotations();
		for (JavaAnnotationReference jar : list){
			if (jar.getType().getUniqueName().toString().equals("org.junit.Test")){
				List<JavaCallExpr> l = new ArrayList<JavaCallExpr>(jar.getValues());
				Iterator<JavaCallExpr> listExpr = l.iterator();
				// obtain reference to method "expected"
				Type type =  jar.getType();
				List<MethDec> listMethDec = type.getClassDef().getMethDecByName(new JavaName("expected"));
				MethDec md = listMethDec.get(0);
				while(listExpr.hasNext()){
					JavaCallExpr jcel1 = listExpr.next();
					if (jcel1.getFirstElement().equals(((JavaFunctionDec)md).getFunctionResultEntity())){
						// remove expr...
						jar.remove(jcel1);
					}
				} // for
			} // if
		} // for
		// Remove throws...
		for (JavaThrows jt: undoExceptions){
			jrd.remove(jt);
		}

	} // undo
}
