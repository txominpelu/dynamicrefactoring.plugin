package dynamicrefactoring.domain.metadata.condition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;


public final class ActionConditionTest {
	
	private ActionCondition<Element> condition;

	@Before
	public void setUp(){
		condition = new ActionCondition<Element>("MyAction");
	}
	
	@Test
	public void testConditionTrue(){
		Element element = new ElementAdapter(){

			@Override
			public boolean containsAction(String action) {
				return true;
			}
			
		};
		assertTrue(condition.apply(element));
	}
	
	@Test
	public void testConditionFalse(){
		
		Element element = new ElementAdapter(){

			@Override
			public boolean containsAction(String action) {
				return false;
			}
			
		};
		assertFalse(condition.apply(element));
	}
	
	@Test
	public void testEqualsTrue(){
		assertEquals(new ActionCondition<Element>("MyAction"), condition);
	}
	
	@Test
	public void testHashCode(){
		HashSet<Predicate<Element>> set = new HashSet<Predicate<Element>>();
		set.add(condition);
		assertTrue(set.contains(new ActionCondition<Element>("MyAction")));
	}

	

}
