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

package dynamicrefactoring.integration;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "dynamicrefactoring.integration.messages"; //$NON-NLS-1$
	public static String CodeRegenerator_CreatingStrategy;
	public static String CodeRegenerator_CreatingVisitor;
	public static String CodeRegenerator_Formatting;
	public static String CodeRegenerator_NotRefreshed;
	public static String CodeRegenerator_ObtainingName;
	public static String CodeRegenerator_ObtainingStream;
	public static String CodeRegenerator_ProcessingFiles;
	public static String CodeRegenerator_Regenerating;
	public static String CodeRegenerator_SettingContents;
	public static String CodeRegenerator_SettingFile;
	public static String CodeRegenerator_Traversing;
	public static String CodeRegenerator_UserCancelled;
	public static String ModelGenerator_BeginningGeneration;
	public static String ModelGenerator_CreatingLoader;
	public static String ModelGenerator_Generating;
	public static String ModelGenerator_Libraries;
	public static String ModelGenerator_LoadingFiles;
	public static String ModelGenerator_LoadingSource;
	public static String ModelGenerator_NotGenerated;
	public static String ModelGenerator_NotLoaded;
	public static String ModelGenerator_Resetting;
	public static String ModelGenerator_Saving;
	public static String ModelGenerator_UserCancelled;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
