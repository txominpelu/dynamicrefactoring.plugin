/*<Dynamic Refactoring Plugin For Eclipse 2.0 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings>

Copyright (C) 2009  Laura Fuente De La Fuente

This file is part of Foobar

Foobar is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "dynamicrefactoring.domain.messages"; //$NON-NLS-1$
	public static String DynamicRefactoring_ErrorAddingAction;
	public static String DynamicRefactoring_ErrorAddingPostcondition;
	public static String DynamicRefactoring_ErrorAddingPrecondition;
	public static String DynamicRefactoring_ErrorObtainingParameters;
	public static String DynamicRefactoring_ErrorObtainingValue;
	public static String DynamicRefactoring_ErrorReading;
	public static String DynamicRefactoringDefinition_ErrorLoading;
	public static String RefactoringSummary_DateFormat;
	public static String ExportImportUtilities_ClassesNotFound;
	public static String FileNotFoundMessage0;
	public static String RefactoringPlanExecutor_ObjectNotLoaded;
	public static String RefactoringPlanExecutor_InvokeMethod;
	public static String RefactoringPlanExecutor_RefactoringNotExecuted;
	public static String RefactoringPlanExecutor_ReadingProblem;
	public static String RefactoringPlanExecutor_DefinitionProblem;
	public static String RefactoringPlanExecutor_Executing;
	public static String RefactoringPlanExecutor_ExecutingPlan;
	public static String RefactoringPlanExecutor_Regenerating;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
