package repository.moon.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javamoon.core.expression.JavaCallExpr;
import javamoon.core.expression.operation.JavaOperationInfix;
import javamoon.core.expression.operation.JavaOperationPostfix;
import javamoon.core.instruction.JavaFalseAssignmentInstr;

import moon.core.classdef.AttDec;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.LocalDec;
import moon.core.classdef.MethDec;
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
 * Gets the set of entities assigned to other entity in the class body.
 * 
 * @author rmartico
 * @since JavaMoon-2.6.0
 */
public class EntitiesAssignedToEntity extends Function {

	/**
	 * Target entity.
	 */
	private Entity target;

	/**
	 * List.
	 */
	private Collection<Expr> resultCollection;

	/**
	 * Constructor.
	 * 
	 * @param classDef
	 *            class
	 */
	public EntitiesAssignedToEntity(Entity target) {
		this.target = target;
	}

	/**
	 * Gets the set of entities of the class, including inheritance.
	 * 
	 * @return entities
	 */
	@Override
	public Collection getCollection() {
		resultCollection = new ArrayList<Expr>();
		if (target.getClassDef() != null) { // has class definition
			if (target instanceof AttDec) {
				AttDec ad = ((AttDec) target);
				List<MethDec> list = ad.getClassDef().getMethDec();
				for (MethDec md : list) {
					List<Instr> body = md.getFlattenedInstructions();
					visitInstructionsInBodyMethod(body);
				}
			} else if (target instanceof LocalDec) {
				MethDec md = ((LocalDec) target).getMethDec();
				List<Instr> body = md.getFlattenedInstructions();
				visitInstructionsInBodyMethod(body);
			} else if (target instanceof FormalArgument) {
				MethDec md = ((FormalArgument) target).getMethDec();
				List<Instr> body = md.getFlattenedInstructions();
				visitInstructionsInBodyMethod(body);
			}
		}
		return resultCollection;

	}

	private void visitInstructionsInBodyMethod(List<Instr> body) {
		for (Instr instr : body) {
			if (instr instanceof CompoundInstr) {
				this.visitCompoundInstr((CompoundInstr) instr); // composite
			} else {
				this.visit(instr); // leaf;
			}
		}
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
	 * @param instr
	 *            compound instruction
	 */
	private void visitCompoundInstr(CompoundInstr instr) {
		List<Instr> list = instr.getInstructions();
		visitInstructionsInBodyMethod(list);
	}

	/**
	 * Visits an instruction.
	 * 
	 * @param instr
	 *            instruction
	 */
	private void visit(Instr instr) {

		if (instr instanceof AssignmentInstr) {
			if (instr instanceof JavaFalseAssignmentInstr) { // FIXME
				return;
			}
			Expr exprLeft = ((AssignmentInstr) instr).getLeftSide();
			if (exprLeft instanceof CallExpr
					&& ((CallExpr) exprLeft).getFirstElement().equals(target)) {
				Expr exprRight = ((AssignmentInstr) instr).getRighSide();
				//System.err.println("Adding expression: " + exprRight.toString());
				checkAddExpr(exprRight);
			}
		}
	}

	/**
	 * Checks and adds, if the expr should be added to result set.
	 * 
	 * @param expr
	 *            expr
	 */
	private void checkAddExpr(Expr expr) {
		if (!resultCollection.contains(expr)) {
			resultCollection.add(expr);
		}
	}
}
