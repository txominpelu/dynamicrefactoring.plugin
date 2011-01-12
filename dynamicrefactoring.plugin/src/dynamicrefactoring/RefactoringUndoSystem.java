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

package dynamicrefactoring;

import org.apache.log4j.Logger;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;

import org.eclipse.core.runtime.NullProgressMonitor;

import org.eclipse.ui.PlatformUI;

/**
 * Permite deshacer una operación del entorno de Eclipse si se efectuó 
 * utilizando las herramientas de ejecución de operaciones aptas para ser
 * deshechas.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RefactoringUndoSystem {
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger.getLogger(RefactoringUndoSystem.class);

	/**
	 * Deshace una refactorización identificada por la etiqueta con la que debería
	 * aparecer en el histórico de operaciones que el entorno Eclipse puede
	 * deshacer.
	 * 
	 * @param id etiqueta asociada a la operación que originalmente ejecutó la
	 * refactorización.
	 * @param checkRenamings TODO
	 * 
	 * @throws Exception si se produce un error al deshacer la refactorización.
	 */
	public static void undoRefactoring(String id, boolean checkRenamings) throws Exception {
		
		// Si se ha producido algún renombrado después de la refactorización,
		// hay que deshacerlo primero. Si no, al acceder al histórico de 
		// operaciones, desaparecen las operaciones posteriores.
		if (checkRenamings){
			String renaming = RenamingRegistry.getInstance().getRenaming(id); 
			if (renaming != null)
				undoRefactoring(renaming, false);
			
		}
		
		IUndoableOperation operation = getUndoable(id);
		
		if (operation != null){
			try {
				IOperationHistory history = OperationHistoryFactory.getOperationHistory();
				history.undoOperation(operation, new NullProgressMonitor(), null);
			}
			catch (ExecutionException exception){
				String message = 
					Messages.RefactoringUndoSystem_NotUndone + ":\n\n" + exception.getMessage();  //$NON-NLS-1$
				logger.error(message);
				throw new Exception(message);
			}
		}
	}
	
	/**
	 * Obtiene una operación que se puede deshacer del entorno de operaciones 
	 * de Eclipse a partir de su etiqueta identificadora.
	 * 
	 * @param id etiqueta que identifica la operación que se puede deshacer.
	 * 
	 * @return la operación recuperada o <code>null</code> si no se encuentra
	 * ninguna operación con dicha etiqueta en el contexto global de ejecución.
	 */
	private static IUndoableOperation getUndoable(String id){
		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		IUndoContext context = PlatformUI.getWorkbench().getOperationSupport().getUndoContext();
		IUndoableOperation[] operations = history.getUndoHistory(context);
		
		for (int i = operations.length - 1; i > -1; i--){
			if (operations[i].getLabel().equals(id))
				return operations[i];
		}
		
		return null;
	}
}