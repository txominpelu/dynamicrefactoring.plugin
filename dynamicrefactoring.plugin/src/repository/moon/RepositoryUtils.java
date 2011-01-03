package repository.moon;

import javamoon.core.entity.JavaFunctionResult;
import moon.core.classdef.MethDec;
import moon.core.expression.CallExpr;

public class RepositoryUtils {

	/**
	 * Checks if the call expresion is a call to a method.
	 * 
	 * @param callexpr call expresion
	 * @param method method
	 * @return
	 */
	public static boolean isCallToMethod(CallExpr callexpr, MethDec method) {
		if (callexpr.getFirstElement() instanceof JavaFunctionResult){
			
			JavaFunctionResult jvr  = (JavaFunctionResult) callexpr.getFirstElement();
			if (jvr.getFunctionDec() == method){
				return true;
			}
		}
		return false;
	}

}
