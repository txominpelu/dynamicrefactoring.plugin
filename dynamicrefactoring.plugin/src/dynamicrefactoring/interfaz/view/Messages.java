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

package dynamicrefactoring.interfaz.view;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "dynamicrefactoring.interfaz.view.messages"; //$NON-NLS-1$
	public static String HistoryView_Error;
	public static String HistoryView_ErrorOpening;
	public static String HistoryView_NameCompleted;
	public static String HistoryView_Refactoring;
	public static String HistoryView_RefactoringHistory;
	public static String HistoryView_SelectPoint;
	public static String HistoryView_Time;
	public static String HistoryView_TimeCompleted;
	public static String HistoryView_Undo;
	public static String HistoryView_ErrorUpdating;
	public static String ProgressView_Details;
	public static String ProgressView_Message;
	public static String ProgressView_RefactoringFailed;
	public static String ProgressView_RefactoringFinished;
	public static String ProgressView_RefactoringStarted;
	public static String ProgressView_RefactoringUndone;
	public static String ProgressView_Time;
	public static String AvailableRefactoringView_Title;
	public static String AvailableRefactoringView_NotRepresented;
	public static String AvailableRefactoringView_ReaderFail;
	public static String AvailableRefactoringView_Error;
	public static String AvailableRefactoringView_ErrorOccurred;
	public static String AvailableRefactoringView_SaveChanges;
	public static String RefactoringCatalogBrowserView_Title;
	public static String RefactoringCatalogBrowserView_NoneClassDescription;
	public static String RefactoringCatalogBrowserView_NotAllListed;
	public static String RefactoringCatalogBrowserView_ClassificationsNotListed;
	public static String RefactoringCatalogBrowserView_Error;
	public static String RefactoringCatalogBrowserView_SearchError;
	public static String RefactoringCatalogBrowserView_SearchWarning;
	public static String RefactoringCatalogBrowserView_SearchClassificationNotExist;
	public static String RefactoringCatalogBrowserView_SearchCategoryNotExist;
	public static String RefactoringCatalogBrowserView_SearchQuestion;
	public static String RefactoringCatalogBrowserView_SearchConditionAlreadyExist;
	public static String RefactoringCatalogBrowserView_AvailableNotListed;
	public static String RefactoringCatalogBrowserView_SelectFromClassification;
	public static String RefactoringCatalogBrowserView_Classification;
	public static String RefactoringCatalogBrowserView_Search;
	public static String RefactoringCatalogBrowserView_TextSearchToolTip;
	public static String RefactoringCatalogBrowserView_ShowFiltered;
	public static String RefactoringCatalogBrowserView_ClearAll;
	public static String RefactoringCatalogBrowserView_SelectedCol;
	public static String RefactoringCatalogBrowserView_SelectedColToolTip;
	public static String RefactoringCatalogBrowserView_NameCol;
	public static String RefactoringCatalogBrowserView_ClearCol;
	public static String RefactoringCatalogBrowserView_ClearColToolTip;
	public static String RefactoringCatalogBrowserView_ClassAction;
	public static String RefactoringCatalogBrowserView_RefAction;
	public static String RefactoringCatalogBrowserView_ClassRefAction;
	public static String RefactoringSummaryPanel_Title;
	public static String RefactoringSummaryPanel_Description;
	public static String RefactoringSummaryPanel_Motivation;
	public static String RefactoringSummaryPanel_Categories;
	public static String RefactoringSummaryPanel_KeyWords;
	public static String RefactoringSummaryPanel_Overview;
	public static String RefactoringSummaryPanel_Inputs;
	public static String RefactoringSummaryPanel_Name;
	public static String RefactoringSummaryPanel_Type;
	public static String RefactoringSummaryPanel_From;
	public static String RefactoringSummaryPanel_Main;
	public static String RefactoringSummaryPanel_MainTooltip;
	public static String RefactoringSummaryPanel_Mechanism;
	public static String RefactoringSummaryPanel_Preconditions;
	public static String RefactoringSummaryPanel_Action;
	public static String RefactoringSummaryPanel_Postconditions;
	public static String RefactoringSummaryPanel_Image;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
