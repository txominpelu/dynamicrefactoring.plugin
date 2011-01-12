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

package dynamicrefactoring.interfaz.dynamic;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "dynamicrefactoring.interfaz.dynamic.messages"; //$NON-NLS-1$
	public static String DynamicRefactoringTab_Actions;
	public static String DynamicRefactoringTab_Description;
	public static String DynamicRefactoringTab_Information;
	public static String DynamicRefactoringTab_Main;
	public static String DynamicRefactoringTab_MainInput;
	public static String DynamicRefactoringTab_Motivation;
	public static String DynamicRefactoringTab_Name;
	public static String DynamicRefactoringTab_Postconditions;
	public static String DynamicRefactoringTab_Preconditions;
	public static String DynamicRefactoringTab_Type;
	public static String DynamicRefactoringWindow_ClassNotLoaded;
	public static String DynamicRefactoringWindow_Close;
	public static String DynamicRefactoringWindow_CycleFound;
	public static String DynamicRefactoringWindow_DependsOnUser;
	public static String DynamicRefactoringWindow_Error;
	public static String DynamicRefactoringWindow_ErrorBuilding;
	public static String DynamicRefactoringWindow_ErrorInvokingAccessMethod;
	public static String DynamicRefactoringWindow_ErrorInvokingMethodOnInput;
	public static String DynamicRefactoringWindow_ErrorLoadingClass;
	public static String DynamicRefactoringWindow_ErrorObtainingValues;
	public static String DynamicRefactoringWindow_FieldsMissing;
	public static String DynamicRefactoringWindow_ObjectNotComplies;
	public static String DynamicRefactoringWindow_Parameters;
	public static String DynamicRefactoringWindow_Refactoring;
	public static String DynamicRefactoringWindow_ReferencedObjectNotFound;
	public static String DynamicRefactoringWindow_ReviewDefinition;
	public static String DynamicRefactoringWindow_Root;
	public static String DynamicRefactoringWindow_Run;
	public static String DynamicRefactoringWindow_SeveralInputsMarked;
	public static String DynamicRefactoringWindow_Warning;
	public static String DynamicRefactoringWindowLauncher_Error;
	public static String DynamicRefactoringWindowLauncher_ErrorInitializing;
	public static String DynamicRefactoringWindowLauncher_Refactoring;
	public static String DynamicRefactoringWindowLauncher_RefactoringDoesNotExist;
	public static String InputProcessor_AmbiguousInput;
	public static String InputProcessor_ErrorInvoking;
	public static String InputProcessor_ObjectNotLoaded;
	public static String InputProcessor_ReviewDefinition;
	public static String InputProcessor_RuntimeClassNotLoaded;
	public static String RepositoryElementProcessor_ErrorAccessingRepository;
	public static String DynamicExamplesTab_Examples;
	public static String DynamicExamplesTab_ExamplesOf;
	public static String DynamicExamplesTab_ExampleOf;
	public static String DynamicExamplesTab_Example;
	public static String DynamicExamplesTab_Before;
	public static String DynamicExamplesTab_After;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
