package repository.moon;

import java.util.ArrayList;
import java.util.List;

import javamoon.core.classdef.primitivetypes.JavaVoidType;
import javamoon.core.entity.JavaFunctionResult;
import moon.core.classdef.MethDec;
import moon.core.expression.CallExpr;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CallInstr;
import moon.core.instruction.Instr;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class RepositoryUtils {

	/**
	 * Checks if the call expresion is a call to a method.
	 * 
	 * @param callexpr
	 *            call expresion
	 * @param method
	 *            method
	 * @return true si es una llamada a un metodo falso en caso contrario
	 */
	public static boolean isCallToVoidMethod(CallExpr callexpr, MethDec method) {
		if (callexpr.getFirstElement() instanceof JavaFunctionResult) {

			JavaFunctionResult jvr = (JavaFunctionResult) callexpr
					.getFirstElement();
			if (jvr.getType().equals(JavaVoidType.getInstance())
					&& jvr.getFunctionDec() == method) {
				return true;
			}
		}
		return false;
	}

	public static List<Instr> getAssignmentInstructionsFromMethod(
			MethDec metodo2) {
		List<Instr> instrIt = new ArrayList<Instr>(Collections2.filter(
				metodo2.getFlattenedInstructions(), new Predicate<Instr>() {
					public boolean apply(Instr input) {
						return input instanceof AssignmentInstr;
					}
				}));
		return instrIt;
	}

	public static List<Instr> getCallInstructionsFromMethod(MethDec metodo2) {
		List<Instr> instrIt = new ArrayList<Instr>(Collections2.filter(
				metodo2.getFlattenedInstructions(), new Predicate<Instr>() {
					public boolean apply(Instr input) {
						return input instanceof CallInstr;
					}
				}));
		return instrIt;
	}

}
