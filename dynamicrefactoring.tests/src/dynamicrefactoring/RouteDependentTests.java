package dynamicrefactoring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import repository.java.concreterefactoring.ExtractMethodTest;
import repository.java.concreterefactoring.ExtractMethodWithGenericsTest;

import dynamicrefactoring.domain.TestExport;
import dynamicrefactoring.domain.TestImport;
import dynamicrefactoring.writer.TestAvailableRefactoringsWriter;
import dynamicrefactoring.writer.TestRefactoringPlanWriter;

@RunWith(Suite.class)

@Suite.SuiteClasses({ TestAvailableRefactoringsWriter.class,
TestImport.class,
TestExport.class, ExtractMethodTest.class,
ExtractMethodWithGenericsTest.class, TestRefactoringPlanWriter.class })
public class RouteDependentTests {

}
