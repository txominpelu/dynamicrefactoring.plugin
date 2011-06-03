/*<Dynamic Refactoring Plugin For Eclipse 2.0 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings>

Copyright (C) 2009  Laura Fuente

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

package dynamicrefactoring;

import org.eclipse.osgi.util.NLS;

/**
 * Define las constantes que permiten la internacionalización del
 * paquete dynamicrefactoring.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class Messages extends NLS {
	

	private static final String BUNDLE_NAME = "dynamicrefactoring.messages"; //$NON-NLS-1$
	

	public static String RefactoringPlugin_ErrorInitializing;
	public static String RefactoringPlugin_ErrorNotifying;
	public static String RefactoringPlugin_ErrorRegistering;
	public static String RefactoringPlugin_InvalidElement;
	public static String RefactoringPlugin_NotStopped;
	public static String RefactoringRunner_CouldNotRun;
	public static String RefactoringRunner_DateFormat;
	public static String RefactoringRunner_ErrorRefactoring;
	public static String RefactoringRunner_Loading;
	public static String RefactoringRunner_NotCompleted;
	public static String RefactoringRunner_NotStored;
	public static String RefactoringRunner_NotUndone;
	public static String RefactoringRunner_PostconditionFailed;
	public static String RefactoringRunner_PreconditionFailed;
	public static String RefactoringRunner_ProcessCancelled;
	public static String RefactoringRunner_RefactoringNotUndone;
	public static String RefactoringRunner_Running;
	public static String RefactoringRunner_RunningRefactoring;
	public static String RefactoringRunner_Saving;
	public static String RefactoringRunner_Undoing;
	public static String RefactoringRunner_UserCancelled;
	public static String RefactoringUndoSystem_NotUndone;
	public static String RefactoringRunner_DateFormat2;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
