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

package dynamicrefactoring.integration.selectionhandler;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "dynamicrefactoring.integration.selectionhandler.messages"; //$NON-NLS-1$
	public static String FormalArgumentSelectionHandler_FormalArgumentExpected;
	public static String FormalArgumentSelectionHandler_InvalidSelection;
	public static String FormalArgumentSelectionHandler_TextExpected;
	public static String FormalParameterSelectionHandler_FormalParameterExpected;
	public static String FormalParameterSelectionHandler_InvalidSelection;
	public static String FormalParameterSelectionHandler_TextExpected;
	public static String TextClassSelectionHandler_ClassExpected;
	public static String TextClassSelectionHandler_InvalidSelection;
	public static String TextCodeFragmentSelectionHandler_InvalidSelection;
	public static String TextFieldSelectionHandler_FieldExpected;
	public static String TextFieldSelectionHandler_InvalidSelection;
	public static String TextMethodSelectionHandler_InvalidSelection;
	public static String TextMethodSelectionHandler_MethodSelection;
	public static String TreeClassSelectionHandler_ClassExpected;
	public static String TreeClassSelectionHandler_InvalidSelection;
	public static String TreeFieldSelectionHandler_FieldExpected;
	public static String TreeFieldSelectionHandler_InvalidSelection;
	public static String TreeMethodSelectionHandler_InvalidSelection;
	public static String TreeMethodSelectionHandler_MethodExpected;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
