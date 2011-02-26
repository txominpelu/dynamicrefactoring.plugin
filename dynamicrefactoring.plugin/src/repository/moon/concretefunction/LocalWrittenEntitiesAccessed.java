package repository.moon.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javamoon.core.classdef.JavaType;
import javamoon.core.classdef.primitivetypes.JavaPrimitiveType;
import javamoon.core.expression.JavaCallExpr;
import javamoon.core.expression.operation.JavaOperationInfix;
import javamoon.core.expression.operation.JavaOperationPostfix;
import javamoon.core.expression.operation.JavaOperationPrefix;
import javamoon.core.instruction.JavaFalseAssignmentInstr;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.LocalDec;
import moon.core.entity.Entity;
import moon.core.expression.CallExpr;
import moon.core.expression.Expr;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CallInstr;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.CreationInstr;
import moon.core.instruction.Instr;
import refactoring.engine.Function;

/**
 * Gets the set of entities written locally in an instruction set.
 * 
 * @author rmartico
 * @since JavaMoon-2.3.0
 */
public class LocalWrittenEntitiesAccessed extends Function {
	/**
	 * Instr.
	 */
	private List<Instr> listInstr;
	
	/**
	 * List.
	 */
	private Collection<Entity> collection;
	/**
	 * Constructor.
	 * 
	 * @param classDef class 
	 */
	public LocalWrittenEntitiesAccessed(List<Instr> instr) {
		this.listInstr = instr;
	}

	/**
	 * Gets the set of entities of the class, including inheritance.
	 * 
	 * @return entities
	 */
	@Override
	public Collection getCollection() {		
		collection = new ArrayList<Entity>();
		for (Instr instr : listInstr){
			if (instr instanceof CompoundInstr){
				this.visitCompoundInstr((CompoundInstr)instr); // composite
			}
			else{				
				this.visit(instr); //leaf;
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
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * Visits compound instructions.
	 * 
	 * @param instr compound instruction
	 */
	private void visitCompoundInstr(CompoundInstr instr){
		List<Instr> list = instr.getInstructions();
		for (Instr in : list){
			if (in instanceof CompoundInstr){
				this.visitCompoundInstr((CompoundInstr)in); // composite
			}
			else{				
				this.visit(in);	 // leaf		
			}
		}		
	}
	
	/**
	 * Visits an instruction.
	 * 
	 * @param instr instruction
	 */
	private void visit(Instr instr){
		
		if (instr instanceof AssignmentInstr){
			if (instr instanceof JavaFalseAssignmentInstr){ // FIXME
				return;
			}
			 Expr exprLeft = ((AssignmentInstr) instr).getLeftSide();
			 if (exprLeft instanceof CallExpr){
				 checkAddEntity(((CallExpr) exprLeft).getFirstElement());
			 }
			 
			 Expr exprRight = ((AssignmentInstr) instr).getRighSide();
			 if (exprRight instanceof CallExpr){
				 checkAddEntity(((CallExpr) exprRight).getFirstElement());
			 }
			 if (exprRight instanceof CallExpr){
			 List<Expr> listExpr = ((CallExpr) exprRight).getRealArguments();
				for (Expr expr : listExpr){
		
					if (expr instanceof JavaOperationPostfix){
							checkAddEntity(((JavaOperationPostfix) expr).getFirstElement()); 				
					}
					else if (expr instanceof JavaOperationPrefix){
							checkAddEntity(((JavaOperationPrefix) expr).getFirstElement());
					}
					else if (expr instanceof JavaOperationInfix){
						JavaOperationInfix joi = (JavaOperationInfix) expr;
						checkAddEntity(joi.getFirstElement());
						List<Expr> list = joi.getRealArguments();
						for (Expr expr2 : list){
							if (expr2 instanceof JavaOperationPostfix){
								checkAddEntity(((JavaOperationPostfix) expr2).getFirstElement()); 				
							}
							else if (expr2 instanceof JavaOperationPrefix){
								checkAddEntity(((JavaOperationPrefix) expr2).getFirstElement());
							}
						}
					}
				}
			 }
		}
		else if (instr instanceof CreationInstr){
			checkAddEntity(((CreationInstr) instr).getEntity());
		}
		else if (instr instanceof CallInstr){
		
			List<Expr> list = ((CallInstr) instr).getRealArguments();
			visit(list);
		}
	}
	
	
	private void visit(List<Expr> list){
		for (Expr expr : list){
			if (expr instanceof JavaCallExpr){
				JavaType jt = (JavaType) ((JavaCallExpr) expr).getFirstElement().getType();
				if (jt instanceof JavaPrimitiveType){
					checkAddEntity(((JavaCallExpr) expr).getFirstElement());
				}				
				List<Expr> listArguments = 	((JavaCallExpr) expr).getRealArguments();
				visit(listArguments);
			}
		}
	}

	/**
	 * Checks and adds, if the entity should be added to result set.
	 * 
	 * @param entity entity
	 */
	private void checkAddEntity(Entity entity) {
		
		if (entity instanceof LocalDec || entity instanceof FormalArgument){
			if (!collection.contains(entity)){
				collection.add(entity);
			}
		}
	}	
}
 