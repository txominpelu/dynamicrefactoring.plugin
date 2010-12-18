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
package dynamicrefactoring.util;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "dynamicrefactoring.util.messages"; //$NON-NLS-1$
	public static String DynamicRefactoringLister_InvalidRefactoringPath;
	public static String DynamicRefactoringLister_RefactoringDirectoryNotExists;
	public static String DynamicRefactoringLister_UserCancelled;
	public static String LogManager_ErrorConfiguring;
	public static String MOONTypeLister_FileNotExists;
	public static String PropertyManager_NotLoaded;
	public static String RepositoryElementLister_ActionsDirNotExists;
	public static String RepositoryElementLister_InvalidActionsPath;
	public static String RepositoryElementLister_InvalidElementsPath;
	public static String RepositoryElementLister_InvalidPostconditionsPath;
	public static String RepositoryElementLister_InvalidPreconditionsPath;
	public static String RepositoryElementLister_PostconditionsDirNotExists;
	public static String RepositoryElementLister_PreconditionsDirNotExists;
	public static String RepositoryElementLister_RepositoryDirNotExists;
	public static String ScopeLimitedLister_NotListed;
	public static String ScopeLimitedLister_ErrorLoading;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
