package dynamicrefactoring.domain.xml;

import java.io.File;
import java.text.MessageFormat;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.Messages;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.xml.reader.JDOMXMLRefactoringReaderFactory;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReader;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReaderFactory;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReaderImp;

public class XMLRefactoringUtils {

	/**
	 * Devuelve la definición de una refactorización a partir de un fichero.
	 * 
	 * @param refactoringFilePath
	 *            ruta al fichero que define la refactorización.
	 * 
	 * @return la definición de la refactorización descrita en el fichero.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al cargar la refactorización desde el
	 *             fichero indicado.
	 */
	public static DynamicRefactoringDefinition getRefactoringDefinition(
			String refactoringFilePath) throws RefactoringException {
	
		DynamicRefactoringDefinition definition;
		try {
			XMLRefactoringReaderFactory f = new JDOMXMLRefactoringReaderFactory();
			XMLRefactoringReaderImp implementor = f
					.makeXMLRefactoringReaderImp();
			XMLRefactoringReader temporaryReader = new XMLRefactoringReader(
					implementor);
			definition = temporaryReader
					.getDynamicRefactoringDefinition(new File(
							refactoringFilePath));
		} catch (Exception e) {
			Object[] messageArgs = { refactoringFilePath };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.DynamicRefactoringDefinition_ErrorLoading);
	
			throw new RefactoringException(formatter.format(messageArgs)
					+ ".\n" + //$NON-NLS-1$
					e.getMessage(), e);
		}
		return definition;
	}

}
