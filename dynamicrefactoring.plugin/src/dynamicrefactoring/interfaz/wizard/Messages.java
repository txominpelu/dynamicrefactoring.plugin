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

package dynamicrefactoring.interfaz.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "dynamicrefactoring.interfaz.wizard.messages"; //$NON-NLS-1$
	public static String ExportWizard_0Exported;
	public static String ExportWizard_1Exported;
	public static String ExportWizard_AvailableRefactorings;
	public static String ExportWizard_AvailableTooltip;
	public static String ImportPlanWizard_And;
	public static String ImportPlanWizard_ProblemExecuting;
	public static String ImportPlanWizard_SameUnit;
	public static String ImportWizard_ClassesNotFound;
	public static String ExportWizard_Error;
	public static String ExportWizard_ErrorExporting;
	public static String ExportWizard_Export;
	public static String ExportWizard_ExportDone;
	public static String ExportWizard_Exporting;
	public static String ExportWizard_ExportingRefactoring;
	public static String ExportWizard_ExportRefactorings;
	public static String ExportWizard_NotAllExported;
	public static String ExportWizard_OutputFolder;
	public static String ExportWizard_SelectOutput;
	public static String ExportWizard_SelectOutputFolder;
	public static String ExportWizard_SeveralExported;
	public static String FolderSelectionListener_SelectFolder;
	public static String ImportWizard_0Imported;
	public static String ImportWizard_0WereImproted;
	public static String ImportWizard_1Imported;
	public static String ImportWizard_1WereImproted;
	public static String ImportWizard_Error;
	public static String ImportWizard_ErrorBuilding;
	public static String ImportWizard_ErrorImporting;
	public static String ImportWizard_ErrorLooking;
	public static String ImportWizard_ErrorWhileLooking;
	public static String ImportWizard_FolderCannotBeAccessed;
	public static String ImportWizard_FoundDiscarded;
	public static String ImportWizard_FoundRefactorings;
	public static String ImportWizard_Import;
	public static String ImportWizard_ImportDone;
	public static String ImportWizard_Importing;
	public static String ImportWizard_ImportingFile;
	public static String ImportWizard_ImportRefactorings;
	public static String ImportWizard_InputFolder;
	public static String ImportWizard_Looking;
	public static String ImportWizard_MakeSureSameName;
	public static String ImportWizard_Name;
	public static String ImportWizard_NamesAlreadyExist;
	public static String ImportWizard_NoneFound;
	public static String ImportWizard_NotAllImported;
	public static String ImportWizard_NotCopied;
	public static String ImportWizard_NumberFound;
	public static String ImportWizard_Recursive;
	public static String ImportWizard_SelectImportFolder;
	public static String ImportWizard_SelectInputFolder;
	public static String ImportWizard_SelectRecursive;
	public static String ImportWizard_SelectRefactorings;
	public static String ImportWizard_SeveralImported;
	public static String ImportWizard_SeveralWereImproted;
	public static String ImportWizard_Traversing;
	public static String ImportWizard_Validating;
	public static String ImportWizard_ValidatingFile;
	public static String ImportWizard_WillOverwrite;
	public static String MainInputValidator_ErrorLoading;
	public static String RefactoringWizard_Completed;
	public static String RefactoringWizard_CreatedLower;
	public static String RefactoringWizard_Creation;
	public static String RefactoringWizard_CreationInterrupted;
	public static String RefactoringWizard_DirectoryNotCreated;
	public static String RefactoringWizard_Edition;
	public static String RefactoringWizard_Error;
	public static String RefactoringWizard_ErrorMessage;
	public static String RefactoringWizard_FileNotFound;
	public static String RefactoringWizard_MakeSureNotExists;
	public static String RefactoringWizard_ModifiedLower;
	public static String RefactoringWizard_NotSaved;
	public static String RefactoringWizard_RefactoringSuccessfully;
	public static String RefactoringWizard_WizardTitle;
	public static String RefactoringWizardPage1_Description;
	public static String RefactoringWizardPage1_DescriptionNeeded;
	public static String RefactoringWizardPage1_FillInName;
	public static String RefactoringWizardPage1_GiveDescription;
	public static String RefactoringWizardPage1_GiveMotivation;
	public static String RefactoringWizardPage1_Image;
	public static String RefactoringWizardPage1_Motivation;
	public static String RefactoringWizardPage1_MotivationNeeded;
	public static String RefactoringWizardPage1_Name;
	public static String RefactoringWizardPage1_NameNeeded;
	public static String RefactoringWizardPage1_SelectImage;
	public static String RefactoringWizardPage1_SelectRefactoringImage;
	public static String RefactoringWizardPage1_Step;
	public static String RefactoringWizardPage2_DescriptionInput;
	public static String RefactoringWizardPage2_DynamicRefactoring;
	public static String RefactoringWizardPage2_Error;
	public static String RefactoringWizardPage2_FillInName;
	public static String RefactoringWizardPage2_From;
	public static String RefactoringWizardPage2_InputFromInput;
	public static String RefactoringWizardPage2_Inputs;
	public static String RefactoringWizardPage2_InputsReferToNone;
	public static String RefactoringWizardPage2_ListNotLoaded;
	public static String RefactoringWizardPage2_Main;
	public static String RefactoringWizardPage2_MainCannotBeObtained;
	public static String RefactoringWizardPage2_MainMustConform;
	public static String RefactoringWizardPage2_MainNeeded;
	public static String RefactoringWizardPage2_Method;
	public static String RefactoringWizardPage2_MethodNeeded;
	public static String RefactoringWizardPage2_ModelName;
	public static String RefactoringWizardPage2_ModelRequired;
	public static String RefactoringWizardPage2_Name;
	public static String RefactoringWizardPage2_NameNeeded;
	public static String RefactoringWizardPage2_NameRepeated;
	public static String RefactoringWizardPage2_OnlyOneMain;
	public static String RefactoringWizardPage2_Parameters;
	public static String RefactoringWizardPage2_SelectFromInput;
	public static String RefactoringWizardPage2_SelectMainBox;
	public static String RefactoringWizardPage2_SelectMethod;
	public static String RefactoringWizardPage2_SelectTypes;
	public static String RefactoringWizardPage2_Step;
	public static String RefactoringWizardPage2_Types;
	public static String RefactoringWizardPage2_Warning;
	public static String RefactoringWizardPage4_Actions;
	public static String RefactoringWizardPage3_DescriptionInputElements;
	public static String RefactoringWizardPage3_DynamicRefactoring;
	public static String RefactoringWizardPage3_ElementsNotLoaded;
	public static String RefactoringWizardPage3_Error;
	public static String RefactoringWizardPage3_Postconditions;
	public static String RefactoringWizardPage3_Preconditions;
	public static String RefactoringWizardPage3_Step;
	public static String RefactoringWizardPage6_AddExamples;
	public static String RefactoringWizardPage6_AfterRefactoring;
	public static String RefactoringWizardPage6_BeforeRefactoring;
	public static String RefactoringWizardPage6_DescriptionInputElementsExamples;
	public static String RefactoringWizardPage5_DescriptionInputPostconditions;
	public static String RefactoringWizardPage4_DescriptionInputActions;
	public static String RefactoringWizardPage4_DynamicRefactoring;
	public static String RefactoringWizardPage6_Example1;
	public static String RefactoringWizardPage6_Example2;
	public static String RefactoringWizardPage6_FirstIncomplete;
	public static String RefactoringWizardPage6_JavaFile;
	public static String RefactoringWizardPage6_MustBeType;
	public static String RefactoringWizardPage6_SecondIncomplete;
	public static String RefactoringWizardPage6_SelectFile;
	public static String RefactoringWizardPage4_Step;
	public static String RefactoringWizardPage6_TextFile;
	public static String RefactoringWizardPage7_Actions;
	public static String RefactoringWizardPage7_Confirmation;
	public static String RefactoringWizardPage7_Description;
	public static String RefactoringWizardPage5_DynamicRefactoring;
	public static String RefactoringWizardPage7_From;
	public static String RefactoringWizardPage7_Inputs;
	public static String RefactoringWizardPage7_Main;
	public static String RefactoringWizardPage7_MainTooltip;
	public static String RefactoringWizardPage7_Mechanism;
	public static String RefactoringWizardPage7_Motivation;
	public static String RefactoringWizardPage7_Name;
	public static String RefactoringWizardPage7_Postconditions;
	public static String RefactoringWizardPage7_Preconditions;
	public static String RefactoringWizardPage5_Step;
	public static String RefactoringWizardPage7_Summary;
	public static String RefactoringWizardPage7_Type;
	public static String RepositoryElementTab_AmbiguousParameters;
	public static String RepositoryElementTab_ClassNotFound;
	public static String RepositoryElementTab_Error;
	public static String RepositoryElementTab_Parameter;
	public static String RepositoryElementTab_Value;
	public static String RepositoryElementTab_Type;
	public static String RepositoryElementTab_Qualified;
	public static String RepositoryElementTab_ParametersMissing;
	public static String RepositoryElementTab_SelectElements;
	public static String RefactoringWizardPage2_NotInformationAvailable;
	public static String RefactoringWizardPage2_Search;
	public static String RefactoringWizardPage6_DynamicRefactoring;
	public static String RefactoringWizardPage7_DynamicRefactoring;
	public static String RefactoringWizardPage6_Step;
	public static String RefactoringWizardPage7_Step;
	public static String ExportPlanWizard_ExportFail;
	public static String ExportWizard_Successfully;
	public static String ExportPlanWizard_ErrorExporting;
	public static String ExportPlanWizard_Exporting;
	public static String ExportPlanWizard_ExportRefactoringPlan;
	public static String ImportPlanWizard_ImportedExecuted;
	public static String ImportPlanWizard_Imported1Executed;
	public static String ImportPlanWizard_ImportedExecuted1;
	public static String ImportPlanWizard_Imported1Executed1;
	public static String ImportPlanWizard_SelectImportFolder;
	public static String ImportPlanWizard_PlanRefactorings;
	public static String ImportPlanWizard_Name;
	public static String ImportPlanWizard_Execute;
	public static String ImportPlanWizard_Import;
	public static String ImportPlanWizard_ErrorBuilding;
	public static String ImportPlanWizard_Overwritten;
	public static String ImportPlanWizard_ImportRefactoringPlan;
	public static String ImportPlanWizard_InitialAdvise;
	public static String ImportPlanWizard_FolderCorrupted;
	public static String ImportPlanWizard_NotRefactoringPlanFolder;
	public static String ImportPlanWizard_ExecutingFile;
	public static String ImportPlanWizard_Refactoring;
	public static String ImportPlanWizard_Problems;
	public static String ImportPlanWizard_Imported;
	public static String ImportPlanWizard_Executed;
	public static String ImportPlanWizard_ProblemsExecuting;
	public static String InformationDialog_details;
	public static String ImportPlanWizard_0WereExecuted;
	public static String ImportPlanWizard_1WereExecuted;
	public static String ImportPlanWizard_SeveralWereExecuted;
	
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
