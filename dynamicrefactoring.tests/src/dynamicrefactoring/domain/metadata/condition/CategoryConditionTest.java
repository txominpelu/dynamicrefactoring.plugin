package dynamicrefactoring.domain.metadata.condition;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;


public class CategoryConditionTest {
	
	private CategoryCondition condition;
	private Category category;

	@Before
	public void setUp(){
		category = new Category("mycategory");
		condition = new CategoryCondition<Element>(category);
	}
	
	@Test
	public void testConditionTrue(){
		Element element = new Element(){

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return "";
			}

			@Override
			public boolean belongsTo(Category category) {
				return true;
			}
			
		};
		assertTrue(condition.apply(element));
	}
	
	@Test
	public void testConditionFalse(){
		Element element = new Element(){

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return "";
			}

			@Override
			public boolean belongsTo(Category category) {
				return false;
			}
			
		};
		assertFalse(condition.apply(element));
	}


}
