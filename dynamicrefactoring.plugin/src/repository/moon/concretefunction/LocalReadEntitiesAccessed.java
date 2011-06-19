package repository.moon.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
 * Gets the set of entities read locally in an instruction set.
 * 
 * @author rmartico
 * @since JavaMoon-2.3.0
 */
public class LocalReadEntitiesAccessed extends Function {
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
	public LocalReadEntitiesAccessed(List<Instr> instr) {
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
			/*
			 Expr exprLeft = ((AssignmentInstr) instr).getLeftSide();
			if (exprLeft instanceof CallExpr) {
				if (((CallExpr) exprLeft).getLeftSide()!=null){
					visit((CallExpr) ((CallExpr) exprLeft).getLeftSide());
				}
				checkAddEntity(((CallExpr) exprLeft).getFirstElement());
				// visit arguments...
				List<Expr> listArguments = ((CallExpr) exprLeft)
						.getRealArguments();
				for (Expr expr2 : listArguments) {
					if (expr2 instanceof CallExpr) {
						visit((CallExpr) expr2);
					}
				}
			}*/
			 
			 Expr expr = ((AssignmentInstr) instr).getRighSide();
			 if (expr instanceof CallExpr){
				 if (((CallExpr) expr).getLeftSide()!=null){
						visit((CallExpr) ((CallExpr) expr).getLeftSide());
					}
				 visit((CallExpr) expr);
				// visit arguments...
				List<Expr> listArguments = ((CallExpr)expr).getRealArguments();
				for (Expr expr2 : listArguments){
					if (expr2 instanceof CallExpr){
						visit((CallExpr)expr2);
					}
				}
			 }
		}
		else if (instr instanceof CreationInstr){
			checkAddEntity(((CreationInstr) instr).getEntity());
		}
		else if (instr instanceof CallInstr){
			CallInstr callInstr = ((CallInstr) instr);
			System.out.println("instr: " + callInstr.toString());
			visit(callInstr.getLeftSide());
			List<Expr> listExpr = callInstr.getRealArguments();
			for (Expr expr : listExpr){
				if (expr instanceof CallExpr){
					// visit expression
					visit((CallExpr)expr);
					// visit arguments...
					List<Expr> listArguments = ((CallExpr)expr).getRealArguments();
					for (Expr expr2 : listArguments){
						if (expr2 instanceof CallExpr){
							visit((CallExpr)expr2);
						}
					}					
				}
			}
		}
	}
	
	/**
	 * Visits the expression.
	 * 
	 * @param cel1 expression
	 */
	private void visit(CallExpr cel1){
		if (cel1==null){
			return;
		}
		System.out.println("cel1:" + cel1.toString());
		if (cel1.getLeftSide()!=null){
			if (cel1.getLeftSide() instanceof CallExpr){
				visit((CallExpr) cel1.getLeftSide());
			}
		}
		 checkAddEntity(cel1.getFirstElement());
		 for (Expr expr : cel1.getRealArguments()){
			 if (expr instanceof CallExpr){
				 visit((CallExpr)expr);
			 }
		 }
	}

	/**
	 * Checks and adds, if the entity should be added to result set.
	 * 
	 * @param entity entity
	 */
	private void checkAddEntity(Entity entity) {
		System.out.println("ñAñadir ... " + entity.getName().toString() + "?");
		System.out.println(entity.getClass().toString());
		
		if (entity instanceof LocalDec || entity instanceof FormalArgument){
			if (!collection.contains(entity)){
				collection.add(entity);
			}
		}
	}	
}
 