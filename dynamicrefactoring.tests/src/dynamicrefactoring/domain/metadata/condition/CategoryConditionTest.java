package dynamicrefactoring.domain.metadata.condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

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

			@Override
			public Set<Category> getCategories() {
				return null;
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

			@Override
			public Set<Category> getCategories() {
				return null;
			}
			
		};
		assertFalse(condition.apply(element));
	}


}
