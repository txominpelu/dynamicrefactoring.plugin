package dynamicrefactoring.domain.metadata.condition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;


public final class TextConditionTest {
	
	private Predicate<Element> condition;

	@Before
	public void setUp(){
		condition = new TextCondition<Element>("MyCondition");
	}
	
	@Test
	public void testConditionTrue(){
		Element element = new ElementAdapter(){

			@Override
			public boolean containsText(String condition) {
				return true;
			}
			
		};
		assertTrue(condition.apply(element));
	}
	
	@Test
	public void testConditionFalse(){
		
		Element element = new ElementAdapter(){

			@Override
			public boolean containsText(String condition) {
				return false;
			}
			
		};
		assertFalse(condition.apply(element));
	}
	
	@Test
	public void testEqualsTrue(){
		assertEquals(new TextCondition<Element>("MyCondition"), condition);
	}
	
	@Test
	public void testHashCode(){
		HashSet<Predicate<Element>> set = new HashSet<Predicate<Element>>();
		set.add(condition);
		assertTrue(set.contains(new TextCondition<Element>("MyCondition")));
	}


}
