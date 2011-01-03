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

package repository;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javamoon.construct.binary.BinaryLoader;
import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import javamoon.utils.Comparator;
import javamoon.utils.EclipsePrettyPrinter;

import org.junit.After;
import org.junit.Before;

import repository.moon.MOONRefactoring;

/**
 * Proporciona el comportamiento de inicializaci�n y finalizaci�n por defecto de los
 * test ejecutados sobre el repositorio.
 * 
 * @author Ra�l Marticorena
 * 
 * @since JavaMoon-2.1.0
 */
public abstract class RefactoringTemplateAbstractTest {

	/**
	 * Cargador de c�digo fuente.
	 */
	protected static SourceLoader sourceLoader;

	/**
	 * Fichero binario JAR con las bibliotecas b�sicas de Java.
	 */
	public static final String JARFILE = "rt.jar"; //$NON-NLS-1$

	/**
	 * Fichero binario con la biblioteca JUnit.
	 */
	private static final String JUNITJARFILE = "junit-4.4.jar"; //$NON-NLS-1$
	
	/**
	 * Car�cter separador de rutas en el sistema operativo.
	 */
	public static final String SEPARATOR = File.separator;

	/**
	 * Inicializa los test.
	 * 
	 * @throws Exception si se produce un fallo durante la inicializaci�n.
	 */
	@Before
	public void setUp() throws Exception{     	

		BinaryLoader bl = new BinaryLoader();		
		bl.addClassesFromPackageInJar("java.lang", formatString("./testdata/lib/" + JARFILE)); //$NON-NLS-1$ //$NON-NLS-2$
		bl.addClassesFromPackageInJar("java.lang.annotation", formatString("./testdata/lib/" + JARFILE)); //$NON-NLS-1$ //$NON-NLS-2$
		bl.addClassesFromPackageInJar("java.util", formatString("./testdata/lib/" + JARFILE)); //$NON-NLS-1$ //$NON-NLS-2$
		bl.addClassesFromPackageInJar("java.io", formatString("./testdata/lib/" + JARFILE));		 //$NON-NLS-1$ //$NON-NLS-2$
		bl.addClassesFromPackageInJar("org.junit", formatString("./testdata/lib/" + JUNITJARFILE)); //$NON-NLS-1$ //$NON-NLS-2$
		bl.addClassesFromPackageInJar("junit.framework", formatString("./testdata/lib/" + JUNITJARFILE));    		 //$NON-NLS-1$ //$NON-NLS-2$

		bl.load();  	   	
	}    
    
    /**
     * M�todo de finalizaci�n.
     */
	@After
    public void tearDown() {
		JavaModel.getInstance().reset();
		MOONRefactoring.resetModel();
		System.gc();
	}
    
	/**
	 * Convierte una ruta al formato utilizado de forma local.
	 * 
	 * @param path la ruta original.
	 * 
	 * @return la ruta formateada.
	 * 
	 * @throws Exception si se produce un error al construir la ruta.
	 */
    protected String formatString(String path) throws Exception {
    	return new File(path).getCanonicalPath();
    }
    
    /**
     * Compares a string with file content, line to line.
     * 
     * @param source initial string
     * @param file file to read
     * @throws IOException when errors with file accesss
     */
    protected void compare(String source, String file) throws IOException{
    	final boolean WRITE = true;

		String initial = new Scanner(new File(file)).useDelimiter("" + File.separatorChar + "Z").next();
		String initialFormatted = null;
		if (WRITE) {
			initialFormatted = EclipsePrettyPrinter
					.formatCompilationUnit(initial);
		} else {
			initialFormatted = initial;
		}

		source = EclipsePrettyPrinter.formatCompilationUnit(source);

		// Uncomment if you want to see the strings...
		System.out.println(initialFormatted);
		System.out.println(source);
		
		boolean bool = Comparator.compareText(initialFormatted, source, " ");
		assertTrue("Failure comparing " + file + " with", bool);
		if (WRITE) {
			PrintWriter printWriter = new PrintWriter(new File(file));
			printWriter.write(initialFormatted);
			printWriter.close();
		}
	}
}