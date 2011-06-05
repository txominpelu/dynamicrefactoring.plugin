package dynamicrefactoring.domain.metadata.condition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;


public final class CategoryConditionTest {
	
	private CategoryCondition<Element> condition;
	private Category category;

	@Before
	public void setUp(){
		category = new Category("myparent" , "mycategory");
		condition = new CategoryCondition<Element>(category);
	}
	
	@Test
	public void testConditionTrue(){
		Element element = new ElementAdapter(){

			@Override
			public boolean belongsTo(Category category) {
				return true;
			}
			
		};
		assertTrue(condition.apply(element));
	}
	
	@Test
	public void testConditionFalse(){
		
		Element element = new ElementAdapter(){

			@Override
			public boolean belongsTo(Category category) {
				return false;
			}
			
		};
		assertFalse(condition.apply(element));
	}
	
	@Test
	public void testEqualsTrue(){
		assertEquals(new CategoryCondition<Element>(category), condition);
	}
	
	@Test
	public void testHashCode(){
		HashSet<Predicate<Element>> set = new HashSet<Predicate<Element>>();
		set.add(condition);
		assertTrue(set.contains(new CategoryCondition<Element>(category)));
	}


}
