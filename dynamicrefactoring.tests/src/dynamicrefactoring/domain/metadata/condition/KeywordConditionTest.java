package dynamicrefactoring.domain.metadata.condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.metadata.interfaces.Element;


public final class KeywordConditionTest {
	
	private KeyWordCondition<Element> condition;

	@Before
	public void setUp(){
		condition = new KeyWordCondition<Element>("MyKeyWord");
	}
	
	@Test
	public void testConditionTrue(){
		Element element = new ElementAdapter(){

			@Override
			public boolean belongsTo(String condition) {
				return true;
			}
			
		};
		assertTrue(condition.apply(element));
	}
	
	@Test
	public void testConditionFalse(){
		
		Element element = new ElementAdapter(){

			@Override
			public boolean belongsTo(String condition) {
				return false;
			}
			
		};
		assertFalse(condition.apply(element));
	}


}
