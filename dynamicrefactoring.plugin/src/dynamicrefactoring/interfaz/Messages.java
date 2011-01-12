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

package dynamicrefactoring.interfaz;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "dynamicrefactoring.interfaz.messages"; //$NON-NLS-1$
	public static String ButtonTextProvider_Accept;
	public static String ButtonTextProvider_Back;
	public static String ButtonTextProvider_Cancel;
	public static String ButtonTextProvider_Finish;
	public static String ButtonTextProvider_Next;
	public static String CustomProgressDialog_ProgressInfo;
	public static String DynamicRefactoringList_AvailableNotListed;
	public static String DynamicRefactoringList_Error;
	public static String DynamicRefactoringList_NoneFound;
	public static String DynamicRefactoringList_NotAllListed;
	public static String SelectDynamicRefactoringWindow_AvailableRefactorings;
	public static String SelectDynamicRefactoringWindow_Description;
	public static String SelectDynamicRefactoringWindow_Image;
	public static String SelectDynamicRefactoringWindow_Motivation;
	public static String SelectDynamicRefactoringWindow_RefactoringSelection;
	public static String SelectDynamicRefactoringWindow_SelectRefactoring;
	public static String SelectDynamicRefactoringWindow_Summary;
	public static String SelectForDeletingWindow_AreYouSure;
	public static String SelectForDeletingWindow_Confirmation;
	public static String SelectForDeletingWindow_DeleteCaps;
	public static String SelectForDeletingWindow_Deleted;
	public static String SelectForDeletingWindow_DeleteLower;
	public static String SelectForDeletingWindow_NotDeleted;
	public static String SelectForDeletingWindow_RefactoringDeleted;
	public static String SelectForDeletingWindow_RefactoringNotDeleted;
	public static String SelectForEditingWindow_EditCaps;
	public static String SelectForEditingWindow_EditLower;
	public static String SelectRefactoringWindow_Accept;
	public static String SelectRefactoringWindow_AddParameter;
	public static String SelectRefactoringWindow_BoundedParameterScope;
	public static String SelectRefactoringWindow_Cancel;
	public static String SelectRefactoringWindow_ClassScope;
	public static String SelectRefactoringWindow_CodeFragmentScope;
	public static String SelectRefactoringWindow_DynamicKeyword;
	public static String SelectRefactoringWindow_FieldScope;
	public static String SelectRefactoringWindow_FormalArgumentScope;
	public static String SelectRefactoringWindow_FormalParameterScope;
	public static String SelectRefactoringWindow_MethodScope;
	public static String SelectRefactoringWindow_MoveField;
	public static String SelectRefactoringWindow_MoveMethod;
	public static String SelectRefactoringWindow_RemoveParameter;
	public static String SelectRefactoringWindow_RenameClass;
	public static String SelectRefactoringWindow_RenameMethod;
	public static String SelectRefactoringWindow_RenameParameter;
	public static String SelectRefactoringWindow_ReplaceWithBoundType;
	public static String SelectRefactoringWindow_ReplaceWithType;
	public static String SelectRefactoringWindow_SelectRefactoring;
	public static String SelectRefactoringWindow_SelectRun;
	public static String SelectRefactoringWindow_SpecializeBoundF;
	public static String SelectRefactoringWindow_SpecializeBoundS;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
